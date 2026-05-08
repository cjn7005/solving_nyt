package src.strands;

import java.util.List;
import java.util.Set;

import src.util.Node;

public class StrandsResults {
    public Set<String> words;
    public Set<List<Node<String>>> paths;

    public StrandsResults(Set<String> words, Set<List<Node<String>>> paths) {
        this.words = words;
        this.paths = paths;
    }

    public Set<String> getWords() {
        return words;
    }

    public Set<List<Node<String>>> getPaths() {
        return paths;
    }

    public void addWord(String word) {
        synchronized (words) {
            words.add(word);
        }
    }
    
    public void addPath(List<Node<String>> path) {
        synchronized (paths) {
            paths.add(path);
        }
    }
}
