package pt.isec;

public class Main {
    public static void main(String[] args) {
        BinaryTree binaryTree = new BinaryTree();

        binaryTree.insere("B");
        binaryTree.insere("E");
        binaryTree.insere("A");
        binaryTree.insere("C");
        binaryTree.insere("F");
        binaryTree.insere("H");
        binaryTree.insere("G");
        System.out.println("Em ordem");
        binaryTree.imprimeOrdem();
        System.out.println("Em largura");
        binaryTree.imprimeLarura();
    }
}