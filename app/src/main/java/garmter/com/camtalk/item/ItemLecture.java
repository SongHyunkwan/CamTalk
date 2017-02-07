package garmter.com.camtalk.item;

/**
 * Created by Youjung on 2016-12-28.
 */
public class ItemLecture {

    public String lecture_id;
    public float lec_average;
    public float lec_degree;
    public int tag_count[];
    public String lec_prof;
    public String lecture_name;


    public ItemLecture() {
        lecture_id = "";
        lec_average = 0.0f;
        lec_degree = 0.0f;
        tag_count = new int[16];
        for(int i=0; i<tag_count.length; i++) {
            tag_count[i] = 0;
        }
        lec_prof="";
        lecture_name="";
    }

    public void init(String lecture_id, float lec_average, float lec_degree, int[] tag_count, String lec_prof, String lecture_name) {
        this.lecture_id = lecture_id;
        this.lec_average = lec_average;
        this.lec_degree = lec_degree;
        this.tag_count = tag_count;
        this.lec_prof=lec_prof;
        this.lecture_name=lecture_name;
    }
}
