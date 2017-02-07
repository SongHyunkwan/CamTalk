package garmter.com.camtalk.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import garmter.com.camtalk.R;
import garmter.com.camtalk.db.LectureDB;
import garmter.com.camtalk.dialog.CTDialog;
import garmter.com.camtalk.item.DKClass;
import garmter.com.camtalk.utils.CTActivityUtil;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        if (checkPermission()) {
            startActivity();
        } else {
            requestPermission();
        }
    }

    /**
     * @return 시간표 정보가 있는지 유무
     */
    private boolean hasTimetableDB() {

        LectureDB db = new LectureDB(IntroActivity.this);
        db.open();
        List<DKClass> list = db.DBselect();
        db.close();
        return (!list.isEmpty() && list.size() > 0);
    }

    private void startActivity() {
        final boolean hasDB = hasTimetableDB();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CTActivityUtil activityUtil = new CTActivityUtil();
                if (hasDB)
                    activityUtil.startMainActivity(IntroActivity.this);
                else
                    activityUtil.startLoginActivity(IntroActivity.this);

                finish();
            }
        }, 2000);
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        // 이 권한을 필요한 이유를 설명해야하는가?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            final CTDialog dialog = new CTDialog(IntroActivity.this);
            dialog.setMessage("안내", "시간표와 플래시 카드 읽기 및 쓰기 권한이 필요합니다.", "취소", "승인", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null && dialog.isShowing()) dialog.dismiss();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(IntroActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            CTActivityUtil.PERMISSIONS_REQUEST_STORAGE);

                    // 필요한 권한과 요청 코드를 넣어서 권한허가요청에 대한 결과를 받아야 합니다
                    if (dialog != null && dialog.isShowing()) dialog.dismiss();
                }
            });
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CTActivityUtil.PERMISSIONS_REQUEST_STORAGE);

            // 필요한 권한과 요청 코드를 넣어서 권한허가요청에 대한 결과를 받아야 합니다
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == CTActivityUtil.PERMISSIONS_REQUEST_STORAGE &&
                grantResults.length >= 2
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            // 권한 허가

        } else {
            // 권한 거부
            finish();
        }
        return;
    }
}
