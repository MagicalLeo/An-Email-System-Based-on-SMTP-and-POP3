package com.example.smtpserver;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SMTPClient {

  Properties properties;
  Session session;
  Transport transport;
  String user;
  String password;

  public SMTPClient(String smtpServer, String user, String password) {
    properties = System.getProperties();
    properties.setProperty("mail.host", smtpServer);
    properties.setProperty("mail.transport.protocol", "smtp");
    properties.setProperty("mail.smtp.auth", "true");
    properties.setProperty("mail.debug", "true");
    session = Session.getDefaultInstance(properties);
    this.user = user;
    this.password = password;
    try {
      transport = session.getTransport();
    } catch (NoSuchProviderException e) {
      e.printStackTrace();
    }
  }


  public void sendMail(String to, String subject, String message) {
    MimeMessage mimeMessage = new MimeMessage(session);
    try {
      mimeMessage.setFrom(new InternetAddress(user));
      mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
      mimeMessage.setSubject(subject);
      mimeMessage.setText(message);
      transport.connect(user, password);
      transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    SMTPClient smtpClient = new SMTPClient("localhost", "root@test.com", "123456");
    smtpClient.sendMail("hh@test.com", "test", "testcase\n123");
  }
}
