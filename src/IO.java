/**
 * Created by Lucas on 22/07/2016.
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class IO {

    static String path = "Game Sync List.txt";

    static String curUser = System.getProperty("user.name");

    static int gameInstall;

    static String readFile() throws IOException {
        String PATH = new String(Files.readAllBytes(Paths.get(path)));

        return PATH;
    }

    static String[] splitString() throws IOException {
        String temp = readFile();
        temp = temp.replaceAll("\\busername\\b", curUser);

        String split[] = temp.split("[\\r\\n]+");

        return split;
    }

    public static HashMap<String, String[]> list(String[] split) throws IOException {
        int x = 3;
        int z = 4;
        int y = 5;

        HashMap<String, String[]> save = new HashMap();
        for (int i = 0; i < gameInstall; i++) {
            save.put(split[x], new String[]{split[y], split[z]});
            x += 3;
            y += 3;
            z += 3;
        }
        return save;
    }
}