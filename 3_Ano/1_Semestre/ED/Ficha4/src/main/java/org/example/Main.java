package org.example;

import java.util.Iterator;

class DezReais implements Iterable<Double>{
    private final int MAX = 10;
    private Double[] num;
    protected int last = 0;

    public DezReais(){
        num = new Double[MAX];
    }

    public Double get(int i){
        return num[i];
    }

    public int size(){return last;}

    public boolean add(Double n){
        if(size() >= MAX) throw new RuntimeException();
        num[last++] = n;
        return true;
    }

    public void mostra(){
        for(int i = 0; i < num.length; i++){
            if(num[i] != null){
                System.out.println(num[i]);
            }
        }
    }

    @Override
    public Iterator <Double> iterator() {
        return new IteratorDezReais(this);
    }
}

class IteratorDezReais implements Iterator{
    private int counter = 0;
    DezReais dr;

    public IteratorDezReais (){

    }

    public IteratorDezReais (DezReais d){
        this.dr = d;
    }

    @Override
    public boolean hasNext() {
        if(counter <= dr.size()){
            return true;
        }
        return false;
    }

    @Override
    public Object next() {
        return dr.get(counter++);
    }
}

public class Main {
    public static void main(String[] args) {
        DezReais n = new DezReais();
        IteratorDezReais it = new IteratorDezReais();
        int j;

        for (j = 0; j < 10; j++)
            n.add(j*0.1);

        while(it.hasNext()){
            System.out.println("Ultimo" + it.next());
        }

    }
}
