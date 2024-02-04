package com.example.smtpserver.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.smtpserver.dao.entity.Mail;

public interface MailService extends IService<Mail> {
  /**
   * Checks if the specified user exists in the database
   *
   * @param username the username to check
   * @return true if the user exists, false otherwise
   */
  boolean userExists(String username);

  /**
   * Checks if the specified password is correct for the current user
   *
   * @param username the user account
   * @param password the password to verify
   * @return true if the password is correct, false otherwise
   */
  boolean passwordCorrect(String username, String password);


  boolean addMail(String username,String content);
}
