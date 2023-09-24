package cn.elegent.token.exceptions;

import lombok.Getter;
import lombok.Setter;


/**
 * token自定义运行时异常
 */
@Getter
@Setter
public class TokenException extends RuntimeException{
    private String code;
    private String msg;

    public TokenException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public TokenException(String msg) {
        super(msg);
    }
}
