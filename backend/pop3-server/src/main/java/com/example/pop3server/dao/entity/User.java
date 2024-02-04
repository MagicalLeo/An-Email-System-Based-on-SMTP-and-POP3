package com.example.pop3server.dao.entity;

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
}
