package com.shaw.springboot.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("user")
public class UserEntity extends BaseEntity {

  @Schema(description = "用户id")
  @TableId(value = "id", type = IdType.ASSIGN_UUID)
  private String id;

  @Schema(description = "用户名称")
  @TableField(value = "username")
  private String username;

  @Schema(description = "微信id")
  @TableField(value = "wechat_id")
  private String wechatId;

  @JsonIgnore
  @Schema(description = "用户密码")
  @TableField(value = "password")
  private String password;

  @Schema(description = "用户昵称")
  @TableField(value = "nickname")
  private String nickname;

  @Schema(description = "用户邮箱")
  @TableField(value = "email")
  private String email;

  @Schema(description = "用户手机")
  @TableField(value = "phone")
  private String phone;

  @Schema(description = "用户头像")
  @TableField(value = "avatar")
  private String avatar;

}
