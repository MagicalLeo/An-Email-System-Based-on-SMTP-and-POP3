package com.example.mailserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.mailserver.dao.entity.User;
import com.example.mailserver.service.entity.QueryListResponseBody;
import com.example.mailserver.service.entity.UserItem;

public interface UserService extends IService<User> {

  /**
   * 查询用户列表
   *
   * @param current      当前页
   * @param pageSize     页大小
   * @param queryWrapper 查询的条件构造器
   * @return QueryListResponseBody<UserItem>
   */
  QueryListResponseBody<UserItem> queryUserList(int current, int pageSize,
      QueryWrapper<User> queryWrapper);

  /**
   * 注册新用户
   *
   * @param username 用户名
   * @param password 密码
   */
  boolean register(String username, String password);
}
