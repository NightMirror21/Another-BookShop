import org.junit.*;
import ru.nightmirror.bookshop.commands.Command;
import ru.nightmirror.bookshop.commands.CommandListener;
import ru.nightmirror.bookshop.database.IStockManager;
import ru.nightmirror.bookshop.database.StockManager;
import ru.nightmirror.bookshop.models.Book;
import ru.nightmirror.bookshop.models.User;

import java.io.*;

public class CommandListenerTest {

    private IStockManager iStockManager;
    private User user;

    @Before
    public void before() {
        iStockManager = new StockManager(new File("stock.db")).createTable();
        iStockManager.addBook(new Book("test 1", 50), 3);
        user = new User(120);
    }

    @Test
    public void print_balance() {
        Assert.assertEquals("balance: 120 руб.", executeMessage(Command.PRINT_BALANCE.getName()));
    }

    @Test
    public void show_books_in_stock() {
        Assert.assertEquals("\"test 1\", 3 шт., 50 руб.", executeMessage(Command.SHOW_STOCK.getName()));
    }

    @Test
    public void buy_deal() {
        Assert.assertEquals("deal", executeMessage(Command.BUY_BOOK.getName() + " \"test 1\" 1"));
    }

    @Test
    public void buy_no_deal_count() {
        Assert.assertEquals("no deal", executeMessage(Command.BUY_BOOK.getName() + " \"test 1\" 5"));
    }

    @Test
    public void buy_no_deal_money() {
        Assert.assertEquals("no deal", executeMessage(Command.BUY_BOOK.getName() + " \"test 1\" 3"));
    }

    @Test
    public void buy_no_deal_book() {
        Assert.assertEquals("no deal", executeMessage(Command.BUY_BOOK.getName() + " \"test 2\" 10"));
    }

    @Test
    public void show_bought_book() {
        Book expected = new Book("test 1", 100);
        user.addBookToPurchased(expected, 2);
        Assert.assertEquals("\"test 1\", 2 шт.", executeMessage(Command.SHOW_PURCHASED.getName()));
    }

    @Test
    public void print_unknown() {
        Assert.assertEquals("I don't understand", executeMessage("text_example"));
    }

    @Ignore
    private String executeMessage(String message) {
        ByteArrayOutputStream outputStreamResult = new ByteArrayOutputStream();
        PrintStream outputStream = new PrintStream(outputStreamResult);

        Thread thread = new Thread(new CommandListener(new ByteArrayInputStream(message.getBytes()), outputStream, user, iStockManager));
        thread.start();

        while (outputStreamResult.size() == 0) {
            try { Thread.currentThread().sleep(50); } catch (Exception exception) { };
            outputStream.flush();
        }

        thread.interrupt();

        return outputStreamResult.toString().trim();
    }

    @After
    public void after() {
        iStockManager.close();
    }
}
