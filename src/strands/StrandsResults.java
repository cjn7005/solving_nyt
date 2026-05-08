package src.strands;

import java.util.List;
import java.util.Set;

import src.util.Node;

/**
 * Record class for storing found Strands words as well as their paths
 */
public class StrandsResults {
    public Set<String> words;
    public Set<List<Node<String>>> paths;

    /**
     * Creates a new StrandsResults object
     * 
     * @param words the set of words
     * @param paths the set of paths
     */
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

    /**
     * Adds a new word to the word set
     * 
     * Synchronized on the word set
     * 
     * @param word the new word to add
     */
    public void addWord(String word) {
        synchronized (words) {
            words.add(word);
        }
    }
    
    /**
     * Adds a new path to the path set
     * 
     * Synchronized on the path set
     * 
     * @param path the new path to add
     */
    public void addPath(List<Node<String>> path) {
        synchronized (paths) {
            paths.add(path);
        }
    }
}
