package com.zhuchang.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhuchang.entity.*;
import com.zhuchang.mapper.*;
import com.zhuchang.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuchang.util.ShiroMd5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fengqiang
 * @since 2018-08-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    RoleUserDao roleUserDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    RoleMenuDao roleMenuDao;

    @Autowired
    MenuDao menuDao;

    @Override
    public Page<User> selectUserByPage(Page<User> pageObject, User user) {
        pageObject.setRecords(userDao.selectUserByPage(pageObject, user));
        return pageObject;
    }

    @Override
    public List<User> selectUserByMap(Map<String, Object> map) {
        return userDao.selectByMap(map);
    }

    @Override
    @Transactional
    public void saveUser(User user, String[] roleIds) {
        user.setPassword(ShiroMd5Util.md5UserPassword(user.getPassword(), user.getUsername()));
        user.setDelFlag(1);
        userDao.insert(user);
        if(roleIds != null) {
            for (String roleId : roleIds) {
                RoleUser roleUser = new RoleUser();
                roleUser.setUserId(user.getId());
                roleUser.setRoleId(roleId);
                roleUserDao.insert(roleUser);
            }
        }
    }

    @Override
    @Transactional
    public void deleteUsers(String[] userIds) {
        for(String userId : userIds){
            Map<String,Object> param = new HashMap<>();
            param.put("user_id", userId);
            roleUserDao.deleteByMap(param);
            userDao.deleteById(userId);
        }
    }

    @Override
    @Transactional
    public void updateUser(User user, String[] roleIds) {
        User oldUser = userDao.selectById(user.getId());
        user.setPassword(oldUser.getPassword());
        userDao.updateById(user);

        Map<String,Object> param = new HashMap<>();
        param.put("user_id", user.getId());
        roleUserDao.deleteByMap(param);

        for(String roleId : roleIds){
            RoleUser roleUser = new RoleUser();
            roleUser.setUserId(user.getId());
            roleUser.setRoleId(roleId);
            roleUserDao.insert(roleUser);
        }
    }

    @Override
    @Transactional
    public void activeUser(String[] ids, String active) {
        for(String id : ids){
            User user = userDao.selectById(id);
            user.setDelFlag(Integer.valueOf(active));
            userDao.updateById(user);
        }

    }

    @Override
    public String updatePassword(String id, String password, String newPassword) {
        User oldUser = userDao.selectById(id);
        String oldPasswordMd5 = ShiroMd5Util.md5UserPassword(password, oldUser.getUsername());
        if(oldUser.getPassword().equals(oldPasswordMd5)){
            oldUser.setPassword(ShiroMd5Util.md5UserPassword(newPassword, oldUser.getUsername()));
            userDao.updateById(oldUser);
            return "";
        }else{
            return "原密码不正确";
        }
    }

    @Override
    public User findByUsername(String username) {
        Map<String,Object> param = new HashMap<>();
        param.put("username", username);
        List<User> users = userDao.selectByMap(param);
        return users.get(0);
    }

    @Override
    public Map<String,Set<String>> findRoles(String id) {
        Set<String> roles = new HashSet<>();
        Set<String> roleIds = new HashSet<>();
        Map<String,Object> param = new HashMap<>();
        param.put("user_id", id);
        List<RoleUser> roleUserList = roleUserDao.selectByMap(param);
        for(RoleUser roleUser : roleUserList){
            Role role = roleDao.selectById(roleUser.getRoleId());
            roles.add(role.getRoleName());
            roleIds.add(role.getId());
        }
        Map<String,Set<String>> result = new HashMap<>();
        result.put("roles", roles);
        result.put("roleIds", roleIds);
        return result;
    }



    @Override
    public Set<String> findPermissions(Set<String> roleIds) {
        Set<String> permissions = new HashSet<>();

        for(String roleId : roleIds){
            Map<String,Object> param = new HashMap<>();
            param.put("role_id", roleId);
            List<RoleMenu> roleMapList = roleMenuDao.selectByMap(param);
            for(RoleMenu roleMenu : roleMapList){
                Menu menu = menuDao.selectById(roleMenu.getMenuId());
                if(menu!= null && StringUtils.isNotBlank(menu.getPermission())) {
                    permissions.add(menu.getPermission());
                }

            }
        }

        return permissions;
    }




}
