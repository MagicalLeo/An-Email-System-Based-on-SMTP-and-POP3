package com.example.smtpserver.util;

import com.example.smtpserver.service.CommandInterpreter;
import com.example.smtpserver.service.Impl.CommandInterpreterImpl;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SMTPThread extends Thread {

  private BufferedWriter writer = null;
  private BufferedReader reader = null;
  CommandInterpreter interpreter;

  public SMTPThread(Socket socket) {
    try {
      this.interpreter = new CommandInterpreterImpl();
      //  套接字输入流
      reader = new BufferedReader(
          new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

      //  套接字输出流
      writer = new BufferedWriter(
          new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getMessage() {
    try {
      return reader.readLine();
    } catch (IOException e) {
      interrupt();
    }
    return null;
  }

  public void sendMessage(String message) {
    StringBuilder commandBuffer = new StringBuilder();
    commandBuffer.setLength(0);
    commandBuffer.append(message);
    commandBuffer.append("\r\n");
    try {
      writer.write(commandBuffer.toString());
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    sendMessage("220 test.com this is SMTP Server");
    while (!isInterrupted()) {
      //  邮件服务器接收来自客户端发送的邮件
      String message = getMessage();
      if (message != null) {
        //  接收到了邮件，交给interpreter来处理邮件
        String res = interpreter.handleInput(message);
          if (res != null) {
              sendMessage(res);
          }
      }
    }
  }
}
