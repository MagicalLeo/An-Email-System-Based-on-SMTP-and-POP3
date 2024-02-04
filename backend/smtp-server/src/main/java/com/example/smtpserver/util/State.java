package com.example.smtpserver.util;

import lombok.Getter;

@Getter
public enum State {
  INIT,
  USERNAME,
  PASSWORD,
  DATA
}
