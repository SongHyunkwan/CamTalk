package garmter.com.camtalk.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import garmter.com.camtalk.db.LectureDB;
import garmter.com.camtalk.item.DKClass;
import garmter.com.camtalk.utils.CTContants;
import garmter.com.camtalk.utils.CTUtils;

public class LoginUtil {

    public void requestLogin(Context context, String id, String password, OnNetworkCallback callback) {
        new LoginTask(context, callback).execute(id, password);
    }

    public void setUserId(Context context, String id) {
        SharedPreferences prefs = context.getSharedPreferences("PrefName", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("USER_ID", id);
        editor.commit();
    }

    public String getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PrefName", context.MODE_PRIVATE);
        String user_id = prefs.getString("USER_ID", "");
        return user_id;
    }

    public boolean isLogined(Context context) {
        String user_id = getUserId(context);
        return user_id != null && user_id.length() > 0;
    }

    public class LoginTask extends AsyncTask<String, Void, String> {

        private Context mContext;
        private OnNetworkCallback mCallback;
        private ProgressDialog mDialog;

        public LoginTask(Context context, OnNetworkCallback onNetworkCallback) {
            mContext = context;
            mCallback = onNetworkCallback;
            mDialog = new ProgressDialog(mContext);
        }

        @Override
        protected void onPreExecute() {

            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();

            boolean isConnect = false;
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                isConnect = true;
            }

            if (isConnect) {
                mDialog.setTitle("잠시만 기다려주세요.");
                mDialog.setMessage("시간표를 가져오는 중입니다.");
                mDialog.setCancelable(false);
                mDialog.show();
            } else {
                cancel(true);
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String schoolNumber = params[0];
            String password = params[1];

            LectureDB db = null;

            try {
                // 웹에서 시간표를 긁어온다.
                List<DKClass> classes = getLectureLists(mContext, schoolNumber, password);
                // 내부 DB에 저장
                db = new LectureDB(mContext);
                db.open();
                db.DBclear();
                db.DBinsert(classes);
            } catch (Exception e) {
                new CTUtils().saveError(e);
                return e.getMessage();
            } finally {
                if ( db != null ) db.close();
            }

            return null; // null이 정상
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                    .setTitle("인터넷 연결 실패")
                    .setMessage("Wifi 혹은 3G망이 연결되지 않았거나 원활하지 않습니다.네트워크 확인후 다시 접속해 주세요!")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.create().show();
        }

        @Override
        protected void onPostExecute(String result) {
            if ( mDialog.isShowing() ) mDialog.dismiss();
            if ( result == null || (result.contains("ndex") && result.contains("ize"))) {
                mCallback.onSuccess("");
            }
            else {
                mCallback.onFail(result);
                Log.d("에러2",result);
            }
        }
    }

