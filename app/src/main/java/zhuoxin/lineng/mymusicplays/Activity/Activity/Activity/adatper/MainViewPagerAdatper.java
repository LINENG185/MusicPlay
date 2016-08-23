package zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.adatper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by 繁华丶落尽 on 2016/7/4.
 */
public class MainViewPagerAdatper extends FragmentPagerAdapter {
    Fragment[] frags;
    public MainViewPagerAdatper(FragmentManager fm, Fragment[] frags) {
        super(fm);
        this.frags=frags;
    }

    @Override
    public Fragment getItem(int position) {
        return frags[position];
    }

    @Override
    public int getCount() {
        return frags.length;
    }
}
