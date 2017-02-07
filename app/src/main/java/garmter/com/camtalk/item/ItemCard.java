package garmter.com.camtalk.item;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Youjung on 2016-12-16.
 */
public class ItemCard {

    public String path = "";
    public String title = "";
    public String front = "";
    public String back = "";
    public boolean ok = false;

    public String toJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("front", front);
            obj.put("back", back);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }
}
