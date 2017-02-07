package garmter.com.camtalk.item;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

import garmter.com.camtalk.utils.CTUtils;

/**
 * Created by Youjung on 2016-12-11.
 */
public class ItemComment implements Parcelable {
    public String comment_id;
    public String comment;
    public int tag_a;
    public int tag_b;
    public int tag_c;
    public String timestamp;
    public boolean like_yn;
    public int like_count;

    public ItemComment() {
        comment_id = "-1";
        comment = "";
        tag_a = 16;
        tag_b = 16;
        tag_c = 16;
        timestamp = "0000-00-00";
        like_yn = false;
        like_count = 0;
    }

    public ItemComment(String comment_id, String comment, int tag_a, int tag_b, int tag_c, String timestamp, boolean like_yn, int like_count) {
        this.comment_id = comment_id;
        this.comment = comment;
        this.tag_a = tag_a;
        this.tag_b = tag_b;
        this.tag_c = tag_c;
        this.timestamp = timestamp;
        this.like_yn = like_yn;
        this.like_count = like_count;
    }

    public ItemComment(String comment_id, String comment, String tag_a, String tag_b, String tag_c, String timestamp, boolean like_yn, int like_count) {
        this.comment_id = comment_id;
        this.comment = comment;
        this.tag_a = CTUtils.getTagIntFromString(tag_a);
        this.tag_b = CTUtils.getTagIntFromString(tag_b);
        this.tag_c = CTUtils.getTagIntFromString(tag_c);
        this.timestamp = timestamp;
        this.like_yn = like_yn;
        this.like_count = like_count;
    }

    protected ItemComment(Parcel in) {
        comment_id = in.readString();
        comment = in.readString();
        tag_a = in.readInt();
        tag_b = in.readInt();
        tag_c = in.readInt();
        timestamp = in.readString();
        like_yn = in.readInt() == 1 ? true : false;
        like_count = in.readInt();
    }

    public static final Creator<ItemComment> CREATOR = new Creator<ItemComment>() {
        @Override
        public ItemComment createFromParcel(Parcel in) {
            return new ItemComment(in);
        }

        @Override
        public ItemComment[] newArray(int size) {
            return new ItemComment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(comment_id);
        dest.writeString(comment);
        dest.writeInt(tag_a);
        dest.writeInt(tag_b);
        dest.writeInt(tag_c);
        dest.writeString(timestamp);
        dest.writeInt(like_yn ? 1 : 0);
        dest.writeInt(like_count);
    }
}
