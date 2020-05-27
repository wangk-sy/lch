package com.wangk.filter;

import com.alibaba.fastjson.JSONObject;
import com.wangk.core.ResultGenerator;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName :MyRolesAuthorizationFilter
 * @Description :权限不足处理
 * @Author :16388
 * @Date :2020/5/14 18:11
 * @Version :1.0
 **/
public class MyRolesAuthorizationFilter extends RolesAuthorizationFilter  {
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse res = (HttpServletResponse)response;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",401);
        jsonObject.put("msg","权限不足");
        res.getWriter().write(jsonObject.toJSONString());
        return false;
    }
}
