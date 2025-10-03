package org.example;

public class Main {

    public static <T,E extends T> boolean ex1(T[] array, E x) {
        int cont = 0;
        for (T a : array) {
            if (a == x) {
                cont++;
            }
            if(cont >1)
                return true;
        }
        return false;
    }

    public static <T extends Comparable > boolean ex2(T[] array, T x) {
        int cont = 0;
        for (T a : array) {
            if (a.compareTo(x) == 0) {
                cont++;
            }
            if(cont >1)
                return true;
        }
        return false;
    }



    public static void main(String[] args) {

        Integer array[] = {1, 2, 3, 4, 5, 5,6, 7, 8, 9, 10};
        Double d = 5.0;
        Float f = 5.0f;

        System.out.println(ex1(array, 5));
        System.out.println(ex2(array, 5));
    }

}