package Ex5;

import java.util.Arrays;

public class Aposta {
    private static final int TAM = 5;
    private int []nums; //1 a 49
    private int numsorte; // 1 a 13

    public Aposta() {
        nums = new int [TAM];
        numsorte = 0;
    }

    public void preenche(int num, boolean num_sorte) {

        if (num_sorte) { //num_sorte
            if(numsorte == 0)
                numsorte = num;
            return;
        }

        for (int i = 0; i < TAM; i++) {
            if(nums[i]==0){
                if(verifica(num)){
                    nums[i]=num;
                    break;
                }
            }
        }
    }
    public boolean verifica(int num) {
        for (int i = 0; i < TAM-1; i++) {
            if(nums[i]==num){
                return false;
            }
        }
        return true;
    }
    public boolean completa() {
        if(numsorte == 0){
            return false;
        }

        for (int i = 0; i < TAM; i++) {
            if(nums[i]==0){
                return false;
            }
        }
        return true;
    }
    public void boletim(){
        for (int i = 0; i < TAM; ) {
            int num = (int) (Math.random() * 49) + 1;
            if(verifica(num)){
                nums[i]=num;
                i++;
            }
        }
        numsorte = (int)(Math.random() * 13) + 1;
    }
    public String mostra(){
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < TAM; i++) {
            str.append(String.format("Número %d sorteado  %d\n", i+1,nums[i]));
        }
        str.append(String.format("Número da sorte sorteado  %d", numsorte));
        return str.toString();
    }
    public void reset(){
        Arrays.fill(nums, 0); // Preenche o array com 0
        numsorte = 0;
    }
    public void compara( int array[], int sorte){
        int certos=0;

        for (int i = 0; i < TAM; i++) {
            for(int j=0;j<array.length;j++){
                if(array[j]==nums[i]){
                    certos++;
                }
            }
        }
        if(sorte == numsorte && certos == TAM)
            System.out.println("Aposta certa!!");
        else
            System.out.println("Aposta certa!!");
    }
}
