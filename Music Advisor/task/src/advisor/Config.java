package advisor;

import java.util.List;
public class Config {
    public static boolean AUTHORIZATION = false;
    public static String SERVER_PATH = "https://accounts.spotify.com";
    public static String REDIRECT_URI = "http://localhost:8080";
    public static String CLIENT_ID = "63023a907c8e489bb5e3cb8311f06234";
    public static String CLIENT_SECRET = "e0817117811d4950bfcdf21646509693";
    public static String ACCESS_TOKEN = "";
    public static String AUTH_CODE = "";
    public static String RESOURCE = "https://api.spotify.com";

    public static int ITEMS_PER_PAGE = 5;

    public static int CURRENT_DATA_LIST_INDEX = 0;
    public static int PREVIOUS_DATA_LIST_INDEX = 0;
    public static int CURRENT_PAGE = 0;
    public static String CURRENT_OPTION = "";

    public static void matchArguments(String[] args) {
        List<String> list = List.of(args);

        int indexAccess = list.indexOf("-access");
        if (indexAccess != -1) Config.SERVER_PATH = list.get(indexAccess + 1);

        int indexResource = list.indexOf("-resource");
        if (indexResource != -1) Config.RESOURCE = list.get(indexResource + 1);

        int indexItemsPerPage = list.indexOf("-page");
        if (indexItemsPerPage != -1) Config.ITEMS_PER_PAGE = Integer.parseInt(list.get(indexItemsPerPage + 1));
    }
}
