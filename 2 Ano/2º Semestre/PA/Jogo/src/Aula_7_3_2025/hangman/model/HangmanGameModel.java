package Aula_7_3_2025.hangman.model;


//class Character tem metedos estaticos para mnipular caracters

public class HangmanGameModel {

        private static final int MAX_ERRORS = 7;
        private static final String HIDDEN_CHARACTER = "_";

        private String wordToGuess;
        private StringBuffer currentSituation;
        private StringBuilder attemptCharacters;
        private int nAttempts, nAttemptsSucceded;

        public HangmanGameModel() {
            initialize();
        }
        private void initialize() {
            int i = (int) (Math.random() * HangmanGameDictionary.getNumWords());
            wordToGuess = HangmanGameDictionary.getWord(i).toUpperCase();
            currentSituation = new StringBuffer(HIDDEN_CHARACTER.repeat(wordToGuess.length())); //StringBuffer conseguimos editar a string???
            attemptCharacters = new StringBuilder();
            nAttempts = nAttemptsSucceded = 0;
        }
        public boolean isWordFound(){
            return wordToGuess.equalsIgnoreCase(currentSituation.toString());
        }
        public boolean concluded(){
            return isWordFound() || getNErros() >= MAX_ERRORS;
        }
        public String getCurrentSituation() {
            return currentSituation.toString();
        }
        public String getAttemptCharacters() {
            return attemptCharacters.toString();
        }
        public String getWordToGuess() {
            return wordToGuess;
        }
        public static int getMaxErrors(){
            return MAX_ERRORS;
        }
        public int getnAttempts(){
            return nAttempts;
        }
        public int getNErros() {
            return nAttempts- nAttemptsSucceded;
        }
        public boolean tryOption(String option) {
            if(option == null || concluded() || option.isBlank()) //isEmpty == option.lenght() == 0 o isBlank() == option.trim().lenght() == 0
                return false;

            if(attemptCharacters.indexOf(option.toUpperCase()) >= 0) // se a letra ja se encontra nas tentativas (se for maior ou igual que 0) nao repete
                return false;

            nAttempts++;
            option = option.toUpperCase();

            if (option.length() > 1){ //esta a tentar adivinhar a palavra
                if(wordToGuess.equalsIgnoreCase(option)){
                    currentSituation = new StringBuffer(wordToGuess);
                    nAttemptsSucceded++;
                    return true;
                }
                return false;
            }

            char op = Character.toUpperCase(option.charAt(0));
            attemptCharacters.append(op);

            boolean found = false;
            for(int i = 0; i < wordToGuess.length(); i++){
                char c = wordToGuess.charAt(i); //cast do caracter da string para caracter
                if(c == op){
                    currentSituation.setCharAt(i,c);
                    found = true;
                }
            }
            if(found){
                nAttemptsSucceded++;
            }
            return found;
        }
}
