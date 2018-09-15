package com.zhuchang.config;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.zhuchang.entity.User;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Date;

public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * user 表 name 字段为空自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {

        User currentUser = (User)SecurityUtils.getSubject().getPrincipal();
        setFieldValByName("createBy", currentUser.getId(), metaObject );
        setFieldValByName("updateBy", currentUser.getId(), metaObject );
        setFieldValByName("createDate", LocalDateTime.now(), metaObject );
        setFieldValByName("updateDate", LocalDateTime.now(), metaObject );
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        User currentUser = (User)SecurityUtils.getSubject().getPrincipal();
        setFieldValByName("updateBy", currentUser.getId(), metaObject );
        setFieldValByName("updateDate", LocalDateTime.now(), metaObject );
    }
}