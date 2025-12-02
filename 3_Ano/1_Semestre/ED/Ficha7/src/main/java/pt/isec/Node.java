package pt.isec;

public class Node<T> {
    private T node;
    private Node<T> left,right;
    int pr, pl; //profundidade direita e esquerda

    public Node() {}

    public Node(T node) {
        this.node = node; pr=pl=0;
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

    public void setNode(T node) {
        this.node = node;
    }

    public int getDepth(){
        return (pl>pr) ? pl+1 : pr+1;
    }

    //metedo que indica se o no está balanceado
    public boolean balanced(){
        return Math.abs(pl - pr) <= 1;
    }
}
