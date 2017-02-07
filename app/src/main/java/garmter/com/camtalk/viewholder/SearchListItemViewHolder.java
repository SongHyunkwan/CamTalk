package garmter.com.camtalk.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import garmter.com.camtalk.R;

/**
 * Created by MinSik on 2017-02-01.
 */

public class SearchListItemViewHolder extends RecyclerView.ViewHolder {

    LinearLayout layoutContainer;
    TextView tvLecture;
    View viewBottomLine;

    public SearchListItemViewHolder(View itemView, LinearLayout layoutContainer, TextView tvLecture, View viewBottomLine){
        super(itemView);
        this.layoutContainer = layoutContainer;
        this.tvLecture = tvLecture;
        this.viewBottomLine = viewBottomLine;
    }

    public static SearchListItemViewHolder newInstance(View view){
        LinearLayout layoutContainer = (LinearLayout) view.findViewById(R.id.layoutContainer);
        TextView tvLectureName = (TextView) view.findViewById(R.id.tvLecture);
        View viewBottomLine = view.findViewById(R.id.viewBottomLine);
        return new SearchListItemViewHolder(view, layoutContainer, tvLectureName, viewBottomLine);
    }

    public void setView(String lecture, final String code, final SearchListItemViewHolder.OnItemClickListener onItemClickListener) {
        if ( lecture != null && lecture.length() > 0 ) {
            layoutContainer.setVisibility(View.VISIBLE);
            tvLecture.setText(lecture);

            layoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClicked(code);
                }
            });
        } else {
            layoutContainer.setVisibility(View.GONE);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(String code);
    }
}
