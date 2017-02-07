package garmter.com.camtalk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import garmter.com.camtalk.R;
import garmter.com.camtalk.item.DKClass;
import garmter.com.camtalk.viewholder.LectureListItemViewHolder;

/**
 * Created by Youjung on 2016-12-11.
 */
public class LectureListRvAdapter extends RecyclerView.Adapter {

    private int COUNT_HEADER = 1;
    private int TYPE_HEADER = 1;
    private int TYPE_ITEM = 1;

    private Context mContext;
    private ArrayList<DKClass> mListOfClasses;
    private LectureListItemViewHolder.OnItemClickListener mOnItemClickListener;

    public LectureListRvAdapter(Context context, ArrayList<DKClass> listOfClasses, LectureListItemViewHolder.OnItemClickListener listener) {
        mContext = context;
        mListOfClasses = listOfClasses;
        mOnItemClickListener = listener;
    }

    public void setListOfItem(ArrayList<DKClass> listOfItem) {
        mListOfClasses = listOfItem;
        notifyDataSetChanged();
    }

    /**
     *
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {

        if ( position == 0 ) return TYPE_HEADER;
        else return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if ( viewType == TYPE_HEADER ||  viewType == TYPE_ITEM ) {
            View view = inflater.inflate(R.layout.viewholder_lecture_list_item, parent, false);
            return LectureListItemViewHolder.newInstance(view);
        } else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if ( getItemViewType(position) == TYPE_HEADER
                || getItemViewType(position) == TYPE_ITEM ) {
            LectureListItemViewHolder viewHolder = (LectureListItemViewHolder) holder;
            DKClass item = mListOfClasses.get(position);
            viewHolder.setView(item.getLecture(), item.getCode(),  mOnItemClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return getListOfItems();
    }

    private int getListOfItems() {
        return mListOfClasses == null ? 0 : mListOfClasses.size();
    }
}
