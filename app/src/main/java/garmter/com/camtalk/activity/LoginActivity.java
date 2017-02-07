package garmter.com.camtalk.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import garmter.com.camtalk.R;
import garmter.com.camtalk.network.LoginUtil;
import garmter.com.camtalk.network.OnNetworkCallback;
import garmter.com.camtalk.utils.CTActivityUtil;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText etId;
    private EditText etPassword;
    private Button btnLogIn;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        // Set up the login form.
        etId = (EditText) findViewById(R.id.etId);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.etPassword || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnLogIn = (Button) findViewById(R.id.btnLogin);
        btnLogIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private String mId = "";
    private void attemptLogin() {

        // Store values at the time of the login attempt.
        mId = etId.getText().toString();
        String password = etPassword.getText().toString();

        boolean cancel = false;

        View focusView = null;

        // Check for a valid password, if the user entered one.
        if ( TextUtils.isEmpty(password) ) {
            etPassword.setError(getString(R.string.error_invalid_password));
            focusView = etPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if ( TextUtils.isEmpty(mId)) {
            etId.setError(getString(R.string.error_invalid_id));
            focusView = etId;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            LoginUtil loginUtil = new LoginUtil();
            loginUtil.requestLogin(LoginActivity.this, mId, password, onNetworkCallback);
        }
    }

    private OnNetworkCallback onNetworkCallback = new OnNetworkCallback() {
        @Override
        public void onSuccess(String msg) {
            showProgress(false);
            Toast.makeText(LoginActivity.this, "시간표 가져오기 성공", Toast.LENGTH_LONG).show();
            LoginUtil loginUtil = new LoginUtil();
            loginUtil.setUserId(LoginActivity.this, mId);

            new HttpPostData().execute();
            CTActivityUtil activityUtil = new CTActivityUtil();
            activityUtil.startMainActivity(LoginActivity.this);
        }

        @Override
        public void onFail(String msg) {
            showProgress(false);
            // 강의 목록을 가져오지 못하는 경우.
            // * 비밀번호 틀리거나
            // * 웹정보시스템 접속 안되거나
            // * html이 변경되었거나
            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
            Log.d("에러",msg);
        }
    };

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        btnLogIn.setVisibility(show ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public class HttpPostData extends AsyncTask<Void, Void, String> {

        String myResult;
        @Override
        protected String doInBackground(Void... voids) {
            try {
                //--------------------------
                //   URL 설정하고 접속하기
                //--------------------------
                URL url = new URL("http://imjspserver.cafe24.com");       // URL 설정
                HttpURLConnection http = (HttpURLConnection) url.openConnection();   // 접속
                //--------------------------
                //   전송 모드 설정 - 기본적인 설정이다
                //--------------------------
                http.setDefaultUseCaches(false);
                http.setDoInput(true);                 // 서버에서 읽기 모드 지정
                http.setDoOutput(true);                // 서버로 쓰기 모드 지정
                http.setRequestMethod("POST");         // 전송 방식은 POST
                // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
                http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                //--------------------------
                //   서버로 값 전송
                //--------------------------
                StringBuffer buffer = new StringBuffer();
                buffer.append("id").append("=").append(mId);                 // php 변수에 값 대입

                OutputStream os = http.getOutputStream();
                os.write(buffer.toString().getBytes("UTF-8"));
                os.flush();
                os.close();
                //--------------------------
                //   서버에서 전송받기
                //--------------------------
                InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder builder = new StringBuilder();
                String str;
                while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                    builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
                }
                myResult = builder.toString();                       // 전송결과를 전역 변수에 저장
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return myResult;
        }

        @Override
        protected void onPostExecute(String s) {
            showProgress(false);

//            Toast.makeText(LoginActivity.this, myResult, Toast.LENGTH_LONG).show();
            if ( myResult != null ) Log.d("Login", myResult);
            new CTActivityUtil().startMainActivity(LoginActivity.this);
            finish();
        }
    }

}

