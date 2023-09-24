package cn.elegent.token.refresh.service.impl;

import cn.elegent.token.dto.ElegentUser;
import cn.elegent.token.dto.RefreshDTO;
import cn.elegent.token.dto.TokenDTO;
import cn.elegent.token.refresh.service.CustomService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * CustomServiceImpl
 *
 * @author: wgl
 * @describe: 用户开放接口
 * @date: 2022/12/28 10:10
 */
@ConditionalOnProperty(prefix = "elegent.token.custom",name = "check",havingValue = "default",matchIfMissing = true)
@Service
public class CustomServiceImpl implements CustomService {

    /**
     * 该方法应该是用户自定义的，框架不做任何实现，如果用户不需要自定义任何逻辑，采用框架默认的即可 默认返回true
     * 我们期望用户进行一些给予系统特点的处理
     * 如：
     *  1.一个refreshToken的使用次数的校验
     *  2.一个refreshToken的使用频率校验
     * @param refreshToken
     * @return
     */
    @Override
    public Boolean checkRefreshToken(String refreshToken,Object user) {
        return true;//对接redis
    }

    @Override
    public void customHook(TokenDTO accessTokenDTO, TokenDTO refreshTokenDTO,Object user) {
    }

    @Override
    public void afterRefreshHook(RefreshDTO res, Object payload) {
    }


}
