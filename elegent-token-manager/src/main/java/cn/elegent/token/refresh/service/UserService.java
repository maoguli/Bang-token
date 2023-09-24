package cn.elegent.token.refresh.service;

import cn.elegent.token.dto.ElegentUser;
import cn.elegent.token.vo.RefreshVO;
import lombok.Builder;

import java.util.Map;

/**
 * 提供用户的业务层
 */
public interface UserService {

    /**
     * 根据用户id查询用户对象的方法
     * @param userId
     * @return
     */
    public ElegentUser getUserDetail(String userId, Map params);
}
