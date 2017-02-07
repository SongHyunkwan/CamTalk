package garmter.com.camtalk.network;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import garmter.com.camtalk.fragment.SearchListFragement;
import garmter.com.camtalk.utils.CTUtils;

/**
 * Created by Youjung on 2016-12-20.
 */
public class NetworkUtil {

    private String URL_BASE = "http://imjspserver.cafe24.com";
    private String URL_LECTURE_DETAIL = "/lecture/detail.do";
    private String URL_COMMENT_LIKE = "/comment/like.do";
    private String URL_WRITE_COMMENT = "/write/comment.do";
    private String URL_LECTURE_SEARCH="/lecture/search.do";

    public void requestLectureDetail(Context context, String lecture_id, int page, OnNetworkCallback callback) {
        LoginUtil loginUtil = new LoginUtil();
        if ( loginUtil.isLogined(context) ) {

            String url = URL_BASE + URL_LECTURE_DETAIL;

            HashMap<String, Object> header = new HashMap<>();
            header.put("lecture_id", lecture_id);
            header.put("user_id", loginUtil.getUserId(context));
            header.put("page", page);

            NetworkTask networkTask = new NetworkTask();
            networkTask.setHeader(header);
            networkTask.setCallback(callback);
            networkTask.execute(url);
        } else {
            Toast.makeText(context, "로그인 후 이용하세요.", Toast.LENGTH_SHORT).show();
        }
    }

    public void requestLectureSearch(SearchListFragement context, OnNetworkCallback callback){
        String url=URL_BASE + URL_LECTURE_SEARCH;
        NetworkTask networkTask=new NetworkTask();
        networkTask.setCallback(callback);
        networkTask.execute(url);
    }

    public void requestCommentLike(Context context, String comment_id, boolean like, OnNetworkCallback callback) {
        LoginUtil loginUtil = new LoginUtil();
        String userId = loginUtil.getUserId(context);

        String url = URL_BASE + URL_COMMENT_LIKE;
        HashMap<String, Object> header = new HashMap<>();
        header.put("comment_id", comment_id);
        header.put("user_id", userId);
        header.put("like", like ? "Y" : "N");

        NetworkTask networkTask = new NetworkTask();
        networkTask.setHeader(header);
        networkTask.setCallback(callback);
        networkTask.execute(url);
    }

    public void requestWriteComment(Context context, String lecture_id, String lecture_name, String lec_prof, int[] tags, double rate, double easy, String comment, OnNetworkCallback callback) {
        LoginUtil loginUtil = new LoginUtil();
        String userId = loginUtil.getUserId(context);

        String url = URL_BASE + URL_WRITE_COMMENT;

        HashMap<String, Object> header = new HashMap<>();
        header.put("lecture_id", lecture_id);
        header.put("lecture_name", lecture_name);
        header.put("lec_prof", lec_prof);
        header.put("user_id", userId);
        header.put("lec_comment", String.valueOf(comment));
        header.put("lec_average", rate);
        header.put("lec_degree", easy);
        header.put("tags", tags[0] + ":" + tags[1] + ":" + tags[2]);
        NetworkTask networkTask = new NetworkTask();
        networkTask.setHeader(header);
        networkTask.setCallback(callback);
        networkTask.execute(url);
    }

}
