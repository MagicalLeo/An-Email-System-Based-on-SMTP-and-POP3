package com.example.pop3server.util;

import lombok.Getter;

@Getter
public enum State {
  AUTHORIZATION,
  TRANSACTION,
  UPDATE
}
