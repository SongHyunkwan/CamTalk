package garmter.com.camtalk.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.DrawableMarginSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import garmter.com.camtalk.R;
import garmter.com.camtalk.adapter.CommentRvAdapter;
import garmter.com.camtalk.adapter.DataRvAdapter;
import garmter.com.camtalk.db.LectureDB;
import garmter.com.camtalk.dialog.CTDialog;
import garmter.com.camtalk.item.DKClass;
import garmter.com.camtalk.item.ItemComment;
import garmter.com.camtalk.item.ItemLecture;
import garmter.com.camtalk.item.ItemData;
import garmter.com.camtalk.network.JsonUtils;
import garmter.com.camtalk.network.NetworkUtil;
import garmter.com.camtalk.network.OnNetworkCallback;
import garmter.com.camtalk.utils.CTActivityUtil;
import garmter.com.camtalk.utils.TagSpan;
import garmter.com.camtalk.viewholder.AllHashTagView;
import garmter.com.camtalk.viewholder.CommentListItemViewHolder;
import garmter.com.camtalk.viewholder.DataListItemViewHolder;

public class LectureDetailActivity extends AppCompatActivity {

    TextView tvTitle;
    TextView tvProfessor;
    RatingBar rbAverage;
    RatingBar rbDegree;
    TextView tvTags;
    String[] tags = {"#시험어려움", "#절대평가", "#많이읽어야함", "#학점헬",
            "#개꿀잼", "#참여도중요", "#출첵중요", "#유인물사용",
            "#가산점", "#강의내용좋음", "#교재사용", "#팀플많음",
            "#사람참좋다", "#과제크리", "#강의노잼", "#쪽지시험"};

    ToggleButton tbSwitch;
    ImageView ivWrite;

    RecyclerView rvComment;
    RecyclerView rvData;
    CommentRvAdapter adapterComment;
    DataRvAdapter adapterData;

    ArrayList<ItemComment> listOfComments;
    ArrayList<ItemData> listOfData;

