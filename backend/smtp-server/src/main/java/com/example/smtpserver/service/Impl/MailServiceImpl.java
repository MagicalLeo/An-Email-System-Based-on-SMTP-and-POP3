package com.example.smtpserver.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.smtpserver.dao.entity.Mail;
import com.example.smtpserver.dao.entity.User;
import com.example.smtpserver.dao.mapper.MailMapper;
import com.example.smtpserver.dao.mapper.UserMapper;
import com.example.smtpserver.service.MailService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("mailServiceImpl")
public class MailServiceImpl extends ServiceImpl<MailMapper, Mail> implements MailService {

  @Resource
  UserMapper userMapper;

  @Resource
  MailMapper mailMapper;

  @Override
  public boolean userExists(String username) {
    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(User::getUsername, username);
    return userMapper.selectOne(queryWrapper) != null;
  }

  @Override
  public boolean passwordCorrect(String username, String password) {
    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(User::getUsername, username);
    User user = userMapper.selectOne(queryWrapper);
    return password.equals(user.getAuthCode());
  }


  @Override
  public boolean addMail(String username, String content) {
//    Mail mail = new Mail();
//    mail.setFromMail(username);
//    mail.setMessage(content);
    // 获取收件箱 To: xxx@qq.com
    String[] split = content.split("\n");
    String toList = "";
    for (String s : split) {
      if (s.startsWith("To: ")) {
        toList = s.substring(4);
        break;
      }
    }
    String[] list = toList.split(",");
    for (String s : list) {
      Mail temp = new Mail();
      temp.setToMail(s.trim());
      temp.setFromMail(username);
      temp.setMessage(content);
      mailMapper.insert(temp);
    }
    return true;
  }
}
