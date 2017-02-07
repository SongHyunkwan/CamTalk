package garmter.com.camtalk.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import garmter.com.camtalk.R;
import garmter.com.camtalk.item.ItemData;
import garmter.com.camtalk.utils.CTUtils;

/**
 * Created by Youjung on 2016-12-15.
 */
public class DataListItemViewHolder extends RecyclerView.ViewHolder {

    LinearLayout layoutContainer;
    TextView tvTitle;
    TextView tvTimestamp;
    TextView tvDownloadCount;

    public DataListItemViewHolder(View itemView, LinearLayout layoutContainer, TextView tvTitle,
                                  TextView tvTimestamp, TextView tvDownloadCount) {
        super(itemView);

        this.layoutContainer = layoutContainer;
        this.tvTitle = tvTitle;
        this.tvTimestamp = tvTimestamp;
        this.tvDownloadCount = tvDownloadCount;
    }

    public static DataListItemViewHolder newInstance(View view) {
        LinearLayout layoutContainer = (LinearLayout) view.findViewById(R.id.layoutContainer);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvTimestamp = (TextView) view.findViewById(R.id.tvTimestamp);
        TextView tvDownloadCount = (TextView) view.findViewById(R.id.tvDownloadCount);

        return new DataListItemViewHolder(view, layoutContainer, tvTitle, tvTimestamp, tvDownloadCount);
    }

    public void setView(final ItemData item, final OnItemClickListener listener) {

        if (item != null) {
            layoutContainer.setVisibility(View.VISIBLE);
            tvTitle.setText(item.title);
            tvDownloadCount.setText(String.valueOf(item.download_count));
            CTUtils utils = new CTUtils();
            tvTimestamp.setText(utils.getStringTime(item.timestamp));
            layoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( listener != null ) listener.onItemClicked(item);
                }
            });
        } else {
            layoutContainer.setVisibility(View.GONE);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(ItemData item);
    }

}