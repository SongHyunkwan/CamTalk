package garmter.com.camtalk.activity;

import android.content.DialogInterface;
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
import garmter.com.camtalk.fragment.CardListFragment;
import garmter.com.camtalk.fragment.LectureListFragment;
import garmter.com.camtalk.fragment.ScheduleFragment;
import garmter.com.camtalk.fragment.SearchListFragement;
import garmter.com.camtalk.fragment.SearchTagFragment;
import garmter.com.camtalk.utils.CTActivityUtil;
import garmter.com.camtalk.utils.CTContants;
import garmter.com.camtalk.utils.CTSContants;


public class SearchActivity extends AppCompatActivity implements SearchListFragement.OnFragmentInteractionListener, LectureListFragment.OnFragmentInteractionListener, SearchTagFragment.OnFragmentInteractionListener{
    TextView tvTitle;
    ImageView ivMy;
    ImageView ivSetting;

    TabLayout tabLayout;
    ViewPager viewPager;
    SearchPageAdapter sAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tvTitle=(TextView)findViewById(R.id.tvTitle);
        tvTitle.setText(CTSContants.TITLETAB1);
        ivMy=(ImageView)findViewById(R.id.ivMy);
        ivSetting=(ImageView)findViewById(R.id.ivSetting);
        ivMy.setOnClickListener(onClickListener);
        ivSetting.setOnClickListener(onClickListener);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        sAdapter=new SearchPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sAdapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        viewPager.setOffscreenPageLimit(2);
        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.search_white_tab));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.hash_white_tab));
        tabLayout.setOnTabSelectedListener(viewPagerOnTabSelectedListener);
        tabLayout.getTabAt(CTSContants.INDEX_TAB1).select();

    }

    private void setTitlebar(int position) {
        switch (position) {
            case CTSContants.INDEX_TAB1:
                tvTitle.setText(CTSContants.TITLETAB1);
                break;
            case CTSContants.INDEX_TAB2:
                tvTitle.setText(CTSContants.TITLETAB2);
                break;
        }
    }

    private TabLayout.ViewPagerOnTabSelectedListener viewPagerOnTabSelectedListener = new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition());
            setTitlebar(tab.getPosition());
            if ( tab.getPosition() == CTSContants.INDEX_TAB1 ) {
                ivSetting.setVisibility(View.GONE);
            } else if ( tab.getPosition() == CTSContants.INDEX_TAB2 ) {
                ivSetting.setVisibility(View.GONE);
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

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CTActivityUtil activityUtil = new CTActivityUtil();
            switch (v.getId()) {
                case R.id.ivMy:
                    activityUtil.startMyActivity(SearchActivity.this);
                    break;
                case R.id.ivSetting:
                    if ( viewPager != null && viewPager.getCurrentItem() == CTSContants.INDEX_TAB1) {

                    } else if ( viewPager != null && viewPager.getCurrentItem() == CTSContants.INDEX_TAB2 ) {

                    }
                    break;
            }
        }
    };

    SearchListFragement fragmentSearch;
    SearchTagFragment fragmentTag;

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(SearchActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
        Log.d("Search", uri.toString());
    }

    class SearchPageAdapter extends FragmentPagerAdapter{

        FragmentManager fragmentManager;

        public SearchPageAdapter(FragmentManager fm) {
            super(fm);
            fragmentManager=fm;
            fragmentSearch=new SearchListFragement().newInstance();
            fragmentTag=new SearchTagFragment().newInstance();

        }

        @Override
        public Fragment getItem(int position) {
            FragmentTransaction transaction;
            switch (position) {
                case CTSContants.INDEX_TAB1:
                    if ( fragmentSearch == null ) {
                        fragmentSearch = SearchListFragement.newInstance();
                        transaction = fragmentManager.beginTransaction();
                        transaction.add(R.id.fragment_search_list, fragmentSearch);
                        transaction.commit();
                    }
                    return fragmentSearch;
                case CTSContants.INDEX_TAB2:
                    if ( fragmentTag == null ) {
                        fragmentTag = SearchTagFragment.newInstance();
                        transaction = fragmentManager.beginTransaction();
                        transaction.add(R.id.fragment_search_tag, fragmentTag);
                        transaction.commit();
                    }
                    return fragmentTag;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private void alertDial() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("알림").setMessage("업데이트 예정입니다.").setNeutralButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }
        }).create().show();
    }
}
