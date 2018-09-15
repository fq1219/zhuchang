package com.zhuchang.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 
 * </p>
 *
 * @author fengqiang
 * @since 2018-08-06
 */
@TableName("sys_menu")
public class Menu extends Model<Menu> implements Comparable<Menu>{

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    @TableField("p_id")
    private String pId;

    private String url;

    /**
     * 排序字段
     */
    @TableField("order_num")
    private Integer orderNum;

    /**
     * 图标
     */
    private String icon;

    @TableField(value ="create_by", fill=FieldFill.INSERT)
    private String createBy;

    @TableField(value ="create_date", fill=FieldFill.INSERT)
    private LocalDateTime createDate;

    @TableField(value ="update_by", fill=FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(value = "update_date", fill=FieldFill.INSERT_UPDATE)
    private LocalDateTime updateDate;

    /**
     * 权限
     */
    private String permission;

    /**
     * 1栏目2菜单
     */
    @TableField("menu_type")
    private Integer menuType;

    /**
     * 0 系统原有 1 新添加
     */
    @TableField("use_type")
    private String useType;

    @TableField(exist = false)
    private List<Menu> children = new ArrayList<>();

    @TableField(exist = false)
    private Integer childCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public Integer getChildCount() {
        return childCount;
    }

    public void setChildCount(Integer childCount) {
        this.childCount = childCount;
    }

    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Menu{" +
        "id=" + id +
        ", name=" + name +
        ", pId=" + pId +
        ", url=" + url +
        ", orderNum=" + orderNum +
        ", icon=" + icon +
        ", createBy=" + createBy +
        ", createDate=" + createDate +
        ", updateBy=" + updateBy +
        ", updateDate=" + updateDate +
        ", permission=" + permission +
        ", menuType=" + menuType +
        ", useType=" + useType +
        "}";
    }

    @Override
    public int compareTo(Menu menu) {
        if(this == null || menu == null){
            return 0;
        }
        int result = this.getOrderNum() - menu.getOrderNum();
        if(result == 0) {
            result = this.name.compareTo(menu.getName());
        }
        if(result == 0) {
            result = this.id.compareTo(menu.getId());
        }
        return result;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id) &&
                Objects.equals(name, menu.name) &&
                Objects.equals(pId, menu.pId) &&
                Objects.equals(url, menu.url) &&
                Objects.equals(orderNum, menu.orderNum) &&
                Objects.equals(icon, menu.icon) &&
                Objects.equals(createBy, menu.createBy) &&
                Objects.equals(createDate, menu.createDate) &&
                Objects.equals(updateBy, menu.updateBy) &&
                Objects.equals(updateDate, menu.updateDate) &&
                Objects.equals(permission, menu.permission) &&
                Objects.equals(menuType, menu.menuType) &&
                Objects.equals(useType, menu.useType) &&
                Objects.equals(childCount, menu.childCount);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, pId, url, orderNum, icon, createBy, createDate, updateBy, updateDate, permission, menuType, useType, childCount);
    }
}
