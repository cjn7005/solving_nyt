package src.util;

public class Node<T> {
    private T value;
    private Object[] edges;
    private int edgeSize;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Object[] getEdges() {
        return edges;
    }

    public void setEdges(Object[] edges) {
        this.edges = edges;
    }

    public Node(T value, Object[] edges) {
        this.value = value;
        this.edges = edges;
        this.edgeSize = 0;
        for (Object edge : edges) {
            if (edge != null) {
                edgeSize++;
            }
        }
    }

    public Node(T value) {
        Object[] edges = new Object[8];
        this(value, edges);
    }

    public Node(Object[] edges) {
        this(null, edges);
    }

    public Node() {
        Object[] edges = new Object[8];
        this(null, edges);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}