package cn.elegent.token.service.impl;

import cn.elegent.token.dto.VerifyResult;
import cn.elegent.token.enums.TokenStatus;
import cn.elegent.token.secret.GateWaySecretManager;
import cn.elegent.token.service.TokenService;
import cn.elegent.token.util.JWTAsymmetricUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.security.PublicKey;

/**
 * AsymmetricTokenServiceImpl
 * @author: wgl
 * @describe: ElegentToken框架提供的非对称的校验方式token网关业务层实现类
 * @date: 2022/12/28 14:05
 **/
@ConditionalOnProperty(prefix = "elegent.token.verify",name = "type",havingValue = "asymmetric")
@Service
public class AsymmetricTokenServiceImpl implements TokenService {

    @Autowired
    private GateWaySecretManager gateWaySecretManager;

    /**
     * 系统提供的校验token的方法--此方法为非对称加密的方法
      * @param token
     * @return
     */
    @Override
    public VerifyResult verifyToken(String token) {
        try {
            //对称加密 获取对应对称加密的秘钥---从配置中直接读取
            PublicKey publicKey = (PublicKey) gateWaySecretManager.getSecret();
            VerifyResult verifyResult = JWTAsymmetricUtils.verifyJwt(token, publicKey);
            setVerifyResultSetUserId(verifyResult, false);
            return verifyResult;
        } catch (Exception e) {
            return new VerifyResult(TokenStatus.ERROR, null);
        }
    }

    /**
     * 校验刷新token的方法--非对称得方式
     *
     * @param refreshToken
     * @return
     */
    @Override
    public VerifyResult verifyRefreshToken(String refreshToken) {
        PublicKey secret = (PublicKey) gateWaySecretManager.getSecret();
        VerifyResult verifyResult = JWTAsymmetricUtils.verifyJwt(refreshToken, secret);
        setVerifyResultSetUserId(verifyResult, true);
        return verifyResult;
    }
}
