package Ex8;

import java.util.Scanner;

public class Ex8 {

    private int linhas;
    private int colunas;
    private int[][] mat;

    public Ex8 (int linhas, int colunas){
        this.linhas = linhas;
        this.colunas = colunas;
        mat = new int[linhas][colunas];
    }

    public int  getLinhas() {
        return linhas;
    }
    public int getColunas() {
        return colunas;
    }

    public String mostra() {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < getLinhas(); i++) {
            for (int j = 0; j < getColunas(); j++) {
                str.append(String.format("%d ",mat[i][j]));
            }
            str.append("\n");
        }
        return str.toString();
    }

    public void preenche(){
        Scanner sin = new Scanner(System.in);
        for (int i = 0; i < getLinhas(); i++) {
            for (int j = 0; j < getColunas(); j++) {
                System.out.print("Prencha a posição [" + i + j + "]");
                mat[i][j] = sin.nextInt();
            }
        }
    }

    public int soma(){
        int soma = 0;
        for(int i=0;i<getLinhas();i++){
            for(int j=0;j<getColunas();j++){
                soma+=mat[i][j];
            }
        }
        return soma;
    }

    public void somaLinhas(){
        int soma;
        for(int i=0;i<getLinhas();i++){
            soma = 0;
            for(int j=0;j<getColunas();j++){
                soma+=mat[i][j];
            }
            System.out.println("Soma da linha " + (i+1) + " = " + soma);
        }
    }

    public void somaColunas(){
        int soma;
        for(int i=0;i<getColunas();i++){
            soma = 0;
            for(int j=0;j< getLinhas();j++){
                soma+=mat[j][i];
            }
            System.out.println("Soma da coluna " + (i+1) + " = " + soma);
        }
    }
}