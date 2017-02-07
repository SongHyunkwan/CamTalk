package garmter.com.camtalk.activity;

import android.graphics.Typeface;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import garmter.com.camtalk.R;
import garmter.com.camtalk.dialog.CTDialog;
import garmter.com.camtalk.item.ItemCard;
import garmter.com.camtalk.utils.CTActivityUtil;

public class CardViewerActivity extends AppCompatActivity {

    TextView tvTitle;
    TextView tvCard;
    LinearLayout layoutButton;
    TextView btnOk;
    TextView btnNotOk;


    String mCurDir;
    String mTitle;
    int mCurIndex;
    ArrayList<ItemCard> listOfCards;

    boolean isFront = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_viewer);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvCard = (TextView) findViewById(R.id.tvCard);
        layoutButton = (LinearLayout) findViewById(R.id.layoutButton);
        btnOk = (TextView) findViewById(R.id.tvOk);
        btnNotOk = (TextView) findViewById(R.id.tvNotOk);



        mCurDir = getIntent().getStringExtra(CTActivityUtil.KEY_DIRECTORY);
        mTitle = getIntent().getStringExtra(CTActivityUtil.KEY_TITLE);
        if ( mTitle != null ) tvTitle.setText(mTitle);

        listOfCards = new ArrayList<>();

        if ( !mTitle.contains(".txt") ) mTitle += ".txt";
        File file = new File(mCurDir + File.separator + mTitle);
        int readcount=0;
        if(file!=null&&file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                Reader in = new InputStreamReader(fis, "UTF-8");
                readcount = (int) fis.available();
                char[] buffer = new char[readcount];
                in.read(buffer);
                in.close();

                String content = String.valueOf(buffer);
                Log.d("YJ", content);
                JSONArray array = new JSONArray(content);
                for(int i=0; i<array.length(); i++) {
                    JSONObject obj = new JSONObject(String.valueOf(array.get(i)));
                    if ( obj != null ) {
                        ItemCard item = new ItemCard();
                        item.front = obj.get("front").toString();
                        item.back = obj.get("back").toString();

                        item.title = mTitle;
                        item.path = mCurDir;
                        listOfCards.add(item);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mCurIndex = 0;
        if ( listOfCards != null && listOfCards.size() > 0)
        tvCard.setText(listOfCards.get(mCurIndex).front);

        tvCard.setOnClickListener(onClickListener);
        btnOk.setOnClickListener(onClickListener);
        btnNotOk.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvCard:
                    if ( isFront ) {
                        if ( listOfCards != null && listOfCards.size() > mCurIndex ) {
                            tvCard.setText(listOfCards.get(mCurIndex).back);
                            isFront = !isFront;
                            layoutButton.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case R.id.tvOk:
                    flipCard(btnOk);
                    break;
                case R.id.tvNotOk:
                    flipCard(btnNotOk);
                    break;
            }
        }
    };

    private void flipCard(TextView tvBtn) {
        if ( tvBtn != null && tvBtn.getVisibility() == View.VISIBLE ) {
            mCurIndex++;
            if (listOfCards != null && listOfCards.size() > mCurIndex) {
                tvCard.setText(listOfCards.get(mCurIndex).front);
                isFront = !isFront;
                layoutButton.setVisibility(View.INVISIBLE);
            } else if ( listOfCards != null && listOfCards.size() == mCurIndex ) {
                final CTDialog dialog = new CTDialog(CardViewerActivity.this);
                dialog.setMessage("확인", "마지막 장 입니다.\n맨 처음 카드로 갈까요?", "맨 앞으로", "종료하기",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(CardViewerActivity.this, "맨 처음 카드로 갑니다.", Toast.LENGTH_SHORT).show();
                                mCurIndex = 0;
                                tvCard.setText(listOfCards.get(mCurIndex).front);
                                isFront = !isFront;
                                layoutButton.setVisibility(View.INVISIBLE);
                                if ( dialog != null && dialog.isShowing() ) dialog.dismiss();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if ( dialog != null && dialog.isShowing() ) dialog.dismiss();
                                finish();
                            }
                        });
                dialog.show();
            }
        }
    }
}
