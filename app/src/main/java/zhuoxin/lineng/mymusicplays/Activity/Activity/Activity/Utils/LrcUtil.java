package zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.Utils;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.bean.LrcInfo;

/**
 * Created by 繁华丶落尽 on 2016/7/12.
 */
public class LrcUtil {
    private static final String LRC_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";

    public static ArrayList<LrcInfo> getLrc(String path) {
        ArrayList<LrcInfo> list = new ArrayList<>();
        list.add(new LrcInfo(0,path));
        File file = new File(LRC_DIR + path+".lrc");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                line = line.replace("[", "");
                String[] temp = line.split("]");

                if (temp[0].matches("\\d{2}[:]\\d{2}[\\.]\\d{2}")) {
                    if (temp.length > 1) {
                        int time = gettimefromstring(temp[0]);
                        list.add(new LrcInfo(time, temp[1].trim()));
                    }
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return list;
    }

    private static int gettimefromstring(String str) {
        String[] temptime = str.split(":");
        int min = Integer.parseInt(temptime[0]);
        float sec = Float.parseFloat(temptime[1]);
        return (int) (min * 60000 + sec * 1000);
    }
}
