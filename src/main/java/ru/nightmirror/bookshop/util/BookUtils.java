package ru.nightmirror.bookshop.util;

import ru.nightmirror.bookshop.models.Book;

import java.util.HashMap;

public final class BookUtils {

    public static HashMap<Book, Integer> parseBookList(String str) {
        try {
            HashMap<Book, Integer> bookList = new HashMap<>();

            str = str.replaceAll("\\[", "")
                    .replaceAll("\\]", "")
                    .replaceAll("\\(", "")
                    .trim();

            for (String bookStr : str.split("\\),")) {
                String bookName = bookStr.split("\",")[0].replaceAll("\"", "").trim();
                int count = Integer.parseInt(bookStr.split("\",")[1].split(",")[0].trim());
                int cost = Integer.parseInt(bookStr.split("\",")[1].split(",")[1].replaceAll("\\)", "").trim());
                Book book = new Book(bookName, cost);

                if (bookList.containsKey(book)) {
                    bookList.replace(book, (bookList.get(book) + count));
                } else {
                    bookList.put(book, count);
                }
            }

            return bookList;
        } catch (Exception exception) {
            // IGNORE
            return null;
        }
    }
}
