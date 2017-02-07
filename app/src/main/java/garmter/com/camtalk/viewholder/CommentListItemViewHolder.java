package garmter.com.camtalk.viewholder;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import garmter.com.camtalk.R;
import garmter.com.camtalk.item.ItemComment;
import garmter.com.camtalk.utils.CTUtils;

/**
 * Created by Youjung on 2016-12-14.
 */
public class CommentListItemViewHolder extends RecyclerView.ViewHolder{

    LinearLayout layoutContainer;
    TextView tvComment;
    TextView tvTag1;
    TextView tvTag2;
    TextView tvTag3;
    LinearLayout layoutLike;
    ImageView ivLike;
    TextView tvLikeCount;
    TextView tvTimestamp;

    public CommentListItemViewHolder(View itemView, LinearLayout layoutContainer, TextView tvComment, TextView tvTag1, TextView tvTag2, TextView tvTag3,
                                     LinearLayout layoutLike, ImageView ivLike, TextView tvLikeCount, TextView tvTimestamp) {
        super(itemView);

        this.layoutContainer = layoutContainer;
        this.tvComment = tvComment;
        this.tvTag1 = tvTag1;
        this.tvTag2 = tvTag2;
        this.tvTag3 = tvTag3;
        this.layoutLike = layoutLike;
        this.ivLike = ivLike;
        this.tvLikeCount = tvLikeCount;
        this.tvTimestamp = tvTimestamp;
    }

    public static CommentListItemViewHolder newInstance(View view) {
        LinearLayout layoutContainer = (LinearLayout) view.findViewById(R.id.layoutContainer);
        TextView tvComment = (TextView) view.findViewById(R.id.tvComment);
        TextView tvTag1 = (TextView) view.findViewById(R.id.tvTag1);
        TextView tvTag2 = (TextView) view.findViewById(R.id.tvTag2);
        TextView tvTag3 = (TextView) view.findViewById(R.id.tvTag3);
        LinearLayout layoutLike = (LinearLayout) view.findViewById(R.id.layoutLike);
        ImageView ivLike = (ImageView) view.findViewById(R.id.ivLike);
        TextView tvLikeCount = (TextView) view.findViewById(R.id.tvLikeCount);
        TextView tvTimestamp = (TextView) view.findViewById(R.id.tvTimestamp);

        return new CommentListItemViewHolder(view, layoutContainer, tvComment, tvTag1, tvTag2, tvTag3, layoutLike, ivLike, tvLikeCount, tvTimestamp);
    }

    public void setView(final ItemComment item, final OnItemClickListener listener) {

        if ( item != null ) {
            layoutContainer.setVisibility(View.VISIBLE);
            tvComment.setText(item.comment);
            if ( item.tag_a != 16  ) {
                tvTag1.setText(CTUtils.getTagTextFromInt(item.tag_a));
                tvTag1.setVisibility(View.VISIBLE);
            } else {
                tvTag1.setVisibility(View.GONE);
            }
            if ( item.tag_b != 16 ) {
                tvTag2.setText(CTUtils.getTagTextFromInt(item.tag_b));
                tvTag2.setVisibility(View.VISIBLE);
            } else {
                tvTag2.setVisibility(View.GONE);
            }
            if ( item.tag_c != 16 ) {
                tvTag3.setText(CTUtils.getTagTextFromInt(item.tag_c));
                tvTag3.setVisibility(View.VISIBLE);
            } else {
                tvTag3.setVisibility(View.GONE);
            }
            layoutLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( listener != null) listener.onItemClicked(item);
                }
            });
            ivLike.setImageResource(item.like_yn ? R.drawable.heart_full : R.drawable.heart_empty);
            tvLikeCount.setText(String.valueOf(item.like_count));
            tvTimestamp.setText(item.timestamp);
        } else {
            layoutContainer.setVisibility(View.GONE);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(ItemComment item);
    }
}
