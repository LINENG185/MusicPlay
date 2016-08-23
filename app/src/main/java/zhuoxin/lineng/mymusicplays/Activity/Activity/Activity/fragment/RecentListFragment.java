package zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.Activity_play;
import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.Utils.DBUtil;
import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.adatper.MainListadatper;
import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.bean.Musicinfo;
import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.constants.Constants;
import zhuoxin.lineng.mymusicplays.R;

/**
 * Created by 繁华丶落尽 on 2016/7/11.
 */
public class RecentListFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ImageView img_main;
    private ListView list_play;
    private ArrayList<Musicinfo> recentList=new ArrayList<>();
    private MainListadatper adatper;
    private int index=-1;
    private DBUtil dbUtil;
    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            recentList=dbUtil.getrecentList();
            adatper.setMusiclist(recentList);
        }

    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_main,container,false);
        dbUtil=DBUtil.getSlnstance(getActivity());
        img_main= (ImageView) v.findViewById(R.id.img_main);
        list_play= (ListView) v.findViewById(R.id.list_play);
        adatper=new MainListadatper(getActivity());
        list_play.setAdapter(adatper);
        list_play.setOnItemClickListener(this);
        img_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Activity_play.class);
                intent.putExtra("index",index);
                getActivity().startActivity(intent);
            }
        });
        IntentFilter filter=new IntentFilter(Constants.RECENT_FLUSH);
        getActivity().registerReceiver(receiver,filter);

        recentList=dbUtil.getrecentList();
        adatper.setMusiclist(recentList);
        return v;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (index==position){
            return;
        }
        index=position;
        Intent intent=new Intent(Constants.MUSIC_PLAY);
        intent.putExtra("index",position);
        getActivity().sendBroadcast(intent);
    }
}
