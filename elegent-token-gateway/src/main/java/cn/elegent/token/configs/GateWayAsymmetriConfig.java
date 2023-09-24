package cn.elegent.token.configs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * GateWayAsymmetriConfig
 *
 * @author: wgl
 * @describe: 网关层非对称解密配置类
 * @date: 2022/12/28 10:10
 */
@ConditionalOnProperty(prefix = "elegent.token.verify", name = "type", havingValue = "asymmetric")
@Configuration
@Data
public class GateWayAsymmetriConfig {

    @Value("${elegent.token.verify.secret.filePath.pubKeyPath}")
    private String pubKeyFilePath;


    public String getPubKeyFilePath() {
        return pubKeyFilePath;
    }

    public void setPubKeyFilePath(String pubKeyFilePath) {
        this.pubKeyFilePath = pubKeyFilePath;
    }
}
