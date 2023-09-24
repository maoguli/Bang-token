package cn.elegent.token.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TokenDTO
 * @author: wgl
 * @describe: 每一个token包含的内容
 * @date: 2022/12/28 10:10
 */
public class TokenDTO {

    /**
     * token
     */
    private String token;

    /**
     * 有效期
     */
    private String lifespan;

    public TokenDTO(String token, String lifespan) {
        this.token = token;
        this.lifespan = lifespan;
    }

    public TokenDTO() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLifespan() {
        return lifespan;
    }

    public void setLifespan(String lifespan) {
        this.lifespan = lifespan;
    }

    @Override
    public String toString() {
        return "TokenDTO{" +
                "token='" + token + '\'' +
                ", lifespan='" + lifespan + '\'' +
                '}';
    }
}
