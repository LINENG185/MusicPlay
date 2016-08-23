package zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;

import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.bean.Musicinfo;

/**
 * Created by 繁华丶落尽 on 2016/6/29.
 */
public class Musicutil {
    private static ArrayList<Musicinfo> musicList;
    public static ArrayList<Musicinfo>getmusicList(Context context) {
        if (musicList == null){
            musicList = new ArrayList<>();
            ContentResolver resolver=context.getContentResolver();

            Cursor cursor=resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null, MediaStore.Audio.Media.DATE_MODIFIED);

            while (cursor.moveToNext()){
                String title=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String album=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String data=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                int _id=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                int duration=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                int album_id=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                if (duration>30000){
                    musicList.add(new Musicinfo(title,_id,artist,duration,album,album_id,data));
                }
            }


        }
        return musicList;
    }
}
