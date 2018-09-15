package com.zhuchang.controller;


import com.zhuchang.util.ResultUtil;
import com.zhuchang.vcode.CaptchaUtil;
import com.zhuchang.entity.Menu;
import com.zhuchang.entity.User;
import com.zhuchang.service.MenuService;
import com.zhuchang.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("webs")
public class LoginController {

    @Autowired
    MenuService menuService;

    @RequestMapping("/doLogin")
    @ResponseBody
    public Result doLogin(String username, String password, String captcha, boolean rememberMe, HttpServletRequest request){

        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username,password,rememberMe);
        String error = "";
        String inputId = "";

        //验证码
        String cap = CaptchaUtil.getCaptcha(request);
        if(StringUtils.isBlank(cap) || !cap.equalsIgnoreCase(captcha)){
            inputId = "captcha";
            return ResultUtil.getFailResult(inputId," 验证码输入错误！");
        }

        //清出验证码
        CaptchaUtil.clearCaptcha(request);

        //如果没有验证过
        if(!currentUser.isAuthenticated()) {

            try {
                //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
                //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
                //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
                log.info("对用户[" + username + "]进行登录验证..验证开始");
                currentUser.login(token);
                log.info("对用户[" + username + "]进行登录验证..验证通过");
            } catch (UnknownAccountException uae) {
                log.info("对用户[" + username + "]进行登录验证..验证未通过,未知账户");
                error =  "未知账户";
                inputId = "username";
            } catch (IncorrectCredentialsException ice) {
                log.info("对用户[" + username + "]进行登录验证..验证未通过,错误的凭证");
                error = "密码不正确";
                inputId = "password";
            } catch (LockedAccountException lae) {
                log.info("对用户[" + username + "]进行登录验证..验证未通过,账户已锁定");
                error ="账户已锁定";
                inputId = "username";
            } catch (ExcessiveAttemptsException eae) {
                log.info("对用户[" + username + "]进行登录验证..验证未通过,错误次数大于5次,账户已锁定");
                error ="用户名或密码错误次数大于5次,账户已锁定";
                inputId = "username";
            } catch (DisabledAccountException sae) {
                log.info("对用户[" + username + "]进行登录验证..验证未通过,帐号已经禁止登录");
                error ="帐号已经禁止登录";
                inputId = "username";
            } catch (AuthenticationException ae) {
                //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
                log.info("对用户[" + username + "]进行登录验证..验证未通过,堆栈轨迹如下");
                ae.printStackTrace();
                error ="用户名或密码不正确";
                inputId = "username";
            }
            //验证是否登录成功
            if (currentUser.isAuthenticated()) {
                log.info("用户[" + username + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");

                return ResultUtil.getSuccessResult();
            } else {
                token.clear();
                return ResultUtil.getFailResult(inputId,error);
            }
        }

        return ResultUtil.getSuccessResult();
    }

    /**
     * 生成验证码
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CaptchaUtil.captcha(request, response);
    }


//    @RequestMapping("/logout")
//    public String logout(){
//        SecurityUtils.getSubject().logout();
//        return "redirect:/login";
//    }

    @RequestMapping(value={"/index"})
    public String index(Model model){
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        //List<Menu> menuList = menuService.selectAllMenu();
        //根据用户取得菜单
        Set<Menu> menuList = menuService.selectMenuByUser(user.getId());
        model.addAttribute("menuList", menuList);
        model.addAttribute("user", user);

        return "webs/index";
    }


}
