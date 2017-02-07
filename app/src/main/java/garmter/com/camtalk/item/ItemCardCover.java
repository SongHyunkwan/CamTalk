package garmter.com.camtalk.item;

/**
 * Created by Youjung on 2017-01-08.
 */
public class ItemCardCover {
    public boolean isCover = true;
    public String title = "";
    public String path = "";
    public boolean delete = false;

    public void setCover(String title) {
        isCover = true;
        this.title = title;
        path = "";
        delete = false;
    }

    public void setFile(String title, String path) {
        isCover = false;
        this.title = title;
        this.path = path;
        delete = false;
    }
}
