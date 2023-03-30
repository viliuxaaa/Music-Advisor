package advisor;

import java.util.ArrayList;
import java.util.List;

public class Data {
    String songName = "";
    List<String> artistName = new ArrayList<>();
    String link = "";
    String category = "";
    String playlist = "";

    static List<Data> list = new ArrayList<>();

    public Data (String songName, List<String> artistName, String link) {
        this.songName = songName;
        this.artistName = artistName;
        this.link = link;
    }

    public Data(String category) {
        this.category = category;
    }

    public Data(String playlist, String link) {
        this.playlist = playlist;
        this.link = link;
    }
}
