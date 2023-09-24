package cn.elegent.token.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RefreshDTO
 * @author: wgl
 * @describe: 用于刷新token返回时使用的DTO
 * @date: 2022/12/28 10:10
 **/
public class RefreshDTO {

    /**
     * 访问Token
     */
    private TokenDTO accessToken;

    /**
     * 刷新Token
     */
    private TokenDTO refreshToken;


    public RefreshDTO() {
    }

    public RefreshDTO(TokenDTO accessToken, TokenDTO refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public TokenDTO getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(TokenDTO accessToken) {
        this.accessToken = accessToken;
    }

    public TokenDTO getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(TokenDTO refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "RefreshDTO{" +
                "accessToken=" + accessToken +
                ", refreshToken=" + refreshToken +
                '}';
    }
}
