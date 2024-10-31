package com.shaw.springboot.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaw.springboot.common.util.Result;
import com.shaw.springboot.core.entity.UserEntity;
import com.shaw.springboot.core.mapper.UserMapper;
import com.shaw.springboot.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {


  private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  private final UserMapper userMapper;


  UserServiceImpl(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public Result<UserEntity> getUserById(String id) {
    Result<UserEntity> result = null;

    UserEntity userEntity = userMapper.selectById(id);

    if (userEntity != null) {
      result = Result.success(userEntity);
    } else {
      result = Result.failed(400, "没有找到该用户");
      logger.warn("warning message: 没有找到该用户");
    }

    return result;
  }
}
