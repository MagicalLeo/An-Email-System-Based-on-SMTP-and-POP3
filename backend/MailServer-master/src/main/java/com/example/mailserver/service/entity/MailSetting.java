package com.example.mailserver.service.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MailSetting {

  private String smtpServerName;
  private String pop3ServerName;
  private Integer smtpServerPort;
  private Integer pop3ServerPort;
}
