package pt.isec.pa.library.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

abstract class Library implements ILibrary {
    protected String name;

    public Library(String name) {
        this.name = name;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public void setName(String name) {
        this.name = name;
    }

    abstract int addBook(Book book);

    @Override
    public int addOldBook(String title, List<String>authors, int nrCopies){
        return addBook(new OldBook(title,authors,nrCopies));
    }

    @Override
    public int addRecentBook(String title, List<String>authors, String isbn, double price){
        return addBook(new RecentBook(title,authors,price, isbn));
    }

    abstract Collection<Book> getBooks();

    @Override
    public Book findBook(int id) throws CloneNotSupportedException{
        Collection<Book> books = getBooks();
        for (Book book : books) {
            if(book.getId() == id)
                return book.clone();
        }
        return null;
    }
    @Override
    public boolean removeBook(String title) {
        Collection<Book> books = getBooks();
        return books.remove(new Book(title,List.of()));
    }

    @Override
    public boolean removeBook(int id) {
        Collection<Book> books = getBooks();
        for (Book book : books) {
            if (book.getId() == id)
                return books.remove(book);
        }
        return false;
    }

    @Override
    public String toString() {
        Collection<Book> books = getBooks();
        StringBuilder output = new StringBuilder();
        output.append(String.format("Library Name: %s\n", name));

        for (Book book : books)
            output.append(String.format("\t    - %s\n", book));

        return output.toString();
    }

    @Override
    public String toStringSorted() {
        StringBuilder sb = new StringBuilder(String.format("Library %s:\n",name));
        sb.append(String.format("Books:\n"));
        Collection books = getBooks();
        if(books.isEmpty() || books == null)
            sb.append("  - No Books Found");
        else{
            List<Book> orderList = new ArrayList<>(books); //books por getbooks();
            //Collections.sort(orderList); //necessita do Comparable
            Collections.sort(orderList, new BookComparator());
            for(Book book : orderList)
                sb.append(String.format("  - %s\n",book));
        }
        return sb.toString();
    }
}
