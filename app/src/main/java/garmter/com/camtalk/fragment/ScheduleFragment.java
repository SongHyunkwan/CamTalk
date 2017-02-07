package garmter.com.camtalk.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import garmter.com.camtalk.R;
import garmter.com.camtalk.activity.MyActivity;
import garmter.com.camtalk.adapter.ScheduleRvAdapter;
import garmter.com.camtalk.db.LectureDB;
import garmter.com.camtalk.dialog.ClassInfoDialog;
import garmter.com.camtalk.item.DKClass;
import garmter.com.camtalk.network.LoginUtil;
import garmter.com.camtalk.utils.CTActivityUtil;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ScheduleRvAdapter mAdapter;

    private OnFragmentInteractionListener mListener;
    private ClassInfoDialog.OnButtonClickListener mClassInfoClickListener;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScheduleFragment
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ScheduleRvAdapter(getContext(), onItemClickListener);
        mRecyclerView.setAdapter(mAdapter);

        getListOfLectures();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private Timer timer;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if (context instanceof ClassInfoDialog.OnButtonClickListener) {
            mClassInfoClickListener = (ClassInfoDialog.OnButtonClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        if ( timer == null ) timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateSchedule();
            }
        }, 60000, 60000);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mClassInfoClickListener = null;
        if ( timer != null ) timer.cancel();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private ScheduleRvAdapter.OnItemClickListener onItemClickListener = new ScheduleRvAdapter.OnItemClickListener() {
        @Override
        public void onItemClicked(String code) {
            if ( code == null ) return;
            LectureDB db = new LectureDB(getContext());
            db.open();
            DKClass dkClass = db.getDkClass(code);
            db.close();
            ClassInfoDialog dialog = new ClassInfoDialog(ScheduleFragment.this.getContext());
            dialog.setClass(dkClass, mClassInfoClickListener);
            dialog.show();
        }
    };

    private ArrayList<DKClass.PartialTime> listOfTodayClasses;
    private DKClass.PartialTime currentClass;
    private DKClass.PartialTime nextClass;
    private void getListOfLectures() {

        if ( listOfTodayClasses == null ) listOfTodayClasses = new ArrayList<>();

        LectureDB db = new LectureDB(getContext());
        db.open();
        List<DKClass> listOfClasses = db.DBselect();
        List<DKClass.PartialTime> listOfPartialTime = new ArrayList<>();
        for(DKClass dkClass : listOfClasses) {
            DKClass.PartialTime[] partialTimes = dkClass.getPartialTime();
            for(int i=0; i < partialTimes.length; i++) {
                listOfPartialTime.add(partialTimes[i]);
                if ( partialTimes[i].getDayOfWeekInt() == Calendar.getInstance().get(Calendar.DAY_OF_WEEK) )
                    listOfTodayClasses.add(partialTimes[i]);
            }
        }
        mAdapter.setListOfClasses(listOfClasses);

        // 오늘의 수업을 시간순으로 정렬한다.
        Collections.sort(listOfTodayClasses, new Comparator<DKClass.PartialTime>() {
            @Override
            public int compare(DKClass.PartialTime lhs, DKClass.PartialTime rhs) {
                return lhs.getHour() < rhs.getHour() ? -1 : 1;
            }
        });

        // 현재 수업을 찾는다.
        currentClass = null;
        nextClass = null;

        Date date = Calendar.getInstance(Locale.KOREA).getTime();
        String currentHH_mm = new SimpleDateFormat("HH:mm").format(date);
        for(int i=0; i<listOfTodayClasses.size(); i++) {
            if ( listOfTodayClasses.get(i).getStartTime_HHMM(getContext()).compareTo(currentHH_mm) < 0
                    && listOfTodayClasses.get(i).getEndTime_HHMM(getContext()).compareTo(currentHH_mm) > 0 ) {
                currentClass = listOfTodayClasses.get(i);
            }
            if ( listOfTodayClasses.get(i).getStartTime_HHMM(getContext()).compareTo(currentHH_mm) > 0 ) {
                nextClass = listOfTodayClasses.get(i);
                break;
            }
        }

        mAdapter.setCurrentClass(currentClass);
        mAdapter.setNextClass(nextClass);
        mAdapter.notifyDataSetChanged();
        db.close();

    }

    public void updateSchedule() {

    }
}
