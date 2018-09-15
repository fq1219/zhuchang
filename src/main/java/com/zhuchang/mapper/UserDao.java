package com.zhuchang.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhuchang.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fengqiang
 * @since 2018-08-06
 */
public interface UserDao extends BaseMapper<User> {

    List<User> selectUserByPage(Page<User> pageObject, @Param("user") User user);
}
