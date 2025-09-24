package Ex7;

import static Ex7.Ex7.somas;

public class Main {
    public static void main(String[] args){
        Ex7 matriz1 = new Ex7(2);
        Ex7 matriz2 = new Ex7(2);
        matriz1.preenche();
        System.out.println(matriz1.mostra());
        matriz2.preenche();
        System.out.println(matriz2.mostra());
        System.out.println(matriz1.somam(matriz2));
        System.out.println(matriz1.mostra());

        int [][] matriz3 = {{1, 12}, {3, 4}};

        int [][] matriz4 = {{4, 3}, {2, 10}};

        int [][] matriz5 = new  int[matriz3.length][matriz3[0].length];
        matriz5 = somas(matriz3, matriz4);

        System.out.println("Matriz 5:");
        for (int i = 0; i < matriz3.length; i++) {
            for (int j = 0; j < matriz3[0].length; j++) {
                System.out.printf("%d ",matriz5[i][j]);
            }
            System.out.println();
        }
    }
}
