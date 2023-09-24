package cn.elegent.token.refresh.service;

import cn.elegent.token.constants.ElegentTokenConstant;
import cn.elegent.token.dto.ElegentUser;
import cn.elegent.token.dto.RefreshDTO;
import cn.elegent.token.dto.VerifyResult;
import cn.hutool.core.bean.BeanUtil;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * TokenService
 * @author: wgl
 * @describe: token在微服务中的业务层
 * @date: 2022/12/28 10:29
 **/
public interface TokenService {



    /**
     * 创建刷新token的方法
     * @param verifyResult 校验结果
     * @param refreshToken 刷新token
     * @return
     */
    RefreshDTO createRefreshToken(VerifyResult verifyResult,String refreshToken);


    /**
     * 根据登录用户对象创建token的方法
     * @param user
     * @return
     */
    RefreshDTO createToken(ElegentUser user);


    /**
     * 获取加密后的用户id
     * @param user
     * @return
     */
    default String getUserIdSecre(ElegentUser user){
        String userId = user.getUserId();
        Base64.Encoder encoder = Base64.getEncoder();
        return new String(encoder.encode(userId.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 获取加密后的用户id
     * @param userId
     * @return
     */
    default String getUserIdSecre(String userId){
        Base64.Encoder encoder = Base64.getEncoder();
        return new String(encoder.encode(userId.getBytes(StandardCharsets.UTF_8)));
    }

}