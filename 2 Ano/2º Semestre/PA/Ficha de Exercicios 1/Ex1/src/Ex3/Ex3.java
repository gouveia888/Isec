package Ex3;

import java.util.Scanner;

public class Ex3 {
    public static void main(String[] args) {

        int tam, max, min;

        Scanner num = new Scanner(System.in);

        System.out.println("Digite o número de numeros: ");

        tam = num.nextInt();
        int [] t1 = new int [tam];

        for(int i=0;i<tam;i++){
            System.out.println("Insira numero na posição "+(i+1)+": ");
            t1[i] = num.nextInt();
        }

        max = t1[0];
        min = t1[0];

        for(int x : t1){
            System.out.print(x+" ");
            if(max < x)
                max = x;
            if(min > x)
                min = x;
        }
        System.out.println("Array invertido: ");
        for(int x : inverso(t1)){
            System.out.println(x);
        }
        System.out.println("\nO menor numero e o " + min  + " e o maior e o " + max);
        System.out.print("A soma dos numeros inseridos é " + somaArray(t1) + " a média dos numeros inseridos é " + media(t1));

    }

    private static int somaArray(int []t){
        int soma=0;
        for(int x : t){
            soma = soma + x;
        }
        return soma;
    }
    private static float media(int [] t){
        return ((float)somaArray(t)/t.length);
    }

    private static int[] inverso(int [] t){
        int [] aux = new int[t.length];
        for(int i=0, j=t.length-1;i<t.length;i++,j--){
            aux[i]=t[j];
        }
        return aux;
    }
}
