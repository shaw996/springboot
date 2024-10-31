package com.shaw.springboot.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shaw.springboot.core.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
