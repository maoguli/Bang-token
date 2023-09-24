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
import cn.elegent.token.util.JWTSymmetricUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.HashMap;


/**
 * TokenServiceImpl
 *
 * @author: wgl
 * @describe: token校验的默认的对称加密的实现类
 * @date: 2022/12/28 10:10
 */
@ConditionalOnProperty(prefix = "elegent.token.verify",name = "type",havingValue = "symmetric",matchIfMissing = true)
@Service
public class TokenServiceSymmetricImpl implements TokenService {


    @Autowired
    private TokenManagerConfig tokenConfig;


    @Autowired
    private CustomService customService;


    @Autowired
    private ServiceSecretManager secretManager;


    /**
     * token续期的方法 (对称加密的方法)
     *
     * @param refreshToken 刷新token
     * @return
     */
    @Override
    public RefreshDTO createRefreshToken(VerifyResult verifyResult, String refreshToken) {
        try {
            String secret = (String) secretManager.getSecret();
            //如果刷新token的校验通过了--执行续期逻辑
            //首先从校验结果中拿到载荷信息
            Object payload = verifyResult.getPayload();
            //构建访问token
            TokenDTO accessTokenDTO = JWTSymmetricUtils.createJWTByObj(payload,//载荷部分
                    secret,//秘钥部分
                    tokenConfig.getAccessTokenLifespan(),//访问token的生命周期
                    TokenManagerConfig.getTimeUnitByPerporties(tokenConfig.getAccessTokenTimeUnit()));//换算访问token的时间单位
            TokenDTO refreshTokenDTO = JWTSymmetricUtils.createJWTByObj(new HashMap() {
                                                                            {
                                                                                put(ElegentTokenConstant.USER_ID, getUserIdSecre(verifyResult.getUserId()));
                                                                            }
                                                                        },//载荷部分
                    secret,//秘钥部分
                    tokenConfig.getRefreshTokenLifespan(),//刷新token的生命周期
                    TokenManagerConfig.getTimeUnitByPerporties(tokenConfig.getRefreshTokenTimeUnit()));//换算刷新token的时间单位
            return new RefreshDTO(accessTokenDTO, refreshTokenDTO);
        }catch (Exception e){
            e.printStackTrace();
            throw new TokenException("do refresh error,data is :"+verifyResult.getPayload());
        }
    }


    /**
     * 创建token的方法  ---  根据当前的用户对象创建token的方法
     * 当前是对称的加密方式
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
            String secret = (String) secretManager.getSecret();
            //构建访问token
            TokenDTO accessTokenDTO = JWTSymmetricUtils.createJWTByObj(user,//载荷部分
                    secret,//秘钥部分
                    tokenConfig.getAccessTokenLifespan(),//访问token的生命周期
                    TokenManagerConfig.getTimeUnitByPerporties(tokenConfig.getAccessTokenTimeUnit()));//换算访问token的时间单位
            TokenDTO refreshTokenDTO = JWTSymmetricUtils.createJWTByObj(new HashMap() {
                                                                            {
                                                                                put(ElegentTokenConstant.USER_ID, getUserIdSecre(user));
                                                                            }
                                                                        },//载荷部分
                    secret,//秘钥部分
                    tokenConfig.getRefreshTokenLifespan(),//刷新token的生命周期
                    TokenManagerConfig.getTimeUnitByPerporties(tokenConfig.getRefreshTokenTimeUnit()));//换算刷新token的时间单位
            customService.customHook(accessTokenDTO,refreshTokenDTO,user);
            return new RefreshDTO(accessTokenDTO, refreshTokenDTO);
        }catch (Exception e){
            e.printStackTrace();
            throw new TokenException("create token error,data is :"+user);
        }
    }
}
