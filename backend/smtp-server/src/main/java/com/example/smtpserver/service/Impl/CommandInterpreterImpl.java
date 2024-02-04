package com.example.smtpserver.service.Impl;

import com.example.smtpserver.service.CommandInterpreter;
import com.example.smtpserver.service.MailService;
import com.example.smtpserver.util.Base64Decoder;
import com.example.smtpserver.util.SpringApplicationContextHolder;
import com.example.smtpserver.util.State;

public class CommandInterpreterImpl implements CommandInterpreter {

  /* Interpreter messages */
  private static final String HELLO = "250-mail\n" +
      "250-AUTH LOGIN PLAIN\n" +
      "250-AUTH=LOGIN PLAIN\n" +
      "250 8BITMIME";
  private static final String GET_DATA = "354 Enter mail, end with \".\" on a line by itself";
  private static final String INVALID_COMMAND = "500 invalid command";
  private static final String WAIT_PASSWORD = "334 UGFzc3dvcmQ6";
  private static final String WAIT_USER = "334 dxNlcm5hbWU6";
  private static final String USER_NOT_FOUND = "500 user not found";
  private static final String AUTH_SUCCESS = "235 auth successfully";
  private static final String PASSWORD_INCORRECT = "500 password incorrect";
  private static final String QUIT_OK = "221 Bye";
  private static final String OK = "250 OK";

  private final MailService mailService;
  private State state;
  private String username;
  private String content;

  public CommandInterpreterImpl() {
    state = State.INIT;
    username = "";
    content = "";
    this.mailService = (MailService) SpringApplicationContextHolder.getSpringBean(
        "mailServiceImpl");
  }

  @Override
  public String handleInput(String input) {
    String[] cmdArgs = input.split(" ", 2);
    switch (state) {
      case USERNAME:
        return authUSER(input, cmdArgs);
      case PASSWORD:
        return authPASS(input, cmdArgs);
      case DATA:
        return getData(input, cmdArgs);
    }
    switch (cmdArgs[0].toUpperCase()) {
      case "EHLO":
        return commandEHLO(input, cmdArgs);
      case "AUTH":
        return commandAUTH(input, cmdArgs);
      case "MAIL":
        return commandMAIL(input, cmdArgs);
      case "RCPT":
        return commandRCPT(input, cmdArgs);
      case "DATA":
        return commandDATA(input, cmdArgs);
      case "QUIT":
        return commandQUIT(input, cmdArgs);
      default:
        return INVALID_COMMAND;
    }
  }

  private String getData(String in, String[] cmd) {
    if (in.equals(".")) {
      //content += in;

      mailService.addMail(username, content);
      state = State.INIT;
      return OK;
    }
    content += in;
    content += "\n";
    return null;
  }

  private String authUSER(String in, String[] cmd) {
    if (mailService.userExists(Base64Decoder.decode(cmd[0]))) {
      state = State.PASSWORD;
      username = Base64Decoder.decode(cmd[0]);
      return WAIT_PASSWORD;
    }
    return USER_NOT_FOUND;
  }


  private String authPASS(String in, String[] cmd) {
    if (mailService.passwordCorrect(username, Base64Decoder.decode(cmd[0]))) {
      state = State.INIT;
      return AUTH_SUCCESS;
    }
    return PASSWORD_INCORRECT;
  }

  private String commandQUIT(String in, String[] cmd) {
    return QUIT_OK;
  }

  private String commandDATA(String in, String[] cmd) {
    state = State.DATA;
    return GET_DATA;
  }

  private String commandRCPT(String in, String[] cmd) {
    String to = cmd[1].substring(4, cmd[1].length() - 1);
    return OK;
  }

  private String commandMAIL(String in, String[] cmd) {
    String from = cmd[1].substring(6, cmd[1].length() - 1);
    return OK;
  }

  private String commandEHLO(String input, String[] cmd) {
    return HELLO;
  }

  private String commandAUTH(String input, String[] cmd) {
    state = State.USERNAME;
    return WAIT_USER;
  }

}
