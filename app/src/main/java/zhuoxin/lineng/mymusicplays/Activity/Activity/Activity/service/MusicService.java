package zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.RemoteViews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.datatype.Duration;

import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.Activity_play;
import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.Utils.DBUtil;
import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.Utils.Musicutil;
import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.bean.Musicinfo;
import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.constants.Constants;
import zhuoxin.lineng.mymusicplays.R;

/**
 * Created by 繁华丶落尽 on 2016/6/29.
 */
public class MusicService extends Service {
    private ArrayList<Musicinfo> musiclist;
    private MediaPlayer mediaPlayer;
    private int index = -1;
    private Musicinfo musicnow;
    private int progressnow;
    private int musicstate = Constants.STATE_STOP;
    private int loopMode=Constants.LOOP_LIST;

    private int[] ranBox;
    private Random random=new Random();
    private int ranindex=-1;
    private Notification notification;
    private NotificationManager manager;
    private DBUtil dbutil;
    private MediaPlayer.OnPreparedListener preparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
            musicstate = Constants.STATE_PLAY;
            dbutil.savaMusic(musicnow);
            Intent intent=new Intent(Constants.RECENT_FLUSH);
            sendBroadcast(intent);

            Intent intent1=new Intent(Constants.LIKE_FLUSH);
            sendBroadcast(intent1);
            handler.sendEmptyMessage(0);
        }
    };
    private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            changemusic(Constants.FLAG_NEXT);
        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mediaPlayer != null) {
                progressnow = mediaPlayer.getCurrentPosition();
            }
            initnotify();
            Intent intent = new Intent(Constants.HANDLER_MUSIC_STATE);
            intent.putExtra("index", index);
            intent.putExtra("progress", progressnow);
            intent.putExtra("musicstate", musicstate);
            if (musicstate == Constants.STATE_PLAY) {
                handler.sendEmptyMessageDelayed(0, 300);
            }
            sendBroadcast(intent);
        }
    };
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constants.LOOP_CHANGE:
                    loopMode=intent.getIntExtra("loopMode",loopMode);
                    break;
                case Constants.BUTTON_PRESS:
                    pressButton(intent);
                    break;
                case Constants.PROGRESS_CHANGE:
                    progressChange(intent);
                    break;
                case Constants.MUSIC_PLAY:
                    index = intent.getIntExtra("index", index);
                    playMusic();
                    break;
            }
        }


    };

    private void progressChange(Intent intent) {
        musicstate = intent.getIntExtra("musicstate", musicstate);
        progressnow = intent.getIntExtra("progressnow", progressnow);
        resunemusic();
    }

    private void pressButton(Intent intent) {
        musicstate = intent.getIntExtra("musicstate", musicstate);
        switch (musicstate) {
            case Constants.STATE_STOP:
                stopmusic();
                break;
            case Constants.STATE_PLAY:
                resunemusic();
                break;
            case Constants.STATE_PAUSE:
                pausemusic();
                break;
            case Constants.STATE_BACK:
                changemusic(Constants.FLAG_BACK);
                break;
            case Constants.STATE_NEXT:
                changemusic(Constants.FLAG_NEXT);
                break;
        }
    }

    private void changemusic(int flag) {
        stopmusic();
        switch (flag) {
            case Constants.FLAG_BACK:
                index--;
                if (index < 0) {
                    index = musiclist.size() - 1;
                }
                break;
            case Constants.FLAG_NEXT:
                nextMusic();
                break;
            case Constants.FLAG_SINGLE:

                break;
        }
        playMusic();
    }

    private void nextMusic() {
        switch (loopMode){
            case Constants.LOOP_LIST:
                index++;
                if (index > musiclist.size() - 1) {
                    index = 0;
                }
                break;
            case Constants.LOOP_RANDOM:
            if (ranindex<0){
                for (int i=0;i<ranBox.length;i++){
                    if (index==ranBox[i]){
                        ranindex=i;
                        break;
                    }
                }
            }
                ranindex++;
                if (ranindex>ranBox.length-1){
                    ranindex=0;
                }
                index=ranBox[ranindex];
                break;
            case Constants.LOOP_SINGLE:
                break;
        }
    }

    private void stopmusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    private void pausemusic() {
        musicstate = Constants.STATE_PAUSE;
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            progressnow = mediaPlayer.getCurrentPosition();

        }
    }

    private void resunemusic() {
        musicstate = Constants.STATE_PLAY;
        if (progressnow > 0) {
            mediaPlayer.start();
            mediaPlayer.seekTo(progressnow);
            handler.sendEmptyMessage(0);
        } else {
            playMusic();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        musiclist = Musicutil.getmusicList(this);
        dbutil=DBUtil.getSlnstance(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.LOOP_CHANGE);
        filter.addAction(Constants.BUTTON_PRESS);
        filter.addAction(Constants.PROGRESS_CHANGE);
        filter.addAction(Constants.MUSIC_PLAY);
        registerReceiver(receiver, filter);

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private void initnotify() {
        notification = new Notification();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.icon = R.mipmap.kugou;
        notification.tickerText = "正在播放:" + musicnow.getTitle();

        RemoteViews remoteviews = new RemoteViews(getPackageName(), R.layout.notify_activity);

        notification.contentView = remoteviews;

        remoteviews.setTextViewText(R.id.title_notify, musicnow.getTitle());
        remoteviews.setTextViewText(R.id.artist_notify, musicnow.getArtist());
//        remoteviews.setImageViewResource();设置资源图片id

        remoteviews.setProgressBar(R.id.pb_notify, musicnow.getDuration(), progressnow, false);

        Intent intent = new Intent(this, Activity_play.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        remoteviews.setOnClickPendingIntent(R.id.img_notify, pendingIntent);

        manager.notify(0x110, notification);
        Intent i2 = new Intent();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer != null) {
            return super.onStartCommand(intent, flags, startId);
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(preparedListener);
        mediaPlayer.setOnCompletionListener(completionListener);

        initranBox();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initranBox() {
        ranBox=new int[musiclist.size()];
        for (int i=0;i<musiclist.size();i++){
        ranBox[i]=random.nextInt(musiclist.size());
            for (int j=0;j<i;j++){
                if (ranBox[i]==ranBox[j]){
                    i--;
                    break;
                }
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void playMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        if (index < 0 && musiclist.size() > 0) {
            index = 0;
        }
        musicnow = musiclist.get(index);
        try {
            mediaPlayer.setDataSource(musicnow.getData());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
