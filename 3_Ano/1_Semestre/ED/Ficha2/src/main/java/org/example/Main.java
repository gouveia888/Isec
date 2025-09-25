package org.example;

import java.util.Arrays;
import java.util.Random;

public class Main {

    static int[] criaArrayCom(
            int valor, //Numero que vai existir na tabela
            int dimensao,
            boolean diferentes) { //se for true a tabela nao tera repetidos
        int m[] = new int[dimensao];
        if (diferentes) {
            for (int i = 0; i < dimensao; i++)
                m[i] = i * 10;
            if ((valor % 10 != 0) || (0 > valor) || (valor > (dimensao - 1) * 10))
                m[0] = valor;
        } else {
            Random r = new Random();
            int gama = (Math.abs(valor) < 10) ? 10 : Math.abs(valor);
            for (int i = 0; i < dimensao; i++)
                m[i] = r.nextInt(gama * 4) - gama * 2;
            m[0] = valor;
        }
        Arrays.sort(m); //ordena a tabela
        return m;
    }

    static void imprime(int array[]) {
        for (int i = 0; i < array.length; i++)
            System.out.print(array[i] + " ");
    }

    static boolean ex1(int array[], int x) {
        int start = 0;
        int end = array.length;
        int meio = (start + end) / 2;
        if (array[meio] > x)
            end = meio - 1;
        else
            start = meio + 1;
        if (end < array.length)
            end++;
        return ex1(Arrays.copyOfRange(array, start, end), x);
    }

    static boolean ex2(int array[], int x) {
        int start = 0;
        int end = array.length - 1;
        int meio;

        while (end >= start) {
            meio = (start + end) / 2;
            if (array[meio] == x)
                return true;
            if (array[meio] < x)
                start = meio + 1;
            else
                end = meio - 1;
        }
        return false;
    }

    static int ex3(int array[], int x) {
        int start = 0;
        int end = array.length - 1;
        int meio;

        while (end >= start) {
            meio = (start + end) / 2;
            if (array[meio] == x)
                return meio;
            if (array[meio] < x)
                start = meio + 1;
            else
                end = meio - 1;
        }
        return -1;
    }

    static int ex4(int array[], int x) {
        int start = 0;
        int end = array.length - 1;
        int meio = 0;

        while (end >= start) {
            meio = (start + end) / 2;
            if (array[meio] == x)
                return meio;
            if (array[meio] < x)
                start = meio + 1;
            else
                end = meio - 1;
        }
        if (array[meio] < x)
            return -meio - 2;

        return -meio - 1;
    }

    static double ex5(int array[], int x) {

        int res = ex4(array, x);

        if (res >= 0)
            return (double) res / array.length;
        int posInsert = -res - 1;
        return posInsert / (double) array.length;

    }

    static int ex6(int array[], int inf, int sup) {
        int x1, x2;

        x1 = ex4(array, inf);
        x2 = ex4(array, sup);

        if (x1 < 0)
            x1 = -x1 - 1;
        if (x2 < 0)
            x2 = -x2 - 1;
        else
            x2++;
        return x2 - x1;
    }

    static boolean ex7(int array[], int x) {

        int pos = ex4(array, x);

        if (pos < 0)
            return false;

        if (pos == array.length - 1)
            if (array[pos - 1] == x)
                return true;
            else
                return false;

        if (pos == 0)
            if (array[pos + 1] == x)
                return true;

        if (array[pos - 1] == x || array[pos + 1] == x)
            return true;
        return false;
    }

    public static void main(String[] args) {

        int a[] = criaArrayCom(3, 10, true);
        int b[] = {3, 7, 12, 15};
        int c[] = {3, 3, 7, 12, 12, 15};

        /*if(ex1(a,10))
            System.out.println("O numero existe");
        System.out.println("O numero nao existe");


        if(ex2(a,10))
            System.out.println("O numero existe");
        System.out.println("O numero nao existe");

        int res = ex3(a,10);
        System.out.println("Numero na posicao " + res);

        System.out.println(ex4(b,15));
        System.out.println(ex4(b,3));
        System.out.println(ex4(b,1));
        System.out.println(ex4(b,4));
        System.out.println(ex4(b,10));
        System.out.println(ex4(b,13));
        System.out.println(ex4(b,16));


        System.out.println(ex5(b,15));
        System.out.println(ex5(b,14));
        System.out.println(ex5(b,3));
        System.out.println(ex5(b,1));
        System.out.println(ex5(b,100));


        System.out.println(ex6(b,0,15));
        System.out.println(ex6(b,3,7));
        System.out.println(ex6(b,4,14));
        System.out.println(ex6(b,4,5));
        System.out.println(ex6(b,0,100));
        */

        System.out.println(ex7(c, 15));
        System.out.println(ex7(c, 14));
        System.out.println(ex7(c, 12));
        System.out.println(ex7(c, 3));

    }


}