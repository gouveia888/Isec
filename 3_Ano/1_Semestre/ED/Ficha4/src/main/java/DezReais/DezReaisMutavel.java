package org.example;

import java.util.Iterator;

class DezReaisMutavel implements Iterable<Double>{
    private final int MAX = 10;
    private Double[] num;
    protected int last = 0;

    public DezReaisMutavel(){
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

    public boolean remover(Double n){

        for(int i = 0; i < last; i++){
            if (num[i].equals(n)) { //estamos a usar equals para comparar valores de objetos Double
                num[i] = num[last-1];
                num[last-1] = null;
                last--;
                return true;
            }
        }
        return false;
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
        return new IteradorDezReaisMutavel(this);
    }
}

class IteradorDezReaisMutavel implements Iterator{
    DezReaisMutavel m;
    private int counter = 0;

    public IteradorDezReaisMutavel (){

    }

    public IteradorDezReaisMutavel (DezReaisMutavel n){
        this.m=n;
    }

    @Override
    public boolean hasNext() {
        if(counter < m.size()){
            return true;
        }
        return false;
    }
    @Override
    public Object next() {
        return m.get(counter++);
    }
}

