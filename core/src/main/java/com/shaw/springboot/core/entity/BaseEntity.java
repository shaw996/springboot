package com.shaw.springboot.core.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseEntity {

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
  @Schema(description = "创建时间")
  @TableField(value = "create_time", fill = FieldFill.INSERT)
  private LocalDateTime createTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
  @Schema(description = "更新时间")
  @TableField(value = "update_time", fill = FieldFill.INSERT)
  private LocalDateTime updateTime;

  @JsonIgnore
  @Schema(description = "是否删除")
  @TableLogic
  @TableField(value = "is_deleted", fill = FieldFill.INSERT)
  private int isDeleted;
}
