package com.example.mailserver.service.Impl;

import com.example.mailserver.dao.entity.User;
import com.example.mailserver.dao.mapper.UserMapper;
import com.example.mailserver.service.MailService;
import com.example.mailserver.service.entity.MailItem;
import com.example.mailserver.service.entity.MailSetting;
import com.example.mailserver.service.entity.QueryListResponseBody;
import com.example.mailserver.utils.FetchEmail;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.annotation.Resource;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailServiceImpl implements MailService {

  @Resource
  UserMapper userMapper;

  @Override
  public void sendMail(User user, String subject, String content, String filename, File file,
      Boolean isHtml, String... to) throws MessagingException {
    System.out.println(user);
    Properties prop = new Properties();
    //  调试信息
    prop.setProperty("mail.debug", "true");
    //  协议设置smtp
    prop.setProperty("mail.transport.protocol", "smtp");
    //  设置需要验证用户名和密码
    prop.setProperty("mail.smtp.auth", "true");
    //  smtp服务器设置为localhost
    prop.setProperty("mail.host", user.getSmtpHost());

    // 创建 session
    Session session = Session.getInstance(prop);
    // 通过 session 得到 transport 对象
    Transport transport = session.getTransport();
    // 连接邮件服务器
    transport.connect(user.getSmtpHost(), user.getSmtpPort(), user.getUsername(),
        user.getAuthCode());
    // 创建邮件
    MimeMessage message = new MimeMessage(session);

    // 创建消息
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setFrom(user.getUsername());
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(content, isHtml);
    // 处理附件
    if (file != null) {
      helper.addAttachment(filename, file);
    }
    //  发送给smtp邮件服务器
    transport.sendMessage(message, message.getAllRecipients());
  }

  @Override
  public QueryListResponseBody<MailItem> receiveMail(User user)
      throws MessagingException, IOException {
    List<MailItem> mailItemList = new ArrayList<>();

    System.out.println(user);
    // 创建一个具有连接信息的Properties对象
    Properties prop = new Properties();
    //  调试信息
    prop.setProperty("mail.debug", "true");
    //  协议设置pop3
    prop.setProperty("mail.store.protocol", "pop3");
    prop.setProperty("mail.debug.auth", "true");
    //  设置邮件服务器，这里设置的是本地localhost
    prop.setProperty("mail.pop3.host", user.getPop3Host());

    // 创建 session
    Session session = Session.getInstance(prop);

    // 通过session得到Store对象
    Store store = session.getStore();

    // 连接上邮件服务器
    store.connect(user.getPop3Host(), user.getPop3Port(), user.getUsername(), user.getAuthCode());

    // 获得邮箱内的文件夹
    Folder folder = store.getFolder("inbox");
    folder.open(Folder.READ_ONLY);

    // 获取邮件夹Folder内所有的邮件Message对象
    Message[] messages = folder.getMessages();
    if (messages != null && messages.length != 0) {
      mailItemList = FetchEmail.parseMessage(user.getUsername(), messages);
    }

    // 关闭
    folder.close(false);
    store.close();
    return QueryListResponseBody.<MailItem>builder()
        .total(messages != null ? messages.length : 0)
        .items(mailItemList)
        .build();
  }

  @Override
  public QueryListResponseBody<MailSetting> getMailSettings() {
    // 获取服务器设置信息
    User settings = userMapper.selectById(88);
    List<MailSetting> mailSettingList = new ArrayList<>();
    mailSettingList.add(MailSetting.builder()
        .pop3ServerName(settings.getPassword())
        .smtpServerName(settings.getNickname())
        .smtpServerPort(settings.getSmtpPort())
        .pop3ServerPort(settings.getPop3Port())
        .build());
    return QueryListResponseBody.<MailSetting>builder()
        .total(1)
        .items(mailSettingList)
        .build();
  }
}
