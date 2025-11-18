package pt.isec;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class BinaryTree<T extends Comparable<? super T>>{
    private Node<T> raiz;
    private Comparator<T> comp;
    static int prof = 1;

    public BinaryTree() {
        comp = new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return o1.compareTo(o2);
            }
        };
    }

    public BinaryTree(Comparator<T> comp) {
        this.comp = comp;
    }

    public void insere(T node) {
        Node n = new Node(node);
        if (raiz == null) {
            raiz = n;
            return;
        }
        insere(raiz,n);
    }

    private Node<T> insere (Node <T> raiz, Node<T> node){
        if(raiz==null){
            return raiz;
        }

        int cmp = comp.compare(raiz.getNode(),node.getNode());
        if(cmp==0){
            throw new RuntimeException();
        }
        if(cmp > 0) {
            raiz.setLeft(insere(raiz.getLeft(), node));
            if(raiz.getLeft()==null){
                raiz.setLeft(node);
            }
        }else {
            raiz.setRight(insere(raiz.getRight(), node));
            if(raiz.getRight()==null){
                raiz.setRight(node);
            }
        }
        return raiz;
    }

    public boolean verificaNode(T node){
        return verificaNode(raiz,node);
    }

    public boolean verificaNode(Node <T> raiz,T node){
        if(raiz==null){
            return false;
        }
        int cmp = comp.compare(node,raiz.getNode());
        if(cmp==0){
            return true;
        }
        if(cmp > 0)
            return verificaNode(raiz.getLeft(),node);
        else
            return verificaNode(raiz.getRight(),node);
    }

    public int profundidade(T node){
        return  profundidade(raiz,node);
    }

    private int profundidade(Node <T> raiz, T node){
        int d=1;
        if(raiz==null){
            return 0;
        }
        int cmp = comp.compare(node,raiz.getNode());
        if(cmp==0){
            return 1;
        }else if(cmp > 0){
            d = profundidade(raiz.getLeft(),node);
            if(d > 0)
                return d+1;
            return 0;
        }else{
            d = profundidade(raiz.getRight(),node);
            if(d > 0)
                return d+1;
            return 0;
        }
    }

    public void imprimeOrdem(){
        List<T> inOrderList = new ArrayList<>();
        imprimeOrdem(raiz,inOrderList);
        mostraList(inOrderList);
    }

    private void imprimeOrdem(Node <T> raiz, List<T> storageList){
        if(raiz==null)
            return;

        imprimeOrdem(raiz.getLeft(),storageList);
        storageList.add(raiz.getNode());
        imprimeOrdem(raiz.getRight(),storageList);

    }

    public void mostraList(List<T> list){
        for(T i : list)
            System.out.println(i);
    }

    public int profundidadeDe(){
        return profundidadeDe(raiz);
    }

    private int profundidadeDe(Node <T> raiz){
        if(raiz==null){
            return 0;
        }
        int l = profundidadeDe(raiz.getLeft());
        int d = profundidadeDe(raiz.getRight());
        return d>l ? d+1 : l+1;
    }

    public int size(){
        return size(raiz);
    }

    private int size(Node <T> raiz){
        if(raiz ==null)
            return 0;
        return 1+size(raiz.getLeft())+size(raiz.getRight());
    }

    public void imprimeLarura(){
        imprimeLarura(raiz);
    }

    private void imprimeLarura(Node <T> raiz){
         List<Node<T>> storageList = new LinkedList<>();

         storageList.add(raiz);
         while(!storageList.isEmpty()){
             Node <T> node = storageList.remove(0);
             System.out.println(node.getNode() + " ");
             if(node.getLeft()!=null)
                 storageList.add(node.getLeft());
             if(node.getRight()!=null)
                 storageList.add(node.getRight());
         }
    }


}