    public static List<DKClass> getLectureLists(Context context, String user_id, String password ) throws Exception {

        trustAllHosts();

        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

        // ============ Step1 ==================
        HttpPost httpPost = new HttpPost("https://webinfo.dankook.ac.kr/member/logon.do");
        // 헤더 설정
        httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpPost.setHeader("Accept-Encoding", "deflate");
        httpPost.setHeader("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
        httpPost.setHeader("Cache-Control", "max-age=0");
        httpPost.setHeader("Connection", "keep-alive");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        // 바디 파라미터 설정
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair("username", user_id));
        qparams.add(new BasicNameValuePair("password", password));
        qparams.add(new BasicNameValuePair("dn", ""));
        qparams.add(new BasicNameValuePair("tabIndex", "0"));
        qparams.add(new BasicNameValuePair("sso", "ok"));
        qparams.add(new BasicNameValuePair("returnurl", "http://webinfo.dankook.ac.kr:80/tiac/univ/lssn/ttmg/views/findTkcrsTmtblList.do"));
        // UTF-8 인코딩
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(qparams, "UTF-8");
        httpPost.setEntity(entity);

        // ============ Step2 ==================
        // 파싱
        HttpResponse response = httpclient.execute(httpPost);
        InputStream in = response.getEntity().getContent();
        try {
            String html = writeHtml(in);    // html 페이지 읽어와서 저장
            return parseLectureListFromFilePath(context, html); // 파싱
        } finally {
            in.close();
        }

    }

    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain, String authType)
                    throws java.security.cert.CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain, String authType)
                    throws java.security.cert.CertificateException {
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * inputstream을 통해 들어오는 Html 페이지를 저장
     * @param in
     * @return
     * @throws IOException
     */
    private static String writeHtml(InputStream in) throws IOException {

        String filePath = CTUtils.getExternalPath();
        if (filePath == null) {
            // emulator
            filePath = CTUtils.getLocalDir();
        }
        String htmlPath = filePath + "/tmp/dku_tt.html";

        new File(filePath + "/tmp/").mkdirs();
        new File(htmlPath).delete();

        FileOutputStream out = new FileOutputStream(htmlPath, false);

        byte[] buffer = new byte[4096];

        int readSize;
        while ((readSize = in.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, readSize);
            Log.d( "Parse", "debug. " + new String(buffer, 0, readSize, "UTF-8"));
        }

        out.close();

        return htmlPath;
    }

    /**
     * 응답 메시지의 헤더값 출력
     * @param response http 응답
     */
    private static void debugHeader(HttpResponse response) {
        // debug
        Log.d( "Parse", "statusline. " + response.getStatusLine());
        Header[] h = response.getAllHeaders();
        for (int i = 0; i < h.length; i++) {
            Log.d("Parse", "header. " + h[i].getName() + " : " + h[i].getValue());
        }
    }

    /**
     * 수강 시간표 html 페이지를 파싱한다.
     * @param filePath html 페이지가 저장경로
     * @param context
     * @return 수업 목록
     * @throws Exception
     */
    private static List<DKClass> parseLectureListFromFilePath(Context context, String filePath ) throws Exception {

        List<DKClass> list = new ArrayList<DKClass>();

        try {
            // 파일을 읽어온다.
            FileInputStream in = new FileInputStream(filePath);
            Source source = new Source(new InputStreamReader(in, "UTF-8"));
            in.close();

            // 로그인 에러 확인
            Element error = source.getElementById("errors");
            if (error != null) {
                Element errorP = error.getAllElementsByClass("warn").get(0);
                String errorMsg = errorP.getTextExtractor().toString();
                throw new LoginException(errorMsg);
            }

            // 학년도 학기 찾기
            Segment haggi = new Segment(source, source
                    .getAllElements(HTMLElementName.BR).get(2).getEnd(), source
                    .getAllElements(HTMLElementName.BR).get(3).getBegin());

            String str = haggi.toString();
            Log.d("Parse", "semester. [" + str + "]");
            Pattern ptn = Pattern.compile("([0-9]{4}) 년도  ([0-9]{1}) 학기"); // YYYY년도 1또는 2학기 글자 찾기

            Matcher mt = ptn.matcher(str);
            if (mt.find()) { // 있으면 년도와 학기를 저장
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                editor.putString("year", mt.group(1));
                editor.putString("semester", mt.group(2));
                editor.commit();
            }

            // 시간표 파싱
            Element table = source.getAllElements(HTMLElementName.TABLE).get(1);
            List<Element> trs = table.getAllElements(HTMLElementName.TR);

            for (int i = 1; i < trs.size(); i++) {
                List<Element> tds = trs.get(i).getAllElements(
                        HTMLElementName.TD);

//                if (tds.size() == 10) {
                    String code = tds.get(1).getTextExtractor().toString(); // 교과목번호
                    String _class = tds.get(2).getTextExtractor().toString(); // 분반
                    Element tds3 = tds.get(3);
                    String lecture = tds3.getTextExtractor().toString(); // 교과목명
                    StringBuffer extraInfo = new StringBuffer();

                    try {
                        // 강의계획서 연결을 위한 정보
                        // https://webinfo.dankook.ac.kr/tiac/univ/lssn/lpdm/views/popup/findLecplnDtlForm.do?
                        // opOrgid=2000000989&yy=2015&semCd=2&subjId=404410&dvclsNb=1&mlangCd=0001
                        String opOrgid = tds3.getFirstElement("name", "tmtblDscApl[" + (i - 1) + "].opOrgid", false).getAttributeValue("value");
                        String yy = tds3.getFirstElement("name", "tmtblDscApl[" + (i - 1) + "].yy", false).getAttributeValue("value");
                        String semCd = tds3.getFirstElement("name", "tmtblDscApl[" + (i - 1) + "].semCd", false).getAttributeValue("value");
                        String subjId = tds3.getFirstElement("name", "tmtblDscApl[" + (i - 1) + "].subjId", false).getAttributeValue("value");
                        String dvclsNb = tds3.getFirstElement("name", "tmtblDscApl[" + (i - 1) + "].dvclsNb", false).getAttributeValue("value");
                        String mlangCd = "0001"; // kr:0001, en:0002
                        String abeekCd = tds3.getFirstElement("name", "tmtblDscApl[" + (i - 1) + "].abeekCd", false).getAttributeValue("value");
                        String dsgnYn = tds3.getFirstElement("name", "tmtblDscApl[" + (i - 1) + "].dsgnYn", false).getAttributeValue("value");
                        String deptLoctCd = tds3.getFirstElement("name", "tmtblDscApl[" + (i - 1) + "].deptLoctCd", false).getAttributeValue("value");
                        String wkTkcrsOrgid = tds3.getFirstElement("name", "tmtblDscApl[" + (i - 1) + "].wkTkcrsOrgid", false).getAttributeValue("value");

                        extraInfo.append("&opOrgid=").append(opOrgid);
                        extraInfo.append("&yy=").append(yy);
                        extraInfo.append("&semCd=").append(semCd);
                        extraInfo.append("&subjId=").append(subjId);
                        extraInfo.append("&dvclsNb=").append(dvclsNb);
                        extraInfo.append("&mlangCd=").append(mlangCd);
                        extraInfo.append("&abeekCd=").append(abeekCd);
                        extraInfo.append("&dsgnYn=").append(dsgnYn);
                        extraInfo.append("&deptLoctCd=").append(deptLoctCd);
                        extraInfo.append("&wkTkcrsOrgid=").append(wkTkcrsOrgid);

                    } catch (NullPointerException e) {
                        Log.e("Parse", "extraInfo parse error.", e);
                    }

                    // .replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>",
                    // ""); // 과목명

                    // Element unit = tds.get(4); // 학점
                    String timeRoom = tds.get(5).getTextExtractor().toString(); // 시간,강의실
                    // 화8,9(자연305)/목3(자연516)
                    String professor = tds.get(6).getTextExtractor().toString(); // 교강사

                    DKClass dk = new DKClass(code, _class, lecture, timeRoom, professor, "", extraInfo.toString());
                    list.add(dk);

                    Log.d("Parse", "lecture. [" + dk.toString() + "]");
//                }
            }

        } catch (NullPointerException e) {
            // 서버 상태 이상? 재시도 필요.
            throw new Exception("Parsing Error.", e);
        } catch (LoginException e) {
            // 서버 상태 이상? 재시도 필요.
            throw new Exception("서버 연결 실패(2). " + e.getMessage() , e);

        } catch (IOException e) {
            // 연결이 불안정
            throw new Exception("서버 연결 실패(3)", e);
        }

        return list;
    }

}