package ru.nightmirror.bookshop.commands;

import ru.nightmirror.bookshop.database.IStockManager;
import ru.nightmirror.bookshop.models.Book;
import ru.nightmirror.bookshop.models.User;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class CommandListener implements Runnable {

    private final BufferedReader INPUT;
    private final BufferedWriter OUTPUT;
    private final User USER;
    private final IStockManager STOCK;
    private final Logger LOG = Logger.getLogger("MessageListener");

    public CommandListener(InputStream inputStream, OutputStream outputStream, User user, IStockManager iStockManager) {
        this.INPUT = new BufferedReader(new InputStreamReader(inputStream));
        this.OUTPUT = new BufferedWriter(new OutputStreamWriter(outputStream));
        this.USER = user;
        STOCK = iStockManager;
    }

    @Override
    public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String commandStr = INPUT.readLine();
                    if (commandStr == null || commandStr.equals("")) continue;
                    final Command CMD = Command.parseString(commandStr);

                    if (CMD == null) {
                        print("I don't understand");
                        continue;
                    }

                    switch (CMD) {
                        case PRINT_BALANCE -> {
                            print(String.format("balance: %d руб.%n", USER.getBalance()));
                        }

                        case SHOW_STOCK -> {
                            for (Book book : STOCK.getBooks()) {
                                print(String.format("\"%s\", %d шт., %d руб.%n",
                                        book.getName(), STOCK.getCountOfBook(book), book.getCost()));
                            }
                        }

                        case BUY_BOOK -> {
                            String bookName = CMD.getData().split("\" ")[0].replaceAll("\"", "").trim();
                            int count = Integer.parseInt(CMD.getData().split("\" ")[1].replaceAll("\"", "").trim());

                            Book book = STOCK.getBookFromName(bookName);

                            if (book == null) {
                                print("no deal");
                                continue;
                            }

                            if (book.getCost() * count > USER.getBalance() || STOCK.getCountOfBook(book) < count) {
                                print("no deal");
                                continue;
                            }

                            STOCK.removeBook(book, count);
                            USER.setBalance(USER.getBalance() - (book.getCost() * count));
                            USER.addBookToPurchased(book, count);
                            print("deal");
                        }

                        case SHOW_PURCHASED -> {
                            for (Map.Entry<Book, Integer> entry : USER.getPurchasedBooks().entrySet()) {
                                print(String.format("\"%s\", %d шт.%n",
                                        entry.getKey().getName(), entry.getValue()));
                            }
                        }

                        case EXIT -> {
                            STOCK.close();
                            return;
                        }
                    }
                }
            } catch (Exception exception) {
                LOG.warning(exception.toString());
                STOCK.close();
            }
    }

    private void print(String message) {
        try {
            OUTPUT.write(message.replaceAll("\n", "") + "\n");
            OUTPUT.flush();
        } catch (Exception exception) {
            LOG.severe(exception.toString());
        }
    }
}
