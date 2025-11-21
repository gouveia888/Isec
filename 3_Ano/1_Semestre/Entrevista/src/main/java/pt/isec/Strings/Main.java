package pt.isec.Strings;

public class Main {

    public static boolean palindromo(String p){
        int tam = p.length();
        for( int i=0 ; i<tam/2 ; i++ ){
            if(p.charAt(i)!=p.charAt(tam-i-1)){
                return false;
            }
        }
        return true;
    }

    public static int procura(String texto, String pal){
        String [] pals = texto.trim().split(" ");
        int count = 0;

        for(String p : pals)
            if(p.equals(pal))
               count++;
        return count;
    }

    public static String invertepalavras(String frase, String x) {
        String[] palavras = frase.trim().split("\\s+");
        StringBuilder fraseInvertida = new StringBuilder();

        char charBusca = x.charAt(0);

        for (String palavra : palavras) {
            StringBuilder pInv = new StringBuilder();
            for (int i = palavra.length()-1; i>=0; i--) {
                char c = palavra.charAt(i);
                if (c == charBusca) {
                    pInv.append('@');
                } else {
                    pInv.append(c);
                }
            }
            fraseInvertida.append(pInv);
            fraseInvertida.append(" ");
        }

        return fraseInvertida.toString().trim();
    }

    public static String inverteFrase(String frase){
        String [] f = frase.trim().split(" ");
        StringBuilder fraseInvertida = new StringBuilder();

        for(int i = f.length-1; i>=0; i--) {
            fraseInvertida.append(f[i]);
            fraseInvertida.append(" ");
        }

        return fraseInvertida.toString();
    }

    static void main(){
        System.out.println("Palindromo");
        System.out.println(palindromo("ovo"));
        System.out.println(palindromo("rato"));
        System.out.println(procura("O rato roeu a rolha do rei de roma em roma","roma"));
        System.out.println(invertepalavras("O rato roeu a rolha do rei de roma em roma","r"));
        System.out.println(inverteFrase("O rato roeu a rolha do rei de roma em roma"));

    }
}
