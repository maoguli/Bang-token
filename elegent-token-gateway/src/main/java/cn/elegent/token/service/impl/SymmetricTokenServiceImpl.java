package cn.elegent.token.service.impl;

import cn.elegent.token.dto.VerifyResult;
import cn.elegent.token.enums.TokenStatus;
import cn.elegent.token.secret.GateWaySecretManager;
import cn.elegent.token.service.TokenService;
import cn.elegent.token.util.JWTSymmetricUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * SymmetricTokenServiceImpl
 * @author: wgl
 * @describe: ElegentToken框架提供的默认的对称的 token网关业务层实现类
 * @date: 2022/12/28 14:05
 **/
@ConditionalOnProperty(prefix = "elegent.token.verify",name = "type",havingValue = "symmetric",matchIfMissing = true)
@Service
public class SymmetricTokenServiceImpl implements TokenService {

    @Autowired
    private GateWaySecretManager gateWaySecretManager;

    /**
     * 系统提供的校验token的方法 此方法为对称加密得方法
      * @param token
     * @return
     */
    @Override
    public VerifyResult verifyToken(String token) {
        try {
            //对称加密 获取对应对称加密的秘钥---从配置中直接读取
            String secret = (String) gateWaySecretManager.getSecret();
            VerifyResult verifyResult = JWTSymmetricUtils.verifyJwt(token, secret);
            setVerifyResultSetUserId(verifyResult, false);
            return verifyResult;
        } catch (Exception e) {
            return new VerifyResult(TokenStatus.ERROR, null);
        }
    }

    /**
     * 校验刷新token的方法
     *
     * @param refreshToken
     * @return
     */
    @Override
    public VerifyResult verifyRefreshToken(String refreshToken) {
        //使用秘钥管理器工具类--来获取秘钥
        String seecret = (String) gateWaySecretManager.getSecret();
        //由于当前配置的是对称加密--选择对称加密的工具类来处理对称加密的Token
        VerifyResult verifyResult = JWTSymmetricUtils.verifyJwt(refreshToken, seecret);
        setVerifyResultSetUserId(verifyResult, true);
        return verifyResult;
    }
}
