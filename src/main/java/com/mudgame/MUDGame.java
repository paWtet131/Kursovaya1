package com.mudgame;

import com.mudgame.model.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Random;

/**
 * Главный класс приложения MUD-игры.
 * 
 * Этот класс содержит весь код для графического интерфейса на JavaFX.
 * Здесь создаются все игровые объекты, кнопки управления и отрисовка на Canvas.
 */
public class MUDGame extends Application {
    
    // Игровые объекты
    private TRoom room;              // Комната, в которой происходит игра
    private TPlayer player;          // Игрок
    private TNPC npc;                // NPC (враг)
    private TWeapon weapon;          // Оружие в комнате
    
    // Элементы интерфейса
    private Canvas canvas;           // Холст для отрисовки игровых объектов
    private GraphicsContext gc;      // Контекст для рисования на холсте
    private Button enterRoomButton;  // Кнопка "Войти в локацию"
    private Button attackButton;     // Кнопка "Атаковать"
    private Button moveButton;       // Кнопка "Переместиться"
    private Button pickUpButton;     // Кнопка "Поднять предмет"
    private ListView<String> inventoryList;  // Список предметов в инвентаре
    private Label healthLabel;       // Метка с информацией о здоровье игрока
    
    // Объект для генерации случайных чисел
    private Random random;
    
    /**
     * Метод запуска приложения JavaFX.
     * 
     * @param primaryStage главное окно приложения
     */
    @Override
    public void start(Stage primaryStage) {
        // Создаем объект для генерации случайных чисел
        random = new Random();
        
        // Инициализируем игровые объекты
        initializeGameObjects();
        
        // Создаем интерфейс
        BorderPane root = createInterface();
        
        // Создаем сцену (окно) размером 800x600 пикселей
        Scene scene = new Scene(root, 800, 600);
        
        // Устанавливаем сцену в окно
        primaryStage.setScene(scene);
        
        // Устанавливаем заголовок окна
        primaryStage.setTitle("MUD Game");
        
        // Запрещаем изменение размера окна
        primaryStage.setResizable(false);
        
        // Показываем окно
        primaryStage.show();
    }
    
    /**
     * Инициализирует все игровые объекты.
     * 
     * Создает комнату, игрока, NPC и оружие с начальными параметрами.
     */
    private void initializeGameObjects() {
        // Создаем комнату размером 350x250 пикселей
        // Позиция комнаты: x=25, y=25 (отступы от краев Canvas)
        room = new TRoom("Сумеречный лес", "room1", 25, 25, 350, 250);
        
        // Создаем игрока с начальными параметрами
        // Здоровье: 100, базовая сила атаки: 10
        player = new TPlayer("Игрок", "player1", 200, 100, 100, 10);
        
        // Создаем NPC (врага) с начальными параметрами
        // Здоровье: 50, защита: 5
        npc = new TNPC("Враг", "npc1", 400, 200, 50, 5);
        
        // Создаем оружие с силой атаки 15
        weapon = new TWeapon("Меч", "weapon1", 300, 300, 15);
        
        // Добавляем NPC в комнату
        room.addNPC(npc);
        
        // Добавляем оружие в комнату
        room.addItem(weapon);
    }
    
    /**
     * Создает графический интерфейс приложения.
     * 
     * @return корневой элемент интерфейса
     */
    private BorderPane createInterface() {
        // Создаем корневой контейнер BorderPane
        // BorderPane позволяет размещать элементы в разных частях окна
        BorderPane root = new BorderPane();
        
        // Создаем панель управления слева (с кнопками и инвентарем)
        VBox leftPanel = createLeftPanel();
        root.setLeft(leftPanel);
        
        // Создаем центральную область с Canvas для отрисовки
        VBox centerArea = createCenterArea();
        root.setCenter(centerArea);
        
        // Создаем панель внизу с информацией о здоровье
        HBox bottomPanel = createBottomPanel();
        root.setBottom(bottomPanel);
        
        // Возвращаем корневой элемент
        return root;
    }
    
