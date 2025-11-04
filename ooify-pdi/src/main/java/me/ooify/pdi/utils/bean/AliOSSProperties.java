package me.ooify.pdi.utils.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliOSSProperties {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String region;
    private String arn;
    private String callback;

    public String getHost() {
        // 拼接完整 OSS 访问域名
        return "https://" + bucketName + "." + endpoint;
    }
}
