package com.example.pop3server.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("roles")
public class Role {

  private Integer id;
  private String name;
  private Integer value;
}
