package library;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        // GRASP Controller: App obsluguje interakcję z użytkownikiem, deleguje logike do LibraryService
        Scanner scanner = new Scanner(System.in);
        LibraryService libraryService = new LibraryService(new InMemoryLibraryRepository());

        System.out.println("Witaj w systemie zarządzania biblioteką!");

        boolean running = true;
        while (running) {
            System.out.println("\nWybierz opcję:");
            System.out.println("1. Dodaj czytelnika");
            System.out.println("2. Dodaj książkę (tytuł i autor)");
            System.out.println("3. Dodaj egzemplarz (kopię) do książki");
            System.out.println("4. Wypożycz książkę");
            System.out.println("5. Zwróć książkę (po numerze inwentarzowym)");
            System.out.println("6. Wyświetl użytkowników");
            System.out.println("7. Wyświetl książki");
            System.out.println("0. Wyjdź");
            System.out.print("> ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Podaj nazwę czytelnika: ");
                    String userName = scanner.nextLine();
                    if (libraryService.addUser(userName)) {
                        System.out.println("Dodano użytkownika " + userName + ".");
                    } else {
                        System.out.println("Czytelnik o takiej nazwie już istnieje.");
                    }
                    break;

                case "2":
                    System.out.print("Podaj tytuł książki: ");
                    String title = scanner.nextLine();
                    System.out.print("Podaj autora książki: ");
                    String author = scanner.nextLine();
                    if (libraryService.addBook(title, author)) {
                        System.out.println("Dodano książkę " + title + " autorstwa " + author + ".");
                    } else {
                        System.out.println("Książka o tym tytule już jest w systemie.");
                    }
                    break;

                case "3":
                    System.out.print("Podaj tytuł książki, do której chcesz dodać egzemplarz: ");
                    String bookTitleForCopy = scanner.nextLine();
                    Copy newCopy = libraryService.addCopy(bookTitleForCopy);
                    if (newCopy != null) {
                        System.out.println("Dodano egzemplarz " + newCopy.getInventoryNumber() + ".");
                    } else {
                        System.out.println("Nie znaleziono takiej książki. Najpierw ją dodaj (Opcja 2).");
                    }
                    break;

                case "4":
                    System.out.print("Podaj nazwę czytelnika: ");
                    String borrowerName = scanner.nextLine();
                    System.out.print("Podaj tytuł książki do wypożyczenia: ");
                    String bookToBorrowTitle = scanner.nextLine();
                    
                    if (libraryService.borrowBook(borrowerName, bookToBorrowTitle)) {
                        System.out.println("Wypożyczono książkę.");
                    } else {
                        System.out.println("Błąd: Nie znaleziono czytelnika lub książki w systemie.");
                    }
                    break;

                case "5":
                    System.out.print("Podaj nazwę czytelnika zwracającego książkę: ");
                    String returnerName = scanner.nextLine();
                    if (libraryService.findUser(returnerName) != null) {
                        System.out.print("Podaj numer inwentarzowy zwracanego egzemplarza: ");
                        String invNumToReturn = scanner.nextLine();
                        
                        if (libraryService.returnBook(returnerName, invNumToReturn)) {
                            System.out.println("Zwrócono egzemplarz.");
                        } else {
                            System.out.println("Czytelnik nie posiada egzemplarza o takim numerze.");
                        }
                    } else {
                        System.out.println("Nie znaleziono czytelnika.");
                    }
                    break;

                case "6":
                    if (libraryService.findAllUsers().isEmpty()) {
                        System.out.println("Brak użytkowników w systemie.");
                    } else {
                        System.out.println("Użytkownicy:");
                        for (User user : libraryService.findAllUsers()) {
                            System.out.println("- " + user.getName()
                                    + " (wypożyczonych egzemplarzy: "
                                    + user.getBorrowedCopies().size() + ")");
                        }
                    }
                    break;

                case "7":
                    if (libraryService.findAllBooks().isEmpty()) {
                        System.out.println("Brak książek w systemie.");
                    } else {
                        System.out.println("Książki:");
                        for (Book book : libraryService.findAllBooks()) {
                            System.out.println("- " + book.getTitle() + " - " + book.getAuthor());
                            if (book.getCopies().isEmpty()) {
                                System.out.println("  Brak egzemplarzy.");
                            } else {
                                for (Copy copy : book.getCopies()) {
                                    String status = copy.isBorrowed() ? "wypożyczony" : "dostępny";
                                    System.out.println("  " + copy.getInventoryNumber() + " - " + status);
                                }
                            }
                        }
                    }
                    break;

                case "0":
                    running = false;
                    System.out.println("Zamykanie systemu...");
                    break;

                default:
                    System.out.println("Nieprawidłowa opcja. Spróbuj ponownie.");
            }
        }
        scanner.close();
    }
}