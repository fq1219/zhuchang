package com.zhuchang.service;

        import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
        import com.zhuchang.entity.User;
        import com.baomidou.mybatisplus.extension.service.IService;

        import java.util.List;
        import java.util.Map;
        import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengqiang
 * @since 2018-08-06
 */
public interface UserService extends IService<User> {

    Page<User> selectUserByPage(Page<User> pageObject, User user);

    List<User> selectUserByMap(Map<String, Object> map);

    void saveUser(User user, String[] roleIds);

    void deleteUsers(String[] userIds);

    void updateUser(User user, String[] role);

    void activeUser(String[] ids, String active);

    String updatePassword(String id, String password, String newPassword);

    User findByUsername(String username);

    Map<String,Set<String>> findRoles(String id);

    Set<String> findPermissions(Set<String> roleIds);
}
