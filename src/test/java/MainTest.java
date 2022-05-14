import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.nightmirror.bookshop.Main;
import ru.nightmirror.bookshop.database.IStockManager;
import ru.nightmirror.bookshop.database.StockManager;
import ru.nightmirror.bookshop.models.Book;
import ru.nightmirror.bookshop.models.User;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class MainTest {

    private IStockManager iStockManager;

    @Before
    public void before() {
        iStockManager = new StockManager(new File("stock.db")).createTable();
    }

    @Test
    public void data_load() {
        String testedData = "balance: 1000, books: [(\"Алгебра, 10 класс\", 5, 100), (\"Теория чисел, 2 класс\", 42, 500)]";
        User user = Main.loadData(iStockManager, new ByteArrayInputStream(testedData.getBytes()));

        List<Book> expectedBooks = Arrays.asList(new Book("Алгебра, 10 класс", 100), new Book("Теория чисел, 2 класс", 500));

        Assert.assertTrue(user.getBalance() == 1000 && expectedBooks.hashCode() == iStockManager.getBooks().hashCode());
    }

    @After
    public void after() {
        iStockManager.close();
    }
}
