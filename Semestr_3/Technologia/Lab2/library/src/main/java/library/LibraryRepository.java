package library;

import java.util.List;

// GRASP Pure Fabrication: persistence is separated from domain classes.
// Dependency Inversion: the service depends on this abstraction.
public interface LibraryRepository {
    User findUser(String name);

    Book findBook(String title);

    List<User> findAllUsers();

    List<Book> findAllBooks();

    void saveUser(User user);

    void saveBook(Book book);
}
