package com.example.mailserver.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mailserver.dao.entity.Role;
import com.example.mailserver.dao.entity.User;
import com.example.mailserver.dao.mapper.RoleMapper;
import com.example.mailserver.dao.mapper.UserMapper;
import com.example.mailserver.exception.PasswordErrorException;
import com.example.mailserver.exception.SignTokenException;
import com.example.mailserver.exception.UserDisabledException;
import com.example.mailserver.exception.UserNotFoundException;
import com.example.mailserver.service.LoginService;
import com.example.mailserver.service.entity.LoginResponseBody;
import com.example.mailserver.utils.PasswordMD5;
import com.example.mailserver.utils.Token;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

  @Resource
  UserMapper userMapper;

  @Resource
  RoleMapper roleMapper;

  @Resource
  Token token;

  @Override
  public LoginResponseBody userLoginService(String username, String password)
      throws UserNotFoundException, PasswordErrorException, SignTokenException,
      UserDisabledException {

    // 构造查询构造器，根据用户名查找
    QueryWrapper<User> wrapper = new QueryWrapper<>();
    wrapper.eq("username", username);
    User user = userMapper.selectOne(wrapper);

    // 未找到用户
    if (user == null) {
      throw new UserNotFoundException();
    }

    // 密码错误
    if (!user.getPassword().equals(PasswordMD5.getPasswordMD5(password))) {
      throw new PasswordErrorException();
    }

    // 用户被冻结
    if (user.getDisabled() == 1) {
      throw new UserDisabledException();
    }

    String token;
    try {
      token = this.token.sign(user.getId());
    } catch (Exception e) {
      e.printStackTrace();
      throw new SignTokenException();
    }
    // 获取用户角色信息
    Role userRole = roleMapper.selectById(user.getRoleId());
    return LoginResponseBody.builder()
        .id(user.getId())
        .token(token)
        .username(user.getUsername())
        .nickname(user.getNickname())
        .role(userRole.getValue())
        .build();
  }
}
