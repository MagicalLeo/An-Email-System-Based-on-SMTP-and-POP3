package com.example.mailserver.controller.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

public class UserRequestBody {

  @Data
  public static class ChangePassword {

    @NotEmpty
    private String oldPassword;
    @NotEmpty
    private String newPassword;
  }


  @Data
  public static class GetAuthCode {

    @NotBlank
    @Length(min = 11, max = 11)
    private String phoneNumber;
  }

  @Data
  public static class ResetPassword {

    @NotBlank
    private String uuid;
    @NotBlank
    private String code;
    @NotBlank
    private String newPassword;
  }

  @Data
  public static class AddUser {

    @NotBlank
    private String username;
    @NotBlank
    private String nickname;
    @NotBlank
    private String password;
    private Integer sex;
    private String phone;
    @NotNull
    private Integer roleId;
  }

  @Data
  public static class EditUser {

    private Integer id;
    private String username;
    private String nickname;
    private Integer sex;
    private String phone;
    private Integer disabled;
    private Integer roleId;
    private String authCode;

    private String smtpHost;
    private Integer smtpPort;
    private String pop3Host;
    private Integer pop3Port;
  }

  @Data
  public static class RegisterUser {

    private String username;
    private String password;
  }
}
