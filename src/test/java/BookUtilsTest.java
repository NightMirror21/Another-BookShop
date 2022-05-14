import org.junit.Assert;
import org.junit.Test;
import ru.nightmirror.bookshop.models.Book;
import ru.nightmirror.bookshop.util.BookUtils;

import java.util.HashMap;
import java.util.Map;

public class BookUtilsTest {

    @Test
    public void parse_book_list_null() {
        Assert.assertNull(BookUtils.parseBookList(null));
    }

    @Test
    public void parse_book_list_empty_string() {
        Assert.assertNull(BookUtils.parseBookList(""));
    }

    @Test
    public void parse_book_list_not_correct_format() {
        Assert.assertNull(BookUtils.parseBookList("example not correct value"));
    }

    @Test
    public void parse_book_list_correct_format() {
        HashMap<Book, Integer> expected = new HashMap<>();
        expected.put(new Book("Алгебра, 10 класс", 100), 5);
        expected.put(new Book("Теория чисел, 2 класс", 500), 42);

        HashMap<Book, Integer> actual = BookUtils.parseBookList("[(\"Алгебра, 10 класс\", 5, 100), (\"Теория чисел, 2 класс\", 42, 500)]");

        Assert.assertTrue(expected.hashCode() == actual.hashCode());
    }
}
