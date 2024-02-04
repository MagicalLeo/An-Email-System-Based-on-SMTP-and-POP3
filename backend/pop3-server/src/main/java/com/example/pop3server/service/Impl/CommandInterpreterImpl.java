package com.example.pop3server.service.Impl;

import com.example.pop3server.service.CommandInterpreter;
import com.example.pop3server.service.MailService;
import com.example.pop3server.util.SpringApplicationContextHolder;
import com.example.pop3server.util.State;

public class CommandInterpreterImpl implements CommandInterpreter {

  private final MailService mailService;

  /* Interpreter messages */
  private static final String INVALID_IN_STATE = "-ERR command invalid in the current state";
  private static final String INVALID_ARG_TYPE = "-ERR invalid argument type";
  private static final String TOO_MANY_ARGS = "-ERR too many command arguments";
  private static final String TOO_FEW_ARGS = "-ERR too few command arguments";
  private static final String INCORRECT_NUM_ARGS = "-ERR incorrect number of arguments";
  private static final String INVALID_COMMAND = "-ERR invalid command";
  private static final String USER_OK = "+OK found user account";
  private static final String USER_NOT_FOUND = "-ERR user not found";
  private static final String USER_COMMAND_NOT_SENT = "-ERR USER command not sent";
  private static final String PASSWORD_OK = "+OK user authorised";
  private static final String PASSWORD_INCORRECT = "-ERR password incorrect";
  private static final String QUIT_OK = "+OK quitting";
  private static final String NOOP_OK = "+OK no operation";
  private static final String MESSAGE_NOT_FOUND = "-ERR message not found";
  private static final String MESSAGE_ALREADY_DELETED = "-ERR message already deleted";
  private static final String MESSAGE_MARKED = "+OK message marked as deleted";
  private static final String RESET_OK = "+OK deleted messages restored";
  private static final String INVALID_ARG_VAL = "-ERR invalid argument value";
  private static final String QUIT_ERROR = "-ERR some messages were not deleted";

  private State state;
  private String username;

  public CommandInterpreterImpl() {
    state = State.AUTHORIZATION;
    username = "";
    this.mailService = (MailService) SpringApplicationContextHolder.getSpringBean(
        "mailServiceImpl");
  }

  @Override
  public String handleInput(String input) {
    input = input.replaceAll("[\r\n]+$", "");
    String[] cmdArgs = input.split(" ", 2);
    String in = " " + input;

    switch (cmdArgs[0].toUpperCase()) {
      case "USER":
        return commandUSER(in, cmdArgs);
      case "PASS":
        return commandPASS(in, cmdArgs);
      case "QUIT":
        return commandQUIT(in, cmdArgs);
      case "STAT":
        return commandSTAT(in, cmdArgs);
      case "LIST":
        return commandLIST(in, cmdArgs);
      case "RETR":
        return commandRETR(in, cmdArgs);
      case "DELE":
        return commandDELE(in, cmdArgs);
      case "NOOP":
        return commandNOOP(in, cmdArgs);
      case "RSET":
        return commandRSET(in, cmdArgs);
      case "TOP":
        return commandTOP(in, cmdArgs);
      case "UIDL":
        return commandUIDL(in, cmdArgs);
      default:
        return INVALID_COMMAND + in;
    }
  }

  private String commandUSER(String input, String[] cmd) {
    if (state != State.AUTHORIZATION) {
      return INVALID_IN_STATE + input;
    } else if ((cmd.length != 2) || (cmd[1].split(" ").length != 1)) {
      return INCORRECT_NUM_ARGS + input;
    }

    if (mailService.userExists(cmd[1])) {
      username = cmd[1];
      return USER_OK + input;
    } else {
      return USER_NOT_FOUND + input;
    }
  }

  private String commandPASS(String input, String[] cmd) {
    if (state != State.AUTHORIZATION) {
      return INVALID_IN_STATE + input;
    } else if (cmd.length != 2) {
      return INCORRECT_NUM_ARGS + input;
    } else if (username.equals("")) {
      return USER_COMMAND_NOT_SENT + input;
    }

    if (mailService.passwordCorrect(username, cmd[1])) {
      state = State.TRANSACTION;
      return PASSWORD_OK + input;
    } else {
      return PASSWORD_INCORRECT + input;
    }
  }

