package library;

import java.util.List;

// GRASP Pure Fabrication: intefejs pozwala na odzielenie logiki aplikacji od sposobu przechowywania danych
// Dependency Inversion: Pozwala to na łatwą zmianę na inna implementację bazy danych
public interface LibraryRepository {
    User findUser(String name);

    Book findBook(String title);

    List<User> findAllUsers();

    List<Book> findAllBooks();

    void saveUser(User user);

    void saveBook(Book book);
}
