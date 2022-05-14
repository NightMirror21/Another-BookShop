package ru.nightmirror.bookshop;

import ru.nightmirror.bookshop.commands.CommandListener;
import ru.nightmirror.bookshop.database.IStockManager;
import ru.nightmirror.bookshop.database.StockManager;
import ru.nightmirror.bookshop.models.Book;
import ru.nightmirror.bookshop.models.User;
import ru.nightmirror.bookshop.util.BookUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOG = Logger.getLogger("Main");

    public static void main(String[] args) {
        IStockManager iStockManager = new StockManager(new File("stock.db"))
                .createTable();

        new Thread(new CommandListener(System.in, System.out, loadData(iStockManager, System.in), iStockManager)).start();
    }

    public static User loadData(IStockManager iStockManager, InputStream inputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String data = reader.readLine();

            if (data.equals("exit")) {
                iStockManager.close();
                System.exit(0);
            }

            int balance = Integer.parseInt(data.split(", books:")[0].replaceAll("balance:", "").trim());
            String bookData = data.split("books: ")[1].trim();

            for(Map.Entry<Book, Integer> entry : BookUtils.parseBookList(bookData).entrySet()) {
                iStockManager.addBook(entry.getKey(), entry.getValue());
            }

            return new User(balance);
        } catch (Exception exception) {
            LOG.severe(exception.toString());
        }

        return null;
    }
}
