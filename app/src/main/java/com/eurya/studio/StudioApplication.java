package com.eurya.studio;

import android.app.Application;
import android.content.Context;

public class StudioApplication extends Application {

  private static StudioApplication sApp;

  @Override
  public void onCreate() {
    super.onCreate();
    sApp = this;
  }

  public static Context getContext() {
    return sApp;
  }
}
