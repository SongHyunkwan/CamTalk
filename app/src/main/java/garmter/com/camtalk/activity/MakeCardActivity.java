package garmter.com.camtalk.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import garmter.com.camtalk.R;
import garmter.com.camtalk.dialog.CTDialog;
import garmter.com.camtalk.item.ItemCard;
import garmter.com.camtalk.utils.CTActivityUtil;
import garmter.com.camtalk.utils.CTContants;

public class MakeCardActivity extends AppCompatActivity {

    TextView tvTitle;
    TextView tvWrite;
    ToggleButton tbSwitch;
    ImageView ivPpt;
    EditText etCard;
    TabLayout tabLayout;

    String mTitle = "";
    String mCurDir = "";
    ArrayList<ItemCard> listOfCards = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_card);

        mTitle = getIntent().getStringExtra(CTActivityUtil.KEY_TITLE);
        mCurDir = getIntent().getStringExtra(CTActivityUtil.KEY_DIRECTORY);
        initView();
    }

    private void initView() {
        listOfCards = new ArrayList<>();
        listOfCards.add(new ItemCard());
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvWrite = (TextView) findViewById(R.id.tvWrite);
        tbSwitch = (ToggleButton) findViewById(R.id.tbSwitch);
        ivPpt = (ImageView) findViewById(R.id.ivPpt);
        etCard = (EditText) findViewById(R.id.etCard);

        if ( mTitle != null && mTitle.length() > 0 ) tvTitle.setText(mTitle);

        tvWrite.setOnClickListener(onClickListener);
        tbSwitch.setOnClickListener(onClickListener);
        ivPpt.setOnClickListener(onClickListener);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("1"));
        tabLayout.addTab(tabLayout.newTab().setText("+"));
        tabLayout.setOnTabSelectedListener(onTabSelectedListener);
    }

    private TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int position = tab.getPosition();
            if ( listOfCards != null && listOfCards.size() > tab.getPosition() ) {
                ItemCard item = listOfCards.get(tab.getPosition());
                etCard.setText(item.front);
                tbSwitch.setChecked(false);
            } else if ( listOfCards != null && listOfCards.size() == tab.getPosition() )  {
                // Add New Tab
                if ( listOfCards == null ) listOfCards = new ArrayList<>();

                // get length of flash card
                int tabCount = tabLayout.getTabCount();
                if ( tabCount > 0 ) {
                    ItemCard item = new ItemCard();
                    listOfCards.add(tabCount-1, item);
                    tabLayout.addTab(tabLayout.newTab().setText(String.valueOf(tabCount)), tabCount-1, true);
                    tabLayout.invalidate();
                }
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            if ( tab.getPosition() < tabLayout.getTabCount() - 1 )
                saveFlashCard();
            else if ( tab.getPosition() == tabLayout.getTabCount() -1 ) // (+) tab
                etCard.setText("");
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tbSwitch:
                    switchFlashCard();
                    break;
                case R.id.tvWrite:
                    saveFlashCard();
                    if ( checkValidation() ) {
                        final CTDialog dialog = new CTDialog(MakeCardActivity.this);
                        dialog.setMessage("글쓰기", "저장하시겠습니까?", "아니오", "예",
                                new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        if ( dialog != null && dialog.isShowing() ) dialog.dismiss();
                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (saveAsFile()) {
                                            dialog.dismiss();
                                            Intent intent = new Intent();
                                            intent.putExtra(CTActivityUtil.KEY_DIRECTORY, mCurDir);
                                            intent.putExtra(CTActivityUtil.KEY_TITLE, mTitle);
                                            setResult(RESULT_OK, intent);
                                            finish();
                                        }
                                    }
                                });
                        dialog.show();
                    }
                    break;
                case R.id.ivPpt:
                    CTActivityUtil activityUtil = new CTActivityUtil();
                    activityUtil.startPptViewerActivity(MakeCardActivity.this);
                    break;
            }
        }
    };


    private void switchFlashCard() {
        if ( tbSwitch == null ) return;
        boolean back = tbSwitch.isChecked();  // front - off, back - on

        if ( tabLayout != null ) {
            int index = tabLayout.getSelectedTabPosition();
            if ( listOfCards != null && index < listOfCards.size() ) {
                ItemCard card = listOfCards.get(index);
                if ( card != null ) {
                    if ( !back && card.front != null ) {
                        card.back = etCard.getText().toString();
                        etCard.setText(String.valueOf(card.front));
                    } else if ( back && card.back != null ) {
                        card.front = etCard.getText().toString();
                        etCard.setText(String.valueOf(card.back));
                    } else etCard.setText("");
                }
            }
        }
    }

    private void saveFlashCard() {
        if ( tabLayout != null ) {
            int index = tabLayout.getSelectedTabPosition();
            if ( listOfCards != null && index < listOfCards.size() ) {
                ItemCard card = listOfCards.get(index);
                if ( card != null && tbSwitch.isChecked() ) { // back
                    card.back = etCard.getText().toString();
                } else if ( card != null && !tbSwitch.isChecked() ) { // front
                    card.front = etCard.getText().toString();
                }
            }
        }
    }

    private boolean checkValidation() {
        if ( listOfCards == null ) return false;
        for(int i=0; i<listOfCards.size(); i++) {
            ItemCard card = listOfCards.get(i);
            if ( card.front == null || card.front.length() <= 0 ) {
                Toast.makeText(MakeCardActivity.this, "입력해주세요", Toast.LENGTH_SHORT).show();
                tbSwitch.setChecked(false);
                tabLayout.getTabAt(i);
                etCard.setText("");
                return false;
            } else if ( card.back == null || card.back.length() <= 0 ) {
                Toast.makeText(MakeCardActivity.this, "입력해주세요", Toast.LENGTH_SHORT).show();
                tbSwitch.setChecked(true);
                tabLayout.getTabAt(i);
                etCard.setText("");
                return false;
            }
        }
        return true;
    }

    private boolean saveAsFile() {
        JSONArray array = new JSONArray();
        for(int i=0; i<listOfCards.size(); i++) {
            if ( listOfCards.get(i) != null ) {
                array.put(listOfCards.get(i).toJson());
            }
        }

        File dir = new File(mCurDir);
        if ( !dir.exists() ) dir.mkdir();
        File file = new File(mCurDir + File.separator + mTitle + ".txt");
        try {
            if ( !file.exists() )
                file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(array.toString().getBytes("UTF-8"));
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
