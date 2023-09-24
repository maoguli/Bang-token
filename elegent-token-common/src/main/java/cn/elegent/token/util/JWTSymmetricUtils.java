package cn.elegent.token.util;

import cn.elegent.token.dto.TokenDTO;
import cn.elegent.token.dto.VerifyResult;
import cn.elegent.token.enums.TimeUnit;
import cn.elegent.token.enums.TokenStatus;
import cn.elegent.util.json.JsonUtil;
import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;

/**
 * JWTSymmetricUtils
 *
 * @author: wgl
 * @describe: JWT对称加密工具类
 * @date: 2022/12/28 10:10
 */
public class JWTSymmetricUtils{
    /**
     * 签发JWT
     * @param tokenObject
     * @param key 对称加密秘钥
     * @param expire 过期时间默认天
     * @return
     * @throws IOException
     */
    public static TokenDTO createJWTByObj(Object tokenObject, String key, int expire, TimeUnit timeUnit) throws IOException {
        SecretKey secretKey = generalKey(key);
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = TimeUnit.getEndDate(expire, timeUnit, zoneId);
        Date endTime = Date.from(zdt.toInstant());
        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .setId("elegentToken") //签发应用Id
                .setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())) //签发时间
                .setHeaderParam("alg", "HS256")  //加密算法
                .addClaims(JsonUtil.convertToMap(tokenObject))
                .setExpiration(endTime)  //设置过期时间
                .signWith(SignatureAlgorithm.HS256,secretKey);  //用密钥签名
        //生成JWT
        String token = builder.compact();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endTimeStr = sdf.format(endTime);
        return new TokenDTO(token,endTimeStr);
    }

    /**
     * 验证jwt
     */
    public static VerifyResult verifyJwt(String token, String secret) {
        //签名秘钥，和生成的签名的秘钥一模一样
        SecretKey key =  generalKey(secret);
        try {
            Jwt jwt = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);
            return new VerifyResult(TokenStatus.OK,jwt.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return new VerifyResult(TokenStatus.ERROR,null);
        }
    }

    /**
     * 生成key
     *
     * @param jwtSecret
     * @return
     */
    private static SecretKey generalKey(String jwtSecret) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] encodedKey = Base64.getMimeDecoder().decode(jwtSecret);//DatatypeConverter.parseBase64Binary(jwtSecret);//Base64.getDecoder().decode(jwtSecret);
        SecretKey key = new SecretKeySpec(encodedKey, signatureAlgorithm.getJcaName());
        return key;
    }
}
