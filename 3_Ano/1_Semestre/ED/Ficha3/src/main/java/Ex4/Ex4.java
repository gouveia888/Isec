package Ex4;



public class Ex4 {

    static <T extends Comparable<T>>boolean ex4(T []a, T b){
        for( var x : a)
            if(x.compareTo(b)>0) // x maior que b
                return true;
        return false;
    }

    public static void main(String[] args) {
        Integer m[]={3,2,6,3};
        String n[]={"Ada", "Albino"};
        System.out.println(ex4(m,2));   //true
        System.out.println(ex4(n,"Francisco"));   //false
    }

}
