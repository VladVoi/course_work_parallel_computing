import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildIndexSerial {
    public static void main(String[] args) {
        File data = new File("C:/Users/Vladislav/Desktop/course_data");
        ArrayList<File> files = new ArrayList<File>();
        retrieveFiles(data, files);
        Map<String, List<String>> invertedIndex = new HashMap<String, List<String>>();

        long timeS = System.nanoTime();
        // go through the files and pass them to the parser
        // returns inverted index
        for (File file : files) {
            DocParser parser = new DocParser();

            if (file.getName().endsWith(".txt")) {
                parser.parse(file.getAbsolutePath(), invertedIndex);
            }

        }
        long timeF = System.nanoTime();
        long timeSerial = timeF - timeS;

        // inverted index output
        invertedIndex.forEach((String key, List<String> value) -> {
            System.out.println(key + ":");
            for(String path: value){
                System.out.println(path);
            }
        });
        System.out.println("files: " + files.size() + " Time: " + timeSerial);
        System.out.println("invertedIndex:" + invertedIndex.size());
    }

    // a method that extracts all files from a folder and folders inside
    public static void retrieveFiles(File folder, ArrayList<File> files) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                retrieveFiles(fileEntry, files);
            } else {
                files.add(fileEntry);
            }
        }
    }
}

