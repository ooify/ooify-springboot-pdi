package me.ooify.pdi.service.tool;


import com.alibaba.fastjson2.JSONObject;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.sts20150401.models.AssumeRoleResponse;
import com.aliyun.sts20150401.models.AssumeRoleResponseBody;
import com.aliyun.tea.TeaException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.ooify.common.core.domain.AjaxResult;
import me.ooify.pdi.utils.bean.AliOSSProperties;
import me.ooify.pdi.utils.file.AliOssUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        conditions.add(Arrays.asList("content-length-range", 1, 10240000));
        conditions.add(Arrays.asList("in", "$content-type", Arrays.asList("video/mp4","video/ogg","video/flv","video/avi","video/wmv","video/rmvb","video/mov")));
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
        jasonCallback.put("callbackBody","url="+aliOSSProperties.getHost()+"/"+"${object}&fileId="+fileId);
        jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
        String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());

        Map<String, String> response = new HashMap<>();
        response.put("version", "OSS4-HMAC-`SHA256`");
        response.put("policy", stringToSign);
        response.put("x_oss_credential", x_oss_credential);
        response.put("x_oss_date", x_oss_date);
        response.put("signature", signature);
        response.put("security_token", securityToken);
        response.put("dir", upload_dir);
        response.put("host", aliOSSProperties.getHost());
        response.put("callback", base64CallbackBody);
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


    public String  uploadOSS(MultipartFile file, String fileName, String directory) throws Exception {
        String url = aliOssUtil.upload(file, fileName, directory);
        System.out.println(url);
        return url;
    }

}
