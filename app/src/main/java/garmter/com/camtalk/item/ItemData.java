package garmter.com.camtalk.item;

/**
 * Created by Youjung on 2016-12-11.
 */
public class ItemData {
    public String material_id;
    public String title;
    public long timestamp;
    public String download_url;
    public int download_count;

    public ItemData() {
        material_id = "";
        title = "";
        timestamp = 0;
        download_url = "";
        download_count = 0;
    }

    public ItemData(String material_id, String title, long timestamp, String download_url, int download_count) {
        this.material_id = material_id;
        this.title = title;
        this.timestamp = timestamp;
        this.download_url = download_url;
        this.download_count = download_count;
    }
}
