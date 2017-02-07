package garmter.com.camtalk.fragment;

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

import java.util.ArrayList;
import java.util.List;

import garmter.com.camtalk.R;
import garmter.com.camtalk.adapter.LectureListRvAdapter;
import garmter.com.camtalk.adapter.ScheduleRvAdapter;
import garmter.com.camtalk.db.LectureDB;
import garmter.com.camtalk.item.DKClass;
import garmter.com.camtalk.utils.CTActivityUtil;
import garmter.com.camtalk.viewholder.LectureListItemViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LectureListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LectureListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LectureListFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private LectureListRvAdapter mAdapter;
    private OnFragmentInteractionListener mListener;

    public LectureListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LectureListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LectureListFragment newInstance() {
        return new LectureListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lecture_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new LectureListRvAdapter(getContext(), null, onItemClickListener);
        mRecyclerView.setAdapter(mAdapter);

        initData();

        return view;
    }

    private void initData() {
        LectureDB db = new LectureDB(getContext());
        db.open();
        ArrayList<DKClass> listOfClasses = db.DBselect();
        db.close();

        mAdapter.setListOfItem(listOfClasses);
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void searchLecture() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private LectureListItemViewHolder.OnItemClickListener onItemClickListener = new LectureListItemViewHolder.OnItemClickListener() {
        @Override
        public void onItemClicked(String code) {
            CTActivityUtil activityUtil = new CTActivityUtil();
            activityUtil.startLectureDetailActivity(getContext(), code);
        }
    };
}
