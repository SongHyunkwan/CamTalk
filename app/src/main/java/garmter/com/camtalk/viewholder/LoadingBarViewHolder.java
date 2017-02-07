package garmter.com.camtalk.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Youjung on 2017-01-01.
 */
public class LoadingBarViewHolder extends RecyclerView.ViewHolder {

    public LoadingBarViewHolder(View itemView) {
        super(itemView);
    }

    public static LoadingBarViewHolder newInstance(View parent) {
        return new LoadingBarViewHolder(parent);
    }

}
