package com.example.mailserver.utils;

public class JudgeUserUtil {
  private static final int rootAuth = 1;
  private static final int normalAuth = 1 << 1;

  public static boolean isRoot(int auth) {
    return auth == JudgeUserUtil.rootAuth;
  }


  public static boolean isNormal(int auth) {
    return (JudgeUserUtil.normalAuth & auth) != 0;
  }

}

