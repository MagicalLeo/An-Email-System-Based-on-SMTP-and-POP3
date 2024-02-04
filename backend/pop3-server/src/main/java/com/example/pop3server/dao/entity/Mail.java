package com.example.pop3server.dao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Mail {

  @TableId
  private Integer uid;
  private String fromMail;
  private String toMail;
  private String message;
  private Integer deleted;
}
