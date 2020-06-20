import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildIndex {

    public static int NUMBER_THREADS = 4;

    public static void main(String[] args) throws InterruptedException {
        File data = new File("C:/Users/Vladislav/Desktop/course_data");
        ArrayList<File> files = new ArrayList<File>();
        retrieveFiles(data, files);
        Map<String, List<String>> invertedIndex = new HashMap<String, List<String>>();
        int chunk = files.size()/NUMBER_THREADS;

        long timeS = System.nanoTime();
        // split into threads
        ThreadIndex[] TreadArray = new ThreadIndex[NUMBER_THREADS];
        for (int i = 0; i < NUMBER_THREADS; i++) {
            TreadArray[i] = new ThreadIndex(invertedIndex,
                    new ArrayList<File>(files.subList(chunk*i, chunk*(i+1))));
            TreadArray[i].start();
        }

        for (int i = 0; i < NUMBER_THREADS; i++) {
            TreadArray[i].join();
        }
        long timeF = System.nanoTime();
        long timeParallel = timeF - timeS;

        // inverted index output
        invertedIndex.forEach((String key, List<String> value) -> {
            System.out.println(key + ":");
            for(String path: value){
                System.out.println(path);
            }
        });
        System.out.println("files: " + files.size() + " Time: " + timeParallel);
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

