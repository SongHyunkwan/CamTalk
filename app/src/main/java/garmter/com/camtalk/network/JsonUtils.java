package garmter.com.camtalk.network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import garmter.com.camtalk.item.ItemComment;
import garmter.com.camtalk.item.ItemLecture;
import garmter.com.camtalk.item.ItemSearch;

/**
 * Created by Youjung on 2016-12-28.
 */
public class JsonUtils {

    private static String KEY_LECTURE_ID = "lecture_id";
    private static String KEY_LEC_AVERAGE = "lec_average";
    private static String KEY_LEC_DEGREE = "lec_degree";

    private static String KEY_TAG_COUNT = "tag_count";
    private static String KEY_COMMENTS = "comments";
    private static String KEY_SEARCHS="searchs";
    private static String KEY_COMMENT_ID = "comment_id";
    private static String KEY_LEC_COMMENT = "lec_comment";
    private static String KEY_TAGS = "tags";
    private static String KEY_TIMESTAMP = "timestamp";
    private static String KEY_LIKE_YN = "like_yn";
    private static String KEY_LIKE_COUNT = "like_count";
    private static String KEY_USER_ID = "user_id";
    private static String KEY_LECTURE_NAME="lecture_name";
    private static String KEY_LEC_PROF="lec_prof";

    public ArrayList<ItemSearch> getListOfSearchsFromJsonObject(JSONObject obj) throws JSONException{
        ArrayList<ItemSearch> list=new ArrayList<>();

        JSONArray array=obj.getJSONArray(KEY_SEARCHS);
        for(int i=0; i<array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String lecture_name = object.getString(KEY_LECTURE_NAME);
            String timestamp = object.getString(KEY_TIMESTAMP);
            String comment = object.getString(KEY_LEC_COMMENT);
            String lec_prof = object.getString(KEY_LEC_PROF);
            String lecture_id = object.getString(KEY_LECTURE_ID);
            ItemSearch item = new ItemSearch(lecture_name, timestamp, comment, lec_prof, lecture_id);
            list.add(item);
        }
        return list;
    }

    public ItemLecture getItemLectureFromJsonObject(JSONObject obj) throws JSONException {

        ItemLecture item = new ItemLecture();
        String lecture_id = "";
        String lec_prof="";
        String lecture_name="";
        JSONArray comments = obj.getJSONArray(KEY_COMMENTS);
        if ( comments != null && comments.length() > 0 ){
            lecture_id = comments.getJSONObject(0).getString(KEY_LECTURE_ID);
            lec_prof=comments.getJSONObject(0).getString(KEY_LEC_PROF);
            lecture_name=comments.getJSONObject(0).getString(KEY_LECTURE_NAME);
        }

        float lec_average = (float)obj.getDouble(KEY_LEC_AVERAGE);
        float lec_degree = (float)obj.getDouble(KEY_LEC_DEGREE);
        String tagcount = obj.getString(KEY_TAG_COUNT);
        String[] tagcount_array = tagcount.split(":");

        int[] tag_count = new int[16];
        for(int i=0; i<tag_count.length; i++) {
            tag_count[i] = tagcount_array.length > i ? Integer.valueOf(tagcount_array[i]) : 0;
        }


        item.init(lecture_id, lec_average, lec_degree, tag_count, lec_prof, lecture_name);
        return item;
    };


    public ArrayList<ItemComment> getListOfCommentsFromJsonObject(JSONObject obj) throws JSONException {
        ArrayList<ItemComment> list = new ArrayList<>();

        JSONArray array = obj.getJSONArray(KEY_COMMENTS);
        for(int i=0; i<array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String comment_id = object.getString(KEY_COMMENT_ID);
            String comment = object.getString(KEY_LEC_COMMENT);
            String tags = object.getString(KEY_TAGS);
            String tag_a = "";
            String tag_b = "";
            String tag_c = "";
            if ( tags != null && tags.length() > 0 && tags.contains(":")) {
                String[] tag = tags.split(":");
                if ( tag.length > 0 ) tag_a = tag[0];
                if ( tag.length > 1 ) tag_b = tag[1];
                if ( tag.length > 2 ) tag_c = tag[2];
            }
            String timestamp = object.getString(KEY_TIMESTAMP);
            boolean like_yn = false;
            String likeyn = object.getString(KEY_LIKE_YN);
            if ( likeyn != null && "Y".equalsIgnoreCase(likeyn)) like_yn = true;
            int like_count = object.getInt(KEY_LIKE_COUNT);
            String user_id = object.getString(KEY_USER_ID);
            ItemComment item = new ItemComment(comment_id, comment, tag_a, tag_b, tag_c, timestamp, like_yn, like_count);
            list.add(item);
        };

        return list;
    }
}
