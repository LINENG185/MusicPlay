package zhuoxin.lineng.mymusicplays.Activity.Activity.Activity.bean;

/**
 * Created by 繁华丶落尽 on 2016/6/29.
 */
public class Musicinfo {

        private String title;
        private int _id;
        private String artist;
        private int duration;
        private String album;
        private int album_id;
        private String data;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getAlbum() {
            return album;
        }

        public void setAlbum(String album) {
            this.album = album;
        }

        public int getAlbum_id() {
            return album_id;
        }

        public void setAlbum_id(int album_id) {
            this.album_id = album_id;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public Musicinfo(String title, int _id, String artist, int duration, String album, int anInt_id, String data) {
            this.title = title;
            this._id = _id;
            this.artist = artist;
            this.duration = duration;
            this.album = album;
            this.album_id = anInt_id;
            this.data = data;
        }
    }

