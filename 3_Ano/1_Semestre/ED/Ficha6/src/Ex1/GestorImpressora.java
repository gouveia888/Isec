package Ex1;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Set;

public class GestorImpressora {
    private HashMap<String, Impressora> impressoras;
    PriorityQueue<Trabalho> trabalhos;

    public GestorImpressora() {
        this.impressoras = new HashMap<>();
    }

    //ex1_c
    public void addImpressora(Impressora a){
        impressoras.put(a.getNome(),a);
    }

    public Impressora getImpressora(String nome){
        return impressoras.get(nome);

    }
    //ex1_d
    public void removeImpressora(String nome){
        impressoras.remove(nome);
    }

    public void showImpressoras(){
        for(Impressora imp : impressoras.values()){
            imp.show();
        }
    }

    //Ex1_b
    public Set<String> getkey(){
        return impressoras.keySet();
    }

    //ex1_e
    public boolean existeImpressora(String nome){
        return impressoras.containsKey(nome);
    }


}
