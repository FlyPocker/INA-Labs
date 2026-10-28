package library;

import java.util.List;

public class LibraryService {

    private final LibraryRepository repository;

    public LibraryService(LibraryRepository libraryRepository) {
        repository = libraryRepository;
    }

    // GRASP Controller coordinates use cases; domain objects keep their own state.
    public boolean addUser(String name) {
        if (repository.findUser(name) != null) {
            return false;
        }
        repository.saveUser(new User(name));
        return true;
    }

    public boolean addBook(String title, String author) {
        if (repository.findBook(title) != null) {
            return false;
        }
        repository.saveBook(new Book(title, author));
        return true;
    }

    public Copy addCopy(String bookTitle) {
        Book book = repository.findBook(bookTitle);
        if (book == null) {
            return null;
        }
        Copy copy = new Copy();
        book.addCopy(copy);
        return copy;
    }

    public boolean borrowBook(String userName, String bookTitle) {
        User user = repository.findUser(userName);
        Book book = repository.findBook(bookTitle);
        if (user == null || book == null) {
            return false;
        }
        for (Copy copy : book.getCopies()) {
            if (!copy.isBorrowed()) {
                user.borrowCopy(copy);
                copy.setBorrowed(true);
                return true;
            }
        }
        return false;
    }

    public boolean returnBook(String userName, String inventoryNumber) {
        User user = repository.findUser(userName);
        if (user == null) {
            return false;
        }
        for (Copy copy : user.getBorrowedCopies()) {
            if (copy.getInventoryNumber().equals(inventoryNumber)) {
                user.getBorrowedCopies().remove(copy);
                copy.setBorrowed(false);
                return true;
            }
        }
        return false;
    }

    public User findUser(String name) {
        return repository.findUser(name);
    }

    public Book findBook(String title) {
        return repository.findBook(title);
    }

    public List<User> findAllUsers() {
        return repository.findAllUsers();
    }

    public List<Book> findAllBooks() {
        return repository.findAllBooks();
    }

}
