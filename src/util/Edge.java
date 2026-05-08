package src.util;

public class Edge<T> {
    private Node<T> from;
    private Node<T> to;

    public Node<T> getFrom() {
        return from;
    }

    public void setFrom(Node<T> from) {
        this.from = from;
    }

    public Node<T> getTo() {
        return to;
    }

    public void setTo(Node<T> to) {
        this.to = to;
    }

    public Edge(Node<T> from, Node<T> to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(from.toString());
        builder.append(" -> ");
        builder.append(to.toString());
        return builder.toString();
    }
}