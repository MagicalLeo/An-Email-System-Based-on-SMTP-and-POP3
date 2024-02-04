package com.example.mailserver.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShellUtil {

  public static String executeShell(String path, String... params)
      throws IOException, InterruptedException {
    List<String> cmdList = new ArrayList<>();
    cmdList.add(path);
    cmdList.addAll(Arrays.asList(params));
    String[] cmd = cmdList.toArray(new String[0]);

    //解决脚本没有执行权限
    ProcessBuilder builder = new ProcessBuilder("/bin/chmod", "755", path);
    Process process = builder.start();
    process.waitFor();

    Process ps = Runtime.getRuntime().exec(cmd);
    ps.waitFor();

    BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
    StringBuilder sb = new StringBuilder();
    String line;
    while ((line = br.readLine()) != null) {
      sb.append(line).append("\n");
    }
    return sb.toString();
  }
}
