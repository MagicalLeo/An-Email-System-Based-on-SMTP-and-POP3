package com.example.mailserver.service;

import com.example.mailserver.dao.entity.User;
import com.example.mailserver.service.entity.MailItem;
import com.example.mailserver.service.entity.MailSetting;
import com.example.mailserver.service.entity.QueryListResponseBody;
import java.io.File;
import java.io.IOException;
import javax.mail.MessagingException;

public interface MailService {

  void sendMail(User user, String subject, String content, String filename, File file,
      Boolean isHtml, String... to) throws MessagingException;

  QueryListResponseBody<MailItem> receiveMail(User user)
      throws  MessagingException, IOException;

  QueryListResponseBody<MailSetting> getMailSettings();
}
