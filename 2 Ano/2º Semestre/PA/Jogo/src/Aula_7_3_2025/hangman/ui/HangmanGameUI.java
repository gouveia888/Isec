package Aula_7_3_2025.hangman.ui;

import Aula_7_3_2025.hangman.model.*;
import java.util.Scanner;

public class HangmanGameUI {
    HangmanGameModel game;
    Scanner sc;
    public HangmanGameUI(HangmanGameModel game) {
        this.game = game;
    }

    public void play() {
        sc = new Scanner(System.in);

        while (!game.concluded()) {
            System.out.println("\nCurrent situation: " + game.getCurrentSituation());  // mostrar as letras descobertas
            // se a palavra for CAFE
            // inicialmente deverá mostrar:  ....
            System.out.println("Number of attempts: " + game.getnAttempts()); // inicio: 0
            System.out.printf("Errors: %d (máx.: %d)\n", game.getNErros(), HangmanGameModel.getMaxErrors());
            System.out.println("Attempted characteres: " + game.getAttemptCharacters());

            System.out.println(boneco(game.getNErros()));

            System.out.print("\nSuggestion: ");

            String option = sc.nextLine().trim();

            if (option.length()>0)
                game.tryOption(option);
        }
        System.out.println(boneco(game.getNErros()));
        if (game.isWordFound())
            System.out.printf("Congratulations! You guessed the word %s in %d attempts\n",
                    game.getWordToGuess(), game.getnAttempts());
        else
            System.out.println("Game over!!! The word to guess was: "+ game.getWordToGuess());
    };
    public String boneco(int x) {
        String base = " +----------+\n" +
                " |          |\n" +
                " |          " + (x >= 1 ? "O\n" : "\n") +
                " |       " + (x >= 4 ? "+--" : "") + (x == 2 ? "   +\n" : x > 2 ? "+" : "\n") + (x == 3 ? "--+\n" : x > 3 ? "--+\n" : "") +
                " |          " + (x >= 5 ? "|\n" : "\n") +
                " |         " + (x >= 6 ? "/" : " ") + (x >= 7 ? " \\\n" : "\n") +
                " |\n" +
                "===";

        return base;
    }
}