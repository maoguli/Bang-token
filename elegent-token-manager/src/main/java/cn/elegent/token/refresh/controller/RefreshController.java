package cn.elegent.token.refresh.controller;


import cn.elegent.token.constants.ElegentTokenConstant;
import cn.elegent.token.dto.ElegentUser;
import cn.elegent.token.dto.RefreshDTO;
import cn.elegent.token.dto.VerifyResult;
import cn.elegent.token.refresh.service.CustomService;
import cn.elegent.token.refresh.service.TokenResultErrorService;
import cn.elegent.token.refresh.service.TokenService;
import cn.elegent.token.refresh.service.UserService;
import cn.elegent.token.vo.RefreshVO;
import cn.elegent.util.json.JsonUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * elegentToken框架自带的刷新Token的接口
 *
 * @author wgl
 */
@RestController
@RequestMapping("/elegent")
public class RefreshController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CustomService customService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenResultErrorService tokenResultErrorService;

    /**
     * refreshToken
     *
     * @author: wgl
     * @describe: 使用刷新token来刷新访问token的方法
     * @date: 2022/12/28 10:28
     **/
    @PostMapping("/refreshToken")
    public RefreshDTO refreshToken(@RequestBody(required = false) Map params, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //校验刷新token是否合法
        String refreshToken = request.getHeader(ElegentTokenConstant.REFRESH_HEAD);
        String verifyJson = URLDecoder.decode(request.getHeader(ElegentTokenConstant.VERIFY_RESULT_KEY));
        verifyJson = jsonFormat(verifyJson);
        VerifyResult verifyResult = JsonUtil.getByJson(verifyJson, VerifyResult.class);
        Boolean flag = customService.checkRefreshToken(refreshToken, verifyResult.getPayload());
        if(flag == true){
            //用户自定义方法--可以在这里追加自定义逻辑  --- 如一个刷新token只能使用一次
            ElegentUser elegentUser = userService.getUserDetail(verifyResult.getUserId(),params);
            verifyResult.setPayload(elegentUser);
            RefreshDTO res = tokenService.createRefreshToken(verifyResult, refreshToken);
            customService.afterRefreshHook(res, verifyResult.getPayload());
            return res;
        }
        //Token校验失败处理
        return tokenResultErrorService.getForbidoenResult(response);
    }

    /**
     * URLDecoder解码之后的json数据格式化
     * @param verifyJson
     */
    private String jsonFormat(String verifyJson){
        verifyJson = verifyJson.replaceAll("\\\\","");
        verifyJson = verifyJson.substring(1, verifyJson.length() - 1);
        return verifyJson;
    }
}
