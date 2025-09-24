package pt.isec.pa.library.model;

import java.util.Comparator;
import java.util.List;

public class Book implements Cloneable, Comparable<Book> {
    private static int counter = 0;

    public static int getNewId() {
        return ++counter;
    }

    protected int id;
    private String title;
    protected List<String> authors;

    protected Book(String title, List<String> authors) { //DEIXA DE SER POSSIVEL CEIAR OBJETOS DO TIPO BOOK passando apenas a ser
        // possievel usar o oldbook e o recentbook sendo estes exxtençoes de book conseguindo aceder a todos os metedos e variaveis
        this.id = getNewId();
        this.title = title;
        this.authors = List.copyOf(authors);
    }

    //optional
    public Book(String title, String... authors) {
        this.id = getNewId();
        this.title = title;
        this.authors = List.of(authors);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return List.copyOf(authors);
    }

    public void setAuthors(List<String> authors) {
        this.authors = List.copyOf(authors);
    }

    @Override
    public String toString() {
        String strAuthors = authors.toString();
        return String.format("[%d] %s - %s",
                id,title,
                strAuthors.substring(1,strAuthors.length()-1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        //if (o == null || getClass() != o.getClass()) return false;
        //Book book = (Book) o;
        if(!(o instanceof Book book)) return false;
        return title.equalsIgnoreCase(book.title);
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.toUpperCase().hashCode() : 0;
        return result;
    }

    @Override
    protected Book clone() throws CloneNotSupportedException {
        Book newBook = (Book) super.clone();
        //newBook.id = getNewId(); // ????
        newBook.authors = List.copyOf(authors);
        return newBook;
    }

    @Override
    public int compareTo(Book o) {
        return title.toLowerCase().compareTo(o.title.toLowerCase());
        //return title.compareToIgnoreCase(o.title); igual a linha anterior por
        //return -title.compareToIgnoreCase(o.title); ordem inversa
        //id - o.getId(); //comparar atraves do id
    }

}

class BookComparator implements Comparator<Book> {
    @Override
    public int compare(Book o1, Book o2) {
        return o1.getTitle().compareTo(o2.getTitle());
    }
}
class BookComparatorIgnoreCase implements Comparator<Book> {
    @Override
    public int compare(Book o1, Book o2) {
        return o1.getTitle().compareToIgnoreCase(o2.getTitle());
        //o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase());
    }
}
