package com.zhuchang.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhuchang.entity.Role;
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
public interface RoleDao extends BaseMapper<Role> {

    List<Role> selectRoleByPage(Page<Role> pageObject, @Param("role") Role role);
}
