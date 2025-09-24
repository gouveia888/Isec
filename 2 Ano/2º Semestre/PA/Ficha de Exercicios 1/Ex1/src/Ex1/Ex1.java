package Ex1;

import java.util.Scanner;

public class Ex1 {
    public static void main(String[] args) {
        int num = (int)(Math.random() * 100 + 1);
        int guess;
        System.out.println("Adivinhe o numero!");

        Scanner myObj = new Scanner(System.in);

        do{
            guess = myObj.nextInt();
            if(num == guess) {
                System.out.println("Acertou o numero!");
            }else if(num < guess) {
                System.out.println("O numero é inferior!");
            }else{
                System.out.println("O numero é superior!");
            }
        }while(guess != num);
    }
}