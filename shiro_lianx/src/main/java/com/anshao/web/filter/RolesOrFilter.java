package com.anshao.web.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class RolesOrFilter extends AuthorizationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        //获取当前主题
        Subject subject = getSubject(servletRequest, servletResponse);
        String[] roles = (String[]) o;
        if (roles == null || roles.length == 0) {
            return true;
        }
        for (String role : roles) {
            //如果当前主题有角色的直接访问成功
            if (subject.hasRole(role)) {
                return true;
            }
        }
        return false;
    }
}
