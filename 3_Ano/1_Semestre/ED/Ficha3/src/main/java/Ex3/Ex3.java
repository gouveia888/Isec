package Ex3;

abstract class Figura implements Comparable<Figura> {
    public abstract double area();

    @Override
    public int compareTo(Figura f) {
        return Double.compare(this.area(), f.area());
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


public class Ex3 {

    static double a(Figura f1, Figura f2){
        return f1.compareTo(f2);
    }

    static <T extends Comparable<? super Retangulo>> double b(Retangulo r1, T o){ //T extends a class Comparable de retangulo ou superior(Figura)
        return o.compareTo(r1);
    }

    static <T, U extends Comparable<? super T>> double c(T a, U b) {
        return b.compareTo(a);
    }

    public static void main(String[] args) {
        Retangulo r1 = new Retangulo(2,3);
        Retangulo r2 = new Retangulo(3,3);

        System.out.println(r1.area());
        System.out.println(r2.area());
        System.out.println(a(r1,r2));
        System.out.println(b(r1,r2));
        System.out.println(c(r1,r2));
    }
}
