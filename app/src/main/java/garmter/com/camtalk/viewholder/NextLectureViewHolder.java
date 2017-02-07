package garmter.com.camtalk.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import garmter.com.camtalk.R;
import garmter.com.camtalk.adapter.ScheduleRvAdapter;
import garmter.com.camtalk.db.LectureDB;
import garmter.com.camtalk.item.DKClass;
import garmter.com.camtalk.utils.CTUtils;

/**
 * Created by Youjung on 2016-12-11.
 */
public class NextLectureViewHolder extends RecyclerView.ViewHolder {

    LinearLayout layoutContainer;
    TextView tvStartTime;
    TextView tvLecture;
    TextView tvRoom;
    TextView tvLeftTime;

    public NextLectureViewHolder(View itemView, LinearLayout layoutContainer,
                                 TextView tvStartTime, TextView tvLecture, TextView tvRoom, TextView tvLeftTime) {
        super(itemView);
        this.layoutContainer = layoutContainer;
        this.tvStartTime = tvStartTime;
        this.tvLecture = tvLecture;
        this.tvRoom = tvRoom;
        this.tvLeftTime = tvLeftTime;
    }

    public static NextLectureViewHolder newInstance(View view) {

        LinearLayout layoutContainer = (LinearLayout) view.findViewById(R.id.layoutContainer);
        TextView tvStartTime = (TextView) view.findViewById(R.id.tvStartTime);
        TextView tvLecture = (TextView) view.findViewById(R.id.tvLecture);
        TextView tvRoom = (TextView) view.findViewById(R.id.tvRoom);
        TextView tvLeftTime = (TextView) view.findViewById(R.id.tvLeftTime);

        return new NextLectureViewHolder(view, layoutContainer, tvStartTime, tvLecture, tvRoom, tvLeftTime);
    }

    public void setView(Context context, final DKClass.PartialTime item, final ScheduleRvAdapter.OnItemClickListener listener) {

        if ( item != null && item.getCode() != null && item.getCode().length() > 0 ) {
            tvStartTime.setText(item.getStartTime_HHMM(context));
            tvLecture.setText(item.getLecture());
            tvRoom.setText(item.getRoom());
            Date date = Calendar.getInstance(Locale.KOREA).getTime();
            String currentHH_mm = new SimpleDateFormat("HH:mm").format(date);
            int duration = CTUtils.betweenTwoTimes(currentHH_mm, item.getStartTime_HHMM(context));
            String leftTime = CTUtils.convertMinutesToHHmm(duration) + " 남음";
            tvLeftTime.setText(leftTime);
            layoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(item.getCode());
                }
            });
        } else {
            tvStartTime.setText("00:00");
            tvLecture.setText("다음 강의가 없습니다.");
            tvRoom.setText("---");
            tvLeftTime.setText("*시간 전");
        }
    }
}
