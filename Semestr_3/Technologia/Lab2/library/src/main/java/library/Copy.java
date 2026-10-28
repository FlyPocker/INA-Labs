package library;

import java.util.concurrent.atomic.AtomicInteger;

public class Copy {
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    private String inventoryNumber;

    private boolean isBorrowed;

    public Copy() {
        this.inventoryNumber = String.format("C%03d", NEXT_ID.getAndIncrement());
        this.isBorrowed = false;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        // GRASP Information Expert: kopia kontroluje stan wypożyczenia wlasnej zmeinnej
        isBorrowed = borrowed;
    }
}
