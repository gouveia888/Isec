package Ex1;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Impressora {
    private String modelo;
    private String marca;
    private String nome;
    PriorityQueue<Trabalho> trabalhos = new PriorityQueue<>(
        new Comparator<Trabalho>(){
            public int compare(Trabalho t1, Trabalho t2) {
                return Integer.compare(t1.getPgfinal() - t1.getPginicial(), t2.getPgfinal() - t2.getPginicial());
            }
        }
    );

    public Impressora(String modelo, String marca, String nome) {
        this.modelo = modelo;
        this.marca = marca;
        this.nome = nome;
    }

    public String getModelo() {
        return modelo;
    }
    public String getMarca() {
        return marca;
    }
    public String getNome() {
        return nome;
    }

    public void show(){
        System.out.println("Nome: " + nome + ", Marca: " + marca + ", Modelo: " + modelo);
    }

    public void adicionarTrabalho(Trabalho t){
        trabalhos.add(t);
    }

    public boolean temProximoTrabalho(){
        return !trabalhos.isEmpty();
    }

    public Trabalho proximoTrabalho(){
        return trabalhos.poll();
    }

    public int getNumTrabalhos(){
        return trabalhos.size();
    }
}
