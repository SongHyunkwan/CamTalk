package garmter.com.camtalk.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import garmter.com.camtalk.R;
import garmter.com.camtalk.db.LectureDB;
import garmter.com.camtalk.dialog.ClassInfoDialog;
import garmter.com.camtalk.fragment.BlankFragment;
import garmter.com.camtalk.fragment.CardListFragment;
import garmter.com.camtalk.fragment.LectureListFragment;
import garmter.com.camtalk.fragment.ScheduleFragment;
import garmter.com.camtalk.fragment.SearchFragment;
import garmter.com.camtalk.item.DKClass;
import garmter.com.camtalk.network.LoginUtil;
import garmter.com.camtalk.utils.CTActivityUtil;
import garmter.com.camtalk.utils.CTContants;

public class MainActivity extends AppCompatActivity implements ScheduleFragment.OnFragmentInteractionListener,
        LectureListFragment.OnFragmentInteractionListener, CardListFragment.OnFragmentInteractionListener, BlankFragment.OnFragmentInteractionListener, ClassInfoDialog.OnButtonClickListener {

    TextView tvTitle;
    ImageView ivMy;
    ImageView ivSetting;

    ImageView ivAdd;
    TabLayout tabLayout;
    ViewPager viewPager;
    MainPagerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // 툴바 설정
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(CTContants.TITLE_TAB2);
        ivMy = (ImageView) findViewById(R.id.ivMy);
        ivAdd = (ImageView) findViewById(R.id.ivAdd);
        ivSetting = (ImageView) findViewById(R.id.ivSetting);
        ivMy.setOnClickListener(onClickListener);
        ivAdd.setOnClickListener(onClickListener);
        ivSetting.setOnClickListener(onClickListener);



        // 뷰페이저 설정
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        viewPager.setOffscreenPageLimit(3);

        // 탭 설정
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tab1));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tab2));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tab3));
        tabLayout.setOnTabSelectedListener(viewPagerOnTabSelectedListener);
        tabLayout.getTabAt(CTContants.INDEX_TAB2).select();

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CTActivityUtil activityUtil = new CTActivityUtil();
            switch (v.getId()) {
                case R.id.ivMy:
                    activityUtil.startMyActivity(MainActivity.this);
                    break;
                case R.id.ivAdd:
                    fragmentCard.onClickAddButton();
                    break;
                case R.id.ivSetting:
                    if (viewPager != null && viewPager.getCurrentItem() == CTContants.INDEX_TAB1) {
//                        if ( fragmentSearch == null ) {
//                            fragmentSearch = SearchFragment.newInstance();
//                            FragmentManager fragmentManager = mAdapter.getFragmentManager();
//                            FragmentTransaction fragmentTransaction =        fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.fragment_search, fragmentSearch);
//                            fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.commit();
//                        }
                        activityUtil.startSearchActivity(MainActivity.this);
                    } else if (viewPager != null && viewPager.getCurrentItem() == CTContants.INDEX_TAB2) {
                        alertDiall();
                    } else if (viewPager != null && viewPager.getCurrentItem() == CTContants.INDEX_TAB3) {
                        //fragmentCard.onClickDeleteButton();
                    }
                    break;
            }
        }
    };

    private TabLayout.ViewPagerOnTabSelectedListener viewPagerOnTabSelectedListener = new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition());
            setTitlebar(tab.getPosition());
            if (tab.getPosition() == CTContants.INDEX_TAB1) {
                ivSetting.setImageResource(R.drawable.search_icon);
                ivAdd.setVisibility(View.GONE);
            } else if (tab.getPosition() == CTContants.INDEX_TAB2) {
                ivSetting.setImageResource(R.drawable.refresh);
                ivAdd.setVisibility(View.GONE);
            } else if (tab.getPosition() == CTContants.INDEX_TAB3) {
                ivSetting.setImageResource(R.drawable.trash);
                ivAdd.setVisibility(View.VISIBLE);

                ivAdd.setVisibility(View.GONE);
                alertDial();
            }
        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            tabLayout.getTabAt(position).select();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void setTitlebar(int position) {
        switch (position) {
            case CTContants.INDEX_TAB1:
                tvTitle.setText(CTContants.TITLE_TAB1);
                break;
            case CTContants.INDEX_TAB2:
                tvTitle.setText(CTContants.TITLE_TAB2);
                break;
            case CTContants.INDEX_TAB3:
                tvTitle.setText(CTContants.TITLE_TAB3);
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(MainActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
        Log.d("Main", uri.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CTActivityUtil.REQUEST_MY_LOGOUT && resultCode == RESULT_OK) {
            CTActivityUtil activityUtil = new CTActivityUtil();
            activityUtil.startLoginActivity(MainActivity.this);
            finish();
        } else if (requestCode == CTActivityUtil.REQUEST_MAKE_CARD && resultCode == RESULT_OK) {
            String path = data.getStringExtra(CTActivityUtil.KEY_DIRECTORY);
            String title = data.getStringExtra(CTActivityUtil.KEY_TITLE);
            fragmentCard.addItem(title);
        }
    }

    @Override
    public void OnRateButtonClick(DKClass dkClass) {
        Log.v("언제읽힘","언제읽힘");
        if (dkClass != null && dkClass.getCode() != null && dkClass.getCode().length() > 0) {
            CTActivityUtil activityUtil = new CTActivityUtil();
            activityUtil.startLectureDetailActivity(MainActivity.this, dkClass.getCode());
        }
    }

    @Override
    public void OnStudyButtonClick(final DKClass dkClass) {
        Log.v("언제읽힘","언제읽힘1");
        String title = (dkClass != null && dkClass.getLecture() != null) ? dkClass.getLecture() : "";
        if (title.length() > 0) {
            viewPager.setCurrentItem(CTContants.INDEX_TAB3);
            fragmentCard.openStudyDirectory(title);
//            CardListFragment fragment = fragmentCard;
//
//            if ( fragment != null ) fragment.openStudyDirectory(title);
//            else {
//                fragment = CardListFragment.newInstance();
//                Bundle args = new Bundle();
//                args.putString(CardListFragment.ARG_TITLE, title);
//                fragment.setArguments(args);
//
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_card_list, fragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }

        }
    }

    BlankFragment fragmentBalnk;
    LectureListFragment fragmentLecture;
    ScheduleFragment fragmentSchedule;
    CardListFragment fragmentCard;

    class MainPagerAdapter extends FragmentPagerAdapter {

        FragmentManager fragmentManager;

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);

            fragmentManager = fm;
            if (fragmentLecture == null) {
                fragmentLecture = new LectureListFragment().newInstance();
                fragmentSchedule = new ScheduleFragment().newInstance();
                fragmentCard = new CardListFragment().newInstance();
                fragmentBalnk=new BlankFragment().newInstance();
            }
        }

        @Override
        public Fragment getItem(int position) {
            FragmentTransaction transaction;
            switch (position) {
                case CTContants.INDEX_TAB1:
                    if (fragmentLecture == null) {
                        fragmentLecture = LectureListFragment.newInstance();
                        transaction = fragmentManager.beginTransaction();
                        transaction.add(R.id.fragment_lecture_list, fragmentLecture);
                        transaction.commit();
                    }
                    return fragmentLecture;
                case CTContants.INDEX_TAB2:
                    if (fragmentSchedule == null) {
                        fragmentSchedule = ScheduleFragment.newInstance();
                        transaction = fragmentManager.beginTransaction();
                        transaction.add(R.id.fragment_schedule, fragmentSchedule);
                        transaction.commit();
                    }
                    return fragmentSchedule;
                case CTContants.INDEX_TAB3:
//                    if (fragmentCard== null) {
//                        fragmentCard = CardListFragment.newInstance();
//                        transaction = fragmentManager.beginTransaction();
//                        transaction.add(R.id.fragment_card_list, fragmentCard);
//                        transaction.commit();
//                    }
//                    return fragmentCard;
                    if (fragmentBalnk== null) {
                        fragmentBalnk = BlankFragment.newInstance();
                        transaction = fragmentManager.beginTransaction();
                        transaction.add(R.id.fragment_blank, fragmentBalnk);
                        transaction.commit();
                    }
                    return  fragmentBalnk;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        public FragmentManager getFragmentManager() {
            return fragmentManager;
        }
    }

    private void alertDial() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("알림").setMessage("업데이트 예정입니다.").setNeutralButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //onBackPressed();
            }
        }).create().show();
    }

    private void logOut() {
        LectureDB db = new LectureDB(MainActivity.this);
        db.open();
        db.DBclear();
        db.close();

        LoginUtil loginUtil = new LoginUtil();
        loginUtil.setUserId(MainActivity.this, "");
        CTActivityUtil cu=new CTActivityUtil();
        cu.startLoginActivity(this);
        Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void alertDiall() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("알림").setMessage("시간표를 업데이트 하시겠습니까?").setPositiveButton("업데이트",new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                logOut();
            }

        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        }).create().show();
    }
}

