package ru.nightmirror.bookshop.database;

import org.sqlite.JDBC;
import ru.nightmirror.bookshop.models.Book;
import ru.nightmirror.bookshop.util.BookUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Logger;

public class StockManager implements IStockManager {

    private Connection connection;
    private final Logger LOG = Logger.getLogger("StockManager");
    private final File SQLITE;

    public StockManager(final File SQLITE) {
        this.SQLITE = SQLITE;

        try {
            SQLITE.createNewFile();

            final String CONNECTION_STR = "jdbc:sqlite:" + SQLITE.getAbsolutePath();

            DriverManager.registerDriver(new JDBC());
            connection = DriverManager.getConnection(CONNECTION_STR);
        } catch (Exception exception) {
            LOG.severe(exception.toString());
        }
    }

    public StockManager createTable() {
        final String QUERY = "CREATE TABLE IF NOT EXISTS stock (\n"
                + " `name` TEXT,\n"
                + " `cost` INTEGER,\n"
                + " `count` INTEGER\n"
                + ");";

        try (
                Statement statement = connection.createStatement();
                ) {
            statement.execute(QUERY);
        } catch (Exception exception) {
            LOG.severe(exception.toString());
        }
        return this;
    }

    @Override
    public void addBook(Book book, int count) {
        int countOld = getCountOfBook(book);
        String query = null;

        if (countOld < 1) {
            query = String.format("INSERT INTO stock (`name`, `cost`, `count`) " +
                    "VALUES ('%s', '%d', '%d');", book.getName(), book.getCost(), count);
        } else {
            query = String.format("UPDATE stock SET count = '%d' WHERE name = '%s' AND cost = '%d'",
                    (getCountOfBook(book)+count), book.getName(), book.getCost());
        }

        try (
                Statement statement = connection.createStatement();
                ) {
            statement.execute(query);
        } catch (Exception exception) {
            LOG.warning(exception.toString());
            LOG.info(query);
        }
    }

    @Override
    public int getCountOfBook(Book book) {
        final String QUERY = String.format("SELECT * FROM stock WHERE name = '%s' AND cost = '%d';",
                book.getName(), book.getCost());

        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(QUERY);
                ) {
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        } catch (Exception exception) {
            LOG.warning(exception.toString());
        }

        return 0;
    }

    @Override
    public List<Book> getBooks() {
        final String QUERY = "SELECT * FROM stock;";

        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(QUERY);
        ) {
            List<Book> books = new ArrayList<>();

            if (resultSet == null) return books;

            while (resultSet.next()) {
               books.add(new Book(resultSet.getString("name"), resultSet.getInt("cost")));
            }

            if (books.size() == 0) return books;

            return books;
        } catch (Exception exception) {
            LOG.warning(exception.toString());
        }

        return new ArrayList<>();
    }

    @Override
    public Boolean removeBook(Book book, int count) {
        String query = null;

        if (getCountOfBook(book) <= count) {
            query = String.format("DELETE FROM stock WHERE name = '%s' AND cost = '%d';",
                    book.getName(), book.getCost());
        } else {
            query = String.format("UPDATE stock SET count = '%d' WHERE name = '%s' AND cost = '%d'",
                    (getCountOfBook(book)-count), book.getName(), book.getCost());
        }

        try (
                Statement statement = connection.createStatement();
                ) {
            return statement.execute(query);
        } catch (Exception exception) {
            LOG.warning(exception.toString());
        }

        return false;
    }

    @Override
    public Book getBookFromName(String name) {
        final String QUERY = String.format("SELECT * FROM stock WHERE name = '%s';",
                name);

        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(QUERY);
        ) {
            if (resultSet.next()) {
                return new Book(name, resultSet.getInt("cost"));
            }
        } catch (Exception exception) {
            LOG.warning(exception.toString());
        }

        return null;
    }

    @Override
    public void close() {
        try {
            connection.close();
            SQLITE.delete();
        } catch (Exception exception) {
            LOG.severe(exception.toString());
        }
    }
}
