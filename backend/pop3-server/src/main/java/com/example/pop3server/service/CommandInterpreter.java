package com.example.pop3server.service;

public interface CommandInterpreter {

  /**
   * Parses and executes a POP3 command
   *
   * @param input the command, along with arguments as a string
   * @return the message returned from the server
   */
  String handleInput(String input);

}
