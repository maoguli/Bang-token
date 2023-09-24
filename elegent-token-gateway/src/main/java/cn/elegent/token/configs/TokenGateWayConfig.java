package cn.elegent.token.configs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * ElegentTokenConfig
 *
 * @author: wgl
 * @describe: ElegentToken网关层配置类
 * @date: 2022/12/28 18:38
 **/
@Component
@ConfigurationProperties
@Data
public class TokenGateWayConfig {

    public final static String REFRESHURL = "/elegent/refreshToken";

    /**
     * 请求头中的访问token的名称--访问token的相关配置
     * =================================================================================================
     */
    @Value("${elegent.token.header.access.accessName:access_token}")
    private String accessTokenName;

    /**=================================================================================================*/
    /**
     * 请求头中的刷新token的名称--刷新token相关配置
     * =================================================================================================
     */
    @Value("${elegent.token.header.refresh.refreshName:refresh_token}")
    private String refreshTokenName;

    /**
     * =================================================================================================
     */
    @Value("${elegent.token.ignore.urls}")
    private String[] ignoreUrl;

    @Value("${elegent.token.order.num:0}")
    private Integer filterOrder;


    public TokenGateWayConfig() {
    }

    public TokenGateWayConfig(String accessTokenName, String refreshTokenName, String[] ignoreUrl, Integer filterOrder) {
        this.accessTokenName = accessTokenName;
        this.refreshTokenName = refreshTokenName;
        this.ignoreUrl = ignoreUrl;
        this.filterOrder = filterOrder;
    }

    public String getAccessTokenName() {
        return accessTokenName;
    }

    public void setAccessTokenName(String accessTokenName) {
        this.accessTokenName = accessTokenName;
    }

    public String getRefreshTokenName() {
        return refreshTokenName;
    }

    public void setRefreshTokenName(String refreshTokenName) {
        this.refreshTokenName = refreshTokenName;
    }

    public String[] getIgnoreUrl() {
        return ignoreUrl;
    }

    public void setIgnoreUrl(String[] ignoreUrl) {
        this.ignoreUrl = ignoreUrl;
    }

    public Integer getFilterOrder() {
        return filterOrder;
    }

    public void setFilterOrder(Integer filterOrder) {
        this.filterOrder = filterOrder;
    }
}