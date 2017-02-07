package garmter.com.camtalk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import garmter.com.camtalk.R;
import garmter.com.camtalk.db.LectureDB;
import garmter.com.camtalk.item.DKClass;
import garmter.com.camtalk.item.ItemCardCover;

/**
 * TODO: document your custom view class.
 */
public class CTCardInfoDialog extends Dialog {
    private TextView tvTitle;
    private EditText etTitle;
    private Button btnDelete;
    private Button btnName;

    private OnButtonClickListener mListener;

    private ItemCardCover mItem;

    public CTCardInfoDialog(Context context) {
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

        setContentView(R.layout.dialog_card_info);

        initView();
        initData();
    }

    private void initView(){
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        etTitle = (EditText) findViewById(R.id.etTitle);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnName = (Button) findViewById(R.id.btnName);
        btnDelete.setOnClickListener(onClickListener);
        btnName.setOnClickListener(onClickListener);
    }

    public void setData(String path, String title, OnButtonClickListener listener) {
        mItem = new ItemCardCover();
        mItem.setFile(title, path);
        mListener = listener;
    }

    public void initData() {
        if ( mItem != null && mItem.title != null && mItem.title.length() > 0 ) {
            etTitle.setText(mItem.title);
              btnDelete.setVisibility(View.VISIBLE);
            btnName.setText("이름 변경");
        } else {
            btnDelete.setVisibility(View.GONE);
            btnName.setText("확인");
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnDelete:
                    if ( mListener != null ) mListener.OnDeleteButtonClick(mItem, CTCardInfoDialog.this);
                    break;
                case R.id.btnName:
                    String newTitle = etTitle.getText().toString();
                    if ( newTitle == null || newTitle.length() == 0 ) {
                        Toast.makeText(getContext(), "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        break;
                    } else if ( "BACK".equalsIgnoreCase(newTitle) ) {
                        Toast.makeText(getContext(), "제목으로 설정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    if ( mListener == null ) break;
                    if ( mItem == null || mItem.title == null || mItem.title.length() == 0 ) {
                        mListener.OnAddButtonClick(newTitle, CTCardInfoDialog.this);
                    } else if ( mItem != null && mItem.title != null && !mItem.title.equalsIgnoreCase(newTitle) ) {
                        mListener.OnRenameButtonClick( mItem.title, newTitle, CTCardInfoDialog.this);
                    } else {
                        dismiss();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public interface OnButtonClickListener {
        public void OnDeleteButtonClick(ItemCardCover item, CTCardInfoDialog dialog);
        public void OnRenameButtonClick(String current, String newTitle, CTCardInfoDialog dialog);
        public void OnAddButtonClick(String title, CTCardInfoDialog dialog);
    }

}
