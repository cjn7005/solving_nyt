package src.strands;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import src.util.Edge;
import src.util.Node;
import src.util.WordChecker;

public class FinderThread extends Thread {
    private final Node<String> node;
    private final StrandsResults result;

    public FinderThread(Node<String> node, StrandsResults result) {
        this.node = node;
        this.result = result;
    }

    @SuppressWarnings("unchecked")
    public StrandsResults dfs(Node<String> node, int depth, 
                              StringBuilder builder, StrandsResults result,
                              Set<Node<String>> visited, List<Node<String>> path) {
        if (depth >= Strands.MAX_WORD_LENGTH) {
            return result;
        }
        visited.add(node);
        path.add(node);
        builder.append(node.getValue());
        String word = builder.toString();
        if (depth >= Strands.MIN_WORD_LENGTH-1 && 
            !result.getWords().contains(word) && 
            WordChecker.checkWord(builder.toString())) {
            result.addWord(word);
            result.addPath(new LinkedList<>(path));
        }
        for (Object obj : node.getEdges()) {
            Edge<String> edge = (Edge<String>) obj;
            if (edge != null) {
                Node<String> to = edge.getTo();
                if (!visited.contains(to)) {
                    result = dfs(to, depth+1, builder, result, visited, path);
                }
            }
        }
        path.removeLast();
        visited.remove(node);
        builder.deleteCharAt(builder.length()-1);
        return result;
    }

    @Override
    public void run() {
        StringBuilder builder = new StringBuilder();
        Set<Node<String>> visited = new HashSet<>();
        List<Node<String>> path = new LinkedList<>();
        dfs(node, 0, builder, result, visited, path);
    }
}
