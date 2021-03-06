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
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
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
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtils redisUtils;
    /**
     * 大坑，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
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
        String token = (String) auth.getCredentials();
        if (StringUtils.isBlank(token)) {
            throw new AuthenticationException("token cannot be empty.");
        }
        String username = JwtUtils.getClaim(token,"username");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User user = userService.getOne(queryWrapper);
        ByteSource salt = ByteSource.Util.bytes("wangk");
        boolean verify = JwtUtils.verify(token,"username");
        boolean hasKey = redisUtils.hasKey(RedisConstant.PREFIX_SHIRO_REFRESH_TOKEN + username);
        if (verify&&hasKey){
            String currentTimeMillisRedis = redisUtils.get(RedisConstant.PREFIX_SHIRO_REFRESH_TOKEN+username).toString();
            if (JwtUtils.getClaim(token,"currentTimeMillis").equals(currentTimeMillisRedis)){
                String realmName = this.getName();
                return new SimpleAuthenticationInfo(token,token,realmName);
            }
        }
        if (user == null) {
            throw new AuthenticationException("用户不存在");
        }
        throw new AuthenticationException("token expired or incorrect.");
    }
}
