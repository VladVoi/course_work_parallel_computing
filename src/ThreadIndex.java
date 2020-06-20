import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class ThreadIndex extends Thread {
    Map<String, List<String>> invertedIndex;
    ArrayList<File> files;

    // class constructor that receives data for calculations
    public ThreadIndex(Map<String, List<String>> invertedIndex, ArrayList<File> files) {
        this.invertedIndex = invertedIndex;
        this.files = files;
    }

    // calculation that are done in this thread
    public void run() {

        for (File file : files) {
            DocParser parser = new DocParser();

            if (file.getName().endsWith(".txt")) {
                parser.parse(file.getAbsolutePath(), invertedIndex);
            }

        }
    }
}