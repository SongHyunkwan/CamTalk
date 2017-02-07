package garmter.com.camtalk.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import garmter.com.camtalk.R;
import garmter.com.camtalk.adapter.ScheduleRvAdapter;
import garmter.com.camtalk.item.DKClass;
import me.grantland.widget.AutofitTextView;


/**
 * Created by Youjung on 2016-12-07.
 */
public class TimeTableViewHolder extends RecyclerView.ViewHolder {

    GridLayout layoutGrid;
    int cellWidth;
    int cellHeight;
    int j;
    int ja;
    int colorSave;
    Random rd=new Random();
    ArrayList<String> lectureNameArray=new ArrayList<>();
    ArrayList<Integer> lectureColorArray=new ArrayList<>();

    public TimeTableViewHolder(View itemView, GridLayout layoutGrid, int cellWidth, int cellHeight) {
        super(itemView);
        this.layoutGrid = layoutGrid;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
    }

    public static TimeTableViewHolder newInstance(View itemView, int cellWidth, int cellHeight) {
        GridLayout layoutGrid = (GridLayout) itemView.findViewById(R.id.layoutGrid);
        return new TimeTableViewHolder(itemView, layoutGrid, cellWidth, cellHeight);
    }

    public void setView(Context context, List<DKClass> listOfClasses, ScheduleRvAdapter.OnItemClickListener listener) {

        initGrid(context);
        loadData(context, listOfClasses, listener);
    }

    private void initGrid(Context context) {

        layoutGrid.removeAllViews();
        // 요일
        makeAndAddView(context, "/", 0, 0);
        makeAndAddView(context, "월", 0, 1);
        makeAndAddView(context, "화", 0, 2);
        makeAndAddView(context, "수", 0, 3);
        makeAndAddView(context, "목", 0, 4);
        makeAndAddView(context, "금", 0, 5);

        // 1 ~ 24 교시
        for (int i = 0; i < 24; i++) {
            String s = (i < 9 ? " " : "") + (i + 1) + "  ";
            makeAndAddView(context,s, i + 1, 0);
        }
    }

    // 기본 뷰
    private void makeAndAddView(Context context, String str, int row, int col) {
        AutofitTextView tv = new AutofitTextView(context);
        tv.setText(str);
        tv.setTextColor(context.getResources().getColor(R.color.ct_dark_grey));
        tv.setWidth(cellWidth);
        tv.setHeight(cellHeight);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        // Grid Layout의 row, column 정해주기
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(row), GridLayout.spec(col));
        params.setGravity(Gravity.FILL);
        params.setMargins(1, 1, 1, 1);

