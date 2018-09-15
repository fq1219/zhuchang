package com.zhuchang.mapper;

import com.zhuchang.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fengqiang
 * @since 2018-08-06
 */
public interface MenuDao extends BaseMapper<Menu> {

    List<Menu> selectRootMenu();

    List<Menu> selectChildRoot(String pid);

    Menu selectMenu(String id);
}
