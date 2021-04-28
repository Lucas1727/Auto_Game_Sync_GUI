/**
 * Created by Lucas on 18/07/2016.
 */

import org.fusesource.jansi.AnsiConsole;

import java.io.BufferedReader;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class GameSync {

    private TreeMap sortedHashMap;

    public void Start() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(IO.path));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();

        lines = (lines / 4);

        IO.gameInstall = lines;

        GameSync app = new GameSync();
        File folder = new File("Saves\\");

        AnsiConsole.systemInstall();
        app.makeDirectory(folder);

        //runAppConsole();
    }

    private String[] scanGame(String x, String y, String z) throws IOException {
        String[] temp = new String[4];

        File source = new File(y);     //Location of game save

        File folder = new File("Saves\\");     //"IO" folder in OneDrive

        File dest = new File(String.valueOf(folder) + "\\" + x);      //Creates the new game folder in OneDrive

        File game = new File(z);          //game install location

        int filesNotSynced = 0;

        System.out.println("");
        System.out.println(ansi().fg(WHITE).a(x).reset());     //Print out game title
        MainGUI.PrintOutput(x + ":");

        if (game.exists()) {
            temp[0] = "true";
        } else {
            temp[0] = "false";
        }

        if (source.exists()) {
            temp[1] = "true";
        } else {
            temp[1] = "false";
        }

        if (source.isDirectory()) {
            String files[] = source.list();
            for (String file : files) {
                File srcFile = new File(source, file);
                File destFile = new File(dest, file);

                if (srcFile.lastModified() > destFile.lastModified()) {
                    System.out.println("Local is newer");
                    MainGUI.PrintOutput("Local is newer - " + srcFile);
                    filesNotSynced += 1;
                } else if (srcFile.lastModified() == destFile.lastModified()) {
                    System.out.println("Cloud & Local are same");
                    MainGUI.PrintOutput("Cloud & Local are same");
                } else {
                    System.out.println("Cloud is newer");
                    MainGUI.PrintOutput("Cloud is newer");
                }
            }
            //System.out.println(filesNotSynced);

            if (filesNotSynced > 0) {
                temp[2] = "true";
                temp[3] = "false";
            } else {
                temp[2] = "true";
                temp[3] = "true";
            }
        } else if (!dest.exists()) {
            temp[2] = "false";
            temp[3] = "false";
        } else {
            temp[2] = "false";
            temp[3] = "true";
        }
        MainGUI.PrintOutput("\n");
        return temp;
    }

    private void start(String x, String y, String z) throws IOException {
        File source = new File(y);     //Location of game save

        File folder = new File("Saves\\");     //"IO" folder in OneDrive

        File dest = new File(String.valueOf(folder) + "\\" + x);      //Creates the new game folder in OneDrive

        File game = new File(z);          //game install location

        System.out.println("");
        System.out.println(ansi().fg(WHITE).a(x).reset());     //Print out game title
        copy(source, dest, game);
    }

    private void startSync(String x, String y, String z) throws IOException {
        File dest = new File(y);     //Location of the game save

        File folder = new File("Saves\\");     //"IO" folder in OneDrive

        File source = new File(String.valueOf(folder) + "\\" + x);     //The location of the game in OneDrive

        File game = new File(z);

        System.out.println("");
        System.out.println(ansi().fg(WHITE).a(x).reset());
        if (game.exists()) {
            copy(source, dest, game);
        } else {
            System.out.println(ansi().fg(BLACK).a("Game not installed on this system").reset());
        }
    }

    private void copy(File source, File dest, File game) throws IOException {
        if (!game.exists()) {
            System.out.println(ansi().fg(BLACK).a("Game not installed on this system").reset());
        } else {
            if (!dest.exists()) {
                System.out.println(ansi().fg(RED).a("Destination folder doesn't exist").reset());
                makeDirectory(dest);
                System.out.println(ansi().fg(CYAN).a("Updating save files from : " + "'" + source + "'").reset());
                copyFolder(source, dest);
            } else {
                System.out.println(ansi().fg(CYAN).a("Updating save files from : " + "'" + source + "'").reset());
                copyFolder(source, dest);
            }
            System.out.println(ansi().fg(GREEN).a("Files are up to date").reset());
        }
    }

    private void copyFolder(File source, File dest) throws IOException {
        if (source.isDirectory()) {
            if (!dest.exists()) {
                dest.mkdir();
                System.out.println(ansi().fg(BLUE).a("Directory created :: " + dest).reset());
            }

            String files[] = source.list();
            for (String file : files) {
                File srcFile = new File(source, file);
                File destFile = new File(dest, file);

                copyFolder(srcFile, destFile);
            }
        } else {
            if (source.lastModified() > dest.lastModified()) {
                Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println(ansi().fg(YELLOW).a("File copied :: " + dest).reset());
            } else {
            }
        }
    }

    private void makeDirectory(File folder) {

        if (!folder.exists()) {
            System.out.println(ansi().fg(BLUE).a("Creating directory: " + "'" + folder + "'").reset());
            folder.mkdirs();
        } else {
            System.out.println(ansi().fg(BLACK).a("Directory already exists: " + "'" + folder + "'").reset());
        }
    }

    private void installGame(String[] input) throws IOException {
        BufferedWriter bw = null;
        FileWriter fw = null;

        for (int i = 0; i < 3; i++) {

            try {
                File file = new File(IO.path);

                if (!file.exists()) {
                    file.createNewFile();
                }

                fw = new FileWriter(file.getAbsoluteFile(), true);
                bw = new BufferedWriter(fw);
                bw.write(input[i]);

            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                try {
                    if (bw != null)
                        bw.close();
                    if (fw != null)
                        fw.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void runSyncConsole() throws IOException {
        String Split[] = IO.splitString();
        HashMap<String, String[]> HashMap = IO.list(Split);
        sortedHashMap = new TreeMap(HashMap);

        Set set = sortedHashMap.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();

            String x = (String) mentry.getKey();
            String y[] = (String[]) mentry.getValue();

            start(x, y[0], y[1]);
        }

        System.out.println("");
        System.out.println(ansi().fg(WHITE).a("---------------------------------------------"));
        System.out.println(ansi().fg(WHITE).a("Updating game saves on this PC").reset());
        System.out.println("");

        Set set2 = sortedHashMap.entrySet();
        Iterator iterator2 = set2.iterator();
        while (iterator2.hasNext()) {
            Map.Entry mentry1 = (Map.Entry) iterator2.next();

            String x = (String) mentry1.getKey();
            String y[] = (String[]) mentry1.getValue();

            startSync(x, y[0], y[1]);
        }
        System.exit(0);
    }

    public void runSyncGUI(String[][] data) throws IOException {

        System.out.println("");
        System.out.println(ansi().fg(WHITE).a("---------------------------------------------"));
        System.out.println(ansi().fg(WHITE).a("Updating game saves on this PC").reset());
        System.out.println("");

        //READ DATA AND DETERMINE WHETHER FILES NEED TO SYNC OR NOT

        for (int i = 0; i < data.length; i++) {
            System.out.println(data[i][0]);
        }

        //startSync(x, y[0], y[1]);

    }

    public TreeMap runAppHash() throws IOException {
        String Split[] = IO.splitString();
        HashMap<String, String[]> HashMap = IO.list(Split);
        sortedHashMap = new TreeMap(HashMap);

        return sortedHashMap;
    }

    public String[] runAppGUI(String x, String y, String z) throws IOException {
        String[] temp;

        temp = scanGame(x, y, z);

        //temp[0] = "True";

        return temp;
    }

    public TreeMap returnTreeMap() throws IOException {
        return sortedHashMap;
    }
}