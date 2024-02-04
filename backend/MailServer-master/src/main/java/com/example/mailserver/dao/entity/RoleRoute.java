package com.example.mailserver.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("role_routes")
public class RoleRoute {

  private Integer roleId;
  private Integer routeId;
}
