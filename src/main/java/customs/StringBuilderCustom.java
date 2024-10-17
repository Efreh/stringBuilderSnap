package customs;

import java.util.Stack;

/**
 * Кастомная реализация StringBuilder с поддержкой паттерна Снимок (Snapshot).
 * Позволяет сохранять состояния автоматически или вручную, а также отменять изменения.
 */
public final class StringBuilderCustom {
    private StringBuilder builder; // Основной экземпляр StringBuilder
    private final Stack<Snapshot> history; // Стек для хранения снимков состояний
    private int maxHistorySize; // Максимальный размер стека истории
    private boolean autoSnapshot = true; // Флаг для автоматического сохранения снимков

    /**
     * Конструктор по умолчанию с максимальным размером истории 5.
     */
    public StringBuilderCustom() {
        this(5); // Максимальный размер стека истории по умолчанию - 5
    }

    /**
     * Конструктор, позволяющий задать максимальный размер стека истории.
     *
     * @param maxHistorySize максимальное количество сохраняемых снимков.
     */
    public StringBuilderCustom(int maxHistorySize) {
        this.builder = new StringBuilder();
        this.history = new Stack<>();
        this.maxHistorySize = maxHistorySize;
    }

    /**
     * Конструктор, который инициализирует StringBuilder с начальной строкой.
     *
     * @param string начальное содержимое StringBuilder.
     */
    public StringBuilderCustom(String string) {
        this.builder = new StringBuilder(string);
        this.history = new Stack<>();
        if (autoSnapshot) saveSnapshot(); // Сохраняем снимок при инициализации
    }

    /**
     * Конструктор, позволяющий задать начальную строку и максимальный размер стека истории.
     *
     * @param string начальное содержимое StringBuilder.
     * @param maxHistorySize максимальное количество сохраняемых снимков.
     */
    public StringBuilderCustom(String string, int maxHistorySize) {
        this.builder = new StringBuilder(string);
        this.history = new Stack<>();
        this.maxHistorySize = maxHistorySize;
        if (autoSnapshot) saveSnapshot(); // Сохраняем снимок при инициализации
    }

    /**
     * Добавляет строку к текущему содержимому.
     * Сохраняет снимок автоматически, если включен режим autoSnapshot.
     *
     * @param str строка для добавления.
     */
    public void append(String str) {
        if (autoSnapshot) saveSnapshot(); // Сохраняем состояние перед изменением
        builder.append(str);
    }

    /**
     * Отменяет последнее изменение, восстанавливая предыдущее сохраненное состояние.
     */
    public void undo() {
        if (!history.isEmpty()) {
            Snapshot snapshot = history.pop();
            this.builder = new StringBuilder(snapshot.getState());
        }
    }

    /**
     * Удаляет часть текущего содержимого между указанными индексами.
     * Сохраняет снимок автоматически, если включен режим autoSnapshot.
     *
     * @param start начальный индекс (включительно).
     * @param end конечный индекс (исключительно).
     */
    public void delete(int start, int end) {
        if (autoSnapshot) saveSnapshot(); // Сохраняем состояние перед изменением
        builder.delete(start, end);
    }

    /**
     * Отключает автоматическое сохранение снимков. Изменения больше не будут сохранять состояния автоматически.
     */
    public void offAutoSnapshot() {
        autoSnapshot = false;
    }

    /**
     * Включает автоматическое сохранение снимков. Изменения будут сохранять состояния автоматически.
     */
    public void onAutoSnapshot() {
        autoSnapshot = true;
    }

    /**
     * Очищает историю сохраненных снимков.
     */
    public void clearHistory() {
        history.clear();
    }

    /**
     * Устанавливает новый максимальный размер стека истории.
     * Если новый размер меньше текущего количества снимков, старые снимки будут удалены.
     *
     * @param newSize новый максимальный размер стека истории.
     */
    public void newSizeHistory(int newSize) {
        this.maxHistorySize = newSize;
        while (history.size() > maxHistorySize) {
            history.remove(0); // Удаляем самый старый снимок
        }
    }

    /**
     * Сохраняет снимок текущего состояния в стек истории.
     * Если стек заполнен, удаляется самый старый снимок.
     */
    private void saveSnapshot() {
        if (history.size() == maxHistorySize) {
            history.remove(0); // Удаляем самый старый снимок, если стек полон
        }
        history.push(new Snapshot(builder.toString())); // Сохраняем текущее состояние
    }

    /**
     * Позволяет вручную сохранить снимок текущего состояния.
     */
    public void saveManualSnapshot() {
        saveSnapshot();
    }

    /**
     * Возвращает текущее содержимое StringBuilder в виде строки.
     *
     * @return строковое представление текущего состояния StringBuilder.
     */
    @Override
    public String toString() {
        return builder.toString();
    }

    /**
     * Внутренний класс для представления снимка состояния.
     */
    private class Snapshot {
        private final String state;

        /**
         * Конструктор, который сохраняет переданное состояние.
         *
         * @param state строковое состояние, которое необходимо сохранить.
         */
        public Snapshot(String state) {
            this.state = state;
        }

        /**
         * Возвращает сохраненное состояние.
         *
         * @return сохраненное строковое состояние.
         */
        public String getState() {
            return state;
        }
    }
}
