package com.zhuchang.util;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class KaptchaUtil {
    private Properties props = new Properties();

    private Producer kaptchaProducer = null;

    private String sessionKeyValue = null;

    private String sessionKeyDateValue = null;

    public KaptchaUtil() {
        // Switch off disk based caching.
        ImageIO.setUseCache(false);

        props.setProperty("kaptcha.border", "yes");
        props.setProperty("kaptcha.border.color", "105,179,90");
        props.setProperty("kaptcha.textproducer.font.color", "blue");
        props.setProperty("kaptcha.image.width", "125");
        props.setProperty("kaptcha.image.height", "45");
        props.setProperty("kaptcha.session.key", "code");
        props.setProperty("kaptcha.textproducer.char.length", "4");
        props.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");

        Config config = new Config(this.props);
        this.kaptchaProducer = config.getProducerImpl();
        this.sessionKeyValue = config.getSessionKey();
        this.sessionKeyDateValue = config.getSessionDate();
    }

    /**
     * map it to the /url/captcha.jpg
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void captcha(HttpServletRequest req, HttpServletResponse resp)
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
        resp.setContentType("image/jpeg");

        // create the text for the image
        String capText = this.kaptchaProducer.createText();

        // store the text in the session
        req.getSession().setAttribute(this.sessionKeyValue, capText);

        // store the date in the session so that it can be compared
        // against to make sure someone hasn't taken too long to enter
        // their kaptcha
        req.getSession().setAttribute(this.sessionKeyDateValue, new Date());

        // create the image with the text
        BufferedImage bi = this.kaptchaProducer.createImage(capText);

        ServletOutputStream out = resp.getOutputStream();

        // write the data out
        ImageIO.write(bi, "jpg", out);

        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    public static String getGeneratedKey(HttpServletRequest req) {
        HttpSession session = req.getSession();
        return (String) session
                .getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
    }
}
