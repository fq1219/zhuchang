package com.zhuchang.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhuchang.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengqiang
 * @since 2018-08-06
 */
public interface RoleService extends IService<Role> {

    Page<Role> selectRoleByPage(Page<Role> pageObject, Role role);

    void deleteRoleById(String[] ids);

    void saveUser(Role role, String[] menuIds);

    void updateRole(Role role, String[] menuIds);
}
