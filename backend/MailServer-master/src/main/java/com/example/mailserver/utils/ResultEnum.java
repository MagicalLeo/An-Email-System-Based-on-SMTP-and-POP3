package com.example.mailserver.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultEnum {
  SUCCESS(0, "请求成功"),
  DEFAULT_ERROR(10001, "请求失败"),
  ARGUMENT_ERROR(10002, "参数错误"),
  SIGN_TOKEN_ERROR(10003, "生成签名失败"),
  PASSWORD_ERROR(20001, "密码错误"),
  USER_NOT_FOUND(20002, "用户未找到"),
  AUTH_ERROR(20003, "权限不满足"),
  PARSE_TOKEN_ERROR(20004, "Token 解析失败"),
  JSON_PARSE_ERROR(20005, "Json解析失败"),
  MAIL_CONNECTION_ERROR(20006, "无法连接到邮件服务器"),
  USER_DISABLED_ERROR(20008, "用户已被冻结"),
  USER_EXISTED_ERROR(20009, "用户已存在，请检查用户名"),
  NO_BALANCE_ERROR(200010, "短信余额不足"),
  AUTH_CODE_EXPIRED_ERROR(200011, "验证码过期"),
  AUTH_CODE_ERROR(200012, "验证码错误");

  private final int code;
  private final String msg;
}
