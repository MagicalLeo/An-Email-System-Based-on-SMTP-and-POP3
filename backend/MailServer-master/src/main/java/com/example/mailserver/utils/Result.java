package com.example.mailserver.utils;

import lombok.Data;

@Data
public class Result<V> {
  private int code;
  private V data;
  private String msg;

  public static <T> Result<T> success(T data) {
    final Result<T> result = new Result<>();
    result.setCode(0);
    result.setMsg(ResultEnum.SUCCESS.getMsg());
    result.setData(data);

    return result;
  }

  public static Result<Void> success() {
    final Result<Void> result = new Result<>();
    result.setCode(0);
    result.setMsg(ResultEnum.SUCCESS.getMsg());
    result.setData(null);

    return result;
  }

  public static <T> Result<T> error(ResultEnum resultEnum) {
    final Result<T> result = new Result<>();
    result.setCode(resultEnum.getCode());
    result.setMsg(resultEnum.getMsg());
    result.setData(null);

    return result;
  }

  public static <T> Result<T> error(ResultEnum resultEnum, T data) {
    final Result<T> result = new Result<>();
    result.setCode(resultEnum.getCode());
    result.setMsg(resultEnum.getMsg());
    result.setData(data);

    return result;
  }

  public static <T> Result<T> error(T data) {
    final Result<T> result = new Result<>();
    result.setCode(ResultEnum.DEFAULT_ERROR.getCode());
    result.setMsg(ResultEnum.DEFAULT_ERROR.getMsg());
    result.setData(data);

    return result;
  }

  public static Result<Void> error() {
    final Result<Void> result = new Result<>();
    result.setCode(ResultEnum.DEFAULT_ERROR.getCode());
    result.setMsg(ResultEnum.DEFAULT_ERROR.getMsg());
    result.setData(null);

    return result;
  }
}

