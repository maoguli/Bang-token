package cn.elegent.token.config;

import cn.elegent.token.interceptors.ElegentTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * InterceptorConfig
 *
 * @author: wgl
 * @describe: TODO
 * @date: 2022/12/28 10:10
 */
@Configuration//自动装配;配置类
public class InterceptorConfig implements WebMvcConfigurer {//实现WebMvc-->重写
    @Autowired
    private ElegentTokenInterceptor tokenInterceptor;//注入令牌拦截器Bean

    @Override
    public void addInterceptors(InterceptorRegistry registry) {//添加拦截器并传入拦截器注册信息(注册器的图纸)
        //addInterceptor中传入的必须是HandlerInterceptor下的具体实现
        //因为tokenInterceptor实现了HandlerInterceptor,所以可以传入tokenInterceptor自定义的拦截器
        registry.addInterceptor(this.tokenInterceptor).addPathPatterns("/**");//这个拦截器的作用是拦截所有
    }
}