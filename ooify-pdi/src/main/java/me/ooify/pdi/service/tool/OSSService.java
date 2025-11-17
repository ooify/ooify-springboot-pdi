package me.ooify.pdi.service.tool;


import com.alibaba.fastjson2.JSONObject;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.sts20150401.models.AssumeRoleResponse;
import com.aliyun.sts20150401.models.AssumeRoleResponseBody;
import com.aliyun.tea.TeaException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import me.ooify.common.utils.SecurityUtils;
import me.ooify.pdi.utils.bean.AliOSSProperties;
import me.ooify.pdi.utils.file.AliOssUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

@Service
public class OSSService {

    @Autowired
    private AliOssUtil aliOssUtil;

    @Autowired
    private AliOSSProperties aliOSSProperties;

    private final String upload_dir = "video/";
    private final Long expire_time = 3600L;

    /**
     * 生成过期时间字符串
     */
    public static String generateExpiration(long seconds) {
        Instant instant = Instant.now().plusSeconds(seconds);
        ZonedDateTime zonedDateTime = instant.atZone(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return zonedDateTime.format(formatter);
    }

    /**
     * 创建 STS Client
     */
    private com.aliyun.sts20150401.Client createStsClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(aliOSSProperties.getAccessKeyId())
                .setAccessKeySecret(aliOSSProperties.getAccessKeySecret());
        config.endpoint = "sts.cn-beijing.aliyuncs.com";
        return new com.aliyun.sts20150401.Client(config);
    }

    /**
     * 获取 STS 临时凭证
     */
    private AssumeRoleResponseBody.AssumeRoleResponseBodyCredentials getCredential() throws Exception {
        com.aliyun.sts20150401.Client client = createStsClient();
        com.aliyun.sts20150401.models.AssumeRoleRequest request = new com.aliyun.sts20150401.models.AssumeRoleRequest()
                .setRoleArn(aliOSSProperties.getArn())
                .setRoleSessionName("PdiSession");

        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            AssumeRoleResponse response = client.assumeRoleWithOptions(request, runtime);
            return response.body.credentials;
        } catch (TeaException error) {
            System.out.println("STS Error: " + error.getMessage());
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception ex) {
            System.out.println("General Error: " + ex.getMessage());
        }

        // 返回默认错误凭证
        AssumeRoleResponseBody.AssumeRoleResponseBodyCredentials defaultCred = new AssumeRoleResponseBody.AssumeRoleResponseBodyCredentials();
        defaultCred.accessKeyId = "ERROR_ACCESS_KEY_ID";
        defaultCred.accessKeySecret = "ERROR_ACCESS_KEY_SECRET";
        defaultCred.securityToken = "ERROR_SECURITY_TOKEN";
        return defaultCred;
    }


    public Map<String, String> getPostSignatureForOssUpload(Long fileId) throws Exception {
        AssumeRoleResponseBody.AssumeRoleResponseBodyCredentials stsData = getCredential();

        String accessKeyId = stsData.accessKeyId;
        String accessKeySecret = stsData.accessKeySecret;
        String securityToken = stsData.securityToken;

        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String x_oss_date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));

        String region = aliOSSProperties.getRegion();
        String bucket = aliOSSProperties.getBucketName();
        String x_oss_credential = accessKeyId + "/" + date + "/" + region + "/oss/aliyun_v4_request";

        Map<String, Object> policy = new HashMap<>();
        policy.put("expiration", generateExpiration(expire_time));

        List<Object> conditions = new ArrayList<>();
        conditions.add(Map.of("bucket", bucket));
        conditions.add(Map.of("x-oss-security-token", securityToken));
        conditions.add(Map.of("x-oss-signature-version", "OSS4-HMAC-SHA256"));
        conditions.add(Map.of("x-oss-credential", x_oss_credential));
        conditions.add(Map.of("x-oss-date", x_oss_date));
        conditions.add(Arrays.asList("content-length-range", 1, 50 * 1024 * 1024));
        conditions.add(Arrays.asList("in", "$content-type", Arrays.asList("video/mp4", "video/ogg", "video/flv", "video/avi", "video/wmv", "video/rmvb", "video/mov")));
        conditions.add(Arrays.asList("eq", "$success_action_status", "200"));
        conditions.add(Arrays.asList("starts-with", "$key", upload_dir));

        policy.put("conditions", conditions);

        ObjectMapper mapper = new ObjectMapper();
        String jsonPolicy = mapper.writeValueAsString(policy);
        String stringToSign = Base64.encodeBase64String(jsonPolicy.getBytes());

        byte[] dateKey = hmacsha256(("aliyun_v4" + accessKeySecret).getBytes(), date);
        byte[] dateRegionKey = hmacsha256(dateKey, region);
        byte[] dateRegionServiceKey = hmacsha256(dateRegionKey, "oss");
        byte[] signingKey = hmacsha256(dateRegionServiceKey, "aliyun_v4_request");
        String signature = BinaryUtil.toHex(hmacsha256(signingKey, stringToSign));

        // 步骤5：设置回调。
        JSONObject jasonCallback = new JSONObject();
        jasonCallback.put("callbackUrl", aliOSSProperties.getCallback());
