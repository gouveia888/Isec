package org.example;

import java.util.*;

public class Main {

    public static <T> void  ex1(List<? super T> lista, T value){
        /*for(int i = 0; i < lista.size(); i++)
            lista.set(i,value);*/ // Complexidade Quadratica O(n^2) usando for
        ListIterator it = lista.listIterator();
        while(it.hasNext()){
            it.next();
            it.set(value); //it.set O(1)
        }
    }

    public static <T> void  ex2(List<T> lista){
        ListIterator it = lista.listIterator(lista.size()); // Comeca do final da lista
        while(it.hasPrevious()){
            System.out.println(it.previous());
        }
    }

    public static double testa(Ex3 lista, long sz){
        long startTime = System.nanoTime();
        for(long i = 0; i < sz; i++)
            lista.push(i);
        for(long i = 0; i < sz; i++)
            lista.pop();
        return (System.nanoTime() - startTime)/1000000.0;
    }

    public static double testa(Ex4 lista, long sz){
        long startTime = System.nanoTime();
        for(long i = 0; i < sz; i++)
            lista.add(i);
        for(long i = 0; i < sz; i++)
            lista.remove();
        return (System.nanoTime() - startTime)/1000000.0;
    }

    public static void TestaPilha(){
        //Testar a pilha
        List alist = new ArrayList();
        List llist = new LinkedList();
        long n, sz=50000, nruns=20;
        double tm;
        Ex3 pal = new Ex3(alist);
        Ex3 pll = new Ex3(llist);
        System.out.println("Tempo execuçao medio " + nruns + "execuçoes(ms)");
        System.out.println("N:ArrayList;LinkedList");
        for(int i = 0; i <=30; i++){
            n=sz*i; //Tamanho do pacote de dados a entrar neste ciclo
            tm=0;
            for(int j=0; j < nruns; j++)
                tm += testa(pal, n);
            System.out.printf("%d: %9.2f;", i,(float)tm/nruns);
            tm=0;
            for(int j=0; j < nruns; j++)
                tm += testa(pll, n);
            System.out.printf("%.2f\n",(float)tm/nruns);

        }
    }

    public static void TestaFila(){
        List alist = new ArrayList();
        List llist = new LinkedList();
        long n, sz=3000, nruns=20;
        double tm;
        Ex4 pal = new Ex4(alist);
        Ex4 pll = new Ex4(llist);
        System.out.println("Tempo execuçao medio " + nruns + "execuçoes(ms)");
        System.out.println("N:ArrayList;LinkedList");
        for(int i = 0; i <=30; i++){
            n=sz*i; //Tamanho do pacote de dados a entrar neste ciclo
            tm=0;
            for(int j=0; j < nruns; j++)
                tm += testa(pal, n);
            System.out.printf("%d: %9.2f;", i,(float)tm/nruns);
            tm=0;
            for(int j=0; j < nruns; j++)
                tm += testa(pll, n);
            System.out.printf("%.2f\n",(float)tm/nruns);

        }

    }

    public static <T> void ex5(List<? super T> lista){
        ListIterator iti = lista.listIterator();
        ListIterator itf = lista.listIterator(lista.size());

        while(iti.nextIndex() < itf.previousIndex()){ // Enquanto o indice do proximo elemento do inicio for menor que o indice do elemento anterior do final
            T temp = (T) iti.next();
            iti.set(itf.previous());
            itf.set(temp);
        }
    }

    public static <T> void ex6(Collection <T> lista){
        List l = new ArrayList(lista);
        Ex3 <T> pilha = new Ex3(l);
        for(T item : lista){
            pilha.push(item);
        }
        while(!pilha.empty()){
            pilha.pop();
        }
    }

    public static void main(String[] args) {
        List list = new ArrayList();
        for(int i = 0; i < 10; i++)
            list.add(i);
        System.out.println(list);
        //ex1(list,1);

        System.out.println("Ex1" + list);
        //ex2(list);

        Ex3 ex3 = new Ex3(list);
        System.out.println("Ex3 peek: " + ex3.peek());
        System.out.println("Ex3 pop: " + ex3.pop());
        //System.out.println("Ex3 pop: " + ex3.pop());

        //TestaPilha(); //Ex3 - linear | linear

        //TestaFila();//Ex4 - Quadratico | Constante

        ex5(list);
        System.out.println("Ex5: " + list);

        ex6(list);
        System.out.println("Ex6: " + list);
    }
}