  /**
   * 1) 如果服务器处于“处理”状态，么将进入“更新”状态以删除任何标记为删除的邮件，并重返“认证”状态。 2) 如果服务器处于“认证”状态，则结束会话，退出连接
   */
  private String commandQUIT(String input, String[] cmd) {
    if (cmd.length != 1) {
      return INCORRECT_NUM_ARGS + input;
    }

    if (state == State.AUTHORIZATION) {
      return QUIT_OK + input;
    } else {
      state = State.UPDATE;
      return performUpdate() + input;
    }
  }

  /**
   * 服务器返回一个肯定的响应，用于测试连接是否成功
   */
  private String commandNOOP(String input, String[] cmd) {
    if (state != State.TRANSACTION) {
      return INVALID_IN_STATE + input;
    } else if (cmd.length != 1) {
      return INCORRECT_NUM_ARGS + input;
    } else {
      return NOOP_OK + input;
    }
  }

  /**
   * 请求服务器发回关于邮箱的统计资料，如邮件总数和总字节数
   */
  private String commandSTAT(String input, String[] cmd) {
    if (state != State.TRANSACTION) {
      return INVALID_IN_STATE + input;
    } else if (cmd.length != 1) {
      return INCORRECT_NUM_ARGS + input;
    }

    return "+OK " + mailService.numMessages(username, false) + " "
        + mailService.sizeOfMail(username);
  }

  /**
   * 返回邮件的唯一标识符，POP3会话的每个标识符都将是唯一的
   */
  private String commandLIST(String input, String[] cmd) {
    int id;

    if (state != State.TRANSACTION) {
      return INVALID_IN_STATE + input;
    } else if (cmd.length > 2) {
      return TOO_MANY_ARGS + input;
    }

    if (cmd.length == 1) {
      int numMessages = mailService.numMessages(username, true);
      StringBuilder out = new StringBuilder("+OK " + mailService.numMessages(username, false) + " ("
          + mailService.sizeOfMail(username) + ")\r\n");
      for (int i = 1; i <= numMessages; i++) {
        if (!mailService.messageMarked(username, i)) {
          out.append(i).append(" ").append(mailService.sizeOfMessage(username, i)).append("\r\n");
        }
      }
      return out + ".";
    } else {
      try {
        id = Integer.parseInt(cmd[1]);
      } catch (Exception ex) {
        return INVALID_ARG_TYPE + input;
      }

      if (mailService.messageExists(username, id) && !mailService.messageMarked(username, id)) {
        return "+OK " + id + " " + mailService.sizeOfMessage(username, id);
      } else {
        return MESSAGE_NOT_FOUND + input;
      }
    }
  }

  /**
   * 返回由参数标识的邮件的全部文本
   */
  private String commandRETR(String input, String[] cmd) {
    int id;

    if (state != State.TRANSACTION) {
      return INVALID_IN_STATE + input;
    } else if (cmd.length != 2) {
      return INCORRECT_NUM_ARGS + input;
    }

    try {
      id = Integer.parseInt(cmd[1]);
    } catch (NumberFormatException ex) {
      return INVALID_ARG_TYPE + input;
    }

    if (!mailService.messageExists(username, id)) {
      return MESSAGE_NOT_FOUND + input;
    } else if (mailService.messageMarked(username, id)) {
      return MESSAGE_ALREADY_DELETED + input;
    } else {
      return "+OK " + mailService.sizeOfMessage(username, id) + " octets\r\n"
          + mailService.getMessage(username, id) + "\r\n.";
    }
  }

  /**
   * 服务器将由参数标识的邮件标记为删除，由QUIT命令执行
   */
  private String commandDELE(String input, String[] cmd) {
    int id;

    if (state != State.TRANSACTION) {
      return INVALID_IN_STATE + input;
    } else if (cmd.length != 2) {
      return INCORRECT_NUM_ARGS + input;
    }

    try {
      id = Integer.parseInt(cmd[1]);
    } catch (NumberFormatException ex) {
      return INVALID_ARG_TYPE + input;
    }

    if (!mailService.messageExists(username, id)) {
      return MESSAGE_NOT_FOUND + input;
    } else if (mailService.messageMarked(username, id)) {
      return MESSAGE_ALREADY_DELETED + input;
    } else {
      mailService.setMark(username, id, true);
      return MESSAGE_MARKED + input;
    }
  }

