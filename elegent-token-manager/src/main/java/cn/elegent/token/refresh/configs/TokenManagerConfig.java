package cn.elegent.token.refresh.configs;

/**
 * TokenManagerConfig
 *
 * @author: wgl
 * @describe: TODO
 * @date: 2022/12/28 10:10
 */

import cn.elegent.token.enums.TimeUnit;
import cn.elegent.token.exceptions.TokenException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ElegentTokenConfig
 * @author: wgl
 * @describe: ElegentToken业务层配置类
 * @date: 2022/12/28 18:38
 **/
@Component
@ConfigurationProperties
@Data
public class TokenManagerConfig {

    public final static String REFRESHURL = "/elegent/refreshToken";

    /**
     * 请求头中的访问token的名称--访问token的相关配置
     * =================================================================================================
     */
    @Value("${elegent.token.header.access.lifespan:2}")
    private Integer accessTokenLifespan;

    @Value("${elegent.token.header.access.timeUnit:H}")
    private String accessTokenTimeUnit;

    /**=================================================================================================*/
    /**
     * 请求头中的刷新token的名称--刷新token相关配置
     *=================================================================================================
     */
    @Value("${elegent.token.header.refresh.lifespan:30}")
    private Integer refreshTokenLifespan;

    @Value("${elegent.token.header.refresh.timeUnit:D}")
    private String refreshTokenTimeUnit;
    /**=================================================================================================*/


    private final static String HOUR = "H";

    private final static String DAY = "D";

    private final static String MIN = "M";

    private final static String SEC = "S";

    /**
     * 根据配置的时间单位获取系统的时间单位的方法
     * @param perportiesTimeUnit
     * @return
     */
    public static TimeUnit getTimeUnitByPerporties(String perportiesTimeUnit){
        switch (perportiesTimeUnit){
            case HOUR:
                return TimeUnit.HOURS;
            case DAY:
                return TimeUnit.DAY;
            case MIN:
                return TimeUnit.MINUTES;
            case SEC:
                return TimeUnit.SECONDS;
            default:
                throw new TokenException("token配置错误 请检查配置");
        }
    }


    public Integer getAccessTokenLifespan() {
        return accessTokenLifespan;
    }

    public void setAccessTokenLifespan(Integer accessTokenLifespan) {
        this.accessTokenLifespan = accessTokenLifespan;
    }

    public Integer getRefreshTokenLifespan() {
        return refreshTokenLifespan;
    }

    public void setRefreshTokenLifespan(Integer refreshTokenLifespan) {
        this.refreshTokenLifespan = refreshTokenLifespan;
    }

    public String getRefreshTokenTimeUnit() {
        return refreshTokenTimeUnit;
    }

    public void setRefreshTokenTimeUnit(String refreshTokenTimeUnit) {
        this.refreshTokenTimeUnit = refreshTokenTimeUnit;
    }

    public String getAccessTokenTimeUnit() {
        return accessTokenTimeUnit;
    }

    public void setAccessTokenTimeUnit(String accessTokenTimeUnit) {
        this.accessTokenTimeUnit = accessTokenTimeUnit;
    }
}