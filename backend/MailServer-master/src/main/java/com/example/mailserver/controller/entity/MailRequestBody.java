package com.example.mailserver.controller.entity;

import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

public class MailRequestBody {

  @Data
  public static class SendMail {

    private String[] to;
    private String subject;
    private String content;
    private MultipartFile multipartFile;
    private String filename;
    private Boolean isHtml;
  }

  @Data
  public static class MailSetting {

    private String action; // start stop restart
    @NotNull
    private String appName;
    private Integer port;
  }
}
