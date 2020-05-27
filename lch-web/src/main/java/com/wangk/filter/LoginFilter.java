package com.wangk.filter;

import com.alibaba.fastjson.JSONObject;
import com.wangk.core.Result;
import com.wangk.core.ResultGenerator;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName :LoginFilter
 * @Description :认证不通过处理
 * @Author :16388
 * @Date :2020/5/14 18:05
 * @Version :1.0
 **/
public class LoginFilter extends FormAuthenticationFilter {
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse res = (HttpServletResponse)response;
        res.setContentType("application/json;charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",401);
        jsonObject.put("msg","尚未登录");
        res.getWriter().write(jsonObject.toJSONString());
        return false;
    }
}
