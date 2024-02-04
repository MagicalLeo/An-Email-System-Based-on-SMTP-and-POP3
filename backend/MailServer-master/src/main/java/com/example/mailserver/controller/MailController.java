package com.example.mailserver.controller;

import static com.example.mailserver.utils.ResultEnum.ARGUMENT_ERROR;

import com.example.mailserver.controller.entity.MailRequestBody;
import com.example.mailserver.dao.entity.User;
import com.example.mailserver.service.MailService;
import com.example.mailserver.service.UserService;
import com.example.mailserver.service.entity.MailItem;
import com.example.mailserver.service.entity.MailSetting;
import com.example.mailserver.service.entity.QueryListResponseBody;
import com.example.mailserver.utils.CurrentUser;
import com.example.mailserver.utils.JudgeUserUtil;
import com.example.mailserver.utils.Result;
import com.example.mailserver.utils.ResultEnum;
import com.example.mailserver.utils.ShellUtil;
import com.sun.mail.util.MailConnectException;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/mail")
public class MailController {

  @Resource
  private MailService mailService;

  @Resource
  private UserService userService;

  @PostMapping
  public Result<?> sendMail(@CurrentUser User user, MailRequestBody.SendMail req) {
    File file = null;
    try {
      // 非管理员不能群发
      if (req.getTo().length > 1 && !JudgeUserUtil.isRoot(user.getRole().getValue())) {
        return Result.error(ResultEnum.AUTH_ERROR);
      }
      if (req.getMultipartFile() != null) {
        String prefix = Objects.requireNonNull(req.getMultipartFile().getOriginalFilename())
            .split("\\.")[0];
        String suffix = "." + Objects.requireNonNull(req.getMultipartFile().getOriginalFilename())
            .split("\\.")[1];
        file = File.createTempFile(prefix, suffix);
        req.getMultipartFile().transferTo(file);
      }
      mailService.sendMail(user, req.getSubject(), req.getContent(), req.getFilename(), file,
          req.getIsHtml() != null && req.getIsHtml(), req.getTo());
      if (file != null) {
        file.deleteOnExit();
      }
      log.info("用户 " + user.getUsername() + " 发送邮件成功！");
      return Result.success();
    } catch (MailConnectException e) {
      log.error("MailConnectException", e);
      return Result.error(ResultEnum.MAIL_CONNECTION_ERROR);
    } catch (Exception e) {
      log.error("Exception", e);
      return Result.error(ResultEnum.DEFAULT_ERROR);
    }
  }

  @GetMapping
  public Result<QueryListResponseBody<MailItem>> pullMail(@CurrentUser User user) {
    try {
      QueryListResponseBody<MailItem> res = mailService.receiveMail(user);
      log.info("用户 " + user.getUsername() + " 拉取邮件成功！");
      return Result.success(res);
    } catch (MailConnectException e) {
      log.error("MailConnectException", e);
      e.printStackTrace();
      return Result.error(ResultEnum.MAIL_CONNECTION_ERROR);
    } catch (Exception e) {
      log.error("Exception", e);
      e.printStackTrace();
      return Result.error(ResultEnum.DEFAULT_ERROR);
    }
  }

  @GetMapping("/setting")
  public Result<QueryListResponseBody<MailSetting>> getMailSetting() {
    return Result.success(mailService.getMailSettings());
  }

  @PutMapping("/server")
  public Result<?> updateServer(MailRequestBody.MailSetting req) {
    MailSetting mailSetting = (MailSetting) mailService.getMailSettings().getItems().get(0);
    String serverName;
    String serverPort = "";
    if (req.getPort() != null) {
      serverPort = req.getPort().toString();
    }
    if ("pop3".equals(req.getAppName())) {
      serverName = mailSetting.getPop3ServerName();
    } else if ("smtp".equals(req.getAppName())) {
      serverName = mailSetting.getSmtpServerName();
    } else {
      return Result.error(ARGUMENT_ERROR);
    }
    try {
      if (!(req.getAction().equals("start") || req.getAction().equals("stop") || req.getAction()
          .equals("restart"))) {
        return Result.error(ARGUMENT_ERROR);
      }
      ShellUtil.executeShell("mail.sh", req.getAction(), serverName, serverPort);
      if (req.getAction().equals("start") || req.getAction().equals("restart")) {
        //  更新对应的服务器配置
        User setting = userService.getById(88);
        if (serverName.equals("pop3")) {
          setting.setPop3Port(req.getPort());
        } else if (serverName.equals("smtp")) {
          setting.setSmtpPort(req.getPort());
        }
        userService.updateById(setting);
      }
      return Result.success();
    } catch (IOException e) {
      log.error("IOException", e);
    } catch (InterruptedException e) {
      log.error("InterruptedException", e);
    }
    return Result.error(ResultEnum.DEFAULT_ERROR);
  }
}
