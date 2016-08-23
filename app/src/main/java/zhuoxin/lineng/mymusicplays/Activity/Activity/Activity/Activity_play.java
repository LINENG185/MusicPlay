package zhuoxin.lineng.mymusicplays.Activity.Activity.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import javax.xml.datatype.Duration;

import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.Utils.LrcUtil;
import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.Utils.Musicutil;
import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.bean.LrcInfo;
import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.bean.Musicinfo;
import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.constants.Constants;
import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.service.MusicService;
import zhuoxin.lineng.mymusicplays.R;

/**
 * Created by 繁华丶落尽 on 2016/6/30.
 */
public class Activity_play extends Activity implements View.OnClickListener {
    private ArrayList<Musicinfo> musiclist;
    private int index = -1;
    private TextView title_paly, artist_play, now_play, duration_play;
    private int progressnow;
    private Button text_notify,btn_loop;
    private SeekBar sb_play;
    private int musicstate = Constants.STATE_STOP;
    private ImageView play_btn, back_btn, next_btn;
    private int loopMode=Constants.LOOP_LIST;
    private ArrayList<LrcInfo> lrclist;
    private int flg=0;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            progressnow = intent.getIntExtra("progress", progressnow);
            now_play.setText("0" + progressnow / 60000 + ":" + progressnow % 60000 / 1000);
            musicstate = intent.getIntExtra("musicstate", musicstate);

            if (musicstate == Constants.STATE_PLAY) {
                musicstate = Constants.STATE_PLAY;
                play_btn.setImageResource(R.mipmap.btn_pause);
            } else {
                musicstate = Constants.STATE_PAUSE;
                play_btn.setImageResource(R.mipmap.btn_play);
            }


            int tempindex = intent.getIntExtra("index", index);
            sb_play.setProgress(progressnow);
            if (tempindex != index) {
                index = tempindex;
                Musicinfo musicinfo = musiclist.get(index);
                title_paly.setText(musicinfo.getTitle());
                artist_play.setText("---" + musicinfo.getArtist() + "---");
                duration_play.setText("0" + musicinfo.getDuration() / 60000 + ":" + musicinfo.getDuration() % 60000 / 1000);
                sb_play.setMax(musicinfo.getDuration());
            }

            if (lrclist.size() <= 1){
                return;
            }
            if (lrclist.get(flg).getTime()<progressnow){
                flg++;
            }else {
                artist_play.setText(lrclist.get(flg).getLrc());
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_activity);
        musiclist = Musicutil.getmusicList(this);
        index = getIntent().getIntExtra("index", index);
        if (index<0&&musiclist.size() >0){
            index = 0;
        }

        initviews();
        initDatas();
        lrclist=LrcUtil.getLrc("大海");
        sb_play.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Intent intent=new Intent(Constants.PROGRESS_CHANGE);
                intent.putExtra("musicstate",musicstate);
                sendBroadcast(intent);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Intent intent=new Intent(Constants.PROGRESS_CHANGE);
                if (musicstate==Constants.STATE_STOP){
                    musicstate=Constants.STATE_PLAY;
                }
                musicstate=Constants.STATE_PLAY;
                intent.putExtra("musicstate",musicstate);
                progressnow=sb_play.getProgress();
                intent.putExtra("progressnow",progressnow);
                sendBroadcast(intent);
            }

        });

    }

    private void initDatas() {
        if (index >= 0) {
            Musicinfo musicinfo = musiclist.get(index);
            title_paly.setText(musicinfo.getTitle());
            artist_play.setText("---" + musicinfo.getArtist() + "---");
            duration_play.setText("0" + musicinfo.getDuration() / 60000 + ":" + musicinfo.getDuration() % 60000 / 1000);
            sb_play.setMax(musicinfo.getDuration());

        }
        IntentFilter filter = new IntentFilter(Constants.HANDLER_MUSIC_STATE);
        registerReceiver(receiver, filter);

    }

    private void initviews() {
        title_paly = (TextView) findViewById(R.id.title_play);
        artist_play = (TextView) findViewById(R.id.artist_play);
        now_play = (TextView) findViewById(R.id.now_play);
        duration_play = (TextView) findViewById(R.id.duration_play);
        sb_play = (SeekBar) findViewById(R.id.sb_play);
        play_btn = (ImageView) findViewById(R.id.play_btn);
        back_btn = (ImageView) findViewById(R.id.back_btn);
        next_btn = (ImageView) findViewById(R.id.next_btn);
        text_notify= (Button) findViewById(R.id.text_notify);
        btn_loop= (Button) findViewById(R.id.btn_loop);
        btn_loop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (loopMode){
                    case Constants.LOOP_LIST:
                        loopMode =Constants.LOOP_RANDOM;
                        btn_loop.setText("随机播放");
                        break;
                    case Constants.LOOP_RANDOM:
                        loopMode=Constants.LOOP_SINGLE;
                        btn_loop.setText("单曲循环");
                        break;
                    case Constants.LOOP_SINGLE:
                        loopMode=Constants.LOOP_LIST;
                        btn_loop.setText("列表循环");
                        break;
                }
                Intent intent=new Intent(Constants.LOOP_CHANGE);
                intent.putExtra("loopMode",loopMode);
                sendBroadcast(intent);
            }
        });

        play_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        text_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View v) {
        Log.d("debug", "!!!!!!!!!!!!!!!!!!!!");
        switch (v.getId()) {
            case R.id.play_btn:
                if (musicstate == Constants.STATE_PLAY) {
                    musicstate = Constants.STATE_PAUSE;
                    play_btn.setImageResource(R.mipmap.btn_play);

                } else {
                    musicstate = Constants.STATE_PLAY;
                    play_btn.setImageResource(R.mipmap.btn_pause);
                }
                break;
            case R.id.back_btn:
                musicstate = Constants.STATE_BACK;
                break;
            case R.id.next_btn:
                musicstate = Constants.STATE_NEXT;
                break;
        }
        Intent intent = new Intent(Constants.BUTTON_PRESS);
        intent.putExtra("musicstate", musicstate);
        intent.putExtra("index", index);
        sendBroadcast(intent);
    }
}
