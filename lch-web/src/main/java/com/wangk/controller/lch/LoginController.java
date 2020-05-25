package com.wangk.controller.lch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangk.config.ShiroRealm;
import com.wangk.config.VerificationCode;
import com.wangk.core.Result;
import com.wangk.core.ResultGenerator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
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
public class LoginController {

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
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userName, password, ShiroRealm.class.getName());
        Result result=null;
        try {
            SecurityUtils.getSubject().login(usernamePasswordToken);
            result= ResultGenerator.getSuccessInfo();
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

}
