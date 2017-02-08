package garmter.com.camtalk.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import garmter.com.camtalk.activity.CardViewerActivity;
import garmter.com.camtalk.activity.LectureDetailActivity;
import garmter.com.camtalk.activity.LoginActivity;
import garmter.com.camtalk.activity.MainActivity;
import garmter.com.camtalk.activity.MakeCardActivity;
import garmter.com.camtalk.activity.MyActivity;
import garmter.com.camtalk.activity.PptViewerActivity;
import garmter.com.camtalk.activity.SearchActivity;
import garmter.com.camtalk.activity.WriteCommentActivity;


/**
 * Created by Youjung on 2016-12-06.
 */
public class CTActivityUtil {

    public static final int REQUEST_MY_LOGOUT = 1000;
    public static final int REQUEST_WRITE_COMMENT = 1001;
    public static final int REQUEST_MAKE_CARD = 1002;

    public static final String KEY_LECTURE_ID = "lecture_id";
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_DIRECTORY = "directory";
    public static final String KEY_TITLE = "title";
    public static final String KEY_LECTURE_NAME = "lecture_name";
    public static final String KEY_LEC_PROF = "lec_prof";

    public void startLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public void startSearchActivity(Context context){
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    public void startMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public void startMyActivity(Context context) {
        Intent intent = new Intent(context, MyActivity.class);
        if ( context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, REQUEST_MY_LOGOUT);
        }
    }

    public void startLectureDetailActivity(Context context, String lecture_id) {
        if ( lecture_id == null ) lecture_id = "";
        Intent intent = new Intent(context, LectureDetailActivity.class);
        intent.putExtra(KEY_LECTURE_ID, lecture_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public void startLectureDetailActivity(Context context, String lecture_id, String lecture_name, String lec_prof) {
        if ( lecture_id == null ) lecture_id = "";
        Intent intent = new Intent(context, LectureDetailActivity.class);
        intent.putExtra(KEY_LECTURE_ID, lecture_id);
        intent.putExtra(KEY_LECTURE_NAME, lecture_name);
        intent.putExtra(KEY_LEC_PROF, lec_prof);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public void startWriteCommentActivity(Activity activity, String lectureId, String lecturedName, String profName) {
        Intent intent = new Intent(activity, WriteCommentActivity.class);
        intent.putExtra(KEY_LECTURE_ID, lectureId);
        intent.putExtra(KEY_LECTURE_NAME, lecturedName);
        intent.putExtra(KEY_LEC_PROF, profName);
        activity.startActivityForResult(intent, REQUEST_WRITE_COMMENT);
    }

    public void startMakeCardActivity(Activity activity, String directory, String title) {
        Intent intent = new Intent(activity, MakeCardActivity.class);
        intent.putExtra(KEY_DIRECTORY, directory);
        intent.putExtra(KEY_TITLE, title);
        activity.startActivityForResult(intent, REQUEST_MAKE_CARD);
    }

    public void startCardViewerActivity(Context context, String directory, String title) {
        Intent intent = new Intent(context, CardViewerActivity.class);
        intent.putExtra(KEY_DIRECTORY, directory);
        intent.putExtra(KEY_TITLE, title);
        context.startActivity(intent);
    }

    public void startPptViewerActivity(Context context) {
        Intent intent = new Intent(context, PptViewerActivity.class);
        context.startActivity(intent);
    }

    public static int PERMISSIONS_REQUEST_STORAGE = 10002;

}
