package Ex8;

public class Main {
    public static void main(String[] args) {

        Ex8 obj = new Ex8(2,3);
        obj.preenche();
        System.out.println(obj.mostra());
        System.out.println("A soma da matriz é " + obj.soma());
        obj.somaColunas();
        obj.somaLinhas();
    }
}
