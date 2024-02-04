package com.example.mailserver.service;

import com.example.mailserver.exception.PasswordErrorException;
import com.example.mailserver.exception.SignTokenException;
import com.example.mailserver.exception.UserDisabledException;
import com.example.mailserver.exception.UserNotFoundException;
import com.example.mailserver.service.entity.LoginResponseBody;

public interface LoginService {
  LoginResponseBody userLoginService(String username, String password) throws
      PasswordErrorException, UserNotFoundException, SignTokenException, UserDisabledException;
}
