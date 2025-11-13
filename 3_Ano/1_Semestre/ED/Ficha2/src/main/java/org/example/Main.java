package org.example;

import java.util.*;

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
        System.out.println();
    }

    static boolean ex1(int []array, int a){

        int start = 0;
        int end = array.length - 1;
        int meio = (start + end) / 2;

        if(end < start)
            return false;
        if (array[meio] == a)
            return true;
        if (array[meio] > a)
            end = meio - 1;
        if (array[meio] < a)
            start = meio + 1;

        return ex1(Arrays.copyOfRange(array, start, end), a);
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

    static int ex3(int array[]) {
        int start = 0;
        int end = array.length - 1;
        int meio;

        if(array[0]>0)
            return 0;

        while (end >= start) {
            meio = (start + end) / 2;
            if (array[meio] > meio)
                return meio;
            if (array[meio] < meio)
                start = meio + 1;
            else
                end = meio; //pode encontrar um valor igual ao indice
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
        if (array[meio] > x)
            return -meio - 2; //x deveria ser inserido à direita daquele elemento

        return -meio - 1; //x deveria ser inserido antes daquele elemento
    }

    static double ex5(int array[], int x) {

        int pos = ex4(array, x);

        if (pos >= 0)
            return (double) pos / array.length;
        return (-pos - 1) / (double) array.length; //como nao existe no array converter para positivo e remover  a posiçao de 0

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
            x2++;  //caso o limite superior seja igual a um numero deve inclui-lo
        return x2 - x1;
    }

    static boolean ex7(int array[], int x) {

        int pos = ex4(array, x);

        if (pos < 0)
            return false;

        if (pos == array.length - 1)//se for a ultima posiçao
            if (array[pos - 1] == x) //verifica com o penultimo numero
                return true;
            else
                return false;

        if (pos == 0) //sendo o primeiro numero
            if (array[pos + 1] == x) //verifica com o 2 numero
                return true;

        if (array[pos - 1] == x || array[pos + 1] == x)
            return true;
        return false;
    }

    static int ex8(int array[], int x) {
        int pos = ex4(array, x);
        if(array[0] > x)
            return x;
        if(pos > 0)
            return array[pos-1];
        if(pos==0)
            return array[0];
        return array[-pos-2];
    }

    static int ex9(int array[], int x) {
        int start = 0;
        int end = array.length - 1;
        int meio = (start + end) / 2;
        int pos;

        while (start < array[meio]) {
            meio = (start + end) / 2;
            if (array[meio] < 0) {
                end = meio - 1;
            } else {
                start = meio + 1;
            }
        }

        if (x < 0) {
            pos = ex4(Arrays.copyOfRange(array, meio, array.length), x);
            if (pos >= 0)
                return meio + pos; // corrigir índice para o array original
        } else {
            pos = ex4(Arrays.copyOfRange(array, 0, meio), x);
            if (pos >= 0)
                return pos; //nao precisa de correçao de indice
        }
        return -1;
    }

    static int ex9_v2(int array[], int x) {
        int start = 0;
        int end = array.length - 1;
        int meio = (start + end) / 2;
        int pos;

        while (end >= start) {
            meio = (start + end) / 2;
            if (array[meio] == x)
                return meio;
            if (array[meio] * x >=0) //meio e chave tem sinais iguais
                if(array[meio] < x)
                    end = meio + 1;
                else
                    start = meio - 1;
            else //meio e chave tem sinais contrario
                if(array[meio] < x)
                    end = meio - 1;
                else
                    start = meio + 1;
        }
        return -1;
    }

    public static int ex10(int array[]){

        return ex3(array); //versao adaptada do ex3
    }

    static int ex10_v2(int array[]) {
        int start = 0;
        int end = array.length - 1;
        int meio=(start + end) / 2;

        do {
            if (array[meio] > meio)
                end = meio;
            else
                start = meio + 1;
            meio = (start + end) / 2;
        }while (start < end);

        if (array[meio] > meio)
            return  meio;
        else
            return-1;
    }

    public static void main(String[] args) {

        int a[] = criaArrayCom(3, 10, true);
        int b[] = {3, 7, 12, 15};
        int c[] = {3, 3, 7, 12, 12, 15};
        int d[] = {3,6,8, -10,-3,-2,-1};
        int e[] =  {-3,1,7,12,15};
        int f[] =  {-15,-14,1,2,3,4};

      /*  if(ex1(a,60))
            System.out.println("O numero existe");
        else
            System.out.println("O numero nao existe");

        if(ex2(a,10))
            System.out.println("O numero existe");
        else
            System.out.println("O numero nao existe");


        int res = ex3(a,30);
        System.out.println("Numero na posicao " + res);

*/
        System.out.println(ex4(b,15));
        System.out.println(ex4(b,3));
        System.out.println(ex4(b,1));
        System.out.println(ex4(b,4));
        System.out.println(ex4(b,10));
        System.out.println(ex4(b,13));
        System.out.println(ex4(b,16));

/*
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


        System.out.println(ex7(c, 15));
        System.out.println(ex7(c, 14));
        System.out.println(ex7(c, 12));
        System.out.println(ex7(c, 3));

        System.out.println(ex8(b, 15));
        System.out.println(ex8(b, 14));
        System.out.println(ex8(b, 3));
        System.out.println(ex8(b, 1));
        System.out.println(ex8(b, 100));

        System.out.println("Ex9\n" + ex9(d, 0));
        System.out.println(ex9(d, 3));
        System.out.println(ex9(d, 8));
        System.out.println(ex9(d, 9));
        System.out.println(ex9(d, -10));
        System.out.println(ex9(d, -1));
        System.out.println(ex9(d, -15));
*/
        System.out.println("Ex10\n" + ex10(d));
        System.out.println(ex10(e));
        System.out.println(ex10(f));
    }

    public class MinhaString implements Iterable<Character>{
        String s;
        public MinhaString(String s){
            this.s = s;
        }
        public Iterator<Character> iterator() {
            return new ItMinhaString(this);
        }
        public char getChar(int index){
            return s.charAt(index);
        }

        public int getLength(){
            return s.length();
        }
    }

    public class ItMinhaString implements Iterator<Character>{
        MinhaString m;
        int size, pos =0;
        boolean duplicado = false;

        public ItMinhaString(MinhaString ms){
            this.m = ms;
            this.size = ms.getLength();
        }

        public boolean hasNext(){
            return pos < size;
        }

        public Character next(){
            if(pos==size)
                throw new NoSuchElementException();

            Character c = m.getChar(pos++);
            if(c == ' ') {
                if (!duplicado) {
                    duplicado = true;
                    return c;
                }
                while(c == ' ' && pos < size) {
                    c = m.getChar(pos++);
                }
                if (pos > size) throw new NoSuchElementException();
                return m.getChar(pos++);
            }
            duplicado = false;
            return c;
        }
    }

}