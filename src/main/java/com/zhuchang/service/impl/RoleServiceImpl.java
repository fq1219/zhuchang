package com.zhuchang.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhuchang.entity.Role;
import com.zhuchang.entity.RoleMenu;
import com.zhuchang.mapper.RoleDao;
import com.zhuchang.mapper.RoleMenuDao;
import com.zhuchang.mapper.RoleUserDao;
import com.zhuchang.mapper.UserDao;
import com.zhuchang.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fengqiang
 * @since 2018-08-06
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {

    @Autowired
    RoleDao roleDao;

    @Autowired
    RoleUserDao roleUserDao;

    @Autowired
    RoleMenuDao roleMenuDao;

    @Override
    public Page<Role> selectRoleByPage(Page<Role> pageObject, Role role) {
        pageObject.setRecords(roleDao.selectRoleByPage(pageObject, role));
        return pageObject;
    }

    @Override
    @Transactional
    public void deleteRoleById(String[] ids) {
        for(String id : ids){
            Map<String,Object> param = new HashMap<>();
            param.put("role_id", id);
            roleUserDao.deleteByMap(param);
            param = new HashMap<>();
            param.put("role_id", id);
            roleMenuDao.deleteByMap(param);
            roleDao.deleteById(id);
        }
    }

    @Override
    @Transactional
    public void saveUser(Role role, String[] menuIds) {
        roleDao.insert(role);
        for(String menuId : menuIds){
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(role.getId());
            roleMenu.setMenuId(menuId);
            roleMenuDao.insert(roleMenu);
        }

    }

    @Override
    @Transactional
    public void updateRole(Role role, String[] menuIds) {
        roleDao.updateById(role);
        Map<String,Object> param = new HashMap<>();
        param.put("role_id", role.getId());
        roleMenuDao.deleteByMap(param);
        for(String menuId : menuIds){
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(role.getId());
            roleMenu.setMenuId(menuId);
            roleMenuDao.insert(roleMenu);
        }
    }
}
