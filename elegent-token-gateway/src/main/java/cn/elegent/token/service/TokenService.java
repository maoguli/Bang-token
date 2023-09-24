package cn.elegent.token.service;

import cn.elegent.token.constants.ElegentTokenConstant;
import cn.elegent.token.dto.VerifyResult;
import cn.hutool.core.bean.BeanUtil;

import java.util.Base64;
import java.util.Map;

/**
 * 网关层的Token业务层
 */
public interface TokenService {

    VerifyResult verifyToken(String token);


    /**
     * 校验刷新token的方法
     *
     * @param refreshToken
     * @return
     */
    VerifyResult verifyRefreshToken(String refreshToken);

    /**
     * 根据载荷中的内容为校验结果集封装userId的方法
     * flag 表示是否解密
     * true 需要对Base64解密
     * false 不需要进行Base64解密
     * 因为访问Token里的内容是明文，而刷新Token里的内容是Base64加密后的结果
     *
     * @param verifyResult
     */
    default void setVerifyResultSetUserId(VerifyResult verifyResult, Boolean flag) {
        Object payload = verifyResult.getPayload();
        //从载荷中拿到userID
        Map<String, Object> payLoadMap = BeanUtil.beanToMap(payload);
        String userId = payLoadMap.get(ElegentTokenConstant.USER_ID).toString();
        if (flag) {
            Base64.Decoder decoder = Base64.getDecoder();
            userId = new String(decoder.decode(userId));
            payLoadMap.put(ElegentTokenConstant.USER_ID,userId);
            verifyResult.setPayload(payLoadMap);
        }
        verifyResult.setUserId(userId);
    }
}