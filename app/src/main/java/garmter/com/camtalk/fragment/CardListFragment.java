package garmter.com.camtalk.fragment;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import garmter.com.camtalk.R;
import garmter.com.camtalk.adapter.CardListItemDecoration;
import garmter.com.camtalk.adapter.CardListRvAdapter;
import garmter.com.camtalk.db.LectureDB;
import garmter.com.camtalk.dialog.CTCardInfoDialog;
import garmter.com.camtalk.dialog.CTDialog;
import garmter.com.camtalk.item.DKClass;
import garmter.com.camtalk.item.ItemCard;
import garmter.com.camtalk.item.ItemCardCover;
import garmter.com.camtalk.utils.CTActivityUtil;
import garmter.com.camtalk.utils.CTUtils;
import garmter.com.camtalk.viewholder.CardListItemViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CardListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CardListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CardListRvAdapter mAdapter;
    private OnFragmentInteractionListener mListener;

    public static String ARG_TITLE = "title";

    private ArrayList<DKClass> listOfClasses;
    private ArrayList<ItemCardCover> listOfClassTitles;
    private ArrayList<ItemCardCover> listOfFiles;
    private String mCurPath;

    public CardListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CardListFragment.
     */
    public static CardListFragment newInstance() {
        return new CardListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_list, container, false);
        initView(view);
        // read lecture list
        initData();

        // make directory
        makeDirectoryOfLectures();

        // read directory
        if ( savedInstanceState != null ) {
            String title = savedInstanceState.getString(ARG_TITLE, "");
            if (title != null && title.length() > 0) {
                openStudyDirectory(title);
                return view;
            }
        }

        mAdapter.setListOfFiles(true, listOfClassTitles);
        return view;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mAdapter = new CardListRvAdapter(getContext(), onItemClickListener);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new CardListItemDecoration( (int)CTUtils.convertDpToPixel(getContext(), 9.0f), 3));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
    }

    private void initData() {
        LectureDB db = new LectureDB(getContext());
        db.open();
        listOfClasses = db.DBselect();
        db.close();
    }

    private void makeDirectoryOfLectures() {
        String root = CTUtils.getRootFilePath(getContext());
        File directory = new File(root);
        if ( !directory.exists() ) {
            directory.mkdirs();
        }

        mCurPath = root;

        if ( listOfClassTitles == null ) listOfClassTitles = new ArrayList<>();

        for(int i=0; i<listOfClasses.size(); i++) {
            DKClass dkClass = listOfClasses.get(i);
            if ( dkClass != null && dkClass.getLecture() != null && dkClass.getLecture().length() > 0 ) {
                String path = root + File.separator + dkClass.getLecture();
                File dir = new File(path);
                if ( !dir.exists() ) dir.mkdir();
                ItemCardCover item = new ItemCardCover();
                item.setCover(dkClass.getLecture());
                listOfClassTitles.add(item);
            }
        }
    }

    private CardListItemViewHolder.OnItemClickListener onItemClickListener = new CardListItemViewHolder.OnItemClickListener() {
        @Override
        public void onShowCard(String title) {

            if ( "BACK".equalsIgnoreCase(title) ) {
                mCurPath = mCurPath.substring(0, mCurPath.lastIndexOf(File.separator));
                mAdapter.setListOfFiles(true, listOfClassTitles);
                if ( listOfDeletes != null ) listOfDeletes.clear();
                isDeleteMode = false;
                mAdapter.setDeleteMode(isDeleteMode);
            } else {
                CTActivityUtil activityUtil = new CTActivityUtil();
                activityUtil.startCardViewerActivity(getContext(), mCurPath, title);
            }
        }

        @Override
        public void onRenameCard(String title) {
            if ( !"BACK".equalsIgnoreCase(title) ) {
                CTCardInfoDialog dialog = new CTCardInfoDialog(getContext());
                dialog.setData(mCurPath, title, onButtonClickListener);
                dialog.show();
            }
        }

        @Override
        public void onCheckRemoveCard(ItemCardCover item) {
            if ( listOfDeletes == null ) listOfDeletes = new ArrayList<>();
            if ( item.delete ) {
                listOfDeletes.add(item);
            } else {
                listOfDeletes.remove(item);
            }
        }

        @Override
        public void onOpenDirectory(String title) {
            openStudyDirectory(title);
        }
    };

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

    public void openStudyDirectory(String title) {

        if ( title != null && title.length() > 0 && getContext() != null) {
            if ( listOfFiles == null ) listOfFiles = new ArrayList<>();
            listOfFiles.clear();

            mCurPath = CTUtils.getRootFilePath(getContext()) + File.separator + title;
            File dir = new File(mCurPath);

            if ( !dir.exists() ) dir.mkdir();
            ItemCardCover back = new ItemCardCover();
            back.setFile("BACK", CTUtils.getRootFilePath(getContext()));
            listOfFiles.add(back);

            if ( dir.listFiles() != null && dir.listFiles().length > 0 ) {
                for(File file : dir.listFiles() ) {
                    String filename = file.getName();
                    if ( filename.endsWith(".txt") ) filename = filename.substring(0, filename.lastIndexOf(".txt"));
                    ItemCardCover item = new ItemCardCover();
                    item.setFile(filename, mCurPath);
                    listOfFiles.add(item);
                }
            }

            if ( mAdapter != null ) mAdapter.setListOfFiles(false, listOfFiles);
        }
    }

    //---------------------------------------------
    //  Item 추가
    //==============================================
    public void onClickAddButton() {
        if ( mAdapter != null && !mAdapter.getIsCover() ) {
            CTCardInfoDialog dialog = new CTCardInfoDialog(getContext());
            dialog.setData(mCurPath, "", onButtonClickListener);
            dialog.show();
        }
    }

    public void addItem(String title) {
        if ( mAdapter != null && mAdapter.getIsCover() == false ) {
            if (listOfFiles == null) listOfFiles = new ArrayList<>();
            if (title != null && title.endsWith(".txt"))
                title = title.substring(0, title.lastIndexOf(".txt"));
            ItemCardCover item = new ItemCardCover();
            item.setFile(title, mCurPath);
            listOfFiles.add(item);
            if (mAdapter != null) mAdapter.setListOfFiles(false, listOfFiles);
        }
    }

    /*==================================================
        Item 삭제
    ================================================== */
    boolean isDeleteMode = false;
    ArrayList<ItemCardCover> listOfDeletes;
    public void onClickDeleteButton() {
        if ( mAdapter == null ||  mAdapter.getIsCover() ) return;

        if ( isDeleteMode && listOfDeletes != null && listOfDeletes.size() > 0 ) {
            deleteItems();
        } else {
            isDeleteMode = !isDeleteMode;
        }
        mAdapter.setDeleteMode(isDeleteMode);
    }

    private void deleteItems() {
        final CTDialog dialog = new CTDialog(getContext());
        dialog.setMessage("삭제", "정말 삭제하시겠습니까?", "아니요", "네", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( dialog.isShowing()) dialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( listOfDeletes != null ) {
                    for(int i=0; i<listOfDeletes.size(); i++) {

                        ItemCardCover item = listOfDeletes.get(i);
                        String path = mCurPath + File.separator + item.title + ".txt";
                        File file = new File(path);
                        if ( file.exists() )
                            file.delete();
                        listOfFiles.remove(item);
                    }
                    listOfDeletes.clear();
                    mAdapter.setListOfFiles(false, listOfFiles);
                }
                if ( dialog.isShowing() ) dialog.dismiss();
            }
        });
        dialog.show();
    }

    private CTCardInfoDialog.OnButtonClickListener onButtonClickListener = new CTCardInfoDialog.OnButtonClickListener() {
        @Override
        public void OnDeleteButtonClick(ItemCardCover item, CTCardInfoDialog dialog) {
            if ( dialog != null && dialog.isShowing() ) dialog.dismiss();

            if ( listOfDeletes == null ) listOfDeletes = new ArrayList<>();
            listOfDeletes.clear();

            listOfDeletes.add(item);

            deleteItems();
        }

        @Override
        public void OnRenameButtonClick(String current, String newTitle, CTCardInfoDialog dialog) {
            if ( dialog != null && dialog.isShowing() ) dialog.dismiss();

            if ( !newTitle.contains(".txt") ) newTitle += ".txt";
            File file = new File(mCurPath + File.separator + current);
            File rename = new File(mCurPath + File.separator + newTitle);

            if ( file.exists() ) file.renameTo(rename);

            for (int i=0; i<listOfFiles.size(); i++) {
                if ( listOfFiles.get(i).title.equalsIgnoreCase(current) ) {
                    ItemCardCover item = listOfFiles.get(i);
                    item.title = newTitle;
                    listOfFiles.set(i, item);
                }
            }

            mAdapter.setListOfFiles(false, listOfFiles);
        }

        @Override
        public void OnAddButtonClick(String title, CTCardInfoDialog dialog) {
            if ( dialog != null && dialog.isShowing() ) dialog.dismiss();

            if ( title != null && title.length() > 0 ) {
                CTActivityUtil activityUtil = new CTActivityUtil();
                activityUtil.startMakeCardActivity(getActivity(), mCurPath, title);
            }
        }
    };
}
