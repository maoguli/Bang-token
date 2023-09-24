package cn.elegent.token.refresh.service;

import cn.elegent.token.dto.ElegentUser;
import cn.elegent.token.dto.RefreshDTO;
import cn.elegent.token.dto.TokenDTO;

/**
 * CustomService
 * @author: wgl
 * @describe: 开闭原则 用户定制化钩子方法层
 * @date: 2022/12/28 10:37
 **/
public interface CustomService {
    /**
     * 用户定可以自定义的校验刷新token的方法
     * 该校验非jwt校验 属于每个业务系统的特殊化校验
     * 如：
     * 一个刷新token只能使用一次
     * 一个刷新token 10分钟内 只能使用2次 过了10分钟失效 等等
     * 提供开放接口保证用户基于自己的业务功能来进行拓展
     * @param refreshToken
     * @param user
     * @return
     */
    Boolean checkRefreshToken(String refreshToken,Object user);


    /**
     * 用户可以拓展的功能
     * 在创建完Token后考虑到用户可能会做一些额外操作，我们提供了一个钩子方法以便用户实现一些自定义的操作
     * 可以与校验方法配合使用
     * @param accessTokenDTO
     * @param refreshTokenDTO
     * @param user
     */
    void customHook(TokenDTO accessTokenDTO, TokenDTO refreshTokenDTO,Object user);


    /**
     * 在刷新完成后提供给用户进行增强的方法
     * @param res
     * @param payload
     */
    void afterRefreshHook(RefreshDTO res, Object payload);

}
