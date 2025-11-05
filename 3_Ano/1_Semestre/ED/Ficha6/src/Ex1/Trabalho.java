package Ex1;

public class Trabalho {
    private String nome;
    private int pginicial;
    private int pgfinal;

    public Trabalho(String nome, int pginicial, int pgfinal) {
        this.nome = nome;
        this.pginicial = pginicial;
        this.pgfinal = pgfinal;
    }

    public String getNome() {
        return nome;
    }
    public int getPginicial() {
        return pginicial;
    }
    public int getPgfinal() {
        return pgfinal;
    }
}
