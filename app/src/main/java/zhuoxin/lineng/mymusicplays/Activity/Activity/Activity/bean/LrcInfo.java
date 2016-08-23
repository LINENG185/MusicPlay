package zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.bean;

/**
 * Created by 繁华丶落尽 on 2016/7/12.
 */
public class LrcInfo {
    private int time;
    private String lrc;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }

    public LrcInfo(int time, String lrc) {

        this.time = time;
        this.lrc = lrc;
    }
}
