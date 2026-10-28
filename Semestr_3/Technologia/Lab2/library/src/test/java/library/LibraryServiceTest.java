package library;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LibraryServiceTest {
    private LibraryService service;

    @BeforeEach
    void setUp() {
        service = new LibraryService(
                new InMemoryUserRepository(), new InMemoryBookRepository());
    }

    @Test
    void shouldAddUsersAndBooksOnlyOnce() {
        assertTrue(service.addUser("Ala"));
        assertFalse(service.addUser("Ala"));
        assertTrue(service.addBook("Lalka", "Bolesław Prus"));
        assertFalse(service.addBook("Lalka", "Inny autor"));
        assertEquals(1, service.findAllUsers().size());
        assertEquals(1, service.findAllBooks().size());
    }

    @Test
    void shouldGenerateCopyNumberWhenAddingCopy() {
        assertTrue(service.addBook("Lalka", "Bolesław Prus"));

        Copy copy = service.addCopy("Lalka");

        assertNotNull(copy);
        assertTrue(copy.getInventoryNumber().matches("C\\d{3}"));
        assertFalse(copy.isBorrowed());
    }

    @Test
    void shouldBorrowAndReturnCopy() {
        service.addUser("Ala");
        service.addBook("Lalka", "Bolesław Prus");
        service.addCopy("Lalka");

        assertTrue(service.borrowBook("Ala", "Lalka"));
        assertEquals(1, service.findUser("Ala").getBorrowedCopies().size());
        String inventoryNumber = service.findUser("Ala").getBorrowedCopies()
                .get(0).getInventoryNumber();

        assertTrue(service.returnBook("Ala", inventoryNumber));
        assertTrue(service.findUser("Ala").getBorrowedCopies().isEmpty());
        assertFalse(service.returnBook("Ala", inventoryNumber));
    }

    @Test
    void shouldRejectBorrowingWithoutAvailableCopy() {
        service.addUser("Ala");
        service.addUser("Jan");
        service.addBook("Lalka", "Bolesław Prus");
        service.addCopy("Lalka");

        assertTrue(service.borrowBook("Ala", "Lalka"));
        assertFalse(service.borrowBook("Jan", "Lalka"));
    }
}
