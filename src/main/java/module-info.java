/**
 * Модуль MUD Game приложения.
 * 
 * Этот файл определяет модульную структуру приложения для Java 9+.
 * Он экспортирует пакет com.mudgame для доступа JavaFX и требует необходимые модули.
 */
module com.mudgame {
    // Экспортируем пакет com.mudgame, чтобы JavaFX мог получить к нему доступ
    exports com.mudgame;
    
    // Экспортируем пакет model, чтобы JavaFX мог использовать игровые объекты
    exports com.mudgame.model;
    
    // Требуем модуль javafx.controls для работы с элементами управления JavaFX
    requires javafx.controls;
    
    // Требуем модуль javafx.fxml для работы с FXML (хотя мы его не используем, но он в зависимостях)
    requires javafx.fxml;
}
