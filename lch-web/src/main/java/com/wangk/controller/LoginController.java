package com.wangk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangk.config.LoginRealm;
import com.wangk.config.ShiroProperties;
import com.wangk.config.ShiroRealm;
import com.wangk.config.VerificationCode;
import com.wangk.constant.RedisConstant;
import com.wangk.core.Result;
import com.wangk.core.ResultGenerator;
import com.wangk.utils.JwtUtils;
import com.wangk.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName :LoginController
 * @Description :TODO
 * @Author :16388
 * @Date :2020/5/12 14:54
 * @Version :1.0
 **/
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class LoginController {

    private final RedisUtils redisUtils;
    private final ShiroProperties properties;

    @PostMapping("/login")
    public Result login(HttpServletRequest request){
        Map<String, String> loginData = new HashMap<>(16);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            loginData = objectMapper.readValue(request.getInputStream(),Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String userName= loginData.get("username");
        String password = loginData.get("password");
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userName, password, LoginRealm.class.getName());
        Result result=null;
        try {
            if(redisUtils.hasKey(RedisConstant.PREFIX_SHIRO_CACHE+userName)){
                redisUtils.del(RedisConstant.PREFIX_SHIRO_CACHE+userName);
            }
            String currentTimeMillis = String.valueOf(System.currentTimeMillis());
            redisUtils.set(RedisConstant.PREFIX_SHIRO_REFRESH_TOKEN+userName,currentTimeMillis,properties.getRefreshTokenExpireTime());
            String toekn = JwtUtils.createToekn(userName, currentTimeMillis);
            SecurityUtils.getSubject().login(usernamePasswordToken);
            result= ResultGenerator.getSuccessInfo(toekn);
        }catch (Exception e) {
            e.printStackTrace();
            result = ResultGenerator.getFailureInfo("登录失败");
        }
        return result;
    }

    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        VerificationCode code = new VerificationCode();
        BufferedImage image = code.getImage();
        String text = code.getText();
        HttpSession session = request.getSession(true);
        session.setAttribute("verifyCode",text);
        VerificationCode.output(image,resp.getOutputStream());
    }

    @GetMapping("/logout")
    public Result logout(){

        return ResultGenerator.getSuccessInfo();
    }
}
