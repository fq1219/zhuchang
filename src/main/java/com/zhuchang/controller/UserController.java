package com.zhuchang.controller;




import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhuchang.entity.Role;
import com.zhuchang.entity.RoleUser;
import com.zhuchang.entity.User;
import com.zhuchang.service.RoleService;
import com.zhuchang.service.RoleUserService;
import com.zhuchang.service.UserService;
import com.zhuchang.util.ResultUtil;
import com.zhuchang.vo.PageResult;
import com.zhuchang.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
@RequestMapping("webs/user")
public class UserController {

    @Autowired
    public UserService userService;

    @Autowired
    public RoleService roleService;

    @Autowired
    public RoleUserService roleUserService;

    @RequestMapping("/userList")
    public String toUserList(){
        return  "webs/user/userList";
    }

    @RequestMapping("/addUser")
    public String toAddUser(Model model){
        List<Role> roleList = (List<Role>) roleService.listByMap(new HashMap<>());
        model.addAttribute("roleList", roleList);
        return  "webs/user/addUser";
    }

    @RequestMapping("/updateUser")
    public String toUpdateUser(Model model, String id){
        List<Role> roleList = (List<Role>) roleService.listByMap(new HashMap<>());
        model.addAttribute("roleList", roleList);

        Map<String,Object> param = new HashMap<>();
        param.put("user_id", id);
        List<RoleUser> roleUserList = (List<RoleUser>) roleUserService.listByMap(param);
        model.addAttribute("roleUserList", roleUserList);

        User user = userService.getById(id);
        model.addAttribute("user", user);
        return  "webs/user/updateUser";
    }

    @RequestMapping("/detailUser")
    public String toDetailUser(Model model, String id){
        List<Role> roleList = (List<Role>) roleService.listByMap(new HashMap<>());
        model.addAttribute("roleList", roleList);

        Map<String,Object> param = new HashMap<>();
        param.put("user_id", id);
        List<RoleUser> roleUserList = (List<RoleUser>) roleUserService.listByMap(param);
        model.addAttribute("roleUserList", roleUserList);

        User user = userService.getById(id);
        model.addAttribute("user", user);
        return  "webs/user/detailUser";
    }

    @RequestMapping("/changePassword")
    public String changePassword(Model model, String id){
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return  "webs/user/changePassword";
    }

    @RequestMapping("/getUserByPage")
    @ResponseBody
    public PageResult getUserByPage(User user, Long page, Long limit){
        Page<User> pageObject = new Page<User>();
        pageObject.setCurrent(page);
        pageObject.setSize(limit);
        Page<User> pageList = userService.selectUserByPage(pageObject, user);
        for(User u : pageList.getRecords()){
            Map<String,Object> param = new HashMap<>();
            param.put("user_id", u.getId());
            List<RoleUser> roleUserList = (List<RoleUser>) roleUserService.listByMap(param);
            Set<String> roleSet = new HashSet<>();
            for(RoleUser roleUser : roleUserList){
                Role role = roleService.getById(roleUser.getRoleId());
                if(role != null) {
                    roleSet.add(role.getRemark());
                }
            }
            u.setRoles(StringUtils.join(roleSet,","));
        }
        return  new PageResult<User>(pageList);
    }

    @RequestMapping("/checkUser")
    @ResponseBody
    public Result checkUser(String username){
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        List<User> users = userService.selectUserByMap(map);
        if(users.size() == 0){
            return ResultUtil.getSuccessResult();
        }else{
            return ResultUtil.getFailResult("用户名已经存在！");
        }

    }

    @RequestMapping("/doAddUser")
    @ResponseBody
    public Result addUser(User user, String[] role){
        try {
            userService.saveUser(user, role);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getFailResult(e.getMessage());
        }
        return ResultUtil.getSuccessResult();
    }

    @RequestMapping("/doUpdateUser")
    @ResponseBody
    public Result updateUser(User user, String[] role){
        try {
            userService.updateUser(user, role);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getFailResult(e.getMessage());
        }
        return ResultUtil.getSuccessResult();
    }

    @RequestMapping("/deleteUser")
    @ResponseBody
    public Result deleteUser(String[] ids){
        try {
            userService.deleteUsers(ids);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getFailResult(e.getMessage());
        }
        return ResultUtil.getSuccessResult();
    }

    @RequestMapping(value={"/activeUser","/unactiveUser"})
    @ResponseBody
    public Result activeUser(String[] ids , String active){
        try {
            userService.activeUser(ids, active);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getFailResult(e.getMessage());
        }
        return ResultUtil.getSuccessResult();
    }

    @RequestMapping("/doChangePassword")
    @ResponseBody
    public Result doChangePassword(String id, String password, String newPassword){
        try {
            String result =userService.updatePassword(id, password, newPassword);
            if(StringUtils.isNotBlank(result)){
                return ResultUtil.getFailResult(result);
            }else{
                return ResultUtil.getSuccessResult();
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getFailResult(e.getMessage());
        }

    }

}
