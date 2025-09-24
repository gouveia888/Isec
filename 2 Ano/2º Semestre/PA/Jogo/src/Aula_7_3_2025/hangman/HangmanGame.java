package Aula_7_3_2025.hangman;

import Aula_7_3_2025.hangman.model.*;
import Aula_7_3_2025.hangman.ui.HangmanGameUI;

public class HangmanGame {
    public static void main(String args[]) {
        HangmanGameModel game = new HangmanGameModel();
        HangmanGameUI gameUI = new HangmanGameUI(game);
        gameUI.play();
    }
}
