import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.nightmirror.bookshop.database.IStockManager;
import ru.nightmirror.bookshop.database.StockManager;
import ru.nightmirror.bookshop.models.Book;

import java.io.File;

public class StockManagerTest {

    private IStockManager iStockManager;

    @Before
    public void before() {
        iStockManager = new StockManager(new File("stock.db")).createTable();
    }

    @Test
    public void is_file_created() {
        Assert.assertTrue(new File("stock.db").exists());
    }

    @Test
    public void add_one_type_book() {
        Book expected = new Book("test 1", 100);
        iStockManager.addBook(expected, 1);

        Assert.assertTrue(iStockManager.getCountOfBook(expected) == 1 &&
                iStockManager.getBooks().get(0).hashCode() == expected.hashCode());
    }

    @Test
    public void add_many_type_books() {
        Book expected1 = new Book("test 1", 100);
        Book expected2 = new Book("test 2", 300);
        iStockManager.addBook(expected1, 10);
        iStockManager.addBook(expected2, 5);
        iStockManager.addBook(expected1, 8);

        Assert.assertTrue(iStockManager.getBooks().size() == 2 &&
                iStockManager.getCountOfBook(expected1) == 18 &&
                iStockManager.getCountOfBook(expected2) == 5);
    }

    @Test
    public void get_book_from_name() {
        Book expected = new Book("test 1", 100);
        iStockManager.addBook(expected, 20);

        Book actual = iStockManager.getBookFromName("test 1");

        Assert.assertTrue(expected.hashCode() == actual.hashCode());
    }

    @Test
    public void remove_book_part() {
        Book book = new Book("test 1", 100);
        iStockManager.addBook(book, 2);

        iStockManager.removeBook(book, 1);

        Assert.assertTrue(iStockManager.getBooks().size() == 1);
    }

    @Test
    public void remove_books_all() {
        Book book = new Book("test 1", 100);
        iStockManager.addBook(book, 1);

        iStockManager.removeBook(book, 1);

        Assert.assertTrue(iStockManager.getBooks().size() == 0);
    }

    @After
    public void after() {
        iStockManager.close();
    }
}
