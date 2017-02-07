package garmter.com.camtalk.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by Youjung on 2016-12-04.
 */
public class CTContants {

    public static final String TITLE_TAB1 = "수업자료";
    public static final String TITLE_TAB2 = "시간표";
    public static final String TITLE_TAB3 = "공부";

    public static final int INDEX_TAB1 = 0;
    public static final int INDEX_TAB2 = 1;
    public static final int INDEX_TAB3 = 2;

    public static final String URL_NOTICE = "http://www.dankook.ac.kr/web/kor/-390";
    public static final String URL_FOOD_J = "http://www.dankook.ac.kr/web/kor/-556";
    public static final String URL_FOOD_C = "http://www.dankook.ac.kr/web/kor/-561";

    public static final String FILE_FLASH_CARD = Environment.getExternalStorageDirectory() + File.separator + "card";
}
