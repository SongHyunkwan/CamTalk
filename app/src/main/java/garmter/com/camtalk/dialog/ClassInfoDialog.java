package garmter.com.camtalk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import garmter.com.camtalk.R;
import garmter.com.camtalk.db.LectureDB;
import garmter.com.camtalk.item.DKClass;
import garmter.com.camtalk.utils.CTActivityUtil;

/**
 * Created by Youjung on 2016-12-13.
 */
public class ClassInfoDialog extends Dialog {

    private TextView tvLecture;
    private TextView tvRoom;
    private EditText etMemo;
    private Button btnRate;
    private Button btnStudy;

    private DKClass mClass;
    private OnButtonClickListener mListener;

    public ClassInfoDialog(Context context) {
        // Dialog 배경을 투명 처리 해준다.
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);

        setContentView(R.layout.dialog_class_info);

        initView();
        initData();
    }

    private void initView(){
        tvLecture = (TextView) findViewById(R.id.tvLecture);
        tvRoom = (TextView) findViewById(R.id.tvRoom);
        etMemo = (EditText) findViewById(R.id.etMemo);
        btnRate = (Button) findViewById(R.id.btnRate);
        btnStudy = (Button) findViewById(R.id.btnStudy);
        btnRate.setOnClickListener(onClickListener);
        btnStudy.setOnClickListener(onClickListener);
    }

    String firstMemo;
    public void setClass(DKClass mclass, OnButtonClickListener listener) {
        mClass = mclass;
        mListener = listener;
        firstMemo = mClass == null || mClass.getMemo() == null ? "" : mClass.getMemo();
    }

    public void initData() {
        if ( mClass == null ) return;
        if ( tvLecture != null ) tvLecture.setText(mClass.getLecture());
        if ( tvRoom != null ) tvRoom.setText(mClass.getTimeRoom());
        if ( etMemo != null ) etMemo.setText(mClass.getMemo());
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnRate:
                    dismiss();
                    if ( mListener != null ) mListener.OnRateButtonClick(mClass);
                    break;
                case R.id.btnStudy:
                    dismiss();
                    if ( mListener != null ) mListener.OnStudyButtonClick(mClass);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void dismiss() {
        String memo = etMemo.getText().toString();
        if ( !memo.equalsIgnoreCase(firstMemo) ) {
            LectureDB db = new LectureDB(getContext());
            db.open();
            db.updateMemo(memo, mClass.getCode());
            Toast.makeText(getContext(), "메모가 저장되었습니다.", Toast.LENGTH_SHORT).show();
        }
        super.dismiss();
    }

    public interface OnButtonClickListener {
        public void OnRateButtonClick(DKClass dkClass);
        public void OnStudyButtonClick(DKClass dkClass);
    }
}
