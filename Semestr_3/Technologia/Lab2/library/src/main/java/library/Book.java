package library;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private String title;

    private String author;

    private List<Copy> copies;

    public Book(String bookTitle, String bookAuthor) {
        title = bookTitle;
        author = bookAuthor;
        this.copies = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public List<Copy> getCopies() {
        return copies;
    }

    public void addCopy(Copy copy) {
        // GRASP Information Expert: Book kontroluje stan swoich kopii bo jest ich właścicielem
        copies.add(copy);
    }
}