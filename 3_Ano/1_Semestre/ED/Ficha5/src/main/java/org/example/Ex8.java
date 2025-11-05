package org.example;

import java.util.ArrayList;
import java.util.ListIterator;

public class Ex8 <T extends Comparable<T>>{
    private Ex3<T> dados = new Ex3<>(new ArrayList<T>());
    private Ex3<T> min;


    public void push(T num){
        if(min.empty() || num.compareTo((T)min.peek()) >= 0){
            min.push(num);
        }
        dados.push(num);
    }

    public T pop(){
        T ret = dados.pop();
        if(ret.compareTo((T)min.peek()) == 0){
            min.pop();
        }
        return ret;
    }

    public T findMin(){
        return (T) min.peek(); //O(1)
    }


}
