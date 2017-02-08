package garmter.com.camtalk.item;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;


/**
 * Created by MinSik on 2017-01-31.
 */

public class ItemSearch extends RealmObject implements Parcelable{
    public String lecture_name;
    public String timestamp;
    public String lec_comment;
    public String lec_prof;
    public String lecture_id;
    public String tag_a;
    public String tag_b;
    public String tag_c;

    public ItemSearch(){

    }

    public String toString(){
        return "[lecture_name=" + lecture_name + ", lec_timestamp=" + timestamp + ", lec_comment="
                + lec_comment + ", lec_prof=" + lec_prof + ", lecture_id=" + lecture_id + ", tag_a="+ tag_a + ", tag_b="+ tag_b + ", tag_c="+ tag_c + "]";
    }

    public ItemSearch(String lecture_name, String timestamp, String lec_comment, String lec_prof, String lecture_id, String tag_a, String tag_b, String tag_c){
        this.lecture_name=lecture_name;
        this.timestamp=timestamp;
        this.lec_comment=lec_comment;
        this.lec_prof=lec_prof;
        this.lecture_id=lecture_id;
        this.tag_a=tag_a;
        this.tag_b=tag_b;
        this.tag_c=tag_c;
    }

    protected ItemSearch(Parcel in) {
        lecture_name = in.readString();
        timestamp = in.readString();
        lec_comment = in.readString();
        lec_prof = in.readString();
        lecture_id = in.readString();
        tag_a=in.readString();
        tag_b=in.readString();
        tag_c=in.readString();
    }
    public String getTag_a(){
        return tag_a;
    }
    public void setTag_a(String tag_a){
        this.tag_a=tag_a;
    }
    public String getTag_b(){
        return tag_b;
    }
    public void setTag_b(String tag_b){
        this.tag_b=tag_b;
    }
    public String getTag_c(){
        return tag_c;
    }
    public void setTag_c(String tag_c){
        this.tag_a=tag_c;
    }
    public String getLecture_id() {
        return lecture_id;
    }
    public void setLecture_id(String lecture_id) {
        this.lecture_id = lecture_id;
    }
    public String getLecture_name() {
        return lecture_name;
    }
    public void setLecture_name(String lecture_name) {
        this.lecture_name = lecture_name;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String lec_timestamp) {
        this.timestamp = lec_timestamp;
    }
    public String getLec_comment() {
        return lec_comment;
    }
    public void setLec_comment(String lec_comment) {
        this.lec_comment = lec_comment;
    }
    public String getLec_prof() {
        return lec_prof;
    }
    public void setLec_prof(String lec_prof) {
        this.lec_prof = lec_prof;
    }

    public static final Creator<ItemSearch> CREATOR = new Creator<ItemSearch>() {
        @Override
        public ItemSearch createFromParcel(Parcel in) {
            return new ItemSearch(in);
        }

        @Override
        public ItemSearch[] newArray(int size) {
            return new ItemSearch[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(lecture_name);
        parcel.writeString(timestamp);
        parcel.writeString(lec_comment);
        parcel.writeString(lec_prof);
        parcel.writeString(lecture_id);
        parcel.writeString(tag_a);
        parcel.writeString(tag_b);
        parcel.writeString(tag_c);
    }
}
