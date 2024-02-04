package com.example.mailserver.controller.entity;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequestBody {
  @NotEmpty
  private String username;
  @NotEmpty
  private String password;
}
