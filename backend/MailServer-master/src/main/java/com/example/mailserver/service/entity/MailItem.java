package com.example.mailserver.service.entity;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MailItem {

  private String from;
  private String to;
  private String subject;
  private String content;
  private Date sentDate;
  private Date receivedDate;
  private Integer size;
  private String messageId;
  private Boolean isContainAttachment;
}
