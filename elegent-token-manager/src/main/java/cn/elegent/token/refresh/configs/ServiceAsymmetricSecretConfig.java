package cn.elegent.token.refresh.configs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * ServiceAsymmetricSecretConfig
 *
 * @author: wgl
 * @describe: 业务层非对称秘钥配置
 * @date: 2022/12/28 10:10
 */
@ConditionalOnProperty(prefix = "elegent.token.verify",name = "type",havingValue = "asymmetric")
@Configuration
@Data
public class ServiceAsymmetricSecretConfig {

    @Value("${elegent.token.verify.secret.filePath.priKeyPath}")
    private String priKeyFilePath;


    public String getPriKeyFilePath() {
        return priKeyFilePath;
    }

    public void setPriKeyFilePath(String priKeyFilePath) {
        this.priKeyFilePath = priKeyFilePath;
    }
}