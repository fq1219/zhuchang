package com.zhuchang.vcode;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;

public class CaptchaUtil {

    private final static String CAPTCHA_SESSION_KEY = "CAPTCHA_SESSION_KEY";


    /**
     * map it to the /url/captcha.jpg
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public static void captcha(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Set to expire far in the past.
        resp.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        resp.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        resp.setHeader("Pragma", "no-cache");
        // return a jpeg
        resp.setContentType("image/gif");

        //取得session
        HttpSession session = req.getSession();
        /**
         * gif格式动画验证码
         * 宽，高，位数。
         */
        Captcha captcha = new GifCaptcha(146,33,4);
        //输出
        captcha.out(resp.getOutputStream());

        //存入Session
        session.setAttribute(CAPTCHA_SESSION_KEY, captcha.text().toLowerCase());

    }

    public static String getCaptcha(HttpServletRequest req) {
        HttpSession session = req.getSession();
        return (String) session.getAttribute(CAPTCHA_SESSION_KEY);
    }


    public static void clearCaptcha(HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.removeAttribute(CAPTCHA_SESSION_KEY);
    }
}
