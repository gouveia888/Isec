package Ex2;

import java.util.Scanner;

public class Ex2 {
    public static void main(String[] args) {
        int num, max=100, min=0;
        String guess;
        System.out.println("Vou adivinhar o numero! \nS=Numero superior, I=numero inferior, A=acertou no numero");

        Scanner myObj = new Scanner(System.in);

        do{
            num = (int)(Math.random() * (max - min + 1)) + min;
            System.out.println("O seu número é: " + num);
            guess = myObj.nextLine();
            if(guess.equals("S")) {
                System.out.println("O numero é superior a " + num + "!");
                if(num > min)
                    min = num;
            }else if(guess.equals("I")) {
                System.out.println("O numero é inferior a " + num + "!");
                if(num < max)
                    max = num;
            }
        }while(!guess.equals("A"));
        System.out.println("Acertou no número!");
    }
}