package pt.isec.pa.library.model;

import java.util.*;
//Quando inserimos um objeto com a mesma KEY se esta ja existir ele substitui o atual e retorna o objeto que estava la anteriormente
public class LibraryMap extends Library {
    private final Map<Integer,Book> books;

    public LibraryMap(String name) {
        super(name);
        books = new HashMap<>();
    }

    @Override
    public int addBook(Book newBook) {
        if (books.containsValue(newBook))
            return -1;
        books.put(newBook.getId(), newBook);
        return newBook.getId();
    }

    @Override
    Collection<Book> getBooks() {
        return books.values(); //RETORNA O VALOR DO MAP MAP<KEY,VALUE>
    }

    @Override
    public Book findBook(String title) throws CloneNotSupportedException {
        for(Book book : books.values()) //retorna a lista de valores ou podemos usar o books.keySet() para usar a key
            if (book.getTitle().equalsIgnoreCase(title))
                return book.clone();
        return null;
    }

    @Override
    public Book findBook(int id) throws CloneNotSupportedException {
        Book book = books.get(id);
        return  book != null ? book.clone() : null;

    }

    @Override
    public boolean removeBook(String title) {
        for(Book book : books.values())
            if (book.getTitle().equalsIgnoreCase(title))
                return books.remove(book.getId())!=null;
        return false;
    }

    @Override
    public boolean removeBook(int id) {
        return books.remove(id) != null;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(String.format("Library %s:\n",name));
        for(Book book : books.values())
            output.append(String.format("  - %s\n",book));
        return output.toString();
    }
}
