package org.example;

import java.util.List;
import java.util.ListIterator;

public class Ex4 <T>{
    private List<? super T> lista;

    public Ex4(List<? super T> lista) {
        //this.lista.clear();
        this.lista = lista;
    }

    public boolean empty(){
        return lista.isEmpty();
    }

    public <T> T remove(){
        return (T) lista.remove(0);
    }

    public <T> T element(){
        ListIterator it = lista.listIterator();
        return (T) it.next();
    }

    public void add(T value){
        lista.add(value);
    }

}