package com.example.smtpserver.service;

public interface CommandInterpreter {

  /**
   * Parses and executes an SMTP command
   *
   * @param input the command, along with arguments as a string
   * @return the message returned from the server
   */
  String handleInput(String input);

}
