package com.zhuchang.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("开始检测是否登录");
        log.info(request.getRequestURL().toString());
        // 判断是否已有该用户登录的session
        if (request.getSession().getAttribute("login_user") != null) {
            log.info("用户已登录");
            return true;
        }
        log.info("用户尚未登录");
        // 跳转到登录页
        response.sendRedirect(request.getContextPath() + "/login");
        return false;
    }
}
