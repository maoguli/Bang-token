package cn.elegent.token.filters;


import cn.elegent.token.configs.TokenGateWayConfig;
import cn.elegent.token.constants.ElegentTokenConstant;
import cn.elegent.token.dto.VerifyResult;
import cn.elegent.token.exceptions.TokenException;
import cn.elegent.token.service.TokenService;
import cn.elegent.util.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * JWT filter
 */
@Component
@Slf4j
public class TokenFilter implements GlobalFilter, Ordered {

    private List<HttpMessageReader<?>> messageReaders;

    @Autowired
    private TokenGateWayConfig tokenConfig;

    @Autowired
    private TokenService tokenService;

    /**
     * 不进行校验token的请求
     */
    private static Set<String> ALLOWED_PATHS = new HashSet<String>();

    /**
     * 静态资源类目
     */
    private static Set<String> staticResources = new HashSet<String>(){
        {
            add("js");
            add("css");
            add("ico");
            add("jpg");
            add("png");
        }
    };

    @PostConstruct
    public void init(){
        String[] ignoreUrl = tokenConfig.getIgnoreUrl();
        for (String index: ignoreUrl) {
            ALLOWED_PATHS.add(index);
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        //获取当前请求的url
        String url = exchange.getRequest().getURI().getPath();
        //如果是不需要token拦截的路径 直接放行
        if (checkAllowedPathOrStaticResources(url)) {
            return chain.filter(exchange);
        } else if (url.contains(TokenGateWayConfig.REFRESHURL)) {
            try {
                //如果是刷新Token接口，则说明需要对刷新Token进行校验
                String refreshToken = request.getHeaders().get(tokenConfig.getRefreshTokenName()).get(0);
                VerifyResult result = tokenService.verifyRefreshToken(refreshToken);
                if (result.isOK()) {
                    //如果校验成功将校验结果传递到下一层
                    String encode = URLEncoder.encode(JsonUtil.serialize(JsonUtil.serialize(result)), "UTF-8");
                    request = exchange.getRequest().mutate()
                            //==============传递框架必须打包传递的参数=====================
                            .header(ElegentTokenConstant.VERIFY_RESULT_KEY, encode)
                            .header(ElegentTokenConstant.REFRESH_HEAD, refreshToken)
                            .build();
                    return chain.filter(exchange.mutate().request(request).build());
                }
            } catch (Exception e) {
                throw new TokenException("refresh token is error");
            }
        } else {
            //从header中获取访问token
            String token = request.getHeaders().get(tokenConfig.getAccessTokenName()).get(0);
            if (null == token) {
                throw new TokenException("illegal request，token is necessary!");
            }
            //首先校验token是否合法---校验的方法由于每个系统的校验逻辑不一样---需要客户微服务自己实现
            VerifyResult result = tokenService.verifyToken(token);
            if (result.isOK()) {
                //证明拿到了用户角色--在请求头上设置
                try {
                    String encode = URLEncoder.encode(JsonUtil.serialize(result.getPayload()), "UTF-8");
                    request = exchange.getRequest().mutate()
                            //==============传递框架必须打包传递的参数=====================
                            .header(ElegentTokenConstant.PAYLOAD_KEY, encode)
                            .header(ElegentTokenConstant.USERID_KEY, result.getUserId())
                            .build();
                } catch (Exception e) {
                    throw new TokenException("token build error,data is :" + result);
                }
                return chain.filter(exchange.mutate().request(request).build());
            }
        }
        //AccessToken校验失败  用户需要重新登录
        return authError(exchange.getResponse());
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 校验是静态资源或
     * @return
     */
    private boolean checkAllowedPathOrStaticResources(String path){
        boolean allowedPath = isAllowPath(path);
        boolean staticFlag = false;
        if(path.contains(".")){
            String[] split = path.split("\\.");
            if(staticResources.contains(split[1])){
                staticFlag = true;
            }
        }
        return allowedPath||staticFlag;
    }

    /**
     * 判断是否是
     * @param path
     * @return
     */
    private boolean isAllowPath(String path) {
        boolean falg = false;
        for (String index : ALLOWED_PATHS) {
            if(path.contains(index)){
                falg = true;
                break;
            }
        }
        return ALLOWED_PATHS.contains(path)||falg;
    }

     /**
      * 认证错误输出
      * @param resp 响应对象
      * @return
      */
    private Mono<Void> authError(ServerHttpResponse resp) {
        resp.setStatusCode(HttpStatus.UNAUTHORIZED);
        resp.getHeaders().add("Content-Type","application/json;charset=UTF-8");
        String returnStr = "token校验失败，用户重新登录";
        DataBuffer buffer = resp.bufferFactory().wrap(returnStr.getBytes(StandardCharsets.UTF_8));
        return resp.writeWith(Flux.just(buffer));
    }
}
