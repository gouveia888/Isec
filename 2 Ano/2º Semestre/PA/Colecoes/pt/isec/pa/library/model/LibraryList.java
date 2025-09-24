package pt.isec.pa.library.model;

import java.util.ArrayList;
import java.util.List;

public class LibraryList implements ILibrary {
    private String name;
    private List<Book> books;

    public LibraryList(String name) {
        this.name = name;
        books = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int addBook(String title, List<String> authors) {
        Book newBook = new Book(title,authors);
        if (books.contains(newBook))
            return -1;
        books.add(newBook);
        return newBook.getId();
    }

    @Override
    public Book findBook(String title) throws CloneNotSupportedException {
        Book tempBook = new Book(title,List.of());
        int index = books.indexOf(tempBook);
        if (index>=0)
            return books.get(index).clone();
        return null;
    }

    @Override
    public Book findBook(int id) throws CloneNotSupportedException {
        for(Book book : books)
            if (book.getId()==id)
                return book.clone();
        return null;
    }

    @Override
    public boolean removeBook(String title) {
        return books.remove(new Book(title,List.of()));
    }

    @Override
    public boolean removeBook(int id) {
        for(Book book : books)
            if (book.getId()==id)
                return books.remove(book);
        return false;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(String.format("Library %s:\n",name));
        for(Book book : books)
            output.append(String.format("  - %s\n",book));
        return output.toString();
    }
}
