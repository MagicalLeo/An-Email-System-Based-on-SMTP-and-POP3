package com.example.mailserver.service.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseBody {
  private String token;
  private String username;
  private String nickname;
  private Integer id;
  private Integer role;
}

