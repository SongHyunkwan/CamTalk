package garmter.com.camtalk.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import garmter.com.camtalk.R;
import garmter.com.camtalk.db.LectureDB;
import garmter.com.camtalk.network.LoginUtil;
import garmter.com.camtalk.utils.CTActivityUtil;
import garmter.com.camtalk.utils.CTContants;

public class MyActivity extends AppCompatActivity {

    private ImageView ivSchoolLogo;
    private Button btnNotice;
    private Button btnFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        initView();
    }

    private void initView() {
        ivSchoolLogo = (ImageView)findViewById(R.id.ivSchoolLogo);
        btnNotice = (Button) findViewById(R.id.btnNotice);
        btnFood = (Button) findViewById(R.id.btnFood);

        ivSchoolLogo.setOnClickListener(onClickListener);
        btnNotice.setOnClickListener(onClickListener);
        btnFood.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnNotice:
                    startWebviewNotice();
                    break;
                case R.id.btnFood:
                    startWebviewFood();
                    break;
                default:
                    break;
            }
        }
    };

    private void logOut() {
        LectureDB db = new LectureDB(MyActivity.this);
        db.open();
        db.DBclear();
        db.close();

        LoginUtil loginUtil = new LoginUtil();
        loginUtil.setUserId(MyActivity.this, "");
        CTActivityUtil cu=new CTActivityUtil();
        cu.startLoginActivity(this);
        Toast.makeText(MyActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void startWebviewNotice() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri u = Uri.parse(CTContants.URL_NOTICE);
        intent.setData(u);
        startActivity(intent);
    }

    private void startWebviewFood() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri u = Uri.parse(CTContants.URL_FOOD_J);
        intent.setData(u);
        startActivity(intent);
    }

}
