package com.example.mailserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.mailserver.mapper")
public class MailServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(MailServerApplication.class, args);
  }

}
