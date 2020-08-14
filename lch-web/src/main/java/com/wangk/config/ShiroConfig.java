package com.wangk.config;

import com.wangk.filter.JwtFilter;
import com.wangk.filter.LoginFilter;
import com.wangk.filter.MyRolesAuthorizationFilter;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.*;

/**
 * @ClassName :ShiroConfig
 * @Description :TODO
 * @Author :16388
 * @Date :2020/5/7 9:37
 * @Version :1.0
 **/
@Configuration
public class ShiroConfig {

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 初始化Authenticator验证管理器，如不注入，则会导致验证失败返回未登录
     * Authorizer授权器：赋予主体有哪些权限
     */
    @Bean
    public Authenticator authenticator() {
        //扩展父类原方法，捕获原始异常
        ModularRealmAuthenticator authenticator = new MyModularRealmAuthenticator();
        //设置两个Realm，一个用于用户登录验证和访问权限获取；一个用于jwt token的认证
        authenticator.setRealms(Arrays.asList(shiroRealm(), loginRealm()));
        /**
         FirstSuccessfulStrategy：只要有一个 Realm 验证成功即可，只返回第一个 Realm 身份验证成功的认证信息，其他的忽略；
         AtLeastOneSuccessfulStrategy：只要有一个 Realm 验证成功即可，和FirstSuccessfulStrategy 不同，返回所有 Realm 身份验证成功的认证信息；（默认）
         AllSuccessfulStrategy：所有 Realm 验证成功才算成功，且返回所有 Realm
         身份验证成功的认证信息，如果有一个失败就失败了。
         */
        //设置多个realm认证策略，一个成功即跳过其它的
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return authenticator;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        hashedCredentialsMatcher.setHashIterations(2);
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }
    /**
     * ShiroRealm，这是个自定义的认证类，继承自AuthorizingRealm，
     * 负责用户的认证和权限的处理，可以参考JdbcRealm的实现。
     */
    @Bean(name="shiroRealm")
    @DependsOn("lifecycleBeanPostProcessor")
    public ShiroRealm shiroRealm() {
        ShiroRealm shiroRealm = new ShiroRealm();
//        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return shiroRealm;
    }
    @Bean(name="loginRealm")
    @DependsOn("lifecycleBeanPostProcessor")
    public LoginRealm loginRealm() {
        LoginRealm loginRealm = new LoginRealm();
        loginRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return loginRealm;
    }
    /**
     * SecurityManager，权限管理，这个类组合了登陆，登出，权限，session的处理，是个比较重要的类。
     * //
     */
    @Bean("securityManager")
    public DefaultSecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置自定义realm
        Set<Realm> realms = new HashSet<Realm>();
        realms.add(shiroRealm());
        realms.add(loginRealm());
        securityManager.setRealms(realms);
        //关闭shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        securityManager.setAuthenticator(authenticator());
        return securityManager;
    }
    /**
     * ShiroFilterFactoryBean，是个factorybean，为了生成ShiroFilter。
     * 它主要保持了三项数据，securityManager，filters，filterChainDefinitionManager。
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        shiroFilterFactoryBean.setSecurityManager(securityManager());
        Map<String, Filter> filters = new LinkedHashMap<String, Filter>();

        filters.put("jwtFilter",new JwtFilter());
        filters.put("authc",new LoginFilter());
        filters.put("roles",new MyRolesAuthorizationFilter());
        shiroFilterFactoryBean.setFilters(filters);
        Map<String, String> filterChainDefinitionManager = new LinkedHashMap<String, String>();
        filterChainDefinitionManager.put("/logout", "anon");
        filterChainDefinitionManager.put("/login", "anon");
        filterChainDefinitionManager.put("/verifyCode","anon");
        filterChainDefinitionManager.put("/user/viewImg","anon");
        filterChainDefinitionManager.put("/**","jwtFilter");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionManager);
        return shiroFilterFactoryBean;
    }
    /**
     * DefaultAdvisorAutoProxyCreator，Spring的一个bean，由Advisor决定对哪些类的方法进行AOP代理。
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    /**
     * AuthorizationAttributeSourceAdvisor，shiro里实现的Advisor类，
     * 内部使用AopAllianceAnnotationsAuthorizingMethodInterceptor来拦截用以下注解的方法。
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor aASA = new AuthorizationAttributeSourceAdvisor();
        aASA.setSecurityManager(securityManager());
        return aASA;
    }

}
