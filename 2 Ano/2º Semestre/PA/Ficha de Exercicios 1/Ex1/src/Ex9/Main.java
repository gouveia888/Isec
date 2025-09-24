package Ex9;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int num;
        Scanner Sin = new Scanner(System.in);
        System.out.println("Digite um numero: ");
        num = Sin.nextInt();
        boolean align;
        Triangulo obj = new Triangulo(num);
        align = false;
        obj.imprime(obj.mostra(align));
        align = true;
        obj.imprime(obj.mostra(align));
    }
}
