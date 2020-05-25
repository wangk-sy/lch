package com.wangk.filter;

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
        res.getWriter().write(ResultGenerator.getFailureInfo("尚未登录").toString());
        return false;
    }
}
