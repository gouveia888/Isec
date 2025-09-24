package pt.isec.pa.library.model;

import java.util.List;



public class OldBook extends Book{

    private String ISBN;
    private double price;
    private int nrCopies;

    OldBook(String title, List<String>authors, int NrCopies){
        super(title, authors); //passa os argumentos para o contrutor book
        this.nrCopies = NrCopies;
    }

    public int getNrCopies(){
        return nrCopies;
    }

    public void setNrCopies(int nrCopies){
        this.nrCopies = nrCopies;
    }

    @Override
    public String toString(){
        return "-OldBook" + super.toString() + ", #Copie=" + getNrCopies();
    }
}
