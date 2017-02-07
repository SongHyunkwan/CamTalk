package garmter.com.camtalk.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import garmter.com.camtalk.R;
import garmter.com.camtalk.adapter.ScheduleRvAdapter;
import garmter.com.camtalk.db.LectureDB;
import garmter.com.camtalk.item.DKClass;

/**
 * Created by Youjung on 2016-12-11.
 */
public class CurrentLectureViewHolder extends RecyclerView.ViewHolder {

    private LinearLayout layoutContainer;
    private TextView tvWeekOfDays;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvLecture;
    private TextView tvProfessor;
    private TextView tvRoom;
    private TextView tvMemo;

    public CurrentLectureViewHolder(View itemView, LinearLayout layoutContainer, TextView tvWeekOfDays, TextView tvStartTime, TextView tvEndTime,
                                    TextView tvLecture, TextView tvProfessor, TextView tvRoom, TextView tvMemo) {
        super(itemView);

        this.layoutContainer = layoutContainer;
        this.tvWeekOfDays = tvWeekOfDays;
        this.tvStartTime = tvStartTime;
        this.tvEndTime = tvEndTime;
        this.tvLecture = tvLecture;
        this.tvProfessor = tvProfessor;
        this.tvRoom = tvRoom;
        this.tvMemo = tvMemo;
    }

    public static CurrentLectureViewHolder newInstance(View view) {
        LinearLayout layoutContainer = (LinearLayout) view.findViewById(R.id.layoutContainer);
        TextView tvWeekOfDays = (TextView) view.findViewById(R.id.tvWeekOfDays);
        TextView tvStartTime = (TextView) view.findViewById(R.id.tvStartTime);
        TextView tvEndTime = (TextView) view.findViewById(R.id.tvEndTime);
        TextView tvLecture = (TextView) view.findViewById(R.id.tvLecture);
        TextView tvProfessor = (TextView) view.findViewById(R.id.tvProfessor);
        TextView tvRoom = (TextView) view.findViewById(R.id.tvRoom);
        TextView tvMemo = (TextView) view.findViewById(R.id.tvMemo);
        return new CurrentLectureViewHolder(view, layoutContainer, tvWeekOfDays, tvStartTime, tvEndTime, tvLecture, tvProfessor, tvRoom, tvMemo);
    }

    public void setView(Context context, final DKClass.PartialTime item, final ScheduleRvAdapter.OnItemClickListener listener) {
        if ( item != null && item.getCode() != null ) {
            tvWeekOfDays.setText(String.valueOf(item.getDayOfWeekChar()));
            tvStartTime.setText(item.getStartTime_HHMM(context));
            tvEndTime.setText(item.getEndTime_HHMM(context));

            LectureDB db = new LectureDB(context);
            db.open();
            DKClass dkClass = db.getDkClass(item.getCode());
            db.close();
            tvLecture.setText(dkClass.getLecture());
            tvProfessor.setText(dkClass.getProfessor());
            tvRoom.setText(item.getRoom());
            tvMemo.setText(dkClass.getMemo());

            layoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(item.getCode());
                }
            });
        } else {
            tvWeekOfDays.setText("+");
            tvStartTime.setText("00:00");
            tvEndTime.setText("00:00");
            tvLecture.setText("");
            tvProfessor.setText("");
            tvRoom.setText("");
            tvMemo.setText("현재 강의가 없습니다.");
        }
    }
}
