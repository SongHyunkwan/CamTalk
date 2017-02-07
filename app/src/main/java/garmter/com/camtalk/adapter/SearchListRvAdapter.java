package garmter.com.camtalk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import garmter.com.camtalk.R;
import garmter.com.camtalk.item.DKClass;
import garmter.com.camtalk.item.ItemComment;
import garmter.com.camtalk.item.ItemSearch;
import garmter.com.camtalk.viewholder.CommentListItemViewHolder;
import garmter.com.camtalk.viewholder.LectureListItemViewHolder;
import garmter.com.camtalk.viewholder.LoadingBarViewHolder;
import garmter.com.camtalk.viewholder.SearchListItemViewHolder;

/**
 * Created by MinSik on 2017-02-01.
 */

public class SearchListRvAdapter extends RecyclerView.Adapter {

    private int COUNT_BOTTOM_LOADING = 1;

    private int TYPE_ITEM = 20;
    private int TYPE_BOTTOM_LOADING = 21;

    private Context mContext;
    private ArrayList<ItemSearch> listOfSearchs;
    private SearchListItemViewHolder.OnItemClickListener mListener;

    public SearchListRvAdapter(Context context, ArrayList<ItemSearch> listOfClasses, SearchListItemViewHolder.OnItemClickListener listener){
        mContext=context;
        mListener=listener;
        listOfSearchs=listOfClasses;
    }

    public void setListOfSearchs(ArrayList<ItemSearch> list) {
        listOfSearchs = list;
        notifyDataSetChanged();
    }

    private boolean visibleLoadingBar = false;
    public void setVisibleLoadingBar(boolean visible) {
        visibleLoadingBar = visible;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if ( visibleLoadingBar && position == getSearchCount() ) {
            return TYPE_BOTTOM_LOADING;
        } else if ( !visibleLoadingBar || ( visibleLoadingBar && position < getSearchCount() ) ) {
            return TYPE_ITEM;
        } else return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if ( viewType == TYPE_ITEM ) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.viewholder_search_list_item, parent, false);
            return SearchListItemViewHolder.newInstance(view);
        } else if ( viewType == TYPE_BOTTOM_LOADING ) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.viewholder_bottom_loading_bar, parent, false);
            return LoadingBarViewHolder.newInstance(view);
        } else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if ( getItemViewType(position) == TYPE_ITEM ) {
            if (listOfSearchs != null && listOfSearchs.size() > position) {
                ItemSearch item = listOfSearchs.get(position);
                SearchListItemViewHolder viewHolder=(SearchListItemViewHolder)holder;
                viewHolder.setView(item.getLecture_name(), item.getLecture_name(), mListener);
            }
        }
    }

    @Override
    public int getItemCount() {
        return getSearchCount();
    }

    private int getSearchCount() {
        return listOfSearchs == null ? 0 : listOfSearchs.size();
    }
}