    /**
     * Создает панель управления слева с кнопками.
     * 
     * @return панель с кнопками
     */
    private VBox createLeftPanel() {
        // Создаем вертикальный контейнер для кнопок
        VBox panel = new VBox(10);  // Отступ между элементами 10 пикселей
        panel.setPadding(new Insets(20));  // Отступы от краев 20 пикселей
        panel.setAlignment(Pos.TOP_CENTER);  // Выравнивание по центру сверху
        
        // Создаем кнопку "Войти в локацию"
        enterRoomButton = new Button("Войти в локацию");
        enterRoomButton.setPrefWidth(150);  // Ширина кнопки
        enterRoomButton.setPrefHeight(40);  // Высота кнопки
        
        // Устанавливаем обработчик события нажатия на кнопку
        enterRoomButton.setOnAction(e -> enterRoom());
        
        // Создаем кнопку "Атаковать"
        attackButton = new Button("Атаковать");
        attackButton.setPrefWidth(150);
        attackButton.setPrefHeight(40);
        attackButton.setDisable(true);  // Изначально кнопка неактивна
        attackButton.setOnAction(e -> attackNPC());
        
        // Создаем кнопку "Переместиться"
        moveButton = new Button("Переместиться");
        moveButton.setPrefWidth(150);
        moveButton.setPrefHeight(40);
        moveButton.setDisable(true);  // Изначально кнопка неактивна
        moveButton.setOnAction(e -> movePlayer());
        
        // Создаем кнопку "Поднять предмет"
        pickUpButton = new Button("Поднять предмет");
        pickUpButton.setPrefWidth(150);
        pickUpButton.setPrefHeight(40);
        pickUpButton.setDisable(true);  // Изначально кнопка неактивна
        pickUpButton.setOnAction(e -> pickUpItem());
        
        // Добавляем все кнопки в панель
        panel.getChildren().addAll(enterRoomButton, attackButton, moveButton, pickUpButton);
        
        // Добавляем разделитель перед инвентарем
        Label separator = new Label("─────────");
        separator.setStyle("-fx-text-fill: gray;");
        panel.getChildren().add(separator);
        
        // Создаем метку "Инвентарь"
        Label inventoryLabel = new Label("Инвентарь");
        inventoryLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        // Создаем список для отображения предметов
        inventoryList = new ListView<>();
        inventoryList.setPrefWidth(150);  // Ширина равна ширине кнопок
        inventoryList.setPrefHeight(200);  // Высота для инвентаря
        
        // Добавляем метку и список инвентаря в панель
        panel.getChildren().addAll(inventoryLabel, inventoryList);
        
        // Возвращаем панель
        return panel;
    }
    
    /**
     * Создает центральную область с Canvas для отрисовки игровых объектов.
     * 
     * @return контейнер с Canvas
     */
    private VBox createCenterArea() {
        // Создаем вертикальный контейнер
        VBox area = new VBox();
        area.setPadding(new Insets(10));
        area.setAlignment(Pos.CENTER);
        
        // Создаем Canvas размером 500x350 пикселей (чтобы помещался в окно)
        canvas = new Canvas(500, 350);
        
        // Получаем контекст для рисования
        gc = canvas.getGraphicsContext2D();
        
        // Добавляем Canvas в контейнер
        area.getChildren().add(canvas);
        
        // Возвращаем контейнер
        return area;
    }
    
    
    /**
     * Создает панель внизу с информацией о здоровье игрока.
     * 
     * @return панель с информацией
     */
    private HBox createBottomPanel() {
        // Создаем горизонтальный контейнер
        HBox panel = new HBox(10);
        panel.setPadding(new Insets(10));
        panel.setAlignment(Pos.CENTER_LEFT);
        
        // Создаем метку с информацией о здоровье
        healthLabel = new Label("Здоровье: 100");
        healthLabel.setStyle("-fx-font-size: 14px;");
        
        // Добавляем метку в панель
        panel.getChildren().add(healthLabel);
        
        // Возвращаем панель
        return panel;
    }
    
