package com.capy.capyjara.admin.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.capy.capyjara.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Schema
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_user")
public class SysUser extends BaseEntity {
    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.NONE)
    @Schema(description = "用户id")
    private String id;

    /**
     * 账号
     */
    @TableField(value = "username")
    @Schema(description = "账号")
    private String username;

    /**
     * 昵称
     */
    @TableField(value = "nickname")
    @Schema(description = "昵称")
    private String nickname;

    /**
     * 真实姓名
     */
    @TableField(value = "`name`")
    @Schema(description = "真实姓名")
    private String name;

    /**
     * 密码(加密存储)
     */
    @TableField(value = "`password`")
    @Schema(description = "密码(加密存储)")
    private String password;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    @Schema(description = "邮箱")
    private String email;

    /**
     * 手机号
     */
    @TableField(value = "phone_number")
    @Schema(description = "手机号")
    private String phoneNumber;

    /**
     * 用户类型(字典)
     */
    @TableField(value = "user_type")
    @Schema(description = "用户类型(字典)")
    private String userType;

    /**
     * 性别(字典)
     */
    @TableField(value = "gender")
    @Schema(description = "性别(字典)")
    private String gender;

    /**
     * 工号
     */
    @TableField(value = "user_no")
    @Schema(description = "工号")
    private String userNo;

    /**
     * 最近一次修改密码时间
     */
    @TableField(value = "pwd_update_time")
    @Schema(description = "最近一次修改密码时间")
    private Date pwdUpdateTime;

    /**
     * 头像地址
     */
    @TableField(value = "avatar")
    @Schema(description = "头像地址")
    private String avatar;
}