    String mProfName;
    String mLectureName;
    String mLectureId;
    int mPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lecture_detail);

        initView();
        initClass();
        mPage = 1;
        initComment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
        initClass();
        mPage=1;
        initComment();

    }

    @Override
    public void recreate() {
        super.recreate();
        initView();
        initClass();
        mPage=1;
        initComment();
    }

    private void initView() {

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvProfessor = (TextView) findViewById(R.id.tvProfessor);
        rbAverage = (RatingBar) findViewById(R.id.rbAverage);
        rbDegree = (RatingBar) findViewById(R.id.rbDegree);
        tvTags = (TextView) findViewById(R.id.tvTags);
        tbSwitch = (ToggleButton) findViewById(R.id.tbSwitch);
        ivWrite = (ImageView) findViewById(R.id.ivWrite);
        rvComment = (RecyclerView) findViewById(R.id.rvComment);
        rvData = (RecyclerView) findViewById(R.id.rvData);


        adapterComment = new CommentRvAdapter(LectureDetailActivity.this, mCommentClickListener);
        adapterData = new DataRvAdapter(LectureDetailActivity.this, mDataClickListener);
        rvComment.setAdapter(adapterComment);
        rvComment.setLayoutManager(new LinearLayoutManager(LectureDetailActivity.this));
        rvComment.addOnScrollListener(onCommentScrollListener);
        rvData.setAdapter(adapterData);
        rvData.setLayoutManager(new LinearLayoutManager(LectureDetailActivity.this));
        rvData.addOnScrollListener(onDataCommentListener);
        tbSwitch.setOnClickListener(onClickListener);
        tbSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){
                    alertDial();
                }
            }
        });
        ivWrite.setOnClickListener(onClickListener);
    }

    private void initClass() {
        mLectureId = getIntent().getStringExtra(CTActivityUtil.KEY_LECTURE_ID);
        if (mLectureId != null && mLectureId.length() > 0) {

            LectureDB db = new LectureDB(LectureDetailActivity.this);
            db.open();
            DKClass dkClass = db.getDkClass(mLectureId);
            mLectureName = getIntent().getStringExtra(CTActivityUtil.KEY_LECTURE_NAME);
            if (dkClass != null && dkClass.getLecture() != null) {
                tvTitle.setText(dkClass.getLecture());
                //mLectureName=tvTitle.getText().toString();
            }
            mLectureName=tvTitle.getText().toString();

            mProfName = getIntent().getStringExtra(CTActivityUtil.KEY_LEC_PROF);
            if (dkClass != null && dkClass.getProfessor() != null) {
                tvProfessor.setText(dkClass.getProfessor());
                //mProfName = tvProfessor.getText().toString();
            }
            db.close();
        }

        String tag = "";
        for (int i = 0; i < tags.length; i++) {
            tag += (" " + tags[i] + " " + 0);
        }

        SpannableString span = new SpannableString(tag);
        int start = 1;
        int end = 0;
        for (int i = 0; i < tags.length; i++) {
            end = start + tags[i].length();
            span.setSpan(new BackgroundColorSpan(R.color.ct_light_grey), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            start = end + 3;
        }

        tvTags.setText(span);
    }

    private void initComment() {
        if (loading) return;
        if (mLectureId != null && mLectureId.length() > 0) {
            NetworkUtil networkUtil = new NetworkUtil();

            System.out.println("확인확인확인 : "+mLectureId+" : "+mProfName+" : "+mPage);
            networkUtil.requestLectureDetail(LectureDetailActivity.this, mLectureId, mProfName, mPage, onNetworkCallback);

        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tbSwitch:
                    if (rvData != null){
                        rvData.setVisibility(tbSwitch.isChecked() ? View.VISIBLE : View.GONE);
                    }
                    if (rvComment != null)
                        rvComment.setVisibility(tbSwitch.isChecked() ? View.GONE : View.VISIBLE);
                    break;
                case R.id.ivWrite:
                    CTActivityUtil activityUtil = new CTActivityUtil();
                    activityUtil.startWriteCommentActivity(LectureDetailActivity.this, mLectureId, tvTitle.getText().toString(), tvProfessor.getText().toString());
                    break;
            }
        }
    };

    private OnNetworkCallback onNetworkCallback = new OnNetworkCallback() {
        @Override
        public void onSuccess(String msg) {

            JSONObject obj = null;
            JsonUtils utils = new JsonUtils();
            try {
                obj = new JSONObject(msg);
                if (mPage == 1) {
                    ItemLecture item = utils.getItemLectureFromJsonObject(obj);
                    rbAverage.setRating(item.lec_average);
                    rbDegree.setRating(item.lec_degree);
                    int loop = 0;
                    String tag = "";
                    String[] tagOld = new String[20];
                    int[] tagCount = new int[20];
                    int count = 0;
                    int jump = 2;
                    for (int i = 0; i < item.tag_count.length; i++) {
                        if (jump > 1 ){
                            jump = 0;
                            tag += '\n';
                        }
                        if (item.tag_count[i] > 0) {
                            tag += ( "     " + tags[i] + " " + item.tag_count[i]);
                            tagOld[loop] = tags[i];
                            loop +=1;
                            tagCount[count] = count;
                            jump += 1;
                        }
                        count += 1;
                    }

                    //SpannableString span = new SpannableString(tag);

                    tvTags.setText(tag);

                    //error tag
                    if(tvProfessor.getText().toString().equals("신원용"))tvProfessor.setText(item.lec_prof);
                    if(tvTitle.getText().toString().equals("강의제목")) tvTitle.setText(item.lecture_name);
                }
                ArrayList<ItemComment> listOfComment = utils.getListOfCommentsFromJsonObject(obj);
                adapterComment.setListOfComments(listOfComment);
                mPage++;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                loading = false;
            }
        }

        @Override
        public void onFail(String msg) {
            loading = false;
            Toast.makeText(LectureDetailActivity.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    };

    private CommentListItemViewHolder.OnItemClickListener mCommentClickListener = new CommentListItemViewHolder.OnItemClickListener() {
        @Override
        public void onItemClicked(final ItemComment item) {
            if (item != null) {
                NetworkUtil networkUtil = new NetworkUtil();
                networkUtil.requestCommentLike(LectureDetailActivity.this, item.comment_id, !item.like_yn, new OnNetworkCallback() {
                    @Override
                    public void onSuccess(String msg) {
                            if ( msg != null && msg.contains("성공")) {
                                if ( item.like_yn ) {
                                    item.like_yn = false;
                                    item.like_count--;
                                } else {
                                    item.like_yn = true;
                                    item.like_count++;
                                }
                                if (adapterComment != null) adapterComment.notifyDataSetChanged();
                            }}

                    @Override
                    public void onFail(String msg) {
                        Toast.makeText(LectureDetailActivity.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };

    private boolean loading = false;
    private RecyclerView.OnScrollListener onCommentScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0) //check for scroll down
            {
                int visibleItemCount = rvComment.getChildCount();
                int totalItemCount = rvComment.getLayoutManager().getItemCount();
                int firstVisibleItemAt = ((LinearLayoutManager) rvComment.getLayoutManager()).findFirstVisibleItemPosition();

                if (!loading && (visibleItemCount + firstVisibleItemAt >= totalItemCount)) {
                    // End has been reached
                    loading = true;
                    initComment();

                }
            }
        }
    };

    private DataListItemViewHolder.OnItemClickListener mDataClickListener = new DataListItemViewHolder.OnItemClickListener() {
        @Override
        public void onItemClicked(ItemData item) {
            if (item != null) {
                final CTDialog dialog = new CTDialog(LectureDetailActivity.this);
                dialog.setMessage("확인", "다운로드 받으시겠습니까?", "아니오", "네", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog != null) dialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO 다운로드

                        Toast.makeText(LectureDetailActivity.this, "다운로드 중입니다", Toast.LENGTH_SHORT).show();
                        if (dialog != null) dialog.dismiss();
                    }
                });
                dialog.show();
            }
        }
    };

    private RecyclerView.OnScrollListener onDataCommentListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0) //check for scroll down
            {
                int visibleItemCount = rvData.getChildCount();
                int totalItemCount = rvData.getLayoutManager().getItemCount();
                int firstVisibleItemAt = ((LinearLayoutManager) rvData.getLayoutManager()).findFirstVisibleItemPosition();

                if (!loading && (visibleItemCount + firstVisibleItemAt >= totalItemCount)) {
                    // End has been reached
                    loading = true;
                    initComment();
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CTActivityUtil.REQUEST_WRITE_COMMENT && resultCode == RESULT_OK) {
            ItemComment comment = data.getParcelableExtra(CTActivityUtil.KEY_COMMENT);
            if (comment != null) {
                if (listOfComments == null) listOfComments = new ArrayList<>();
                listOfComments.add(0, comment);
                adapterComment.setListOfComments(listOfComments);
            }
        }
    }

    private void alertDial() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("알림").setMessage("업데이트 예정입니다.").setNeutralButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create().show();
    }
}
