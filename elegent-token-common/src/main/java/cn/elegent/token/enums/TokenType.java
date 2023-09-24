package cn.elegent.token.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TokenType
 * @author: wgl
 * @describe: token校验的类型
 * @date: 2022/12/28 11:55
 **/
@AllArgsConstructor
@Getter
public class TokenType {
    public final static String  ASYMMETRIC = "asymmetric";

    public final static String SYMMETRIC = "symmetric";
}
