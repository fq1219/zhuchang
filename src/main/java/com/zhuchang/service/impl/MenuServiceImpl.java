package com.zhuchang.service.impl;

import com.google.common.collect.Lists;
import com.zhuchang.entity.Menu;
import com.zhuchang.entity.Role;
import com.zhuchang.entity.RoleMenu;
import com.zhuchang.entity.RoleUser;
import com.zhuchang.mapper.*;
import com.zhuchang.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.support.AopUtils;
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
public class MenuServiceImpl extends ServiceImpl<MenuDao, Menu> implements MenuService {

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
    public List<Menu> selectAllMenu() {
        List<Menu> rootList = this.selectRootMenu();
        selectMenu(rootList);
        return rootList;
    }


    /**
     * 取得全部节点
     * @param rootList
     */
    private void selectMenu(List<Menu> rootList) {
        for(Menu menu : rootList){
            if(menu.getChildCount() > 0) {
                List<Menu> childRootList = this.selectChildRoot(menu.getId());
                if(childRootList.size()> 0) {
                    menu.setChildren(childRootList);
                    selectMenu(childRootList);
                }
            }
        }
    }

    /**
     * 取得孩子节点
     * @param pid
     */
    private List<Menu> selectChildRoot(String pid) {
        return menuDao.selectChildRoot(pid);
    }

    /**
     * 取得根节点
     * @return
     */
    private List<Menu> selectRootMenu() {
        return menuDao.selectRootMenu();

    }

    @Override
    @Transactional
    public void deleteMenu(String id) {
        Menu menu = menuDao.selectMenu(id);
        ((MenuServiceImpl)AopContext.currentProxy()).deleteChildMenu(menu);
        menuDao.deleteById(id);
        Map<String, Object> param = new HashMap<>();
        param.put("menu_id", id);
        roleMenuDao.deleteByMap(param);

    }



    @Transactional
    public void deleteChildMenu(Menu menu) {
        if(menu.getChildCount()!=null && menu.getChildCount() > 0) {
            List<Menu> childRootList = this.selectChildRoot(menu.getId());
            for (Menu me : childRootList) {
                ((MenuServiceImpl)AopContext.currentProxy()).deleteChildMenu(me);
                Map<String, Object> param = new HashMap<>();
                param.put("menu_id", me.getId());
                roleMenuDao.deleteByMap(param);
            }
            Map<String, Object> param = new HashMap<>();
            param.put("p_id", menu.getId());
            menuDao.deleteByMap(param);
        }
    }

    /**
     * 根据用户取得菜单
     * @param id
     * @return
     */
    @Override
    public Set<Menu> selectMenuByUser(String id) {
        Set<Menu> menus = new HashSet<>();
        Set<Menu> menuSet = new HashSet<>();

        //根据用户取得菜单，可能既有父亲菜单，又有孩子菜单
        Map<String,Object> param = new HashMap<>();
        param.put("user_id", id);
        List<RoleUser> roleUserList = roleUserDao.selectByMap(param);
        for(RoleUser roleUser : roleUserList){
            Role role = roleDao.selectById(roleUser.getRoleId());
            param = new HashMap<>();
            param.put("role_id", role.getId());
            List<RoleMenu> roleMapList = roleMenuDao.selectByMap(param);
            for(RoleMenu roleMenu : roleMapList){
                Menu menu = menuDao.selectMenu(roleMenu.getMenuId());
                if(menu != null && (0 == menu.getMenuType() || 2 == menu.getMenuType() )) {
                    menus.add(menu);

                }
            }
        }

        //填充父亲菜单的孩子菜单
        for(Menu menu : menus) {
            if (StringUtils.isBlank(menu.getpId())) {

                List<Menu> children = new ArrayList<>();
                for(Menu me : menus) {
                    if(StringUtils.isNotBlank(me.getpId()) && me.getpId().equals(menu.getId())){
                        children.add(me);
                    }
                }
                menu.setChildren(children);
                menuSet.add(menu);
            }
        }

        //填充孩子菜单的父亲菜单
        for(Menu menu : menus) {
            if (StringUtils.isNotBlank(menu.getpId())) {

                Menu menuParent = menuDao.selectMenu(menu.getpId());
                boolean exist = false;
                for(Menu me : menuSet) {
                    if (menuParent.getId().equals(me.getId()) ) {
                        if(!me.getChildren().contains(menu)) {
                            me.getChildren().add(menu);
                        }
                        exist = true;
                        break;
                    }
                }
                if(!exist){
                    menuParent.getChildren().add(menu);
                    menuSet.add(menuParent);
                }
            }
        }

        return oderMenuSet(menuSet);
    }

    private Set<Menu> oderMenuSet(Collection<Menu> menuSet) {
        Set<Menu> menuTreeSet = new TreeSet<>();
        for(Menu menu : menuSet){
            menuTreeSet.add(menu);
            if(menu.getChildren() != null){
                Set<Menu> childList = oderMenuSet(menu.getChildren());
                menu.setChildren(Lists.newArrayList(childList));
            }
        }
        return menuTreeSet;
    }


}

