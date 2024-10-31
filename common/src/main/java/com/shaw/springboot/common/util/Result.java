package com.shaw.springboot.common.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Result<T> {
  @Schema(description = "状态码", example = "200")
  private Integer code;

  @Schema(description = "消息", example = "操作成功")
  private String msg;

  @Schema(description = "数据")
  private T data;

  private Result(Integer code, String msg, T data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  public static Result<Void> success() {
    return new Result<>(200, "操作成功", null);
  }

  public static <T> Result<T> success(T data) {
    return new Result<>(200, "操作成功", data);
  }

  public static <T> Result<T> success(String message, T data) {
    return new Result<>(200, message, data);
  }

  public static <T> Result<T> failed(String message) {
    return new Result<>(500, message, null);
  }

  public static <T> Result<T> failed(Integer code, String message) {
    return new Result<>(code, message, null);
  }

}
