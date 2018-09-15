package com.zhuchang.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhuchang.entity.*;
import com.zhuchang.entity.Role;
import com.zhuchang.service.MenuService;
import com.zhuchang.service.RoleMenuService;
import com.zhuchang.service.RoleService;
import com.zhuchang.util.ResultUtil;
import com.zhuchang.vo.PageResult;
import com.zhuchang.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fengqiang
 * @since 2018-08-06
 */
@Controller
@RequestMapping("webs/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @Autowired
    MenuService menuService;

    @Autowired
    RoleMenuService roleMenuService;

    @RequestMapping("/roleList")
    public String toRoleList(Model model){
        return  "webs/role/roleList";
    }

    @RequestMapping("/addRole")
    public String toAddRole(Model model){
        List<Menu> menuList = menuService.selectAllMenu();
        model.addAttribute("menuList", menuList);
        return  "webs/role/addRole";
    }

    @RequestMapping("/updateRole")
    public String toUpdateRole(Model model, String id){
        Role role = roleService.getById(id);
        model.addAttribute("role", role);

        Map<String, Object> map = new HashMap<>();
        map.put("role_id", id);
        Collection<RoleMenu> roleMenuList = roleMenuService.listByMap(map);
        Set<String> menuIds = new HashSet<>();
        for(RoleMenu roleMenu : roleMenuList){
            Menu menu = menuService.getById(roleMenu.getMenuId());
            if(menu != null) {
                menuIds.add(menu.getId());
            }
        }
        model.addAttribute("menuIds", StringUtils.join(menuIds,","));
        List<Menu> menuList = menuService.selectAllMenu();
        model.addAttribute("menuList", menuList);

        return  "webs/role/updateRole";
    }

    @RequestMapping("/detailRole")
    public String toDetailRole(Model model, String id){
        Role role = roleService.getById(id);
        model.addAttribute("role", role);

        Map<String, Object> map = new HashMap<>();
        map.put("role_id", id);
        Collection<RoleMenu> roleMenuList = roleMenuService.listByMap(map);
        Set<String> menuIds = new HashSet<>();
        for(RoleMenu roleMenu : roleMenuList){
            Menu menu = menuService.getById(roleMenu.getMenuId());
            if(menu != null) {
                menuIds.add(menu.getId());
            }
        }
        model.addAttribute("menuIds", StringUtils.join(menuIds,","));
        List<Menu> menuList = menuService.selectAllMenu();
        model.addAttribute("menuList", menuList);

        return  "webs/role/detailRole";
    }

    @RequestMapping("/getRoleByPage")
    @ResponseBody
    public PageResult getRoleByPage(Role role, Long page, Long limit){
        Page<Role> pageObject = new Page<Role>();
        pageObject.setCurrent(page);
        pageObject.setSize(limit);
        Page<Role> pageList = roleService.selectRoleByPage(pageObject, role);
        return  new PageResult<Role>(pageList);
    }

    @RequestMapping("/checkRole")
    @ResponseBody
    public Result checkRole(String roleName){
        Map<String, Object> map = new HashMap<>();
        map.put("role_name", roleName);
        Collection<Role> users = roleService.listByMap(map);
        if(users.size() == 0){
            return ResultUtil.getSuccessResult();
        }else{
            return ResultUtil.getFailResult("角色已经存在！");
        }

    }

    @RequestMapping("/deleteRole")
    @ResponseBody
    public Result deleteUser(String[] ids){
        try {
            roleService.deleteRoleById(ids);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getFailResult(e.getMessage());
        }
        return ResultUtil.getSuccessResult();
    }

    @RequestMapping("/doAddRole")
    @ResponseBody
    public Result addRole(Role role, String[] menuIds){
        try {
            roleService.saveUser(role, menuIds);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getFailResult(e.getMessage());
        }
        return ResultUtil.getSuccessResult();
    }

    @RequestMapping("/doUpdateRole")
    @ResponseBody
    public Result updateRole(Role role, String[] menuIds){
        try {
            roleService.updateRole(role, menuIds);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getFailResult(e.getMessage());
        }
        return ResultUtil.getSuccessResult();
    }

}
