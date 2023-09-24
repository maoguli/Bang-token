package cn.elegent.token.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * RefreshVO
 *
 * @author: wgl
 * @describe: TODO
 * @date: 2022/12/28 10:10
 */
public class RefreshVO {

    /**
     * 刷新token
     */
    private String refreshToken;

    /**
     * 其他拓展字段
     */
    private Map params;

    public RefreshVO() {
    }

    public RefreshVO(String refreshToken, Map params) {
        this.refreshToken = refreshToken;
        this.params = params;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Map getParams() {
        return params;
    }

    public void setParams(Map params) {
        this.params = params;
    }

}
