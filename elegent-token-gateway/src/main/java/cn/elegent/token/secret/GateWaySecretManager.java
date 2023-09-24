package cn.elegent.token.secret;

import cn.elegent.token.config.SymmetricSecretConfig;
import cn.elegent.token.configs.GateWayAsymmetriConfig;
import cn.elegent.token.enums.TokenType;
import cn.elegent.token.exceptions.TokenException;
import cn.elegent.token.util.RsaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * GateWaySecretManager
 *
 * @author: wgl
 * @describe: 网关秘钥管理器
 * 网关的作用只做解密，所以该秘钥管理器只处理公钥
 * @date: 2022/12/28 10:10
 */
@Component
public class GateWaySecretManager implements SecretManager {

    private static PublicKey publicKey;
    @Value("${elegent.token.verify.type:symmetric}")
    private String type;
    /**
     * 注入非对称配置类
     */
    @Autowired(required = false)
    @Lazy
    private GateWayAsymmetriConfig asymmetricSecretConfig;
    /**
     * 注入对称配置类
     */
    @Autowired(required = false)
    @Lazy
    private SymmetricSecretConfig symmetricSecretConfig;

    /**
     * 空间换时间---服务启动的时候
     * 如果是对称加密：
     * <p>
     * 如果是非对称加密：
     *
     * @throws Exception
     */
    @PostConstruct
    private void init() throws Exception {
        if (TokenType.ASYMMETRIC.equals(type)) {
            //只有非对称加密时才会加载这个配置  而网关只需要读取公钥
            publicKey = RsaUtils.getPublicKey(asymmetricSecretConfig.getPubKeyFilePath());
        }
    }

    /**
     * 获取秘钥的方法.
     *
     * @return
     */
    public Object getSecret() {
        switch (type) {
            case TokenType.ASYMMETRIC:
                return publicKey;
            case TokenType.SYMMETRIC:
                return symmetricSecretConfig.getKey();
            default:
                throw new TokenException("错误的配置信息 请校验elegent-token配置");
        }
    }
}
