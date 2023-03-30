package advisor;

import java.util.Scanner;

public class Controller {
    public Controller() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if(!Config.AUTHORIZATION) {
                switch (scanner.next()) {
                    case "exit" -> { Printable.printExit(); continue; }
                    case "auth" -> Authorization.createHttpServer();
                    default -> System.out.println("Please, provide access for application.");
                }
            } else {
                switch (scanner.next()) {
                    case "featured" -> featured();
                    case "new" -> newReleases();
                    case "categories" -> categories();
                    case "playlists" -> {
                        String parameter = scanner.nextLine().trim();
                        playlists(parameter);
                    }
                    case "next" -> Printable.next();
                    case "prev" -> Printable.prev();
                    case "exit" -> { Printable.printExit(); continue; }
                }
            }
        }
    }
    static ListUpdater updater = new ListUpdater();
    static void featured() {
        updater.setMethod(new Featured());
        updater.update();
        Printable.next();
    }
    static void categories() {
        updater.setMethod(new Category());
        updater.update();
        Printable.next();
    }
    static void playlists(String searchablePlaylist) {
        updater.setMethod(new Playlists(searchablePlaylist));
        updater.update();
        Printable.next();
    }
    static void newReleases() {
        updater.setMethod(new NewReleases());
        updater.update();
        Printable.next();
    }
}
