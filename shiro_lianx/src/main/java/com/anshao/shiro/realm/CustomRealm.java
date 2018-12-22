package com.anshao.shiro.realm;

import com.anshao.dao.UserDao;
import com.anshao.domain.User;
import com.anshao.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userName = (String) principalCollection.getPrimaryPrincipal();
        //获取角色信息
        Set<String> roles = getRolesByUserName(userName);
        //获取权限信息
        Set<String> permissions = getPermissionByUserName(userName);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //设置我们的权限
        authorizationInfo.setStringPermissions(permissions);
        authorizationInfo.setRoles(roles);
        return authorizationInfo;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 1.从主体传过来的认证信息中，获得用户名
        String userName =(String) authenticationToken.getPrincipal();
        // 2.用户名到数据库中查询获取凭证
        String password = getPasswordByUserName(userName);
        if (password == null) {
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName, password, getName());
        // 加盐
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(userName));
        return authenticationInfo;
    }

    //获取角色信息
    private Set<String> getRolesByUserName(String username) {
        List<String> list = userService.queryRoleByUserName(username);
        Set<String> sets = new HashSet<>(list);
        return sets;
    }

    //获取权限
    private Set<String> getPermissionByUserName(String userName) {
        Set<String> set = new HashSet<>();
        set.add("user:delete");
        set.add("user:add");
        return set;
    }

    //获取密码
    private String getPasswordByUserName(String userName) {
        User user = userService.getUserByUserName(userName);
        if (user != null) {
            return user.getPassword();
        }
        return null;
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("admin123", "root",1);
        System.out.println(md5Hash.toString());
    }
}
