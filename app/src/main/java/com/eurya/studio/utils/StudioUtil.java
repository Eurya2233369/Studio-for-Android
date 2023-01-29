package com.eurya.studio.utils;

import com.eurya.studio.StudioApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @autuor Eurya QiuZhu
 * @date 2023.1.16
 * @emali 2644635373@qq.com
 */
 
public class StudioUtil {

  private static final String PATH = "/sdcard/StudioIde";

  public static List<File> getProjectHome() {
    List<File> list = new ArrayList<>();  
    var home = new File(PATH);
    if (!home.exists()) {
      home.mkdirs();
    }
    Collections.addAll(list, home.listFiles());
    return list;
  }
  
  public static int dp(float px) {
    return Math.round(StudioApplication.getContext().getResources().getDisplayMetrics().density * px);
  }
}