    /**
     * Обработчик нажатия кнопки "Войти в локацию".
     * 
     * Игрок входит в комнату, размещаются все объекты, активируются кнопки.
     */
    private void enterRoom() {
        // Устанавливаем текущую комнату игрока
        player.setCurrentRoom(room);
        
        // Размещаем игрока в случайной позиции внутри комнаты
        placeObjectInRoom(player);
        
        // Размещаем NPC в случайной позиции внутри комнаты
        placeObjectInRoom(npc);
        
        // Размещаем оружие в случайной позиции внутри комнаты
        placeObjectInRoom(weapon);
        
        // Делаем кнопку "Войти в локацию" неактивной
        enterRoomButton.setDisable(true);
        
        // Активируем остальные кнопки
        attackButton.setDisable(false);
        moveButton.setDisable(false);
        pickUpButton.setDisable(false);
        
        // Обновляем список инвентаря (на случай, если уже есть предметы)
        updateInventoryList();
        
        // Обновляем информацию о здоровье
        updateHealthLabel();
        
        // Отрисовываем все объекты на Canvas
        drawGame();
    }
    
    /**
     * Размещает объект в случайной позиции внутри комнаты.
     * 
     * @param obj объект для размещения
     */
    private void placeObjectInRoom(TGameObject obj) {
        // Получаем границы комнаты
        double roomX = room.getX();
        double roomY = room.getY();
        double roomWidth = room.getWidth();
        double roomHeight = room.getHeight();
        
        // Генерируем случайные координаты внутри комнаты с отступом от краев
        double minX = roomX + 20;
        double maxX = roomX + roomWidth - 20;
        double newX = minX + random.nextDouble() * (maxX - minX);
        
        double minY = roomY + 20;
        double maxY = roomY + roomHeight - 20;
        double newY = minY + random.nextDouble() * (maxY - minY);
        
        // Устанавливаем новые координаты объекта
        obj.setX(newX);
        obj.setY(newY);
    }
    
    /**
     * Обработчик нажатия кнопки "Атаковать".
     * 
     * Игрок атакует NPC в комнате.
     */
    private void attackNPC() {
        // Проверяем, что NPC жив
        if (!npc.isAlive()) {
            // Если NPC уже мертв, ничего не делаем
            return;
        }
        
        // Игрок атакует NPC (вся логика находится в методе attack класса TPlayer)
        player.attack(npc);
        
        // Обновляем отрисовку
        drawGame();
        
        // Обновляем информацию о здоровье игрока
        updateHealthLabel();
    }
    
    /**
     * Обработчик нажатия кнопки "Переместиться".
     * 
     * Игрок и NPC случайно перемещаются в комнате.
     */
    private void movePlayer() {
        // Игрок перемещается (вся логика находится в методе move класса TPlayer)
        // Метод move также перемещает всех живых NPC в комнате
        player.move();
        
        // Обновляем отрисовку
        drawGame();
    }
    
    /**
     * Обработчик нажатия кнопки "Поднять предмет".
     * 
     * Игрок поднимает предмет из комнаты и добавляет его в инвентарь.
     */
    private void pickUpItem() {
        // Игрок поднимает предмет (вся логика находится в методе pickUpItem класса TPlayer)
        player.pickUpItem();
        
        // Обновляем список инвентаря
        updateInventoryList();
        
        // Обновляем отрисовку (предмет исчезнет из комнаты)
        drawGame();
        
        // Если предметов в комнате больше нет, делаем кнопку неактивной
        if (room.getItemCount() == 0) {
            pickUpButton.setDisable(true);
        }
    }
    
    /**
     * Отрисовывает все игровые объекты на Canvas.
     * 
     * Рисует комнату, игрока, NPC и предметы.
     */
    private void drawGame() {
        // Очищаем Canvas (закрашиваем белым цветом)
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        // Проверяем, что игрок находится в комнате
        if (player.getCurrentRoom() == null) {
            // Если игрок не в комнате, ничего не рисуем
            return;
        }
        
        // Рисуем комнату (прямоугольник с границами)
        drawRoom();
        
        // Рисуем предметы в комнате
        drawItems();
        
        // Рисуем NPC (если он жив)
        if (npc.isAlive()) {
            drawNPC();
        }
        
        // Рисуем игрока
        drawPlayer();
    }
    
