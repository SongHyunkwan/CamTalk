package garmter.com.camtalk.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Hwang MinSik on 2016-12-03.
 */
public class NetworkTask extends AsyncTask<String, String, String[]> {

    String TAG = "NetworkTask";
    HashMap<String, Object> mapOfHeaders;
    OnNetworkCallback onNetworkCallback;

    public void setHeader(HashMap<String, Object> map) {
        this.mapOfHeaders = map;
    }

    public void setCallback(OnNetworkCallback callback) {
        Log.d("성공6","성공6");
        onNetworkCallback = callback;
    }

    @Override
    protected String[] doInBackground(String... url) {
        // TODO Auto-generated method stub

        String[] result= request(url[0]);
        Log.d("성공5","성공5");
        return result;
    }

    protected void onPostExecute(String[] result) {
        Log.d(TAG, result[0] + ", " + result[1]);
        Log.d("성공4","성공4");
        if ( result[0] != null && result[0].equalsIgnoreCase("Y") ){
            onNetworkCallback.onSuccess(result[1]);
            Log.d("성공3","성공3");
        }
        else {
            onNetworkCallback.onFail(result[1]);
            Log.d(TAG, "fail");
        }
    }


    private String[] request(String urlStr) {
        Log.d(TAG, "===================================================================");
        Log.d(TAG, "URL\t" + urlStr);
        StringBuilder output = new StringBuilder();
        boolean success = false;
        String result = "";
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
                conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                if ( mapOfHeaders != null ) {
                    Iterator<String> keys = mapOfHeaders.keySet().iterator();
                    StringBuffer buffer = new StringBuffer();
                    while ( keys.hasNext() ) {
                        String key = keys.next();
                        if (buffer != null && buffer.length() > 0 ) buffer.append("&");
                        buffer.append(key).append("=").append(String.valueOf(mapOfHeaders.get(key)));                 // php 변수에 값 대입
                    }
                    OutputStream os = conn.getOutputStream();
                    os.write(buffer.toString().getBytes("UTF-8"));
                    os.flush();
                    os.close();
                }

                int resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())) ;
                    String line = null;
                    while(true) {
                        line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        output.append(line + "\n");
                    }

                    reader.close();
                    conn.disconnect();
                    success = true;
                    result = output.toString();
                }
            }
            Log.d("성공","성공");
        } catch(Exception ex) {
            Log.d("실패","실패");
            Log.e("SampleHTTP", "Exception in processing response.", ex);
            ex.printStackTrace();
            result = ex.getMessage();
        }
        Log.d(TAG, "===================================================================");

        String[] str = { success ? "Y" : "N", result };
        Log.d("성공2","성공2");
        return str;
    }
}