//        jasonCallback.put("callbackBody","url="+aliOSSProperties.getHost()+"/"+"${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
//        fileId
        jasonCallback.put("callbackBody","fileId="+fileId+"&url="+aliOSSProperties.getHost()+"/"+"${object}"+"&userId="+ SecurityUtils.getUserId());
//        jasonCallback.put("callbackBody", "bucket=${bucket}&object=${object}");
        jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
//        jasonCallback.put("callbackBody", "{\"bucket\":${bucket},\"object\":${object}}");
//        jasonCallback.put("callbackBodyType", "application/json");
        String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());

        Map<String, String> response = new HashMap<>();
        response.put("version", "OSS4-HMAC-`SHA256`");
        response.put("policy", stringToSign);
        response.put("x_oss_credential", x_oss_credential);
        response.put("x_oss_date", x_oss_date);
        response.put("signature", signature);
        response.put("security_token", securityToken);
        response.put("dir", upload_dir+ SecurityUtils.getUsername()+"/");
        response.put("host", aliOSSProperties.getHost());
        response.put("callback", base64CallbackBody);
        response.put("fileId", String.valueOf(fileId));
        return response;
    }

    /**
     * 计算 HMAC-SHA256
     */
    private static byte[] hmacsha256(byte[] key, String data) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);
            return mac.doFinal(data.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate HMAC-SHA256", e);
        }
    }


    public String uploadOSS(MultipartFile file, String fileName, String directory) throws Exception {
        return aliOssUtil.upload(file, fileName, directory);
    }

    public String uploadOSS(MultipartFile file, String directory) throws Exception {
        return aliOssUtil.upload(file, directory);
    }

    private  final Logger log = LoggerFactory.getLogger(OSSService.class);

    /**
     * 验证 OSS 回调请求的合法性
     *
     * @param request HTTP 请求对象
     * @return true 表示验证通过，false 表示失败
     */
    public boolean verifyOSSCallbackRequest(HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String pubKeyUrlHeader = request.getHeader("x-oss-pub-key-url");

            log.info("Authorization header: {}", authorizationHeader);
            log.info("x-oss-pub-key-url header (raw): {}", pubKeyUrlHeader);

            if (authorizationHeader == null || pubKeyUrlHeader == null) {
                log.warn("Missing required headers");
                return false;
            }

            // 解码签名
            byte[] signatureBytes = java.util.Base64.getDecoder().decode(authorizationHeader);
            log.info("Decoded signature length: {} bytes", signatureBytes.length);

            // 解码公钥 URL
            byte[] pubKeyUrlBytes = java.util.Base64.getDecoder().decode(pubKeyUrlHeader);
            String pubKeyUrl = new String(pubKeyUrlBytes, StandardCharsets.UTF_8);
            log.info("Decoded public key URL: {}", pubKeyUrl);

            if (!pubKeyUrl.startsWith("http://gosspublic.alicdn.com/") &&
                    !pubKeyUrl.startsWith("https://gosspublic.alicdn.com/")) {
                log.error("Invalid public key domain: {}", pubKeyUrl);
                return false;
            }

            // 下载公钥
            String publicKeyPem = fetchPublicKey(pubKeyUrl);
            if (publicKeyPem == null) {
                log.error("Failed to download public key");
                return false;
            }
            log.info("Public key PEM:\n{}", publicKeyPem);

            String publicKeyBase64 = publicKeyPem
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            log.info("Cleaned public key (base64): {}", publicKeyBase64);

            // 构造 authStr
            String uri = request.getRequestURI();
            String decodedUri = URLDecoder.decode(uri, StandardCharsets.UTF_8);
            String queryString = request.getQueryString();
            String authStr = decodedUri;
            if (queryString != null && !queryString.isEmpty()) {
                authStr += "?" + queryString;
            }

            String body = getRequestBody(request);
            authStr += "\n" + body;

            log.info("Constructed authStr (hex preview): {}", bytesToHex(authStr.getBytes(StandardCharsets.UTF_8)));
            log.info("Full authStr:\n---\n{}\n---", authStr);

            // 验签
            boolean verified = doCheck(authStr, signatureBytes, publicKeyBase64);
            log.info("Signature verification result: {}", verified);

            return verified;

        } catch (Exception e) {
            log.error("Exception during OSS callback verification", e);
            return false;
        }
    }
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString().substring(0, Math.min(100, sb.length())) + "...";
    }
    private static String fetchPublicKey(String url) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            try (var response = httpClient.execute(httpGet)) {
                return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getRequestBody(HttpServletRequest request) throws java.io.IOException {
        return new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    private static boolean doCheck(String content, byte[] sign, String publicKeyBase64) {
        try {
            byte[] keyBytes =  java.util.Base64.getDecoder().decode(publicKeyBase64);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            java.security.Signature signature = java.security.Signature.getInstance("MD5withRSA");
            signature.initVerify(publicKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            return signature.verify(sign);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
