package ru.nightmirror.bookshop.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {

    private int balance;
    private HashMap<Book, Integer> purchasedBooks = new HashMap<>();

    public User(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void addBookToPurchased(Book book, int count) {
        if (purchasedBooks.containsKey(book)) {
            purchasedBooks.replace(book, (purchasedBooks.get(purchasedBooks) + count));
        } else {
            purchasedBooks.put(book, count);
        }
    }

    public HashMap<Book, Integer> getPurchasedBooks() {
        return purchasedBooks;
    }
}
