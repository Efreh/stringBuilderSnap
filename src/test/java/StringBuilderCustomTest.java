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
    void testNewSizeHistory() {
        StringBuilderCustom builder = new StringBuilderCustom(2);  // Устанавливаем максимальный размер истории в 2
        builder.append("First");  // Состояние 1
        builder.saveManualSnapshot();  // Сохраняем снимок состояния "First"

        builder.append("Second");  // Состояние 2
        builder.saveManualSnapshot();  // Сохраняем снимок состояния "FirstSecond"

        builder.append("Third");  // Состояние 3

        builder.newSizeHistory(1);  // Уменьшаем размер истории до 1 снимка

        builder.undo();  // Ожидаем, что снимок вернется к состоянию "FirstSecond"

        assertEquals("FirstSecond", builder.toString());  // Ожидаем строку "FirstSecond"
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
