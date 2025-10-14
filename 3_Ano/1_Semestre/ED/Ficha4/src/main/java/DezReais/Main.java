package DezReais;

import java.util.Iterator;
import java.util.NoSuchElementException;

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
        if(counter < dr.size()){
            return true;
        }
        return false;
    }

    @Override
    public Object next() {
        if(!hasNext()) throw new NoSuchElementException();
        return dr.get(counter++);
    }

    public Object previous() {
        if(counter-1 < 0) throw new NoSuchElementException();
        return dr.get(--counter);
    }
}

public class Main {
        //ex7
    public Double maiorNum (Iterable <Double> m){
        Iterator<Double> it = m.iterator();
        Double maior, temp;
        maior = it.next();
        while(it.hasNext()){
            temp = it.next();
            if(temp > maior)
                maior = temp;
        }
    return maior;
    }

    public <T extends Comparable <? super T>> T maiorNumGen (Iterable <T> m){
        Iterator<T> it = m.iterator();
        T maior, temp;
        maior = it.next();
        while(it.hasNext()){
            temp = it.next();
            if(temp.compareTo(maior) > 0) //compareTo devido a estarmos a comparar objetos
                maior = temp;
        }
        return maior;
    }

    public static void main(String[] args) {
        DezReais n = new DezReais();
        DezReaisMutavel nm = new DezReaisMutavel();
        IteratorDezReais it = new IteratorDezReais(n);
        IteradorDezReaisMutavel ipm = new IteradorDezReaisMutavel(nm);
        int j;

        for (j = 0; j < 9; j++) { //com 10 vamos ver a exceção
            n.add(j * 0.1);
            nm.add(j * 0.2);
        }
        while(it.hasNext()){
            System.out.println("DezReais " + it.next()); // + "\nDezReaisMutavel " + ipm.next()
        }

        while(ipm.hasNext()){
            System.out.println("DezReaisMutavel " + ipm.next());
        }
        System.out.println("Apos remoção de DezReaisMutavel ");

        ipm = new IteradorDezReaisMutavel(nm);  //reiniciando o iterador
        ipm.next();
        ipm.next();
        ipm.remove();
        while(ipm.hasNext()){
            System.out.println("DezReaisMutavel " + ipm.next());
        }

    }
}
