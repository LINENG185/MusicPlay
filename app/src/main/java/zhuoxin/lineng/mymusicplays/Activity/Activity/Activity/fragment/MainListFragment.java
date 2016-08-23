package zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.fragment;

import android.content.Intent;
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
import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.Utils.Musicutil;
import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.adatper.MainListadatper;
import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.bean.Musicinfo;
import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.constants.Constants;
import zhuoxin.lineng.mymusicplays.R;

/**
 * Created by 繁华丶落尽 on 2016/7/4.
 */
public class MainListFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ListView list_play;
    private ArrayList<Musicinfo> musicList;
    private MainListadatper adatper;
    private int index = -1;
    private ImageView img_main;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v=inflater.inflate(R.layout.fragment_main,container,false);
        list_play= (ListView)v.findViewById(R.id.list_play);
        img_main= (ImageView) v.findViewById(R.id.img_main);
        img_main.setOnClickListener(this);
        adatper=new MainListadatper(getActivity());
        list_play.setAdapter(adatper);
        new Thread(new Runnable() {
            @Override
            public void run() {
                musicList= Musicutil.getmusicList(getActivity());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adatper.setMusiclist(musicList);
                    }
                });
            }
        }).start();
        list_play.setOnItemClickListener(this);
        return v;
    }

    public void Mainclick(View v){
        Intent intent=new Intent(getActivity(),Activity_play.class);
        if (index<0){
            index=0;
        }
        intent.putExtra("index",index);
        startActivity(intent);

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

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getActivity(),Activity_play.class);
        intent.putExtra("index",index);
        getActivity().startActivity(intent);
    }
}
