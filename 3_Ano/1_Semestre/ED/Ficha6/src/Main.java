import Ex1.GestorImpressora;
import Ex1.Impressora;
import Ex1.Trabalho;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Impressora imp1 = new Impressora("X", "HP", "imp1");
        Impressora imp2 = new Impressora("Y", "Brother", "imp2");
        Impressora imp3 = new Impressora("X", "Cannon", "imp3");

        GestorImpressora gestor = new GestorImpressora();

        gestor.addImpressora(imp1);
        gestor.addImpressora(imp2);
        gestor.addImpressora(imp3);
        gestor.showImpressoras();
        gestor.removeImpressora("imp2");
        System.out.println("Impressora removida com sucesso");
        gestor.showImpressoras();
        System.out.println(gestor.getkey());
        Impressora m = gestor.getImpressora("imp3");
        m.show();
        if (gestor.existeImpressora("imp2"))
            System.out.println("A impressora imp2 existe");
        if(gestor.existeImpressora("imp3"))
            System.out.println("A impressora imp3 existe");

        Trabalho t1 = new Trabalho("Trabalho1", 1, 10);
        Trabalho t2 = new Trabalho("Trabalho2", 1, 20);
        imp1.adicionarTrabalho(t2);
        imp1.adicionarTrabalho(t1);
        imp1.
    }
}