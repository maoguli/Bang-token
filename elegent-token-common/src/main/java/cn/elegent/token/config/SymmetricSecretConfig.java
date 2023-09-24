package cn.elegent.token.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * SymmetricSecretConfig
 *
 * @author: wgl
 * @describe: 对称加密配置
 * @date: 2022/12/28 10:10
 */
@ConditionalOnProperty(prefix = "elegent.token.verify",name = "type",havingValue = "symmetric",matchIfMissing = true)
@Configuration
@Data
public class SymmetricSecretConfig {

    @Value("${elegent.token.verify.secret}")
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
