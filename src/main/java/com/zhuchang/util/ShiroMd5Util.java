package com.zhuchang.util;

import com.zhuchang.entity.User;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class ShiroMd5Util {

    //添加user的密码加密方法
    public static String  md5UserPassword(String crdentials, String userName) {
        String hashAlgorithmName = "MD5";//加密方式

        ByteSource salt = ByteSource.Util.bytes(userName);//以账号作为盐值

        int hashIterations = 1024;//加密1024次

        SimpleHash hash = new SimpleHash(hashAlgorithmName, crdentials, salt, hashIterations);

        return hash.toString();
    }
}
