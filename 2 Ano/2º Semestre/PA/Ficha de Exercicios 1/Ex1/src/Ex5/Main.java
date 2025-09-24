package Ex5;

public class Main {
    public static void main(String[] args) {
        Aposta bet = new Aposta();

        bet.preenche(34, false);
        bet.preenche(12, false);
        bet.preenche(23, false);
        System.out.println("Aposta: " + bet.completa());
        bet.preenche(9, false);
        bet.preenche(42, false);
        bet.preenche(5, true);
        System.out.println(bet.mostra());
        bet.reset();
        System.out.println(bet.mostra());
        bet.boletim();
        System.out.println(bet.mostra());
        //bet.completa();
    }
}
