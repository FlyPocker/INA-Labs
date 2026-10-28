package library;

import java.util.List;

public interface BookRepository {
    Book findBook(String title);

    List<Book> findAllBooks();

    void saveBook(Book book);
}
