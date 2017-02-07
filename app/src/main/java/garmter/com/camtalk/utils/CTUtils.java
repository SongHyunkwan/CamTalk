package garmter.com.camtalk.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import garmter.com.camtalk.CTApplication;
import garmter.com.camtalk.network.LoginUtil;

public class CTUtils {
    
    public static String getExternalPath(){

        String ext = Environment.getExternalStorageState();
        if(ext.equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else{
            return null;
        }
    }

    public static String getLocalDir(){
        Context context = CTApplication.get().getApplicationContext();
        return context.getFilesDir().toString();
    }
    
    public static String arrayToString(String[] array, String delimiter) {
        StringBuilder sb = new StringBuilder();
        if (array.length > 0) {
            sb.append(array[0]);
            for (int i=1; i<array.length; i++) {
                sb.append(delimiter);
                sb.append(array[i]);
            }
        }
        return sb.toString();
    }

    public void saveError(Exception e) {
        String folderName = CTUtils.getExternalPath() + "/tmp";
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String fileName = folderName + "/dku_time_table.txt";
        File file = new File(fileName);

        try {
            PrintWriter w = new PrintWriter(file);
            Throwable t = e.getCause();
            if (t != null) {
                e.getCause().printStackTrace(w);
            } else if (e.getMessage() != null) {
                w.write(e.getMessage());
            }
            w.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    public String getStringTime(long timestamp) {
        String strTime = "";

        Timestamp ts = new Timestamp(System.currentTimeMillis());
        long cur_timestamp = ts.getTime();
        long duration = cur_timestamp - timestamp;

        if ( duration < 60 * 60* 1000 ) {
            SimpleDateFormat formater = new SimpleDateFormat("mm분 전");
            strTime = formater.format(duration);
        } else if ( duration < 24 * 60 * 60 * 1000 ) {
            SimpleDateFormat formater = new SimpleDateFormat("HH시간 전");
            strTime = formater.format(duration);
        } else {
            SimpleDateFormat formater = new SimpleDateFormat("yyyy.MM.dd");
            strTime = formater.format(timestamp);
        }
        return strTime;
    }

    public static float getWindowWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getWidth();
    }

    public static float getWindowHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getHeight();
    }

    public static float convertDpToPixel(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static float convertPixelsToDp(Context context, float px){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static int convertHHmmToMinutes(String HHmm) {
        if ( HHmm == null || HHmm.length() == 0 ) return 0;

        if ( HHmm.contains(":")) HHmm = HHmm.replace(":", "");

        if ( HHmm.length() == 0 ) return 0;
        if ( HHmm.length() == 1 ) HHmm = "000" + HHmm;
        else if ( HHmm.length() == 2 ) HHmm = "00" + HHmm;
        else if ( HHmm.length() == 3 ) HHmm = "0" + HHmm;

        int hour = Integer.valueOf(HHmm.substring(0, 2));
        int minutes = Integer.valueOf(HHmm.substring(2, 4));

        return 60 * hour + minutes;
    }

    public static String convertMinutesToHHmm(int minutes) {
        int hour = minutes / 60;
        int minute = minutes % 60;

        String _hour = String.valueOf(hour);
        if ( _hour.length() == 1 ) _hour = "0" + _hour;
        String _minute = String.valueOf(minute);
        if ( _minute.length() == 1 ) _minute = "0" + _minute;

        return _hour + ":" +_minute;
    }

    public static String getYYYY_MM_dd() {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        long timestamp = ts.getTime();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
        String strTime = formater.format(timestamp);
        return strTime;
    }

    public static int betweenTwoTimes(String HHmm1, String HHmm2) {
        int min1 = convertHHmmToMinutes(HHmm1);
        int min2 = convertHHmmToMinutes(HHmm2);

        return min2-min1;
    }

    public static String getRootFilePath(Context context) {
        LoginUtil loginUtil = new LoginUtil();
        if ( loginUtil.isLogined(context)) {
            return CTContants.FILE_FLASH_CARD + File.separator + loginUtil.getUserId(context) + File.separator + getSemester();
        } else {
            return "";
        }
    }

    public static String getSemester() {
        String year = "";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat yearform = new SimpleDateFormat("yyyy");
        year = yearform.format(timestamp);

        SimpleDateFormat monthform = new SimpleDateFormat("MM");
        String month = monthform.format(timestamp);

        String semester = "1";
        if ( Integer.valueOf(month) >= 9 ) {
            semester = "2";
        }

        return year + "_" + semester;
    }

    public static int getTagIntFromString(String key) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("tag_hard", 0);
        map.put("tag_absol", 1);
        map.put("tag_read", 2);
        map.put("tag_hell", 3);
        map.put("tag_honey", 4);
        map.put("tag_join", 5);
        map.put("tag_check", 6);
        map.put("tag_handout", 7);
        map.put("tag_point", 8);
        map.put("tag_goodlec", 9);
        map.put("tag_book", 10);
        map.put("tag_teamplay", 11);
        map.put("tag_goodprof", 12);
        map.put("tag_homework", 13);
        map.put("tag_boring", 14);
        map.put("tag_quiz", 15);
        map.put("null", 16);

        int value = 16;
        if ( key != null && key.length() > 0 && map.containsKey(key) )  value = map.get(key);

        return value;
    }

    public static String getTagTextFromInt(int key) {
        HashMap<Integer, String> map = new HashMap<>();
        map.put(0, "#시험어려움");
        map.put(1, "#절대평가");
        map.put(2, "#많이읽어야함");
        map.put(3, "#학점헬");
        map.put(4, "#개꿀잼");
        map.put(5, "#참여도중요");
        map.put(6, "#출첵중요");
        map.put(7, "#유인물사용");
        map.put(8, "#가산점");
        map.put(9, "#강의내용좋음");
        map.put(10, "#교재사용");
        map.put(11, "#팀플많음");
        map.put(12, "#사람참좋다");
        map.put(13, "#과제크리");
        map.put(14, "#강의노잼");
        map.put(15, "#쪽지시험");
        map.put(16, "");

        String value = map.get(key);
        return value;
    }

}
