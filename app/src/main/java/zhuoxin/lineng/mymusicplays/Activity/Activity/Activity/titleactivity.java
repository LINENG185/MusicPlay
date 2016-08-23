package zhuoxin.lineng.mymusicplays.Activity.Activity.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.fragment.MainListFragment;
import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.adatper.MainViewPagerAdatper;
import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.fragment.RecentListFragment;
import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.fragment.likeListFragment;
import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.service.MusicService;
import zhuoxin.lineng.mymusicplays.R;

/**
 * Created by 繁华丶落尽 on 2016/6/29.
 */
public class titleactivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager vp_fragment;
    private Fragment[] frags = new Fragment[3];
    private MainViewPagerAdatper adatper;
    private Button[] btns = new Button[3];
    private int[] btnids = {R.id.btn_1, R.id.btn_2, R.id.btn_3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_activity);
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        vp_fragment = (ViewPager) findViewById(R.id.vp_fragment);
        for (int i = 0; i < btns.length; i++) {
            btns[i] = (Button) findViewById(btnids[i]);
            btns[i].setOnClickListener(this);
        }
        btns[0].setEnabled(false);
        frags[0] = new MainListFragment();
        frags[2] = new RecentListFragment();
        frags[1] = new likeListFragment();
        adatper = new MainViewPagerAdatper(getSupportFragmentManager(), frags);
        vp_fragment.setAdapter(adatper);
        vp_fragment.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setBtnEnble(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, MusicService.class);
        stopService(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                setBtnEnble(0);
                break;
            case R.id.btn_2:
                setBtnEnble(1);
                break;
            case R.id.btn_3:
                setBtnEnble(2);
                break;
        }
    }

    public void setBtnEnble(int number) {
        vp_fragment.setCurrentItem(number);
        for (int i = 0; i < btns.length; i++) {
            if (i == number) {
                btns[i].setEnabled(false);
            } else {
                btns[i].setEnabled(true);
            }
        }
    }
}
