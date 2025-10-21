package org.example;

import java.util.List;
import java.util.ListIterator;

public class Ex3 <T>{
    private List<? super T> lista;

    public Ex3(List<? super T> lista) {
        //this.lista.clear();
        this.lista = lista;
    }

    public boolean empty(){
        return lista.isEmpty();
    }

    public <T> T peek(){
        ListIterator it = lista.listIterator(lista.size());
        return (T) it.previous();
    }

    public <T> T pop(){
        return (T) lista.remove(lista.size() - 1); //lista.getLast() dentro do lista.remove()
    }

    public void push(T value){
        lista.add(value);
    }

}
