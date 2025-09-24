package Aula_7_3_2025.report.model;

import java.util.Scanner;
import java.util.StringTokenizer;

public class Report {

    private static final int INC_AUTHORS = 5;

    String title;
    String [] authors;
    int qtAuthors;

    StringBuilder text;

    public Report(String title){
        this.title = title;
        this.text = new StringBuilder();
        this.authors = new String[INC_AUTHORS];
        this.qtAuthors = 0;
    }

    public String getTitle() {
        return title;
    }

    public String[] getAuthors() {
        return authors;
    }

    public int getQtAuthors() {
        return qtAuthors;
    }

    public StringBuilder getText() {
        return text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public void setQtAuthors(int qtAuthors) {
        this.qtAuthors = qtAuthors;
    }

    public void setText(StringBuilder text) {
        this.text = text;
    }

    public boolean addAuthor(String author){

        for(int i = 0; i < qtAuthors; i++){
            if(authors[i].equalsIgnoreCase(author))
                return false;
        }

        if(qtAuthors >= INC_AUTHORS){
            String [] newAuthors = new String[qtAuthors+INC_AUTHORS];
            System.arraycopy(authors, 0, newAuthors, 0, authors.length);
            authors = newAuthors;

            //authors = Arrays.copyof(authors, authores.lenght+INC_AUTHORS)
        }
        authors[qtAuthors++] = author;
        return true;
    }

    public boolean removeAuthor(String author){
        for(int i = 0; i < qtAuthors; i++){
            if(authors[i].equalsIgnoreCase(author)){
                for(int j = i; j < qtAuthors; j++){
                    authors[j] = authors[j+1];
                }
                qtAuthors--;
                authors[qtAuthors] = null;
                return true;
            }
        }
        return false;
    }

    public void addText(String newText){
        if(text == null){
            text = new StringBuilder(newText);
        }else{
            text.append(newText);
        }
    }

    public void capitalizeSentences(){
        boolean changeNext = true;

        for(int i = 0; i < text.length(); i++){
            char c = text.charAt(i);
            if(changeNext && Character.isLetter(c)){
                text.setCharAt(i, Character.toUpperCase(c));
                changeNext = false;
            }else if(".!?".indexOf(c) >= 0){ // != -1
                changeNext = true;
            }
        }
    }

    public int getNumberOfWords(){
        int counter=0;
        Scanner stext = new Scanner(text.toString());
        stext.useDelimiter("[\\s,.!?]+");
        while(stext.hasNext()){
            counter++;
            stext.next();
        }
        return counter;

        //versao 2
        String [] words = text.toString().split("[\\s,.!?]+");
        return words.length;

        //versao 3
        StringTokenizer st = new StringTokenizer(text.toString(),"[\\s,.!?]+");
        return st.countTokens();
    }

    public int getNumberofOccurences(String word){
        int counter = 0;
        StringTokenizer st = new StringTokenizer(text.toString(), "[\\s,.!=+]");
        while(st.hasMoreTokens()){
            if(word.equalsIgnoreCase(st.nextToken())){
                counter++;
            }
        }
        return counter;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Report\n");
        sb.append(String.format("Title: %s\n", title));
    }
}
