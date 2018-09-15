package com.zhuchang.shiro;

import com.zhuchang.entity.User;
import com.zhuchang.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

@Component
public class UserRealm extends AuthorizingRealm {

    /**
     * 将shiro和ehcache集成，使用的是shiro框架提供的缓存管理器，
     * 有个需要特别注意的是，UserRealm里注入的SysUserService等service，
     * 需要延迟注入，所以都要添加@Lazy注解(如果不加需要自己延迟注入)，
     * 否则会导致该service里的@Cacheable缓存注解、@Transactional事务注解等失效。
     */
    @Resource
    @Lazy
    UserService userService;


    /*授权*/
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //PrincipalCollection是一个身份集合，因为我们现在就一个Realm，所以直接调用getPrimaryPrincipal得到之前传入的用户名即可
        User user = (User)principals.getPrimaryPrincipal();
        //在组装SimpleAuthorizationInfo信息
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        Map<String,Set<String>> roleMap = userService.findRoles(user.getId());
        simpleAuthorizationInfo.setRoles(roleMap.get("roles"));
        simpleAuthorizationInfo.setStringPermissions(userService.findPermissions(roleMap.get("roleIds")));

        return simpleAuthorizationInfo;
    }

    /*认证*/
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();

        User user = userService.findByUsername(username);
        // 账号不存在
        if (user == null) {
            throw new UnknownAccountException("用户名不正确");
        }

        // 账号锁定
        if (user.getDelFlag() == 0) {
            throw new LockedAccountException("账号已被管理员锁定,请联系管理员");

        }
        //在组装SimpleAuthenticationInfo信息时，需要传入：身份信息（用户名）、凭据（密文密码）、盐（username+salt），
        // CredentialsMatcher(HashedCredentialsMatcher)使用盐加密传入的明文密码和此处的密文密码进行匹配
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                user,
                user.getPassword(),
                ByteSource.Util.bytes(user.getUsername()),
                this.getName()
        );
        return  simpleAuthenticationInfo;

    }
}
