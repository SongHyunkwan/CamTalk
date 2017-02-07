package garmter.com.camtalk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import garmter.com.camtalk.R;
import garmter.com.camtalk.item.ItemComment;
import garmter.com.camtalk.viewholder.CommentListItemViewHolder;
import garmter.com.camtalk.viewholder.LoadingBarViewHolder;

/**
 * Created by Youjung on 2016-12-14.
 */
public class CommentRvAdapter extends RecyclerView.Adapter {

    private int COUNT_BOTTOM_LOADING = 1;

    private int TYPE_ITEM = 20;
    private int TYPE_BOTTOM_LOADING = 21;

    private Context mContext;
    private CommentListItemViewHolder.OnItemClickListener mListener;
    private ArrayList<ItemComment> listOfComments;

    public CommentRvAdapter(Context context, CommentListItemViewHolder.OnItemClickListener listener) {
        mContext = context;
        mListener = listener;
        listOfComments = new ArrayList<>();
    }

    public void setListOfComments(ArrayList<ItemComment> list) {
        listOfComments = list;
        notifyDataSetChanged();
    }

    private boolean visibleLoadingBar = false;
    public void setVisibleLoadingBar(boolean visible) {
        visibleLoadingBar = visible;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if ( visibleLoadingBar && position == getCommentCount() ) {
            return TYPE_BOTTOM_LOADING;
        } else if ( !visibleLoadingBar || ( visibleLoadingBar && position < getCommentCount() ) ) {
            return TYPE_ITEM;
        } else return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if ( viewType == TYPE_ITEM ) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.viewholder_comment, parent, false);
            return CommentListItemViewHolder.newInstance(view);
        } else if ( viewType == TYPE_BOTTOM_LOADING ) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.viewholder_bottom_loading_bar, parent, false);
            return LoadingBarViewHolder.newInstance(view);
        } else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if ( getItemViewType(position) == TYPE_ITEM ) {
            if (listOfComments != null && listOfComments.size() > position) {
                ItemComment item = listOfComments.get(position);
                CommentListItemViewHolder viewHolder = (CommentListItemViewHolder) holder;
                viewHolder.setView(item, mListener);
            }
        }
    }

    @Override
    public int getItemCount() {
        return getCommentCount() + (visibleLoadingBar ? COUNT_BOTTOM_LOADING : 0);
    }

    private int getCommentCount() {
        return listOfComments == null ? 0 : listOfComments.size();
    }

}
