package com.wangk.filter;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.wangk.Jwt.JwtToken;
import com.wangk.config.ShiroProperties;
import com.wangk.constant.RedisConstant;
import com.wangk.core.ResultGenerator;
import com.wangk.exception.CustomException;
import com.wangk.utils.JsonConvertUtil;
import com.wangk.utils.JwtUtils;
import com.wangk.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName :JwtFilter
 * @Description :TODO
 * @Author :16388
 * @Date :2020/6/9 9:49
 * @Version :1.0
 **/
public class JwtFilter  extends BasicHttpAuthenticationFilter {
    private AntPathMatcher pathMatcher = new AntPathMatcher();
    @Autowired
    private RedisUtils redisUtils;
    @Value("${jwt.refreshToken-expireTime}")
    private String refreshTokenExpireTime;
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        if (this.isLoginAttempt(request, response)){
            //用户是登录操作
            try {
                this.executeLogin(request, response);
            } catch (Exception e) {
                // 认证出现异常，传递错误信息msg
                String msg = e.getMessage();
                // 获取应用异常(该Cause是导致抛出此throwable(异常)的throwable(异常))
                Throwable throwable = e.getCause();
                if (throwable != null && throwable instanceof SignatureVerificationException) {
                    // 该异常为JWT的AccessToken认证失败(Token或者密钥不正确)
                    msg = "token或者密钥不正确(" + throwable.getMessage() + ")";
                } else if (throwable != null && throwable instanceof TokenExpiredException) {
                    // 该异常为JWT的AccessToken已过期，判断RefreshToken未过期就进行AccessToken刷新
                    if (this.refreshToken(request, response)) {
                        return true;
                    } else {
                        msg = "token已过期(" + throwable.getMessage() + ")";
                    }
                } else {
                    // 应用异常不为空
                    if (throwable != null) {
                        // 获取应用异常msg
                        msg = throwable.getMessage();
                    }
                }
                /**
                 * 错误两种处理方式 1. 将非法请求转发到/401的Controller处理，抛出自定义无权访问异常被全局捕捉再返回Response信息 2.
                 * 无需转发，直接返回Response信息 一般使用第二种(更方便)
                 */
                // 直接返回Response信息
                this.response401(request, response, msg);
                return false;
            }
        }
        return true;
    }
    /**
     * 刷新AccessToken，进行判断RefreshToken是否过期，未过期就返回新的AccessToken且继续正常访问
     */
    private boolean refreshToken(ServletRequest request, ServletResponse response) {
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        String token = this.getAuthzHeader(request);
        // 获取当前Token的帐号信息
        String account = JwtUtils.getClaim(token, "username");
        // 判断Redis中RefreshToken是否存在
        if (redisUtils.hasKey(RedisConstant.PREFIX_SHIRO_REFRESH_TOKEN + account)) {
            // Redis中RefreshToken还存在，获取RefreshToken的时间戳
            String currentTimeMillisRedis = redisUtils.get(RedisConstant.PREFIX_SHIRO_REFRESH_TOKEN + account).toString();
            // 获取当前AccessToken中的时间戳，与RefreshToken的时间戳对比，如果当前时间戳一致，进行AccessToken刷新
            if (JwtUtils.getClaim(token, "currentTimeMillis").equals(currentTimeMillisRedis)) {
                // 获取当前最新时间戳
                String currentTimeMillis = String.valueOf(System.currentTimeMillis());
                // 读取配置文件，获取refreshTokenExpireTime属性
                // PropertiesUtil.readProperties("config.properties");
                // String refreshTokenExpireTime =
                // PropertiesUtil.getProperty("refreshTokenExpireTime");
                // 设置RefreshToken中的时间戳为当前最新时间戳，且刷新过期时间重新为30分钟过期(配置文件可配置refreshTokenExpireTime属性)
                redisUtils.set(RedisConstant.PREFIX_SHIRO_REFRESH_TOKEN + account, currentTimeMillis,
                        Integer.parseInt(refreshTokenExpireTime));
                // 刷新AccessToken，设置时间戳为当前最新时间戳
                token = JwtUtils.createToekn(account, currentTimeMillis);
                // 将新刷新的AccessToken再次进行Shiro的登录
                JwtToken jwtToken = new JwtToken(token);
                // 提交给UserRealm进行认证，如果错误他会抛出异常并被捕获，如果没有抛出异常则代表登入成功，返回true
                this.getSubject(request, response).login(jwtToken);
                // 最后将刷新的AccessToken存放在Response的Header中的Authorization字段返回
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setHeader("Authorization", token);
                httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
                return true;
            }
        }
        return false;
    }
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String requestURI = req.getRequestURI();
        boolean match = pathMatcher.match("/login", requestURI);
        if (match){
            return false;
        }
        boolean match1 = pathMatcher.match("/viewImg", requestURI);
        if (match1){
            return false;
        }
        return true;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        //拿到当前Header中的Authorization的token
        String authzHeader = this.getAuthzHeader(request);
        JwtToken jwtToken = new JwtToken(this.getAuthzHeader(request));
        //执行登录操作，交给realm进行验证
        this.getSubject(request, response).login(jwtToken);
        return true;
    }
    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers",
                httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
    private void response401(ServletRequest req, ServletResponse resp, String msg) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        ServletOutputStream outputStream = null;
        try {
             outputStream = httpServletResponse.getOutputStream();
            String data = JsonConvertUtil.objectToJson(ResultGenerator.getFailureInfo(msg));
            outputStream.write(data.getBytes());
        } catch (IOException e) {
            throw new CustomException("直接返回Response信息出现IOException异常:" + e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
