package Ex4;



public class Ex4 {

    static <T> boolean ex4(T []a, Comparable<? super T> b){
        for( var x : a)
            if(b.compareTo(x)>0) // x maior que b
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
