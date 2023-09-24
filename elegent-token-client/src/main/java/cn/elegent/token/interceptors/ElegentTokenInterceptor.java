package cn.elegent.token.interceptors;

import cn.elegent.token.constants.ElegentTokenConstant;
import cn.elegent.token.context.ElegentTokenContext;
import cn.elegent.util.json.JsonUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Map;

/**
 * ElegentTokenInterceptor
 *
 * @author: wgl
 * @describe: 权限拦截器
 * @date: 2022/12/28 10:10
 */
@Component //自定义的拦截器组件单例模式
public class ElegentTokenInterceptor implements HandlerInterceptor {//实现处理器拦截器

    /**
     * 前置拦截--封装业务数据到ThreadLocal中 本地线程
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override //实现处理器拦截器重写方法
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //得到有效密钥
        String payLoad = request.getHeader(ElegentTokenConstant.PAYLOAD_KEY);
        //得到用户ID密钥
        String userId = request.getHeader(ElegentTokenConstant.USERID_KEY);
        //载荷不为空
        if(!StringUtils.isEmpty(payLoad)){
            //以UTF-8格式对payLoad进行解码
            String decode = URLDecoder.decode(payLoad, "UTF-8");
            //给入decode 是Json格式的字符创进行反序列化用Map封装然后设置到当前登录用户对象上下文的LoginUser中
            ElegentTokenContext.setLoginUser(JsonUtil.getByJson(decode, Map.class));
        }//
        if(!StringUtils.isEmpty(userId)){
            ElegentTokenContext.setUserId(userId);
        }
        return true;
    }

    /**
     * 回收资源
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ElegentTokenContext.remove();
    }

    /**
     * 回收资源
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ElegentTokenContext.remove();
    }
}
