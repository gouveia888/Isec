package pt.isec;

import java.util.*;

public class Main {

    public static Comparator<String> comp = new Comparator<String>() {

        @Override
        public int compare(String o1, String o2) {
            return o1.length() - o2.length();
        }
    };

    public static void main(String[] args) {
        BinaryTree binaryTree = new BinaryTree();

        String []iniciais = {"B","E","A","C","F","H","G"};
        for(String s : iniciais)
            binaryTree.insere(s);
        System.out.println("Em ordem");
        binaryTree.imprimeOrdem();
        System.out.println("Em largura");
        binaryTree.imprimeLargura();

//       BinaryTree<String> binaryTree2 = new BinaryTree<>(comp);
//        //se inserir 2 strings com o mesmo tamanho, lança exceção
//        binaryTree2.insere("B");
//        binaryTree2.insere("AA");
//        binaryTree2.insere("DDDD");
//        binaryTree2.insere("EEE");
//        binaryTree2.insere("FFFFFF");
//        System.out.println("Em ordem com comparator");
//        binaryTree2.imprimeOrdem();
//        System.out.println("Em largura com comparator");
//        binaryTree2.imprimeLarura();


//        BinaryTreeWihSentinela binaryTree3 = new BinaryTreeWihSentinela<>(comp);
//        binaryTree3.insere("B");
//        binaryTree3.imprimeLarura();

        //Ex6

//        List<Integer> lista1 = new ArrayList<>();
//        List<Integer> lista2 = new ArrayList<>();
//
//        BinaryTree binaryTree1 = new BinaryTree();
//        BinaryTree binaryTree2 = new BinaryTree();
//
//        int i = 0, hl,a,b;
//        int n1 = 1000000;
//        int n2 = 10000000;
//        Random r = new Random();
//        while(i<n1)
//            lista1.add(i++);
////        while(i<n2)
////            lista2.add(i++);
//
//        //suffle the lista1 para n/2 etc etc
//        hl = n1*4;
//        for(i = 0; i < hl; i++) {
//            a = r.nextInt(n1);
//            b = r.nextInt(n1);
//            Integer temp = lista1.get(a);
//            lista1.set(a, lista1.get(b));
//            lista1.set(b, temp);
//        }
//
//        Iterator it = lista1.iterator();
//
//        while(it.hasNext())
//            binaryTree1.insere((Integer)it.next());
//
//        System.out.println("A árvore tem " + binaryTree1.size() + " elementos, em " + binaryTree1.profundidadeDe() + " níveis.");
//        System.out.println("Devia ter estes niveis " + Math.log(n1)/Math.log(2));

        //Ex7

        //Ex8
//        System.out.println("Ex 8 - Antes de remover folhas");
//        binaryTree.imprimeLargura();
//        binaryTree.removeFolhas();
//        System.out.println("Ex 8 - Depois de remover folhas");
//        binaryTree.imprimeLargura();

        //Ex9

        System.out.println("Ex 9 - Verifica sucessor");
        System.out.println(binaryTree.sucessor("B").getNode());

        //Ex10
        BinaryTree binaryTreeEx10_1 = new BinaryTree();
        BinaryTree binaryTreeEx10_2 = new BinaryTree();
        for(String s : iniciais) {
            binaryTreeEx10_1.insere(s);
            binaryTreeEx10_2.insere(s);
        }
        binaryTreeEx10_2.insere("Z");
        System.out.println("Ex 10 - Verifica igualdade");
        System.out.println("Primeiro teste verdadeiro: " + binaryTree.compararArvores(binaryTreeEx10_1));
        System.out.println("Segundo teste falso: " + binaryTree.compararArvores(binaryTreeEx10_2));

        //Ex12
        System.out.println("Ex 12 - Iterador");
        BinaryTreeIterable binaryTreeEx12 = new BinaryTreeIterable();

        for(String s : iniciais)
            binaryTreeEx12.insere(s);

        BinaryTreeIterator it = new BinaryTreeIterator(binaryTreeEx12.getRaiz());

        while(it.hasNext())
            System.out.println(it.next().getNode());

    }

}