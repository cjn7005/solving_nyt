package src.strands;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import src.util.Edge;
import src.util.Node;

public class Strands {
    private static Set<String> WORDS = new HashSet<>();
    public static final int MIN_WORD_LENGTH = 4;
    public static final int MAX_WORD_LENGTH = 10;
    private static final int GRAPH_ROWS = 8;
    private static final int GRAPH_COLS = 6;
    private static final int GRAPH_SIZE = GRAPH_ROWS * GRAPH_COLS;

    private Object[] nodes;
    private int nodeSize;

    static {
        File file = new File("src/util/words.txt");
        Scanner scanner;
        try {
            scanner = new Scanner(file);
            while ((scanner.hasNextLine())) {
                WORDS.add(scanner.nextLine().toLowerCase());
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object[] getNodes() {
        return this.nodes;
    }

    public Strands() {
        this.nodes = new Object[GRAPH_SIZE];
        this.nodeSize = 0;
    }

    public void addNode(Node<String> node) {
        if (nodeSize >= nodes.length) {
            nodes = resize(nodes);
        }
        nodes[nodeSize] = node;
        nodeSize++;
    }

    public void removeNode(Node<String> node) {
        int idx = findNode(node);
        nodes[idx] = null;
        for (; idx <= nodeSize; idx++) {
            nodes[idx] = nodes[idx+1];
        }
        nodeSize--;
    }

    public static boolean checkWord(String word) {
        return WORDS.contains(word.toLowerCase());
    }

    private int findNode(Node<String> node) {
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].equals(node)) {
                return i;
            }
        }
        return -1;
    }

    private static Object[] resize(Object[] arr) {
        Object[] newArray = new Object[arr.length*2];
        for (int i = 0; i < arr.length; i++) {
            newArray[i] = arr[i];
        }
        return newArray;
    }

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

    @SuppressWarnings("unchecked")
    private StrandsResults findAll() throws InterruptedException {
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

        Strands graph = new Strands();
        graph.buildStrands(arr);
        System.out.println(graph);

        StrandsResults results = graph.findAll();
        // for (String word : words) { 
        //     System.out.println(word);
        // }
        Set<String> words = results.getWords();
        Set<List<Node<String>>> paths = results.getPaths();

        List<String> pathStrs = graph.pathsToStr(paths);

        for (String str : pathStrs) {
            System.out.println(str);
        }
        System.out.println("Found " + words.size() + " words.");
    }
}