    /**
     * Рисует комнату на Canvas.
     */
    private void drawRoom() {
        // Рисуем название локации над комнатой
        gc.setFill(Color.BLACK);  // Черный цвет для текста
        gc.setFont(Font.font(16));  // Размер шрифта 16, жирный
        // Вычисляем позицию текста по центру комнаты по горизонтали
        // Центрируем текст: позиция комнаты + половина ширины - половина ширины текста (примерно 60 пикселей)
        double textX = room.getX() + room.getWidth() / 2 - 60;  // Примерно по центру
        double textY = room.getY() - 10;  // Над комнатой с небольшим отступом
        gc.fillText(room.getName(), textX, textY);
        
        // Устанавливаем цвет для границ комнаты (черный)
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        
        // Рисуем прямоугольник (границы комнаты)
        gc.strokeRect(room.getX(), room.getY(), room.getWidth(), room.getHeight());
    }
    
    /**
     * Рисует игрока на Canvas.
     */
    private void drawPlayer() {
        // Устанавливаем цвет для игрока (синий)
        gc.setFill(Color.BLUE);
        
        // Рисуем круг для игрока (радиус 15 пикселей)
        // Координаты центра круга = координаты игрока
        gc.fillOval(player.getX() - 15, player.getY() - 15, 30, 30);
        
        // Рисуем название игрока под кругом
        gc.setFill(Color.BLACK);  // Черный цвет для текста
        gc.setFont(Font.font(12));  // Размер шрифта 12
        // Рисуем текст под кругом (координата Y + радиус + отступ)
        gc.fillText(player.getName(), player.getX() - 20, player.getY() + 25);
    }
    
    /**
     * Рисует NPC на Canvas.
     */
    private void drawNPC() {
        // Устанавливаем цвет для NPC (красный)
        gc.setFill(Color.RED);
        
        // Рисуем круг для NPC (радиус 15 пикселей)
        gc.fillOval(npc.getX() - 15, npc.getY() - 15, 30, 30);
        
        // Рисуем название NPC под кругом
        gc.setFill(Color.BLACK);  // Черный цвет для текста
        gc.setFont(Font.font(12));  // Размер шрифта 12
        // Рисуем текст под кругом (координата Y + радиус + отступ)
        gc.fillText(npc.getName(), npc.getX() - 15, npc.getY() + 25);
    }
    
    /**
     * Рисует предметы в комнате на Canvas.
     */
    private void drawItems() {
        // Проходим по всем предметам в комнате
        for (int i = 0; i < room.getItemCount(); i++) {
            TItem item = room.getItem(i);
            // Устанавливаем цвет для предметов (зеленый)
            gc.setFill(Color.GREEN);
            
            // Рисуем квадрат для предмета (размер 10x10 пикселей)
            // Координаты левого верхнего угла = координаты предмета минус половина размера
            gc.fillRect(item.getX() - 5, item.getY() - 5, 10, 10);
            
            // Рисуем название предмета под квадратом
            gc.setFill(Color.BLACK);  // Черный цвет для текста
            gc.setFont(Font.font(10));  // Размер шрифта 10
            // Рисуем текст под квадратом (координата Y + половина размера + отступ)
            gc.fillText(item.getName(), item.getX() - 15, item.getY() + 20);
        }
    }
    
    /**
     * Обновляет список инвентаря в интерфейсе.
     */
    private void updateInventoryList() {
        // Очищаем список
        inventoryList.getItems().clear();
        
        // Проходим по инвентарю игрока
        for (int i = 0; i < player.getInventoryCount(); i++) {
            TItem item = player.getInventoryItem(i);
            // Добавляем имя предмета в список
            String itemName = item.getName();
            
            // Если это оружие, добавляем информацию о силе атаки
            if (item instanceof TWeapon) {
                TWeapon weapon = (TWeapon) item;
                itemName = itemName + " (Атака: +" + weapon.getAttackPower() + ")";
            }
            
            // Добавляем предмет в список
            inventoryList.getItems().add(itemName);
        }
    }
    
    /**
     * Обновляет метку с информацией о здоровье игрока.
     */
    private void updateHealthLabel() {
        // Обновляем текст метки с текущим здоровьем игрока
        healthLabel.setText("Здоровье: " + player.getHealth());
    }
    
    /**
     * Главный метод приложения - точка входа в программу.
     * 
     * Этот метод запускает JavaFX приложение. JavaFX требует специальной инициализации,
     * поэтому используется метод launch() из класса Application.
     * 
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        // Запускаем JavaFX приложение
        // Метод launch() автоматически инициализирует JavaFX runtime и вызывает метод start()
        launch(args);
    }
}
