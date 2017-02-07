package garmter.com.camtalk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import garmter.com.camtalk.R;

/**
 * Created by Youjung on 2016-12-15.
 */
public class CTDialog extends Dialog {

    private String mTitle, mContent, mBtnLeftText, mBtnRightText;
    private View.OnClickListener onLeftClickListener;
    private View.OnClickListener onRightClickListener;

    private TextView tvTitle;
    private TextView tvContent;
    private Button btnLeft;
    private Button btnRight;

    public CTDialog(Context context) {
        // Dialog 배경을 투명 처리 해준다.
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
    }

    public void setMessage(String title, String content, String btnLeftText, String btnRightText, View.OnClickListener onLeftClickListener, View.OnClickListener onRightClickListener) {
        mTitle = title;
        mContent = content;
        mBtnLeftText = btnLeftText;
        mBtnRightText = btnRightText;
        this.onLeftClickListener = onLeftClickListener;
        this.onRightClickListener = onRightClickListener;
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

        setContentView(R.layout.dialog_ct);

        initView();
        initData();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvContent = (TextView) findViewById(R.id.tvContent);
        btnLeft = (Button) findViewById(R.id.btnLeft);
        btnRight = (Button) findViewById(R.id.btnRight);
        if ( onLeftClickListener != null ) btnLeft.setOnClickListener(onLeftClickListener);
        if ( onRightClickListener != null ) btnRight.setOnClickListener(onRightClickListener);
    }

    private void initData() {
        if ( mTitle == null || mTitle.length() == 0 ) tvTitle.setText("확인");
        else tvTitle.setText(mTitle);

        if ( mContent == null ) mContent = "";
        tvContent.setText(mContent);

        if ( mBtnLeftText == null || mBtnLeftText.length() == 0 ) {
            btnLeft.setVisibility(View.GONE);
        } else {
            if ( onLeftClickListener != null )btnLeft.setOnClickListener(onLeftClickListener);
            btnLeft.setVisibility(View.VISIBLE);
            btnLeft.setText(mBtnLeftText);
        }

        if ( mBtnRightText == null || mBtnRightText.length() == 0 ) {
            btnRight.setVisibility(View.GONE);
        } else {
            if ( onRightClickListener == null ) btnRight.setOnClickListener(onRightClickListener);
            btnRight.setVisibility(View.VISIBLE);
            btnRight.setText(mBtnRightText);
        }
    }
}
