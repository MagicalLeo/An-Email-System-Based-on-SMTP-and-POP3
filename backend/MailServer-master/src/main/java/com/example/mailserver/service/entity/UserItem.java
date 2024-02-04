package com.example.mailserver.service.entity;

import com.example.mailserver.dao.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserItem {
    private Integer id;
    private String username;
    private String nickname;
    private Integer sex;
    private String phone;
    private Role role;
    private Integer disabled;
    private String smtpHost;
    private Integer smtpPort;
    private String pop3Host;
    private Integer pop3Port;
    private String authCode;
}
