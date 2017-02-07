package garmter.com.camtalk.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import garmter.com.camtalk.R;
import garmter.com.camtalk.item.ItemCardCover;
import garmter.com.camtalk.utils.CTUtils;

/**
 * Created by Youjung on 2016-12-16.
 */
public class CardListItemViewHolder extends RecyclerView.ViewHolder {

    RelativeLayout layoutContainer;
    ImageView ivBackground;
    CheckBox cbDelete;
    TextView tvTitle;

    public CardListItemViewHolder(View itemView, RelativeLayout layoutContainer,
                                  ImageView ivBackground, CheckBox cbDelete, TextView tvTitle) {
        super(itemView);
        this.layoutContainer = layoutContainer;
        this.ivBackground = ivBackground;
        this.cbDelete = cbDelete;
        this.tvTitle = tvTitle;
    }

    public static CardListItemViewHolder newInstance(View view, int mHeight) {

        RelativeLayout layoutContainer = (RelativeLayout) view.findViewById(R.id.layoutContainer);
        ImageView ivBackground = (ImageView) view.findViewById(R.id.ivBackground);
        CheckBox cbDelete = (CheckBox) view.findViewById(R.id.cbDelete);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeight);
        layoutContainer.setLayoutParams(params);

        return new CardListItemViewHolder(view, layoutContainer, ivBackground, cbDelete, tvTitle);
    }

    public void setView(Context context, final ItemCardCover item, boolean deleteMode, final OnItemClickListener listener) {
        if ( item != null ) {
            if (item.title != null && item.title.length() > 0) {
                layoutContainer.setVisibility(View.VISIBLE);
                tvTitle.setText(item.title);
                ivBackground.setImageResource(item.isCover ? R.drawable.img_folder : R.drawable.solid_box_white);
                tvTitle.setPadding((item.isCover ? (int) CTUtils.convertDpToPixel(context, 18) : 0), 0, 0, 0);
                ivBackground.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            if (item.isCover) listener.onOpenDirectory(item.title);
                            else listener.onShowCard(item.title);
                        }
                    }
                });
                ivBackground.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (listener != null) {
                            listener.onRenameCard(item.title);
                            return true;
                        } else {
                            return false;
                        }
                    }
                });

                if (deleteMode && !item.title.equalsIgnoreCase("BACK")) {
                    cbDelete.setVisibility(View.VISIBLE);
                    cbDelete.setChecked(item.delete);
                    cbDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                item.delete = !item.delete;
                                cbDelete.setChecked(item.delete);
                                listener.onCheckRemoveCard(item);
                            }
                        }
                    });
                } else {
                    cbDelete.setVisibility(View.GONE);
                }
                if (!deleteMode) cbDelete.setChecked(false);
            } else {
                layoutContainer.setVisibility(View.GONE);
            }
        }
    }

    public interface OnItemClickListener {
        public void onShowCard(String title);
        public void onRenameCard(String title);
        public void onCheckRemoveCard(ItemCardCover item);
        public void onOpenDirectory(String title);
    }
}
