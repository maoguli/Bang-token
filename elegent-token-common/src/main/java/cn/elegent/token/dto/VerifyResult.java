package cn.elegent.token.dto;

import cn.elegent.token.enums.TokenStatus;
import cn.elegent.util.json.JsonUtil;
import lombok.Data;

import java.io.IOException;
import java.util.Map;

/**
 * VerifyResult
 * @author: wgl
 * @describe: 校验结果对象
 * @date: 2022/12/20 18:39
 **/
@Data
public class VerifyResult {

    private boolean isValidate;
    /**
     * 5001:token过期;5002:无效token;5003:token校验异常
     */
    private int code;

    private String msg;

    /**
     * 载荷
     */
    private Object payload;


    private String userId;

    public VerifyResult() {
    }

    public VerifyResult(int code, Object payload) {
        if(code==200){
            this.isValidate = true;
        }else{
            this.isValidate = false;
        }
        this.code = code;
        this.payload = payload;
    }

    public VerifyResult(TokenStatus tokenStatus,Object payload) {
        if(tokenStatus==TokenStatus.OK){
            this.isValidate = false;
        }else{
            this.isValidate = true;
        }
        this.code = tokenStatus.getCode();
        this.msg = tokenStatus.getName();
        this.payload = payload;
    }

    public VerifyResult(boolean isValidate, int code, String msg, Map payload, String userId) {
        this.isValidate = isValidate;
        this.code = code;
        this.msg = msg;
        this.payload = payload;
        this.userId = userId;
    }

    /**
     * 判断是否是正常
     * @return
     */
    public boolean isOK(){
        if(code == TokenStatus.OK.getCode()){
            return true;
        }
        return false;
    }

    /**
     * 判断是否是过期
     * @return
     */
    public boolean isOverdue(){
        if(code == TokenStatus.OVERDUE.getCode()){
            return true;
        }
        return false;
    }

    /**
     * 判断是否是无效token
     * @return
     */
    public boolean isInvalid(){
        if(code == TokenStatus.INVALID.getCode()){
            return true;
        }
        return false;
    }

    /**
     * 判断是否是校验异常token
     * @return
     */
    public boolean isError(){
        if(code == TokenStatus.ERROR.getCode()){
            return true;
        }
        return false;
    }

    /**
     * 判断是否是校验异常token或无效
     * @return
     */
    public boolean isErrorORInvalid(){
        if((code == TokenStatus.ERROR.getCode())||(code == TokenStatus.INVALID.getCode())){
            return true;
        }
        return false;
    }

    public boolean isValidate() {
        return isValidate;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "VerifyResult{" +
                "isValidate=" + isValidate +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", payload=" + payload +
                ", userId='" + userId + '\'' +
                '}';
    }
}
