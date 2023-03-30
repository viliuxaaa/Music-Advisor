package advisor;

import java.util.List;

public class Printable {
    static void next() {
        List<Data> list = Data.list;
        if (Config.CURRENT_DATA_LIST_INDEX == list.size()) { System.out.println("No more pages."); return; }

        Config.CURRENT_PAGE++;
        int currentIndex = Config.CURRENT_DATA_LIST_INDEX;
        int untilIndex;
        if (currentIndex + Config.ITEMS_PER_PAGE < list.size()) { untilIndex = currentIndex + Config.ITEMS_PER_PAGE; } else { untilIndex = list.size(); }

        for (int i = currentIndex; i < untilIndex; i++) {
            print(i);
        }
        System.out.println("---PAGE " + Config.CURRENT_PAGE + " OF " + (int) Math.ceil(list.size() / (double) Config.ITEMS_PER_PAGE) + "---");

        Config.PREVIOUS_DATA_LIST_INDEX = Config.CURRENT_DATA_LIST_INDEX;
        Config.CURRENT_DATA_LIST_INDEX = untilIndex;
    }

    static void prev() {
        List<Data> list = Data.list;
        if (Config.PREVIOUS_DATA_LIST_INDEX == 0) { System.out.println("No more pages."); return; }

        Config.CURRENT_PAGE--;
        int currentIndex = Config.PREVIOUS_DATA_LIST_INDEX - Config.ITEMS_PER_PAGE;
        int untilIndex = Config.PREVIOUS_DATA_LIST_INDEX;

        for (int i = currentIndex; i < untilIndex; i++) {
            print(i);
        }
        System.out.println("---PAGE " + Config.CURRENT_PAGE + " OF " + (int) Math.ceil(list.size() / (double) Config.ITEMS_PER_PAGE) + "---");

        Config.CURRENT_DATA_LIST_INDEX = untilIndex;
        Config.PREVIOUS_DATA_LIST_INDEX = untilIndex - Config.ITEMS_PER_PAGE;
    }

    static void print(int index) {
        if (Config.CURRENT_OPTION.equals("FEATURED") || Config.CURRENT_OPTION.equals("PLAYLISTS")) {
            System.out.println(Data.list.get(index).playlist);
            System.out.println(Data.list.get(index).link);
            System.out.println();
        } else if (Config.CURRENT_OPTION.equals("NEW")) {
            System.out.println(Data.list.get(index).songName);
            System.out.println(Data.list.get(index).artistName);
            System.out.println(Data.list.get(index).link);
            System.out.println();
        } else {
            System.out.println(Data.list.get(index).category);
        }
    }
    static void printExit() {
        System.out.println("---GOODBYE!---");
    }
}
