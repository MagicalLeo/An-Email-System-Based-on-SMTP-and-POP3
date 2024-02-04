package com.example.mailserver.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mailserver.dao.entity.Role;
import com.example.mailserver.dao.entity.User;
import com.example.mailserver.dao.mapper.RoleMapper;
import com.example.mailserver.dao.mapper.UserMapper;
import com.example.mailserver.exception.UserExistedException;
import com.example.mailserver.service.UserService;
import com.example.mailserver.service.entity.QueryListResponseBody;
import com.example.mailserver.service.entity.UserItem;
import com.example.mailserver.utils.PasswordMD5;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

  @Resource
  UserMapper userMapper;

  @Resource
  RoleMapper roleMapper;

  @Override
  public QueryListResponseBody<UserItem> queryUserList(int current, int pageSize,
      QueryWrapper<User> queryWrapper) {
    List<Role> roleList = roleMapper.selectList(null);
    Map<Integer, Role> roleMap = new HashMap<>(5);
    roleList.forEach(role -> roleMap.put(role.getId(), role));

    Page<User> page = new Page<>(current, pageSize);
    Page<User> userPage = userMapper.selectPage(page, queryWrapper);
    int total = (int) userPage.getTotal();
    List<User> userList = userPage.getRecords();
    List<UserItem> userItemList = new ArrayList<>();
    userList.forEach(user -> {
      UserItem userItem = UserItem.builder()
          .id(user.getId())
          .nickname(user.getNickname())
          .username(user.getUsername())
          .phone(user.getPhone())
          .sex(user.getSex())
          .role(roleMap.get(user.getRoleId()))
          .smtpHost(user.getSmtpHost())
          .smtpPort(user.getSmtpPort())
          .pop3Host(user.getPop3Host())
          .pop3Port(user.getPop3Port())
          .authCode(user.getAuthCode())
          .disabled(user.getDisabled())
          .build();
      userItemList.add(userItem);
    });
    QueryListResponseBody.QueryListResponseBodyBuilder<UserItem> queryListResponseBodyBuilder = QueryListResponseBody.builder();
    return queryListResponseBodyBuilder
        .total(total)
        .items(userItemList)
        .build();
  }

  @Override
  public boolean register(String username, String password) throws UserExistedException {
    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(User::getUsername, username);
    if (userMapper.exists(queryWrapper)) {
      throw new UserExistedException();
    }
    User user = new User();
    user.setPassword(PasswordMD5.getPasswordMD5(password));
    user.setAuthCode(password);
    user.setUsername(username);
    user.setSmtpHost("localhost");
    user.setPop3Host("localhost");
    user.setSmtpPort(25);
    user.setPop3Port(110);
    user.setRoleId(2); // 普通用户
    return userMapper.insert(user) > 0;
  }
}

