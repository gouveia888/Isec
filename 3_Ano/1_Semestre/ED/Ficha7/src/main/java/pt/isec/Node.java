package pt.isec;

public class Node<T> {
    private T node;
    private Node<T> left,right;

    public Node() {
    }
    public Node(T node) {
        this.node = node;
    }

    public T getNode() {
        return node;
    }

    public Node<T> getLeft() {
        return left;
    }
    public Node<T> getRight() {
        return right;
    }
    public void setRight(Node<T> node) {
        right = node;
    }
    public void setLeft(Node<T> node) {
        left = node;
    }
    public void setNode(Node<T> node) {
        this.node = node.getNode();
    }

}