  /**
   * 用于清除所有邮件的删除标记。
   */
  private String commandRSET(String input, String[] cmd) {
    if (state != State.TRANSACTION) {
      return INVALID_IN_STATE + input;
    } else if (cmd.length != 1) {
      return INCORRECT_NUM_ARGS + input;
    } else {
      mailService.restoreMarked(username);
      return RESET_OK + input;
    }
  }

  /**
   * Handles the TOP command in the transaction state. An error status is returned if the command is
   * called in an invalid state, an incorrect number of arguments are provided, the arguments have
   * an incorrect type or the message couldn't be found.
   *
   * @param cmd the command, along with arguments
   * @return the server response for the command
   */
  private String commandTOP(String input, String[] cmd) {
    int id, n;
    String[] args;

    if (state != State.TRANSACTION) {
      return INVALID_IN_STATE + input;
    } else if (cmd.length < 2) {
      return TOO_FEW_ARGS + input;
    } else {
      args = cmd[1].split(" ");
      if (args.length != 2) {
        return INCORRECT_NUM_ARGS + input;
      }
    }

    try {
      id = Integer.parseInt(args[0]);
      n = Integer.parseInt(args[1]);
    } catch (NumberFormatException ex) {
      return INVALID_ARG_TYPE + input;
    }

    if (!mailService.messageExists(username, id)) {
      return MESSAGE_NOT_FOUND + input;
    } else if (mailService.messageMarked(username, id)) {
      return MESSAGE_ALREADY_DELETED + input;
    } else if (n < 0) {
      return INVALID_ARG_VAL + input;
    } else {
      String message = mailService.getMessage(username, id);

      /* Check for an empty message */
      if (message == null) {
        return "+OK\r\n.";
      }

      // 将邮件内容分为 头部 和 主体两部分
      String[] tmp = message.split("\n\n", 2);
      String header = tmp[0];
      StringBuilder body = new StringBuilder(tmp[1]);

      /* Split the body into lines */
      String[] bodyLines = body.toString().split("\n");
      body = new StringBuilder();

      /* Reconstruct the body for n lines */
      for (int i = 0; i < bodyLines.length - 1; i++) {
        if (n == i) {
          break;
        }
        body.append("\n").append(bodyLines[i]);
      }
      /* Return the requested lines */
      return "+OK \r\n" + header + "\r\n" + body + "\r\n.";
    }
  }

  /**
   * Handles the UIDL command in the transaction state. An error status is returned if the command
   * was called in an invalid state, if an incorrect number of arguments were provided or if the
   * specified message couldn't be found.
   *
   * @param cmd the command, along with arguments
   * @return the server response for the command
   */
  private String commandUIDL(String input, String[] cmd) {
    int id;

    if (state != State.TRANSACTION) {
      return INVALID_IN_STATE + input;
    } else if (cmd.length > 2) {
      return TOO_MANY_ARGS + input;
    }

    if (cmd.length == 1) {
      int numMessages = mailService.numMessages(username, false);
      StringBuilder out = new StringBuilder("+OK " + numMessages + " ("
          + mailService.sizeOfMail(username) + ")\r\n");
      for (int i = 1; i <= numMessages; i++) {
        out.append(i).append(" ")
            .append(mailService.messageUIDL(username, i)).append("\r\n");
      }
      return out + ".";
    } else {
      try {
        id = Integer.parseInt(cmd[1]);
      } catch (NumberFormatException ex) {
        return INVALID_ARG_TYPE + input;
      }

      if (mailService.messageExists(username, id) && !mailService.messageMarked(username, id)) {
        return "+OK " + id + " " + mailService.messageUIDL(username, id);
      } else {
        return MESSAGE_NOT_FOUND + input;
      }
    }
  }

  /**
   * Deletes any messages marked to be deleted and returns the result from the operation
   *
   * @return +OK if all messages were deleted, -ERR otherwise
   */
  private String performUpdate() {
    if (state != State.UPDATE) {
      return "-ERR cannot delete outside of UPDATE state";
    }

    /* Delete marked messages */
    int numMessagesBeforeDelete = mailService.numMessages(username, true);
    int n = mailService.deleteMarkedMessages(username);

    /* Check how many messages were deleted */
    if (n == (numMessagesBeforeDelete - mailService.numMessages(username, true))) {
      return "+OK " + n + " messages deleted";
    } else {
      return QUIT_ERROR;
    }
  }

}
