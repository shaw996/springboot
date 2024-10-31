package com.shaw.springboot.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shaw.springboot.common.util.Result;
import com.shaw.springboot.core.entity.UserEntity;

public interface UserService extends IService<UserEntity> {

  Result<UserEntity> getUserById(String id);

}
