package com.example.mailserver.controller;

import com.example.mailserver.controller.entity.LoginRequestBody;
import com.example.mailserver.exception.PasswordErrorException;
import com.example.mailserver.exception.SignTokenException;
import com.example.mailserver.exception.UserDisabledException;
import com.example.mailserver.exception.UserNotFoundException;
import com.example.mailserver.service.LoginService;
import com.example.mailserver.service.entity.LoginResponseBody;
import com.example.mailserver.utils.Result;
import com.example.mailserver.utils.ResultEnum;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@CrossOrigin
public class LoginController {

  @Resource
  private LoginService loginService;

  /**
   * 登录接口
   *
   * @param req LoginRequestBody
   * @return LoginResponseBody
   */
  @PostMapping("/login")
  public Result<LoginResponseBody> login(@Valid @RequestBody LoginRequestBody req) {
    try {
      LoginResponseBody res = loginService.userLoginService(req.getUsername(), req.getPassword());
      log.info("用户 " + req.getUsername() + " 登录成功！");
      return Result.success(res);
    } catch (PasswordErrorException e) {
      log.error("PasswordErrorException", e);
      return Result.error(ResultEnum.PASSWORD_ERROR);
    } catch (UserNotFoundException e) {
      log.error("UserNotFoundException", e);
      return Result.error(ResultEnum.USER_NOT_FOUND);
    } catch (SignTokenException e) {
      log.error("SignTokenException", e);
      return Result.error(ResultEnum.SIGN_TOKEN_ERROR);
    } catch (UserDisabledException e) {
      log.error("UserDisabledException", e);
      return Result.error(ResultEnum.USER_DISABLED_ERROR);
    } catch (Exception e) {
      log.error("Exception", e);
      return Result.error(ResultEnum.DEFAULT_ERROR);
    }
  }
}

