package com.wangk.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangk.Jwt.JwtToken;
import com.wangk.constant.RedisConstant;
import com.wangk.model.Permission;
import com.wangk.model.Role;
import com.wangk.model.User;
import com.wangk.service.UserService;
import com.wangk.utils.JwtUtils;
import com.wangk.utils.RedisUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName :ShiroRealm
 * @Description :TODO
 * @Author :16388
 * @Date :2020/5/12 10:11
 * @Version :1.0
 **/
public class LoginRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtils redisUtils;
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",principals.getPrimaryPrincipal());
        User user = userService.getOne(queryWrapper);
        //把principals放session中 key=userId value=principals
        SecurityUtils.getSubject().getSession().setAttribute(user.getId(), SecurityUtils.getSubject().getPrincipals());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //赋予角色
        for (Role role :user.getRoles()) {
            info.addRole(role.getName());
        }
        //赋予权限
        for (Role role :user.getRoles()) {
            List<Permission> permis = role.getPermis();
            for (Permission permission:permis) {
                info.addStringPermission(permission.getName());
            }
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String username = (String) auth.getPrincipal();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User user = userService.getOne(queryWrapper);
        ByteSource salt = ByteSource.Util.bytes("wangk");
        if (user == null) {
            throw new AuthenticationException("用户不存在");
        }
        return new SimpleAuthenticationInfo(username,user.getPassword(),salt,getName());
    }
}
