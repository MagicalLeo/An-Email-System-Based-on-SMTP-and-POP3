package com.example.mailserver.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("users")
public class User {

  private Integer id;
  private String username;
  private String nickname;
  private String password;
  private Integer sex;
  private String phone;
  private Integer disabled;
  private Integer roleId;
  @TableField(exist = false)
  private Role role;

  private String smtpHost;
  private Integer smtpPort;
  private String pop3Host;
  private Integer pop3Port;
  private String authCode;

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", nickname='" + nickname + '\'' +
        ", password='" + password + '\'' +
        ", sex=" + sex +
        ", phone='" + phone + '\'' +
        ", disabled=" + disabled +
        ", roleId=" + roleId +
        ", role=" + role +
        ", smtpHost='" + smtpHost + '\'' +
        ", smtpPort=" + smtpPort +
        ", pop3Host='" + pop3Host + '\'' +
        ", pop3Port=" + pop3Port +
        ", authCode='" + authCode + '\'' +
        '}';
  }
}
