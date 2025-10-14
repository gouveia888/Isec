package DezReais;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

class ItDezReaisMutaveisPos implements Iterator<Double> {
    private int posultimo = -1;
    private  DezReaisMutavel dezReais;
    private  boolean flag = false;
    private  int ctr = 0;


    public ItDezReaisMutaveisPos(DezReaisMutavel dezReais) {
        this.dezReais = dezReais;
        this.ctr = dezReais.alteracoes;
    }


    private int nextPos(int from){
        if(from >= dezReais.size())return -1;
        while(dezReais.get(from) <0)
            if(++from >= dezReais.size())return -1;
        return from;
    }


    @Override
    public boolean hasNext() {
        return nextPos(posultimo + 1) >= 0;
    }

    @Override
    public Double next() {
        posultimo = nextPos(++posultimo);
        return dezReais.get(posultimo);
    }

    @Override
    public void remove() {
        valida();
        if (!flag) {
            throw new IllegalStateException();
        }
        dezReais.remover(posultimo);
        flag = false;
        posultimo--;
        ctr++;
    }

    public void valida() {
        if(ctr != dezReais.alteracoes) {
            throw new ConcurrentModificationException();
        }
    }
}