        layoutGrid.addView(tv, params);
    }

    /**
     * @return 시간표 정보가 있는지 유무
     */
    private String loadData(Context context, List<DKClass> listOfClasses, ScheduleRvAdapter.OnItemClickListener listener) {

        for (DKClass dkClass : listOfClasses) {

            DKClass.PartialTime[] partials = dkClass.getPartialTime();
            for (int i = 0; i < partials.length; i++) {
                int col = DKClass.getCol(partials[i].getDayOfWeekChar());
                int startHour = partials[i].getHour();
                int timeLen = partials[i].getTimeLength();

                if (startHour == -1 || col < 1 || col > 5)
                    continue;
                makeAndAddView(context, dkClass, partials[i], startHour, col,
                        timeLen, listener);
                lectureColorArray.clear();
                lectureNameArray.clear();

            }

            // 월9,10,11(자연516)/금5,6,7(자연517)
            // 월1,2/화4(자연517)
            // 월10,11,12(자연305)
        }
        return ( listOfClasses == null || listOfClasses.isEmpty() ) ? "DB 내용 없음" : null;
    }

    private void makeAndAddView(final Context context, final DKClass dkClass, DKClass.PartialTime partialTime, int row, int col, int rowSpan, final ScheduleRvAdapter.OnItemClickListener listener) {

        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.cell_lecture, layoutGrid, false);
        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onItemClicked(dkClass.getCode());
            }
        });

        GridLayout.LayoutParams param = (GridLayout.LayoutParams) v.getLayoutParams();
        param.columnSpec = GridLayout.spec(col);
        param.rowSpec = GridLayout.spec(row, rowSpan);
        param.setGravity(Gravity.FILL);
        param.setMargins(1, 1, 1, 1);
        param.height = cellHeight;
        param.width = cellWidth - 3;
        v.setLayoutParams(param);

        TextView tvLecture = (TextView) v.findViewById(R.id.tvLecture);
        tvLecture.setText(dkClass.getLecture());
        tvLecture.setMaxLines(2 * rowSpan);

        TextView tvRoom = (TextView) v.findViewById(R.id.tvRoom);
        tvRoom.setText(partialTime.getRoom());

        TextView tvProfessor = (TextView) v.findViewById(R.id.tvProfessor);
        tvProfessor.setText(dkClass.getProfessor());

        TextView tvTime = (TextView) v.findViewById(R.id.tvTime);
        String time = partialTime.getStartTime_HHMM(context) + "~" + partialTime.getEndTime_HHMM(context);
        tvTime.setText(time);
        layoutGrid.addView(v);


        class RandomNumberTest {
            private static final int MIN_RANGE = 1;
            private static final int MAX_RANGE = 12;
        }

        j = rd.nextInt(12);
        LinearLayout bg = (LinearLayout) v.findViewById(R.id.bg);
        if (j == 0) {
            bg.setBackgroundColor(Color.rgb(255, 200, 205));
        } else if (j == 1) {
            bg.setBackgroundColor(Color.rgb(238, 197, 146));
        } else if (j == 2) {
            bg.setBackgroundColor(Color.rgb(255, 235, 180));
        } else if (j == 3) {
            bg.setBackgroundColor(Color.rgb(218, 235, 180));
        } else if (j == 4) {
            bg.setBackgroundColor(Color.rgb(188, 230, 222));
        } else if (j == 5) {
            bg.setBackgroundColor(Color.rgb(195, 180, 225));
        } else if (j == 6) {
            bg.setBackgroundColor(Color.rgb(225, 225, 225));
        } else if (j == 7) {
            bg.setBackgroundColor(Color.rgb(255, 220, 255));
        } else if (j == 8) {
            bg.setBackgroundColor(Color.rgb(255, 255, 185));
        } else if (j == 9) {
            bg.setBackgroundColor(Color.rgb(188, 255, 230));
        } else if (j == 10) {
            bg.setBackgroundColor(Color.rgb(220, 255, 155));
        } else if (j == 11) {
            bg.setBackgroundColor(Color.rgb(210, 220, 255));
        }

        String lectureName = tvLecture.getText().toString();
        int lectureColor = j;

        for (int i = 0; i < lectureNameArray.size(); i++){
            if(lectureNameArray.get(i).equals(lectureName)){
                colorSave = lectureColorArray.get(i);
            }
        }

        lectureNameArray.add(lectureName);
        if ( colorSave == -1) {
            lectureColorArray.add(lectureColor);
        }else if (colorSave != -1){
            lectureColorArray.add(colorSave);
        }



        for (int i = 0; i < lectureNameArray.size(); i++) {
            if (lectureNameArray.get(i).equals(lectureName)) {
                ja = lectureColorArray.get(i);
                lectureColorArray.remove(i);
                lectureColorArray.add(ja);
            }
        }


        if (ja == 0) {
            bg.setBackgroundColor(Color.rgb(255, 200, 205));
        } else if (ja == 1) {
            bg.setBackgroundColor(Color.rgb(238, 197, 146));
        } else if (ja == 2) {
            bg.setBackgroundColor(Color.rgb(255, 235, 180));
        } else if (ja == 3) {
            bg.setBackgroundColor(Color.rgb(218, 235, 180));
        } else if (ja == 4) {
            bg.setBackgroundColor(Color.rgb(188, 230, 222));
        } else if (ja == 5) {
            bg.setBackgroundColor(Color.rgb(195, 180, 225));
        } else if (ja == 6) {
            bg.setBackgroundColor(Color.rgb(225, 225, 225));
        } else if (ja == 7) {
            bg.setBackgroundColor(Color.rgb(255, 220, 255));
        } else if (ja == 8) {
            bg.setBackgroundColor(Color.rgb(255, 255, 185));
        } else if (ja == 9) {
            bg.setBackgroundColor(Color.rgb(188, 255, 230));
        } else if (ja == 10) {
            bg.setBackgroundColor(Color.rgb(220, 255, 155));
        } else if (ja == 11) {
            bg.setBackgroundColor(Color.rgb(210, 220, 255));
        }
        String lectureColor2 = Integer.toString(lectureColor);
        Log.v("알림", lectureName);
        Log.v("알림", lectureColor2);
        System.out.println("이름 : "+ lectureNameArray.toString());
        System.out.println("색깔 : "+ lectureColorArray.toString());

        colorSave = -1;
    }


}