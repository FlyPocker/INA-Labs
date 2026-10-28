package library;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;

    private List<Copy> borrowedCopies;

    public User(String userName) {
        name = userName;
        this.borrowedCopies = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Copy> getBorrowedCopies() {
        return borrowedCopies;
    }

    public void borrowCopy(Copy copy) {
        // GRASP Information Expert: a user manages their borrowed copies.
        borrowedCopies.add(copy);
    }
}