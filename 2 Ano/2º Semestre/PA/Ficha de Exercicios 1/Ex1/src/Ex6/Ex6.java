package Ex6;

import java.util.Scanner;

public class Ex6 {

    private int linhas;
    private int colunas;
    private int[][] mat;

    public Ex6 (int linhas, int colunas){
        this.linhas = linhas;
        this.colunas = colunas;
        mat = new int[linhas][colunas];
    }

    public String mostra() {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                str.append(String.format("%d ",mat[i][j]));
            }
            str.append("\n");
        }
        return str.toString();
    }
    public void preenche(){
        Scanner sin = new Scanner(System.in);
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                System.out.print("Prencha a posição [" + i + j + "]");
                mat[i][j] = sin.nextInt();
            }
        }
    }
    public void transposta() {
        int[][] temp = new int[colunas][linhas]; // Inverte dimensões

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                temp[j][i] = mat[i][j]; // Troca linhas por colunas
            }
        }

        int tempValor = linhas;
        linhas = colunas;
        colunas = tempValor;
        mat = temp;

    }
}
