package com.example.mailserver.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("roles")
public class Role {

  private Integer id;
  private String name;
  private Integer value;
}
