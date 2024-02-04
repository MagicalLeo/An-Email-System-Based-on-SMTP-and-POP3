package com.example.mailserver.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("routes")
public class Route {

  private Integer id;
  private String path;
  private String method;
}

