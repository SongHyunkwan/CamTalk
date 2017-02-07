package garmter.com.camtalk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import garmter.com.camtalk.R;
import garmter.com.camtalk.item.DKClass;
import garmter.com.camtalk.utils.CTUtils;
import garmter.com.camtalk.viewholder.CurrentLectureViewHolder;
import garmter.com.camtalk.viewholder.NextLectureViewHolder;
import garmter.com.camtalk.viewholder.TimeTableViewHolder;

/**
 * Created by Youjung on 2016-12-06.
 */
public class ScheduleRvAdapter extends RecyclerView.Adapter {

    private int NUM_CURRENT_LECTURE = 1;
    private int NUM_NEXT_LECTURE = 1;
    private int NUM_TIMETABLE = 1;

    private int TYPE_CURRENT_LECTURE = 1;
    private int TYPE_NEXT_LECTURE = 2;
    private int TYPE_TIMETABLE = 3;

    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private int mCellWidth = 0;
    private int mCellHeight = 0;

    private DKClass.PartialTime currentClass;
    private DKClass.PartialTime nextClass;
    public void setCurrentClass(DKClass.PartialTime currentClass) {
        this.currentClass = currentClass;
    }

    public void setNextClass(DKClass.PartialTime nextClass) {
        this.nextClass = nextClass;
    }

    private List<DKClass> listOfClasses = null;
    public void setListOfClasses(List<DKClass> list) {
        this.listOfClasses = list;
    }

    public ScheduleRvAdapter(Context context, OnItemClickListener listener) {
        mContext = context;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float dp18 = CTUtils.convertDpToPixel(mContext, 18);
        mCellWidth = (dm.widthPixels-(int)dp18)/6;
        mCellHeight = dm.heightPixels/30;
        onItemClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {

        if ( position == 0 ) return TYPE_CURRENT_LECTURE;
        else if ( position == 1 ) return TYPE_NEXT_LECTURE;
        else if ( position == 2 ) return TYPE_TIMETABLE;
        else return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if ( viewType == TYPE_CURRENT_LECTURE ) {
            View view = inflater.inflate(R.layout.viewholder_current_lecture, parent, false);
            return CurrentLectureViewHolder.newInstance(view);
        } else if ( viewType == TYPE_NEXT_LECTURE ) {
            View view = inflater.inflate(R.layout.viewholder_next_lecture, parent, false);
            return NextLectureViewHolder.newInstance(view);
        } else if ( viewType == TYPE_TIMETABLE ) {
            View view = inflater.inflate(R.layout.viewholder_timetable, parent, false);
            return TimeTableViewHolder.newInstance(view, mCellWidth, mCellHeight);
        } else
            return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if ( getItemViewType(position) == TYPE_CURRENT_LECTURE ) {
            ((CurrentLectureViewHolder)holder).setView(mContext, currentClass, onItemClickListener);
        } else if ( getItemViewType(position) == TYPE_NEXT_LECTURE ) {
            ((NextLectureViewHolder)holder).setView(mContext, nextClass, onItemClickListener);
        } else if ( getItemViewType(position) == TYPE_TIMETABLE ) {
            ((TimeTableViewHolder)holder).setView(mContext, listOfClasses, onItemClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return NUM_CURRENT_LECTURE + NUM_NEXT_LECTURE + NUM_TIMETABLE;
    }

    public interface OnItemClickListener {
        public void onItemClicked(String code);
    }
}
