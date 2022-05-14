package ru.nightmirror.bookshop.database;

import ru.nightmirror.bookshop.models.Book;

import java.util.List;

public interface IStockManager {

    void addBook(Book book, int count);

    int getCountOfBook(Book book);

    List<Book> getBooks();

    Boolean removeBook(Book book, int count);

    Book getBookFromName(String name);

    void close();
}
