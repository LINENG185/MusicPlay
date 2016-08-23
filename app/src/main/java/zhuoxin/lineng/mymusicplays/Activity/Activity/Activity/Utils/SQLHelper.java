package zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 繁华丶落尽 on 2016/7/11.
 */
public class SQLHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "music+db";
    public static final String TABLE_RECENT = "recent_list";
    public static final String TABLE_LIKE = "table_like";


    public SQLHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    //当这个类第一次被调用的时候执行的方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TABLE_RECENT + " (title varchar(255)," +
                "_id int," +
                "artist varchar(255)," +
                "duration int," +
                "album varchar(255)," +
                "album_id int," +
                "data varchar(255) )");

        db.execSQL("create table if not exists " + TABLE_LIKE + " (title varchar(255)," +
                "_id int," +
                "artist varchar(255)," +
                "duration int," +
                "album varchar(255)," +
                "album_id int," +
                "data varchar(255) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}

