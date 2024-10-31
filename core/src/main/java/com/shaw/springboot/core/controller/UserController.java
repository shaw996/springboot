package com.shaw.springboot.core.controller;

import com.shaw.springboot.common.util.Result;
import com.shaw.springboot.core.entity.UserEntity;
import com.shaw.springboot.core.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户模块")
@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

  @Autowired
  private UserService userService;

  @Operation(summary = "根据ID查询用户")
  @GetMapping("getUserInfo")
  public Result<UserEntity> getUserInfo(@Parameter(description = "文件url", example = "/getUserInfo?id=<ID>") @RequestParam("id") String id) {
    return userService.getUserById(id);
  }

}
