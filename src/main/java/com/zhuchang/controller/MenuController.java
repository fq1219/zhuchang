package com.zhuchang.controller;


import com.alibaba.fastjson.JSON;
import com.zhuchang.entity.Menu;
import com.zhuchang.entity.User;
import com.zhuchang.service.MenuService;
import com.zhuchang.service.impl.MenuServiceImpl;
import com.zhuchang.util.ResultUtil;
import com.zhuchang.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fengqiang
 * @since 2018-08-06
 */
@Controller
@RequestMapping("webs/menu")
public class MenuController {

    @Autowired
    MenuService menuService;

    @RequestMapping("/menuList")
    public String toMenuList(Model model){
        List<Menu> menuList = menuService.selectAllMenu();
        model.addAttribute("menuList", menuList);
        return  "webs/menu/menuList";
    }

    @RequestMapping("/addMenu")
    public String toAddMenu(Model model){
        List<Menu> menuList = menuService.selectAllMenu();
        model.addAttribute("menuList", menuList);
        return  "webs/menu/addMenu";
    }

    @RequestMapping("/updateMenu")
    public String toUpdateMenu(Model model, String id){
        Menu menu = menuService.getById(id);
        model.addAttribute("menu", menu);

        Menu pmenu = menuService.getById(menu.getpId());
        model.addAttribute("pName", pmenu==null ? "":pmenu.getName());

        return  "webs/menu/updateMenu";
    }

    @RequestMapping("/detailMenu")
    public String toDetailMenu(Model model, String id){
        Menu menu = menuService.getById(id);
        model.addAttribute("menu", menu);

        Menu pmenu = menuService.getById(menu.getpId());
        model.addAttribute("pName", pmenu==null ? "":pmenu.getName());

        return  "webs/menu/detailMenu";
    }

    @RequestMapping("/deleteMenu")
    @ResponseBody
    public Result deleteUser(String id){
        try {
            menuService.deleteMenu(id);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getFailResult(e.getMessage());
        }
        return ResultUtil.getSuccessResult();
    }

    @RequestMapping("/doAddMenu")
    @ResponseBody
    public Result addMenu(Menu menu){
        try {
            if(StringUtils.isBlank(menu.getpId())){
                menu.setpId(null);
            }
            menu.setUseType("1");
            menuService.save(menu);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getFailResult(e.getMessage());
        }
        return ResultUtil.getSuccessResult();
    }

    @RequestMapping("/doUpdateMenu")
    @ResponseBody
    public Result updateMenu(Menu menu){
        try {
            menuService.updateById(menu);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getFailResult(e.getMessage());
        }
        return ResultUtil.getSuccessResult();
    }
}
