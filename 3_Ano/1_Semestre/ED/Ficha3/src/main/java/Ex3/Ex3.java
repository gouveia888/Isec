package Ex3;

abstract class Figura implements Comparable<Figura> {
    public abstract double area();

    @Override
    public int compareTo(Figura f) {
        double diff = this.area() - f.area();
        if (diff < 0)
            return -1;
        if(diff > 0)
            return 1;
        return 0;
    }
}

class Retangulo extends Figura {
    private double largura, altura;

    public Retangulo(double l, double a) {
        largura = l;
        altura = a;
    }

    public double area() {
        return largura * altura;
    }
}

class Quadrado extends Figura {
    private double lado;

    public Quadrado(double l) {
        lado = l;
    }

    public double area() {
        return Math.pow(lado, 2);
    }
}

abstract class X implements Comparable<Retangulo> {
    private double largura, altura;

    public X(double l, double a) {
        largura = l;
        altura = a;
    }
    public double area() {
        return largura * altura;
    }
}

public class Ex3 {

    static double a(Figura f1, Figura f2){
        return f1.compareTo(f2);
    }

    static <T extends Comparable<? super Retangulo>> double b(Retangulo r1, T o){ //T retangulo ou da mesma hierarquia que tenha a mesma super class
        return o.compareTo(r1);
    }

    static <T, U extends Comparable<? super T>> double c(T a, U b) {
        return b.compareTo(a);
    }
/*
    Comparable<Object> co = new Comparable<Object>() {
        @Override
        public int compareTo(Object o) {
            return 0;
        }
    };

    Comparable<Figura> cf = new Comparable<Figura>() {
        @Override
        public int compareTo(Object o) {
            return 0;
        }
    };

    Comparable<Retangulo> cr = new Comparable<Retangulo>() {
        @Override
        public int compareTo(Object o) {
            return 0;
        }
    };

    Dá erro de compilação
*/
    public static void main(String[] args) {
        Retangulo r1 = new Retangulo(2,3);
        Retangulo r2 = new Retangulo(3,3);
        Quadrado q2 = new Quadrado(3);

        System.out.println(a(r1,r2));
        System.out.println(b(r1,r2));
        System.out.println(c(r2,q2));
    }
}
