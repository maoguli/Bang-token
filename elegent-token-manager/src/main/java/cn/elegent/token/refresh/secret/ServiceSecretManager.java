package cn.elegent.token.refresh.secret;

import cn.elegent.token.config.SymmetricSecretConfig;
import cn.elegent.token.enums.TokenType;
import cn.elegent.token.exceptions.TokenException;
import cn.elegent.token.refresh.configs.ServiceAsymmetricSecretConfig;
import cn.elegent.token.secret.SecretManager;
import cn.elegent.token.util.RsaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * ServiceSecretManager
 *
 * @author: wgl
 * @describe: 业务层秘钥管理器
 * 网关的作用只做解密，所以该秘钥管理器只处理公钥
 * @date: 2022/12/28 10:10
 */
@Component
public class ServiceSecretManager implements SecretManager {

    @Value("${elegent.token.verify.type:symmetric}")
    private String type;

    private static String secutiryType;

    /**
     * 注入非对称配置类
     */
    @Autowired(required = false)
    @Lazy
    private ServiceAsymmetricSecretConfig asymmetricSecretConfig;

    /**
     * 注入对称配置类
     */
    @Autowired(required = false)
    @Lazy
    private SymmetricSecretConfig symmetricSecretConfig;


    private static PrivateKey privateKey;


    /**
     * 空间换时间---服务启动的时候
     * 如果是对称加密：
     *
     * 如果是非对称加密：
     * @throws Exception
     */
    @PostConstruct
    private void init() throws Exception {
        secutiryType = type;
        if(TokenType.ASYMMETRIC.equals(type)){
            privateKey =  RsaUtils.getPrivateKey(asymmetricSecretConfig.getPriKeyFilePath());
        }
    }

    /**
     * 获取秘钥的方法.
     * @return
     */
    public Object getSecret() {
        switch (secutiryType){
            case TokenType.ASYMMETRIC:
                return privateKey;
            case TokenType.SYMMETRIC:
                //获取对称加密的秘钥
                return symmetricSecretConfig.getKey();
            default:
                throw new TokenException("错误的配置信息 请校验elegent-token配置");
        }
    }
}
