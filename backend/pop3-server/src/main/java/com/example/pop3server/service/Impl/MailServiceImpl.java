package com.example.pop3server.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pop3server.dao.entity.Mail;
import com.example.pop3server.dao.entity.User;
import com.example.pop3server.dao.mapper.MailMapper;
import com.example.pop3server.dao.mapper.UserMapper;
import com.example.pop3server.service.MailService;
import java.nio.charset.StandardCharsets;
import java.util.List;
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
    return user.getAuthCode().equals(password);
  }

  @Override
  public int deleteMarkedMessages(String username) {
    // 查找出标记删除邮件
    LambdaQueryWrapper<Mail> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Mail::getToMail, username).eq(Mail::getDeleted, 1);
    // 删除对应邮件
    return mailMapper.delete(queryWrapper);
  }

  @Override
  public int numMessages(String username, boolean deleted) {
    LambdaQueryWrapper<Mail> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Mail::getToMail, username).eq(!deleted, Mail::getDeleted, 0);
    return Math.toIntExact(mailMapper.selectCount(queryWrapper));
  }

  @Override
  public int sizeOfMail(String username) {
    int totalSize = 0;
    LambdaQueryWrapper<Mail> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Mail::getToMail, username);
    List<Mail> mailList = mailMapper.selectList(queryWrapper);
    for (Mail mail : mailList) {
      totalSize += mail.getMessage().getBytes(StandardCharsets.UTF_8).length;
    }
    return totalSize;
  }

  @Override
  public int sizeOfMessage(String username, int id) {
    LambdaQueryWrapper<Mail> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Mail::getToMail, username);
    List<Mail> mailList = mailMapper.selectList(queryWrapper);
    Mail mail = mailList.get(id - 1);
    return mail.getMessage().getBytes(StandardCharsets.UTF_8).length;
  }

  @Override
  public boolean messageExists(String username, int id) {
    LambdaQueryWrapper<Mail> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Mail::getToMail, username);
    List<Mail> mailList = mailMapper.selectList(queryWrapper);
    Mail mail = mailList.get(id - 1);
    return mail != null;
  }

  @Override
  public void setMark(String username, int id, boolean marked) {
    LambdaQueryWrapper<Mail> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Mail::getToMail, username);
    List<Mail> mailList = mailMapper.selectList(queryWrapper);
    Mail mail = mailList.get(id - 1);
    mail.setDeleted(marked ? 1 : 0);
    mailMapper.updateById(mail);
  }

  @Override
  public boolean messageMarked(String username, int id) {
    LambdaQueryWrapper<Mail> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Mail::getToMail, username);
    List<Mail> mailList = mailMapper.selectList(queryWrapper);
    Mail mail = mailList.get(id - 1);
    return mail.getDeleted() == 1;
  }

  @Override
  public String getMessage(String username, int id) {
    LambdaQueryWrapper<Mail> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Mail::getToMail, username);
    /*
      根据用户名设置mailList
      再
     */
    List<Mail> mailList = mailMapper.selectList(queryWrapper);
    Mail mail = mailList.get(id - 1);
    return mail.getMessage();
  }

  @Override
  public String messageUIDL(String username, int id) {
    LambdaQueryWrapper<Mail> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Mail::getToMail, username);
    List<Mail> mailList = mailMapper.selectList(queryWrapper);
    Mail mail = mailList.get(id - 1);
    // 利用正则表达式匹配找出 messageId
    // Message-ID: <2088051243.0.1653190512167.JavaMail.Cat@localhost>
    String messageUID = null;
    String[] split = mail.getMessage().split("\n");
    for (String s : split) {
      if (s.startsWith("Message-ID: ")) {
        messageUID = s.substring(12);
        messageUID = messageUID.substring(1, messageUID.length() - 1);
        break;
      }
    }
    return messageUID;
  }

  @Override
  public void restoreMarked(String username) {
    LambdaUpdateWrapper<Mail> updateWrapper = new LambdaUpdateWrapper<>();
    updateWrapper.eq(Mail::getToMail, username).set(Mail::getDeleted, 0);
    mailMapper.update(new Mail(), updateWrapper);
  }

}
