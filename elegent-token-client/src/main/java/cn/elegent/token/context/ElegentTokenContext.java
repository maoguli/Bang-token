package cn.elegent.token.context;

import java.util.Map;

/**
 * ElegentTokenContext
 *
 * @author: wgl
 * @describe: 当前登录用户对象上下文
 * @date: 2022/12/28 10:10
 */
public class ElegentTokenContext {
    private final static ThreadLocal<Map> LOGIN_USER = new ThreadLocal<Map>();

    public static void setLoginUser(Map data){
        LOGIN_USER.set(data);
    }

    public static Map getLoginUser(){
        return LOGIN_USER.get();
    }

    private final static ThreadLocal<String> USER_ID = new ThreadLocal<String>();

    public static void setUserId(String data){
        USER_ID.set(data);
    }

    public static String getUserId(){
        return USER_ID.get();
    }


    public static void remove(){
        LOGIN_USER.remove();
        USER_ID.remove();
    }
}
