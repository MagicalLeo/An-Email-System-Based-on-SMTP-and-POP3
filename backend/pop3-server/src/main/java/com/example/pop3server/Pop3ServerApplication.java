package com.example.pop3server;

import com.example.pop3server.util.ServerThread;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.channels.IllegalBlockingModeException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Pop3ServerApplication {

  /* System Messages */
  private static final String ERROR_INVALID_NUMBER_OF_ARGUMENTS = "An invalid number of arguments were specified. Usage: java Pop3Server port [timeout].";
  private static final String ERROR_INVALID_ARGUMENT = "An invalid argument was specified.";
  private static final String ERROR_INVALID_PORT = "An invalid port was specified. Port must be between 0 and 65535 inclusive.";
  private static final String ERROR_INVALID_TIMEOUT = "An invalid timeout was specified. Timeout must be greater than zero.";
  private static final String ERROR_UNABLE_TO_ESTABLISH_SOCKET = "An error occurred while establishing a socket or thread.";
  public static final int ERROR_STATUS = 1;
  private static Integer port;
  private static Integer timeout;


  public static void main(String[] args) {
    SpringApplication.run(Pop3ServerApplication.class, args);
    try {
      if (args.length == 0) {
        //  默认端口110
        port = 110;
        timeout = 600;
      } else if (args.length == 1) {
        port = Integer.parseInt(args[0]);
        timeout = 600;
      } else if (args.length == 2) {
        port = Integer.parseInt(args[0]);
        timeout = Integer.parseInt(args[1]);
      } else {
        System.err.println(ERROR_INVALID_NUMBER_OF_ARGUMENTS);
        System.exit(ERROR_STATUS);
      }
      if (timeout <= 0) {
        throw new IllegalArgumentException(ERROR_INVALID_TIMEOUT);
      }

      if (port < 0 || port > 65535) {
        throw new IllegalArgumentException(ERROR_INVALID_PORT);
      }
      run();
    } catch (NumberFormatException e) {
      System.err.println(ERROR_INVALID_ARGUMENT);
      System.exit(ERROR_STATUS);
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
      System.exit(ERROR_STATUS);
    }
  }

  public static void run() {
    //  在端口110创建serversocket
    try (ServerSocket socket = new ServerSocket(port)) {
      while (true) {
        new ServerThread(socket.accept(), timeout).start();
      }
    } catch (IOException | SecurityException | IllegalBlockingModeException
             | IllegalArgumentException ex) {
      System.err.println(ERROR_UNABLE_TO_ESTABLISH_SOCKET);
    }
  }

}
