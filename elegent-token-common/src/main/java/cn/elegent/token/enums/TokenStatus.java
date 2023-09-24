package cn.elegent.token.enums;

import lombok.Getter;

/**
 * token状态
 */
@Getter
public enum TokenStatus {
    OK("正常",200),
    OVERDUE("过期",5001),
    INVALID("无效",5002),
    ERROR("异常",5003),
    ;
    private String name;

    TokenStatus(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    private int code;
}
