package com.zhuchang.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.zhuchang.shiro.RetryLimitHashedCredentialsMatcher;
import com.zhuchang.shiro.UserRealm;
import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;
import java.util.Map;


/*
Shiro 配置
1.LifecycleBeanPostProcessor，这是个DestructionAwareBeanPostProcessor的子类，负责org.apache.shiro.util.Initializable类型bean的生命周期的，初始化和销毁。主要是AuthorizingRealm类的子类，以及EhCacheManager类。
2.HashedCredentialsMatcher，这个类是为了对密码进行编码的，防止密码在数据库里明码保存，当然在登陆认证的生活，这个类也负责对form里输入的密码进行编码。
3.ShiroRealm，这是个自定义的认证类，继承自AuthorizingRealm，负责用户的认证和权限的处理，可以参考JdbcRealm的实现。
4.EhCacheManager，缓存管理，用户登陆成功后，把用户信息和权限信息缓存起来，然后每次用户请求时，放入用户的session中，如果不设置这个bean，每个请求都会查询一次数据库。
5.SecurityManager，权限管理，这个类组合了登陆，登出，权限，session的处理，是个比较重要的类。
6.ShiroFilterFactoryBean，是个factorybean，为了生成ShiroFilter。它主要保持了三项数据，securityManager，filters，filterChainDefinitionManager。
7.DefaultAdvisorAutoProxyCreator，Spring的一个bean，由Advisor决定对哪些类的方法进行AOP代理。
8.AuthorizationAttributeSourceAdvisor，shiro里实现的Advisor类，内部使用AopAllianceAnnotationsAuthorizingMethodInterceptor来拦截用以下注解的方法。
*/
@Slf4j
@Configuration
public class ShiroConfig {
    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，以为在初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     * <p>
     * Filter Chain定义说明 1、一个URL可以配置多个Filter，使用逗号分隔 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 任何访问没有认证的情况下，重定向到下面的url。
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/webs/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/webs/index");
        // 未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/web/kitadmin/error/403.html");

        // 拦截器.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/webs/doLogin", "anon");
        filterChainDefinitionMap.put("/web/**", "anon");
        filterChainDefinitionMap.put("/mobile/**", "anon");
        filterChainDefinitionMap.put("/webs/captcha", "anon");

        // 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/webs/logout", "logout");
        //filterChainDefinitionMap.put("/add", "perms[权限添加]");

        /*过滤链定义，从上向下顺序执行，一般将放在最为下边
        authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问*/
        filterChainDefinitionMap.put("/**", "authc");
        //filterChainDefinitionMap.put("/**", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    /**
     * 定义Shiro安全管理配置
     * @param sessionManager
     * @param ehCacheManager
     * @param rememberMeManager
     * @param shiroRealm
     * @return
     */
    @Bean
    public SecurityManager securityManager(DefaultWebSessionManager sessionManager,
                                           CacheManager ehCacheManager,
                                           CookieRememberMeManager rememberMeManager,
                                           AuthorizingRealm shiroRealm) {
        //设置 SecurityManager
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(shiroRealm);
        //缓存管理
        securityManager.setSessionManager(sessionManager);
        // <!-- 用户授权/认证信息Cache, 采用EhCache 缓存 -->
        securityManager.setCacheManager(ehCacheManager);
        //注入记住我管理器;
        securityManager.setRememberMeManager(rememberMeManager);
        return securityManager;
    }

    /**
     * 把shiro生命周期交给spring boot管理
     * @return
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 身份认证realm; (这个需要自己写，账号密码校验；权限等)
     *
     * @return
     */
    @Bean
    public UserRealm shiroRealm(HashedCredentialsMatcher credentialsMatcher) {
        UserRealm userRealm = new UserRealm();
        //加密方式
        userRealm.setCredentialsMatcher(credentialsMatcher);
        return userRealm;
    }

    /**
     * @Bean
     * public EhCacheManager ehCacheManager(EhCacheCacheManager cacheManager) {
     *     EhCacheManager em = new EhCacheManager();
     *     //将ehcacheManager转换成shiro包装后的ehcacheManager对象
     *     em.setCacheManager(cacheManager);
     *     //em.setCacheManagerConfigFile("classpath:ehcache.xml");
     *     return em;
     * }
     * 这是配置shiro的缓存管理器org.apache.shiro.cache.ehcach.EhCacheManager，上面的方法的参数是把spring容器中的cacheManager对象注入到EhCacheManager中，这样就实现了shiro和缓存注解使用同一种缓存方式。
     * 在这里最大的误区是下面这样配置：
     *     @Bean
     *     public EhCacheManager ehCacheManager(){
     *        EhCacheManager cacheManager = new EhCacheManager();
     *        cacheManager.setCacheManagerConfigFile("classpath:config/ehcache.xml");
     *        returncacheManager;
     *     }
     * 一定不要这么配置，这只是shiro集成了ehcache缓存，根本没有交给spring容器去管理
     * @return
     */
    @Bean
    public EhCacheManager ehCacheManager(EhCacheCacheManager cacheManager) {
        EhCacheManager em = new EhCacheManager();
        //将ehcacheManager转换成shiro包装后的ehcacheManager对象
        em.setCacheManager(cacheManager.getCacheManager());
        return em;
    }

/*    *//**
     * 配置加密方式
     * @return
     *//*
    @Bean
    public HashedCredentialsMatcher credentialsMatcher() {
        //加密方式
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setHashIterations(1024);
        return credentialsMatcher;
    }*/

    /**
     * 1 配置加密方式
     * 2 登录5次错误，锁定用户十分钟
     * @return
     */
    @Bean
    public RetryLimitHashedCredentialsMatcher credentialsMatcher(CacheManager ehCacheManager) {
        //加密方式
        RetryLimitHashedCredentialsMatcher credentialsMatcher = new RetryLimitHashedCredentialsMatcher(ehCacheManager);
        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setHashIterations(1024);
        return credentialsMatcher;
    }


    /**
     * 定义Shiro session管理配置
     * @return
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        //缓存管理
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(1800000);//30分钟
        //sessionManager.setGlobalSessionTimeout(60000);//1分钟
        //sessionManager.setGlobalSessionTimeout(-10001);//永不过期
        return sessionManager;
    }

    /**
     * 配置cookie
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        // 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        // <!-- 记住我cookie生效时间3天 ,单位秒;-->
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }

    /**
     * 配置 rememberMeManager
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(SimpleCookie rememberMeCookie) throws Base64DecodingException {
        //cookie管理对象;
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie);
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
        return cookieRememberMeManager;
    }

    /**
     * 开启shiro aop注解支持.使用代理方式;所以需要开启代码支持;
     * 在lifecycleBeanPostProcessor之后运行
     * @return
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;

    }

    /**
     * 整合thymeleaf 的shiro标签
     * @return
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}
