package Ex7;

import java.util.Scanner;

public class Ex7 {
    //this(other.m.lenght, other.m[0].lenght) chama o construtor da public
    //system.arraycopy
    //Arrays.copyof
    //printf("%-8.2f") alinhamento a esquerda
    // printf("%8.2f") alinhamento a direita
    private int tam=0;
    private int [][]mat;

    public Ex7(int t){
        tam = t;
        mat = new int [tam][tam];
    }

    public int getTam(){
        return tam;
    }

    public void preenche(){
        Scanner sin = new Scanner(System.in);
        for (int i = 0; i < getTam(); i++) {
            for (int j = 0; j < getTam(); j++) {
                System.out.print("Prencha a posição [" + i + j + "]");
                mat[i][j] = sin.nextInt();
            }
        }
    }

    public String somam(Ex7 m2){
        StringBuilder str = new StringBuilder();
        if(getTam()==m2.getTam()){
            for(int i=0;i<getTam();i++){
                for(int j=0;j<getTam();j++){
                    mat[i][j]+=m2.mat[i][j];
                }
            }
            str.append("Soma efetuada com sucesso");
            return str.toString();
        }
        str.append("Matrizes tem de ter o mesmo tamanho");
        return str.toString();
    }

    public String mostra() {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < getTam(); i++) {
            for (int j = 0; j < getTam(); j++) {
                str.append(String.format("%d ",mat[i][j]));
            }
            str.append("\n");
        }
        return str.toString();
    }

    public static int[][] somas(int [][]m1,int [][]m2) {
        if(m1.length == m2.length || m1[0].length == m2[0].length){
            int[][] res = new int[m1.length][m1[0].length];
            for(int i=0;i<m1.length;i++){
                for(int j=0;j<m1[0].length;j++){
                    res[i][j]=m1[i][j]+m2[i][j];
                }
            }
            return res;
        }else {
            for (int i = 0; i < m1.length; i++) {
                for (int j = 0; j < m1[i].length; j++) {
                    m1[i][j] = 0;
                }
            }
            return m1;
        }
    }
}