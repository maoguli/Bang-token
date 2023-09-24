package cn.elegent.token.util;


import cn.elegent.token.dto.TokenDTO;
import cn.elegent.token.dto.VerifyResult;
import cn.elegent.token.enums.TimeUnit;
import cn.elegent.token.enums.TokenStatus;
import cn.elegent.util.json.JsonUtil;
import io.jsonwebtoken.*;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
/**
 * JWTAsymmetricUtils
 *
 * @author: wgl
 * @describe: JWT非对称加密工具类
 * @date: 2022/12/28 10:10
 */
public class JWTAsymmetricUtils {

    /**
     * 私钥加密token
     * @param userInfo   载荷中的数据
     * @param privateKey 私钥
     * @param expire     过期时间，单位分钟
     * @return JWT
     */
    public static TokenDTO createJWTByObj(Object userInfo, PrivateKey privateKey, int expire, TimeUnit timeUnit) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = timeUnit.getEndDate(expire, timeUnit, zoneId);
        Date endTime = Date.from(zdt.toInstant());
        String endTimeStr = sdf.format(endTime);
        String token = Jwts.builder()
                .setClaims(JsonUtil.convertToMap(userInfo))
                .setId("elegentToken")
                .setExpiration(endTime)  //设置过期时间
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
        return new TokenDTO(token,endTimeStr);
    }

    /**
     * 公钥解析token
     * @param token     用户请求中的token
     * @param publicKey 公钥
     * @return Jws<Claims>
     */
    public static VerifyResult verifyJwt(String token, PublicKey publicKey) {
        try{
            Jwt jwt = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
            return new VerifyResult(TokenStatus.OK,jwt.getBody());
        }catch (Exception e){
            e.printStackTrace();
            return new VerifyResult(TokenStatus.ERROR,null);
        }
    }
}