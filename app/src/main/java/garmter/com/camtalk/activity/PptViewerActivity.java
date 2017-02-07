package garmter.com.camtalk.activity;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.itsrts.pptviewer.PPTViewer;

import java.io.File;

import garmter.com.camtalk.R;

public class PptViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppt_viewer);

        PPTViewer pptViewer = (PPTViewer) findViewById(R.id.pptViewer);
        pptViewer.setNext_img(R.drawable.ic_launcher).setPrev_img(R.drawable.ic_launcher)
                .setSettings_img(R.drawable.ic_launcher)
                .setZoomin_img(R.drawable.ic_launcher)
                .setZoomout_img(R.drawable.ic_launcher);
        String path = Environment.getExternalStorageDirectory().getPath() + File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator + "Effective_presentation.ppt";
        Log.d("YJ", "path = " + path);
        pptViewer.loadPPT(PptViewerActivity.this, path);
    }
}
