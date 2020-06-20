import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class DocParser {
    // method takes a file path and returns inverted index
    public void parse(String path,
                      Map<String, List<String>> invertedIndex) {
        Map<String, Integer> docVoc = new HashMap<String, Integer>();
        Path file = Paths.get(path);
        try {
            // we read all the lines in the file and transfer them to parseLine
            List<String> lines = Files.readAllLines(file);
            for (String line : lines) {
                parseLine(line, docVoc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // make access to the inverted index available for only one thread at a time.
        synchronized (invertedIndex) {
            addIntoInvertedIndex(docVoc, invertedIndex, path);
        }
    }

    // regular expression by which we separate words in lines
    private static final Pattern CheckLines = Pattern.compile("\\P{IsAlphabetic}+");

    // converts lines into a list of words and writes to a vocabulary
    private void parseLine(String line, Map<String, Integer> docVoc) {

        // split line in words by pattern CheckLines
        for (String word : CheckLines.split(line)) {
            if (!word.isEmpty()){
                docVoc.merge(word.toLowerCase(), 1, (a,b) -> a+b);
            }
        }
    }

    // add file vocabulary to inverted index
    private static void addIntoInvertedIndex(Map<String, Integer> vocabulary,
                                             Map<String, List<String>> invertedIndex, String filePath) {
        // go through the words from the vocabulary,
        // if the word is in the inverted index,
        // then add the name of the document to the list of documents associated with this word.
        // if the word is not in the index,
        // adds the word and document associated with it to the  inverted index
        for (String word : vocabulary.keySet()) {
            if (word.length() >= 3) {
                invertedIndex.computeIfAbsent(word, k -> new ArrayList<>()).add(filePath);
            }
        }
    }
}
