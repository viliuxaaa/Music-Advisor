package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

interface UpdatingList {
    void update();
    default void initializeConfigAndList(String option) {
        Config.CURRENT_DATA_LIST_INDEX = 0;
        Config.CURRENT_PAGE = 0;
        Config.CURRENT_OPTION = option;
        Data.list.clear();
    }
}

class Featured implements UpdatingList {
    @Override
    public void update() {
        String response = Authorization.getResponse("/v1/browse/featured-playlists");
        initializeConfigAndList("FEATURED");

        JsonObject jo = JsonParser.parseString(response).getAsJsonObject();
        JsonArray items = jo.getAsJsonObject("playlists").getAsJsonArray("items");

        for(int i = 0; i < items.size(); i++) {
            JsonObject item = items.get(i).getAsJsonObject();
            JsonObject externalUrls = item.getAsJsonObject("external_urls");

            String playlist = item.get("name").getAsString();
            String link = externalUrls.get("spotify").getAsString();

            Data.list.add(new Data(playlist, link));
        }

    }
}

class NewReleases implements UpdatingList {
    @Override
    public void update() {
        String response = Authorization.getResponse("/v1/browse/new-releases");
        initializeConfigAndList("NEW");

        JsonObject jo = JsonParser.parseString(response).getAsJsonObject();
        JsonArray items = jo.getAsJsonObject("albums").getAsJsonArray("items");

        for(int i = 0; i < items.size(); i++) {
            JsonObject item = items.get(i).getAsJsonObject();
            List<String> artistName = new ArrayList<>();
            JsonArray artists = item.getAsJsonArray("artists");
            for (int j = 0; j < artists.size(); j++) {
                JsonObject artist = artists.get(j).getAsJsonObject();
                artistName.add(artist.get("name").getAsString());
            }
            JsonObject externalUrls = item.getAsJsonObject("external_urls");

            String songName = item.get("name").getAsString();
            String link = externalUrls.get("spotify").getAsString();

            Data.list.add(new Data(songName, artistName, link));
        }
    }
}

class Category implements UpdatingList {
    @Override
    public void update() {
        String response = Authorization.getResponse("/v1/browse/categories");
        initializeConfigAndList("CATEGORY");

        JsonObject jo = JsonParser.parseString(response).getAsJsonObject();
        JsonArray items = jo.getAsJsonObject("categories").getAsJsonArray("items");

        for(int i = 0; i < items.size(); i++) {
            JsonObject item = items.get(i).getAsJsonObject();
            String category = item.get("name").getAsString();

            Data.list.add(new Data(category));
        }
    }
}

class Playlists implements UpdatingList {

    String searchablePlaylist = "";
    public Playlists (String searchablePlaylist) {
        this.searchablePlaylist = searchablePlaylist;
    }

    @Override
    public void update() {
        String playlistId = "";
        String response = Authorization.getResponse("/v1/browse/categories");
        initializeConfigAndList("PLAYLISTS");


        JsonObject jo = JsonParser.parseString(response).getAsJsonObject();
        JsonArray items = jo.getAsJsonObject("categories").getAsJsonArray("items");

        for(int i = 0; i < items.size(); i++) {
            JsonObject item = items.get(i).getAsJsonObject();
            String playlist = item.get("name").getAsString();
            if (Objects.equals(playlist, searchablePlaylist)) {
                playlistId = item.get("id").getAsString();
                break;
            }
        }
        if (playlistId.equals("")) { System.out.println("Unknown category name."); return; }

        String otherResponse = Authorization.getResponse("/v1/browse/categories/" + playlistId + "/playlists");

        JsonObject joTwo = JsonParser.parseString(otherResponse).getAsJsonObject();
        JsonArray itemsTwo = joTwo.getAsJsonObject("playlists").getAsJsonArray("items");

        for(int i = 0; i < itemsTwo.size(); i++) {
            JsonObject item = itemsTwo.get(i).getAsJsonObject();
            JsonObject externalUrls = item.getAsJsonObject("external_urls");

            String playlist = item.get("name").getAsString();
            String link = externalUrls.get("spotify").getAsString();

            Data.list.add(new Data(playlist, link));
        }
    }
}
