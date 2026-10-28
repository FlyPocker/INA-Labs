package library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// GRASP Pure Fabrication: Dane są przechowywanie w pamięci, ale odzielone są od logiki kodu przez interfejs LibraryRepository.
// Dependency Inversion: Pozwala to na łatwą zmianę na inna implementację bazy danych
public class InMemoryLibraryRepository implements LibraryRepository {
    private final Map<String, User> users = new HashMap<>();

    private final Map<String, Book> books = new HashMap<>();

    @Override
    public User findUser(String name) {
        return users.get(name);
    }

    @Override
    public Book findBook(String title) {
        return books.get(title);
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public List<Book> findAllBooks() {
        return new ArrayList<>(books.values());
    }

    @Override
    public void saveUser(User user) {
        users.put(user.getName(), user);
    }

    @Override
    public void saveBook(Book book) {
        books.put(book.getTitle(), book);
    }
}
