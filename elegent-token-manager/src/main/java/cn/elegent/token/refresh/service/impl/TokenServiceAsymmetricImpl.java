package cn.elegent.token.refresh.service.impl;

import cn.elegent.token.constants.ElegentTokenConstant;
import cn.elegent.token.dto.ElegentUser;
import cn.elegent.token.dto.RefreshDTO;
import cn.elegent.token.dto.TokenDTO;
import cn.elegent.token.dto.VerifyResult;
import cn.elegent.token.exceptions.TokenException;
import cn.elegent.token.refresh.configs.TokenManagerConfig;
import cn.elegent.token.refresh.secret.ServiceSecretManager;
import cn.elegent.token.refresh.service.CustomService;
import cn.elegent.token.refresh.service.TokenService;
import cn.elegent.token.util.JWTAsymmetricUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.HashMap;

/**
 * TokenServiceImpl
 *
 * @author: wgl
 * @describe: token校验的默认的非对称加密的实现类
 * @date: 2022/12/28 10:10
 */
@ConditionalOnProperty(prefix = "elegent.token.verify",name = "type",havingValue = "asymmetric")
@Service
public class TokenServiceAsymmetricImpl implements TokenService {


    @Autowired
    private ServiceSecretManager secretManager;

    @Autowired
    private CustomService customService;

    @Autowired
    private TokenManagerConfig tokenManagerConfig;


    /**
     * token续期的方法(非对称)
     * 创建刷新token的方法
     *
     * @param refreshToken 刷新token
     * @return
     */
    @Override
    public RefreshDTO createRefreshToken(VerifyResult verifyResult, String refreshToken) {
        try {
            PrivateKey privateKey = (PrivateKey) secretManager.getSecret();
            //如果刷新token的校验通过了--执行续期逻辑
            //首先从校验结果中拿到载荷信息
            Object payload = verifyResult.getPayload();
            //构建访问token
            TokenDTO accessTokenDTO = JWTAsymmetricUtils.createJWTByObj(payload,//载荷部分
                    privateKey,//秘钥部分
                    tokenManagerConfig.getAccessTokenLifespan(),//访问token的生命周期
                    TokenManagerConfig.getTimeUnitByPerporties(tokenManagerConfig.getAccessTokenTimeUnit()));//换算访问token的时间单位
            TokenDTO refreshTokenDTO = JWTAsymmetricUtils.createJWTByObj(new HashMap() {
                                                                             {
                                                                                 put(ElegentTokenConstant.USER_ID, getUserIdSecre(verifyResult.getUserId()));
                                                                             }
                                                                         },//载荷部分
                    privateKey,//秘钥部分
                    tokenManagerConfig.getRefreshTokenLifespan(),//刷新token的生命周期
                    TokenManagerConfig.getTimeUnitByPerporties(tokenManagerConfig.getRefreshTokenTimeUnit()));//换算刷新token的时间单位
            return new RefreshDTO(accessTokenDTO, refreshTokenDTO);
        }catch (Exception e){
            e.printStackTrace();
            throw new TokenException("do refresh error,data is :"+verifyResult.getPayload());
        }
    }


    /**
     * 创建token的方法  ---  根据当前的用户对象创建token的方法
     * 当前是非对称的实现方式 采用的是RSA的算法
     * 注意：该方法一般在登录的时候生效
     * 1）获取私钥
     * 2）根据当前的登录用户对象创建访问token--并使用配置中的时间参数
     * 3) 根据当前的登录用户对象创建一个刷新token--并使用配置中的刷新token参数
     * @param user
     * @return
     */
    @Override
    public RefreshDTO createToken(ElegentUser user) {
        try {
            PrivateKey privateKey = (PrivateKey) secretManager.getSecret();
            TokenDTO accessTokenDTO = JWTAsymmetricUtils.createJWTByObj(user,//载荷部分
                    privateKey,//秘钥部分
                    tokenManagerConfig.getAccessTokenLifespan(),//访问token的生命周期
                    TokenManagerConfig.getTimeUnitByPerporties(tokenManagerConfig.getAccessTokenTimeUnit()));//换算访问token的时间单位
            TokenDTO refreshTokenDTO = JWTAsymmetricUtils.createJWTByObj(new HashMap() {
                                                                             {
                                                                                 put(ElegentTokenConstant.USER_ID, getUserIdSecre(user));
                                                                             }
                                                                         },//载荷部分
                    privateKey,//秘钥部分
                    tokenManagerConfig.getRefreshTokenLifespan(),//刷新token的生命周期
                    TokenManagerConfig.getTimeUnitByPerporties(tokenManagerConfig.getRefreshTokenTimeUnit()));//换算刷新token的时间单位
            customService.customHook(accessTokenDTO,refreshTokenDTO,user);
            return new RefreshDTO(accessTokenDTO, refreshTokenDTO);
        }catch (Exception e){
            e.printStackTrace();
            throw new TokenException("create Token Exception data is:"+user);
        }
    }
}
