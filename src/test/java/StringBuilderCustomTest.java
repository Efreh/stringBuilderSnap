import customs.StringBuilderCustom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса StringBuilderCustom.
 */
public class StringBuilderCustomTest {

    private StringBuilderCustom builderCustom;

    /**
     * Инициализация нового экземпляра StringBuilderCustom перед каждым тестом.
     */
    @BeforeEach
    public void setUp() {
        builderCustom = new StringBuilderCustom();
    }

    /**
     * Тест для метода append.
     * Проверяет корректность добавления строки.
     */
    @Test
    public void testAppend() {
        builderCustom.append("Hello");
        assertEquals("Hello", builderCustom.toString());

        builderCustom.append(" World");
        assertEquals("Hello World", builderCustom.toString());
    }

    /**
     * Тест для метода undo.
     * Проверяет возможность отмены последнего действия.
     */
    @Test
    public void testUndo() {
        builderCustom.append("Hello");
        builderCustom.append(" World");

        builderCustom.undo();
        assertEquals("Hello", builderCustom.toString()); // После undo должно вернуться состояние "Hello"

        builderCustom.undo();
        assertEquals("", builderCustom.toString()); // После второго undo должно вернуться к пустой строке
    }

    /**
     * Тест для метода delete.
     * Проверяет удаление символов из строки.
     */
    @Test
    public void testDelete() {
        builderCustom.append("Hello World");
        builderCustom.delete(5, 11); // Удаляем часть строки " World"
        assertEquals("Hello", builderCustom.toString());
    }

    /**
     * Тест для ручного сохранения снимков.
     * Проверяет сохранение состояния вручную.
     */
    @Test
    public void testManualSnapshot() {
        builderCustom.append("Initial");
        builderCustom.saveManualSnapshot(); // Сохраняем снимок вручную

        builderCustom.append(" Updated");
        builderCustom.undo(); // Откатываемся к снимку
        assertEquals("Initial", builderCustom.toString()); // Должно быть "Initial" после отката
    }

    /**
     * Тест для изменения максимального размера истории.
     * Проверяет, что старые снимки удаляются при уменьшении размера истории.
     */
    @Test
    public void testNewSizeHistory() {
        builderCustom.append("First");
        builderCustom.saveManualSnapshot();
        builderCustom.append("Second");
        builderCustom.saveManualSnapshot();
        builderCustom.append("Third");
        builderCustom.saveManualSnapshot();

        builderCustom.newSizeHistory(2); // Ограничиваем историю до 2 снимков

        builderCustom.undo(); // Откат к "Second"
        assertEquals("Second", builderCustom.toString());

        builderCustom.undo(); // Откат к "First"
        assertEquals("First", builderCustom.toString());

        builderCustom.undo(); // Попытка откатиться дальше не должна сработать (пустая строка)
        assertEquals("First", builderCustom.toString()); // Ожидается, что останется "First"
    }

    /**
     * Тест для метода clearHistory.
     * Проверяет очистку всей истории.
     */
    @Test
    public void testClearHistory() {
        builderCustom.append("Some text");
        builderCustom.saveManualSnapshot();

        builderCustom.append(" More text");
        builderCustom.clearHistory(); // Очищаем всю историю

        builderCustom.undo(); // Попытка откатиться не должна сработать, т.к. история пуста
        assertEquals("Some text More text", builderCustom.toString()); // Ожидается, что текст останется без изменений
    }
}
