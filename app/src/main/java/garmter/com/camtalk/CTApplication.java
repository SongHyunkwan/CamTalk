package garmter.com.camtalk;

import android.app.Application;

import garmter.com.camtalk.db.LectureDB;

public class CTApplication extends Application {
    private static CTApplication instance;

    public static CTApplication get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}