package com.example.pop3server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pop3server.dao.entity.Mail;

public interface MailService extends IService<Mail> {

  /**
   * 判断当前用户是否存在
   *
   * @param username 用户名
   * @return bool
   */
  boolean userExists(String username);

  /**
   * 判断当前输入的密码是否匹配
   *
   * @param username 用户名
   * @param password 密码
   * @return bool
   */
  boolean passwordCorrect(String username, String password);

  /**
   * 删除标记删除的邮件
   *
   * @param username 用户名
   * @return 删除邮件的数量
   */
  int deleteMarkedMessages(String username);

  /**
   * 获取当前用户的邮件数量
   *
   * @param username 用户名
   * @param deleted  如果为true包含标记删除的邮件，false则不包含
   * @return 返回的邮件数量
   */
  int numMessages(String username, boolean deleted);

  /**
   * 获取该用户整个邮件中所有邮件的大小总和
   *
   * @param username 用户名
   * @return 邮件大小的字节数
   */
  int sizeOfMail(String username);

  /**
   * 获取指定邮件所占的字节大小
   *
   * @param username 用户名
   * @param id       邮件id
   * @return 如果邮件不存在返回-1，否则返回对应的字节数
   */
  int sizeOfMessage(String username, int id);

  /**
   * 检查对应的邮件是否存在
   *
   * @param username 用户名
   * @param id       邮件id
   * @return bool
   */
  boolean messageExists(String username, int id);

  /**
   * 为要删除的邮件设置标记
   *
   * @param username 用户名
   * @param id       邮件id
   * @param marked   标记删除的bool值
   */
  void setMark(String username, int id, boolean marked);

  /**
   * 检查邮件是否标记删除
   *
   * @param username 用户id
   * @param id       邮件id
   * @return 标记删除则为true
   */
  boolean messageMarked(String username, int id);

  /**
   * 返回邮件内容
   *
   * @param username 用户名
   * @param id       邮件id
   * @return 邮件内容的字符串，不存在则返回空
   */
  String getMessage(String username, int id);

  /**
   * 获取邮件的唯一id标识
   *
   * @param username 用户名
   * @param id       邮件id
   * @return 邮件的唯一标识
   */
  String messageUIDL(String username, int id);

  /**
   * 恢复所有标记删除的邮件
   *
   * @param username 用户名
   */
  void restoreMarked(String username);

}
