package zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.bean.Musicinfo;
import zhuoxin.lineng.mymusicplays.R;

/**
 * Created by 繁华丶落尽 on 2016/6/29.
 */
public class MainListadatper extends BaseAdapter {
    private Context context;
    private ArrayList<Musicinfo> musiclist;

    public MainListadatper(Context context) {
        this.context = context;
        musiclist = new ArrayList<>();
    }

    public void setMusiclist(ArrayList<Musicinfo> musiclist) {
        this.musiclist = musiclist;
        notifyDataSetChanged();//提示数据发生变化，刷新

    }

    @Override
    public int getCount() {
        return musiclist.size();
    }

    @Override
    public Object getItem(int position) {
        return musiclist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_adatper_item, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.title_item);
            holder.artist = (TextView) convertView.findViewById(R.id.artist_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(musiclist.get(position).getTitle());
        holder.artist.setText(musiclist.get(position).getArtist());
        return convertView;
    }

    private class ViewHolder {
        TextView title, artist;
    }
}
