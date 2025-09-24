package Ex6;

public class Main {
    public static void main(String[] args){
        Ex6 matriz = new Ex6(1,3);
        matriz.preenche();
        System.out.println("Matriz original!");
        System.out.println(matriz.mostra());
        System.out.println("Matriz transposta!");
        matriz.transposta();
        System.out.println(matriz.mostra());
    }
}
