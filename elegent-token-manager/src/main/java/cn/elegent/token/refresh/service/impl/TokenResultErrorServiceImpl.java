package cn.elegent.token.refresh.service.impl;

import cn.elegent.token.dto.RefreshDTO;
import cn.elegent.token.refresh.service.TokenResultErrorService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

/**
 * TokenResultErrorServiceImpl
 *
 * @author: wgl
 * @describe: 当无法进行Token刷新时的返回值
 * @date: 2022/12/28 10:10
 */
@ConditionalOnProperty(prefix = "elegent.token.onError",name = "strategy",havingValue = "default",matchIfMissing = true)
@Service
public class TokenResultErrorServiceImpl implements TokenResultErrorService {

    /**
     * 获取静止访问的时候的结果
     * @param response
     * @return
     */
    @Override
    public RefreshDTO getForbidoenResult(HttpServletResponse response){
        try {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "无法刷新token");
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
