package com.zhuchang.service;

import com.zhuchang.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengqiang
 * @since 2018-08-06
 */
public interface MenuService extends IService<Menu> {

    List<Menu> selectAllMenu();

    void deleteMenu(String id);

    Set<Menu> selectMenuByUser(String id);
}
