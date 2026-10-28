package library;

import java.util.List;

public class LibraryService {

    private final UserRepository userRepository;

    private final BookRepository bookRepository;

    public LibraryService(UserRepository users, BookRepository books) {
        userRepository = users;
        bookRepository = books;
    }

    // GRASP Controller koordynuje logiką aplikacji, deleguje zmiany danych odpowiednim klasom
    public boolean addUser(String name) {
        if (userRepository.findUser(name) != null) {
            return false;
        }
        userRepository.saveUser(new User(name));
        return true;
    }

    public boolean addBook(String title, String author) {
        if (bookRepository.findBook(title) != null) {
            return false;
        }
        bookRepository.saveBook(new Book(title, author));
        return true;
    }

    public Copy addCopy(String bookTitle) {
        Book book = bookRepository.findBook(bookTitle);
        if (book == null) {
            return null;
        }
        Copy copy = new Copy();
        book.addCopy(copy);
        return copy;
    }

    public boolean borrowBook(String userName, String bookTitle) {
        User user = userRepository.findUser(userName);
        Book book = bookRepository.findBook(bookTitle);
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
        User user = userRepository.findUser(userName);
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
        return userRepository.findUser(name);
    }

    public Book findBook(String title) {
        return bookRepository.findBook(title);
    }

    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAllBooks();
    }

}
