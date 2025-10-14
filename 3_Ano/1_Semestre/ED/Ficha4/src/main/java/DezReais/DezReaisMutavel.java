package DezReais;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DezReaisMutavel implements Iterable<Double> {
    private final int MAX = 10;
    private Double[] num;
    protected int last = 0;
    int alteracoes = 0; //ideal seria private com metedo get


    public DezReaisMutavel() {
        num = new Double[MAX];
    }

    public Double get(int i) {
        return num[i];
    }

    public int size() {
        return last;
    }

    public boolean add(Double n) {
        if (size() >= MAX) throw new RuntimeException();
        num[last++] = n;
        alteracoes++;
        return true;
    }

    void remover (int pos) {
        int i=0;
        for(i=pos; i<last-1; i++){
            num[i] = num[i+1];
        }
        if(last>-1)
            last--;
        alteracoes++;
    }

    public void mostra() {
        for (int i = 0; i < num.length; i++) {
            if (num[i] != null) {
                System.out.println(num[i]);
            }
        }
    }

    public int nextPos(int pos){
        if(pos < 0 || pos >= last) throw new IndexOutOfBoundsException();
        while(num[pos] < 0){
            pos++;
            if (pos >= last)
                return -1;
        }
        return pos;
    }

    @Override
    public Iterator<Double> iterator() {

        //return new IteradorDezReaisMutavel(this);
        return new ItDezReaisMutaveisPos(this); //falta testar
    }

}
    class IteradorDezReaisMutavel implements Iterator{
        DezReaisMutavel m;
        private int pos = 0;
        boolean poderemover = false;
        int alteracoes = 0;
        public IteradorDezReaisMutavel (){

        }

        public IteradorDezReaisMutavel (DezReaisMutavel n){
            this.m=n;
            this.alteracoes = n.alteracoes;
        }

        @Override
        public boolean hasNext() {
            if(m.alteracoes != alteracoes)
                throw new ConcurrentModificationException(); //pode estar num metedo
            if(pos <= m.size()){
                return true;
            }
            return false;
        }

        @Override
        public Object next() {
            if(!hasNext()) throw new NoSuchElementException();
            poderemover = true;
            return m.get(pos++);
        }

        @Override
        public void remove() {
            if(!poderemover)
                throw new IllegalStateException();           poderemover = false;
            m.remover(pos); // remove the last element returned by next()
            pos--;
            alteracoes++;
        }
    }


