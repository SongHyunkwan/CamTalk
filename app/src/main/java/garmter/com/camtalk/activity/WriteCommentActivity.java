package garmter.com.camtalk.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import garmter.com.camtalk.R;
import garmter.com.camtalk.item.ItemComment;
import garmter.com.camtalk.network.NetworkUtil;
import garmter.com.camtalk.network.OnNetworkCallback;
import garmter.com.camtalk.utils.CTActivityUtil;
import garmter.com.camtalk.utils.CTUtils;

public class WriteCommentActivity extends AppCompatActivity {

    String mLectureId;
    String mLectureName;
    String mLecProf;
    TextView[] tvTags;
    int[] tag_id = {R.id.tvTag0, R.id.tvTag1, R.id.tvTag2, R.id.tvTag3,
            R.id.tvTag4, R.id.tvTag5, R.id.tvTag6, R.id.tvTag7,
            R.id.tvTag8, R.id.tvTag9, R.id.tvTag10, R.id.tvTag11,
            R.id.tvTag12, R.id.tvTag13, R.id.tvTag14, R.id.tvTag15};
    boolean[] tagSelect = {false, false, false, false,
            false, false, false, false,
            false, false, false, false,
            false, false, false, false};
    RatingBar rbRate;
    RatingBar rbEasy;
    EditText etComment;
    TextView btnWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);

        mLectureId = getIntent().getStringExtra(CTActivityUtil.KEY_LECTURE_ID);
        mLectureName = getIntent().getStringExtra(CTActivityUtil.KEY_LECTURE_NAME);
        mLecProf=getIntent().getStringExtra(CTActivityUtil.KEY_LEC_PROF);
        initView();
    }

    private void initView() {
        tvTags = new TextView[16];
        for (int i = 0; i < tvTags.length; i++) {
            tvTags[i] = (TextView) findViewById(tag_id[i]);
            tvTags[i].setOnClickListener(onTagClickListener);
        }

        rbRate = (RatingBar) findViewById(R.id.rbAverage);
        rbEasy = (RatingBar) findViewById(R.id.rbDegree);
        etComment = (EditText) findViewById(R.id.etComment);
        btnWrite = (TextView) findViewById(R.id.btnWrite);
        btnWrite.setOnClickListener(onClickListener);
    }

    int mTagNum = 0;
    private View.OnClickListener onTagClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvTag0:
                    onCallback(0);
                    break;
                case R.id.tvTag1:
                    onCallback(1);
                    break;
                case R.id.tvTag2:
                    onCallback(2);
                    break;
                case R.id.tvTag3:
                    onCallback(3);
                    break;
                case R.id.tvTag4:
                    onCallback(4);
                    break;
                case R.id.tvTag5:
                    onCallback(5);
                    break;
                case R.id.tvTag6:
                    onCallback(6);
                    break;
                case R.id.tvTag7:
                    onCallback(7);
                    break;
                case R.id.tvTag8:
                    onCallback(8);
                    break;
                case R.id.tvTag9:
                    onCallback(9);
                    break;
                case R.id.tvTag10:
                    onCallback(10);
                    break;
                case R.id.tvTag11:
                    onCallback(11);
                    break;
                case R.id.tvTag12:
                    onCallback(12);
                    break;
                case R.id.tvTag13:
                    onCallback(13);
                    break;
                case R.id.tvTag14:
                    onCallback(14);
                    break;
                case R.id.tvTag15:
                    onCallback(15);
                    break;
                default:
                    break;
            }
        }
    };

    private void onCallback(int index) {
        if (mTagNum == 3 && !tagSelect[index]) {
            Toast.makeText(WriteCommentActivity.this, "태그는 최대 3개까지 선택할 수 있습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        tagSelect[index] = !tagSelect[index];

        if (tagSelect[index]) {
            mTagNum++;
            tvTags[index].setBackgroundResource(R.drawable.solid_box_dark_grey);
        } else {
            mTagNum--;
            tvTags[index].setBackgroundResource(R.drawable.solid_box_light_grey);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnWrite:
                    if (etComment.getText().toString().length() == 0) {
                        Toast.makeText(WriteCommentActivity.this, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (mTagNum == 0) {
                        Toast.makeText(WriteCommentActivity.this, "태그를 1개 이상 선택해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    NetworkUtil networkUtil = new NetworkUtil();
                    networkUtil.requestWriteComment(WriteCommentActivity.this, mLectureId, mLectureName, mLecProf, getTagInfo(), rbRate.getRating(), rbEasy.getRating(), etComment.getText().toString(), onNetworkCallback);
                    Log.v("Write","Write mLectureId = " + mLectureId + " mLectureName = " + mLectureName + " mLecProf = " +mLecProf);
                    break;
                default:
                    break;
            }
        }
    };

    int[] getTagInfo() {
        int[] tagInfo = { 16, 16, 16 };
        int j = 0;
        for (int i = 0; i < tagSelect.length; i++) {
            if (tagSelect[i]) {
                tagInfo[j] = i ;
                j++;
            }
        }
        return tagInfo;
    }

    private OnNetworkCallback onNetworkCallback = new OnNetworkCallback() {
        @Override
        public void onSuccess(String msg) {
            if ( msg != null ) {
                int[] tags = getTagInfo();
                ItemComment comment = new ItemComment("", etComment.getText().toString(), tags[0], tags[1], tags[2], CTUtils.getYYYY_MM_dd(), false, 0);
                Intent intent = new Intent();
                intent.putExtra(CTActivityUtil.KEY_COMMENT, comment);
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        @Override
        public void onFail(String msg) {
            Toast.makeText(WriteCommentActivity.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    };
}
