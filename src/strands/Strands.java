package src.strands;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import src.util.Edge;
import src.util.Node;

/**
 * This class uses a multi-threaded DFS algorithm to search 
 * for valid English words in the New York Times game Strands.
 */
public class Strands {
    /** The minimum word length to search for (inclusive). 
     * For Strands this value is 4 */
    public static final int MIN_WORD_LENGTH = 4;
    /** The maximum word length to search for (inclusive). 
     * Note that this will have a significant impact on performance with diminishing returns */
    public static final int MAX_WORD_LENGTH = 10;
    /** The grid length */
    private static final int GRAPH_ROWS = 8;
    /** The grid width */
    private static final int GRAPH_COLS = 6;
    /** The total number of letters in the grid */
    private static final int GRAPH_SIZE = GRAPH_ROWS * GRAPH_COLS;

    /** The list of nodes present in the grid.
     * (Must first cast to a Node<String>) */
    private Object[] nodes;
    /** The number of nodes in the grid. May or may not align with nodes.length */
    private int nodeSize;

    /** 
     * Returns the nodes present in the grid
     * 
     * @return the nodes present in the grid
     */
    public Object[] getNodes() {
        return this.nodes;
    }
    
    /**
     * Create a new default Strands object
     */
    public Strands() {
        this.nodes = new Object[GRAPH_SIZE];
        this.nodeSize = 0;
    }
    
    /**
     * Adds a new node to the grid
     * 
     * @param node the node to add
     */
    public void addNode(Node<String> node) {
        if (nodeSize >= nodes.length) {
            nodes = resize(nodes);
        }
        nodes[nodeSize] = node;
        nodeSize++;
    }

    /**
     * Removes a node from the grid
     * 
     * @param node the node to remove
     */
    public void removeNode(Node<String> node) {
        int idx = findNode(node);
        nodes[idx] = null;
        for (; idx <= nodeSize; idx++) {
            nodes[idx] = nodes[idx+1];
        }
        nodeSize--;
    }

    /**
     * Performs a linear search to find the index of a node.
     * Currently only used by {@link src.strands.Strands#removeNode}
     * 
     * @param node the node to search for
     * @return the index of the node in {@code nodes}
     */
    private int findNode(Node<String> node) {
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].equals(node)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Doubles the size of {@code arr} and returns the result
     * 
     * @param arr the array to resize
     * @return the resized array
     */
    private static Object[] resize(Object[] arr) {
        Object[] newArray = new Object[arr.length*2];
        for (int i = 0; i < arr.length; i++) {
            newArray[i] = arr[i];
        }
        return newArray;
    }

    /**
     * Build the Strands grid from the inputted character array row by row
     * 
     * @param arr the Strands character array
    */
    @SuppressWarnings("unchecked")
    public void buildStrands(char[] arr) {
        Object[] nodes = new Object[arr.length];
        for (int i = 0; i < arr.length; i++) {
            nodes[i] = new Node<>(String.valueOf(arr[i]));
            nodeSize++;
        }
        for (int i = 0; i < nodes.length; i++) {
            int row = i / GRAPH_COLS;
            int col = i % GRAPH_COLS;
            Node<String> node = (Node<String>) nodes[i];
            Object[] edges = new Object[8];
            int j = 0;
            for (int dy = 1; dy >= -1; dy--) {
                for (int dx = -1; dx <= 1; dx++) {
                    if (dx==0 && dy==0) continue;
                    if (((row + dy) < 0 || (row + dy) >= GRAPH_ROWS) ||
                        ((col + dx) < 0 || (col + dx) >= GRAPH_COLS)) {
                        j++;
                        continue;
                    }
                    int k = (row + dy) * GRAPH_COLS + (col + dx);
                    Node<String> to = (Node<String>) nodes[k];
                    edges[j++] = new Edge<String>(node,to);
                }
            }
            node.setEdges(edges);
        }
        this.nodes = nodes;
    }

    /**
     * Finds all of the valid words and their respective paths 
     * within the Strands grid
     * 
     * @return the results of the search
     * @throws InterruptedException
    */
    @SuppressWarnings("unchecked")
    public StrandsResults findAll() throws InterruptedException {
        Set<String> words = new HashSet<>();
        Set<List<Node<String>>> paths = new HashSet<>();
        StrandsResults result = new StrandsResults(words, paths);

        Thread[] threads = new Thread[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            Node<String> node = (Node<String>) nodes[i];
            threads[i] = new FinderThread(node, result);
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }
        return result;
    }

    /**
     * Pretty-print function that prints the Strands grid for each
     * valid English word found in {@code paths}. The letters used to 
     * create the word are capitalized.
     * 
     * @param paths the paths identified by {@link src.strands.Strands#findAll}
     * @return the pretty-print string
    */
    @SuppressWarnings("unchecked")
    public List<String> pathsToStr(Set<List<Node<String>>> paths) {
        StringBuilder builder = new StringBuilder();
        List<String> result = new LinkedList<>();

        for (List<Node<String>> path : paths) {

            for (int row = 0; row < GRAPH_ROWS; row++) {
                for (int col = 0; col < GRAPH_COLS; col++) {
                    int i = row * GRAPH_COLS + col;
                    Node<String> node = (Node<String>) nodes[i];
                    if (path.contains(nodes[i])) {
                        builder.append(node.getValue().toUpperCase());
                    } else {
                        builder.append(node.getValue());
                    }
                    builder.append(" ");
                }
                builder.append("\n");
            }
            for (Node<String> node : path) {
                builder.append(node);
            }
            builder.append("\n");
            builder.append("-".repeat(GRAPH_COLS*2-1));
            builder.append("\n");

            result.add(builder.toString());
            builder.delete(0,builder.length());
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < GRAPH_ROWS; row++) {
            for (int col = 0; col < GRAPH_COLS; col++) {
                int i = row * GRAPH_COLS + col;
                builder.append(nodes[i]);
                builder.append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * Prompts the user for a Strands grid and finds all the valid words.
     * 
     * The Strands grid should be inputted as a single string of characters
     * with no spaces, as read from top left to bottom right.
     * 
     * e.g. the grid: <br>
     * c r u n i a <br>
     * i s b o c s <br>
     * s e a f t o <br>
     * t d e p h r <br>
     * r i a m e p <br>
     * r o n i o m <br>
     * d i n l c m <br>
     * y r a l n o <br>
     * 
     * should be inputted as: <br>
     * cruniaisbocsseaftotdephrriameproniomdinlcmyralno
     * 
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter letters from top left to bottom right: ");
        char[] arr = null;
        do {
            if (arr != null) {
                System.out.println("Please enter " + GRAPH_SIZE + " letters with no spaces: ");
            }
            arr = scanner.next().toCharArray();
        } while (arr.length != GRAPH_SIZE);
        scanner.close();

        Strands strands = new Strands();
        strands.buildStrands(arr);
        System.out.println(strands);

        StrandsResults results = strands.findAll();
        Set<String> words = results.getWords();
        Set<List<Node<String>>> paths = results.getPaths();

        List<String> pathStrs = strands.pathsToStr(paths);
        for (String str : pathStrs) {
            System.out.println(str);
        }
        System.out.println("Found " + words.size() + " words.");
    }
}
