package cn.elegent.token.refresh.service;

import cn.elegent.token.dto.RefreshDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TokenResultErrorService
 *
 * @author: wgl
 * @describe: token结果集封装
 * @date: 2022/12/28 10:10
 */
public interface TokenResultErrorService {
    /**
     * 获取静止访问的接口
     * @return
     */
    RefreshDTO getForbidoenResult(HttpServletResponse response);
}
