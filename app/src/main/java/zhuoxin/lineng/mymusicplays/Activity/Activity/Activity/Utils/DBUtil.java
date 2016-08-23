package zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.bean.Musicinfo;

/**
 * Created by 繁华丶落尽 on 2016/7/11.
 */
public class DBUtil {
    private SQLHelper helper;
    private SQLiteDatabase db;
    private static DBUtil slnstance;

    private  DBUtil(Context context) {
       helper=new SQLHelper(context);
        db=helper.getWritableDatabase();
    }

    public static DBUtil getSlnstance(Context context){
        if (slnstance==null){
            slnstance=new DBUtil(context);
        }
        return slnstance;
    }
    public void savaMusic(Musicinfo info){
        ContentValues values=new ContentValues();
        values.put("title",info.getTitle() );
        values.put("_id",info.get_id() );
        values.put("artist",info.getArtist() );
        values.put("duration",info.getDuration() );
        values.put("album",info.getAlbum() );
        values.put("album_id",info.getAlbum_id() );
        values.put("data",info.getData() );
        db.insert(SQLHelper.TABLE_RECENT,null,values);
        db.insert(SQLHelper.TABLE_LIKE,null,values);

    }
    public ArrayList<Musicinfo> getrecentList(){
        ArrayList<Musicinfo> list=new ArrayList<>();
        Cursor cursor=db.query(true,SQLHelper.TABLE_RECENT,null,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String title=cursor.getString(cursor.getColumnIndex("title"));
            String artist=cursor.getString(cursor.getColumnIndex("artist"));
            String album=cursor.getString(cursor.getColumnIndex("album"));
            String data=cursor.getString(cursor.getColumnIndex("data"));

            int _id=cursor.getInt(cursor.getColumnIndex("_id"));
            int duration=cursor.getInt(cursor.getColumnIndex("duration"));
            int album_id=cursor.getInt(cursor.getColumnIndex("album_id"));
            list.add(new Musicinfo(title,_id,artist,duration,album,album_id,data));
        }

        return list;
    }
    public ArrayList<Musicinfo> getLikeList(){
        ArrayList<Musicinfo> list=new ArrayList<>();
        Cursor cursor=db.query(true,SQLHelper.TABLE_LIKE,null,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String title=cursor.getString(cursor.getColumnIndex("title"));
            String artist=cursor.getString(cursor.getColumnIndex("artist"));
            String album=cursor.getString(cursor.getColumnIndex("album"));
            String data=cursor.getString(cursor.getColumnIndex("data"));

            int _id=cursor.getInt(cursor.getColumnIndex("_id"));
            int duration=cursor.getInt(cursor.getColumnIndex("duration"));
            int album_id=cursor.getInt(cursor.getColumnIndex("album_id"));
            list.add(new Musicinfo(title,_id,artist,duration,album,album_id,data));
        }

        return list;
    }
}
