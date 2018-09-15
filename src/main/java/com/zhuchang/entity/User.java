package com.zhuchang.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author fengqiang
 * @since 2018-08-06
 */
@TableName("sys_user")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    private String id;

    private String username;

    private String password;

    private Integer age;

    private String email;

    private String photo;

    @TableField("real_name")
    private String realName;

    @TableField(value ="create_by", fill=FieldFill.INSERT,strategy = FieldStrategy.IGNORED)
    private String createBy;

    @TableField(value ="create_date", fill=FieldFill.INSERT,strategy = FieldStrategy.IGNORED)
    private LocalDateTime createDate;

    @TableField(value ="update_by", fill=FieldFill.INSERT_UPDATE,strategy = FieldStrategy.IGNORED)
    private String updateBy;

    @TableField(value = "update_date", fill=FieldFill.INSERT_UPDATE,strategy = FieldStrategy.IGNORED)
    private LocalDateTime updateDate;

    /**
     * 0可用1封禁
     */
    @TableField("del_flag")
    private Integer delFlag;

    @TableField(exist = false)
    private String roles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "User{" +
        "id=" + id +
        ", username=" + username +
        ", password=" + password +
        ", age=" + age +
        ", email=" + email +
        ", photo=" + photo +
        ", realName=" + realName +
        ", createBy=" + createBy +
        ", updateBy=" + updateBy +
        ", createDate=" + createDate +
        ", updateDate=" + updateDate +
        ", delFlag=" + delFlag +
        "}";
    }
}
