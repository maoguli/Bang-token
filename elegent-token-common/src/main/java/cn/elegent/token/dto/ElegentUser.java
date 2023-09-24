package cn.elegent.token.dto;

import java.io.Serializable;

/**
 * ElegentUserDetail
 *
 * @author: wgl
 * @describe: Elegent框架提供的用户登录对象,所有的用户登录对象需要继承自该登录对象
 * @date: 2022/12/28 10:10
 */
public class ElegentUser implements Serializable {
    //用户id
    private String userId;

    public ElegentUser() {
    }

    public ElegentUser(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}