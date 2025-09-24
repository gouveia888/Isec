package Ex9;

public class Triangulo {

    int[][] mat;

    public Triangulo(int p){
        mat = new int[p][];
        for (int i = 0; i < p; i++) {
            mat[i]= new int[i+1];
            mat[i][0] = mat[i][i] = 1;
            for(int j = 1; j < i; j++)
                mat[i][j] = mat[i-1][j-1] + mat[i-1][j];
        }
    }

    public String mostra(boolean align) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mat.length; i++) {
            if(align){
                sb.append(" ".repeat(((mat.length -1 ) * 6 - i * 6) / 2 ));
            }
            for (int j = 0; j < mat[i].length; j++) {
                sb.append(String.format("%6d ", mat[i][j]));
            }
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

    public void imprime(String t) {
        System.out.print(t);
    }
}