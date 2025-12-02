package pt.isec;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BinaryTreeIterator implements Iterator {
    Node temp;
    List<Node> stack = new ArrayList<>();

    public void addPathToMinFrom(Node n){
        while(n != null){
            stack.add(n);
            n = n.getLeft();
        }
    }

    public BinaryTreeIterator(Node tree){
        this.temp = tree;
    }

    private void advancedIterator(){
        Node current = stack.remove(stack.size() - 1);
        if(current.getRight() != null)
            addPathToMinFrom(current.getRight());
    }

    public boolean hasNext(){
        return stack.size() > 0;
    }

    private Node getNext(){
        return stack.get(stack.size() - 1);
    }

    public Node next(){
        Node ret = getNext();
        advancedIterator();
        return ret;
    }
}
