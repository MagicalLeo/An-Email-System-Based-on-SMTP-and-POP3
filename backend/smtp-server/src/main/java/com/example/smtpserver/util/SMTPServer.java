package com.example.smtpserver.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SMTPServer extends Thread {

  private final ServerSocket serverSocket;
  private Socket socket;

  public SMTPServer(ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
  }

  @Override
  public void run() {
    System.out.println("SMTP Server is running...");
    while (true) {
      try {
        //  监听客户端的请求，返回一个用于与客户端通信的socket对象
        socket = serverSocket.accept();
        //  创建一个SMTP线程并启动
        SMTPThread thread = new SMTPThread(socket);
        thread.start();
        System.out.println("current Ip" + socket.getInetAddress());
      } catch (IOException e) {
        if (currentThread().isInterrupted()) {
          try {
            serverSocket.close();
            System.out.println("SMTP Server is closed");
            break;
          } catch (IOException ex) {
            ex.printStackTrace();
          }
        }
      }
    }
  }
}
