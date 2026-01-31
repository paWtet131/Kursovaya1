package com.mudgame.model;

import java.util.Random;

/**
 * Класс игрока - главный персонаж, которым управляет пользователь.
 * 
 * Игрок может атаковать NPC, перемещаться по комнате и поднимать предметы.
 * Игрок имеет здоровье, инвентарь и текущую комнату, в которой находится.
 * 
 * Взаимодействия (Collaborators):
 * - TRoom - игрок находится в комнате, получает доступ к предметам и NPC
 * - TNPC - игрок атакует NPC, рассчитывает урон
 * - TItem - игрок поднимает предметы и добавляет их в инвентарь
 * - TWeapon - игрок использует оружие из инвентаря для увеличения силы атаки
 */
public class TPlayer extends TGameObject {
    
    // Максимальное количество предметов в инвентаре
    private static final int MAX_INVENTORY = 10;
    
    // Здоровье игрока - когда становится 0 или меньше, игрок умирает
    private int health;
    
    // Инвентарь игрока - массив предметов, которые игрок поднял
    private TItem[] inventory;
    
    // Количество предметов в инвентаре
    private int inventoryCount;
    
    // Текущая комната, в которой находится игрок
    private TRoom currentRoom;
    
    // Базовая сила атаки игрока (без оружия)
    private int baseAttackPower;
    
    // Объект для генерации случайных чисел (для случайного перемещения)
    private Random random;
    
    /**
     * Конструктор - создает нового игрока.
     * 
     * @param name имя игрока
     * @param id уникальный идентификатор игрока
     * @param x координата X начальной позиции
     * @param y координата Y начальной позиции
     * @param health начальное здоровье
     * @param baseAttackPower базовая сила атаки
     */
    public TPlayer(String name, String id, double x, double y, int health, int baseAttackPower) {
        // Вызываем конструктор родительского класса TGameObject
        super(name, id, x, y);
        
        // Сохраняем здоровье и базовую силу атаки
        this.health = health;
        this.baseAttackPower = baseAttackPower;
        
        // Создаем пустой инвентарь (массив)
        this.inventory = new TItem[MAX_INVENTORY];
        this.inventoryCount = 0;
        
        // Изначально игрок не находится ни в одной комнате
        this.currentRoom = null;
        
        // Создаем объект для генерации случайных чисел
        this.random = new Random();
    }
    
    /**
     * Получить текущее здоровье игрока.
     * 
     * @return здоровье игрока
     */
    public int getHealth() {
        return health;
    }
    
    /**
     * Получить количество предметов в инвентаре.
     */
    public int getInventoryCount() {
        return inventoryCount;
    }
    
    /**
     * Получить предмет из инвентаря по индексу (индекс от 0 до getInventoryCount()-1).
     */
    public TItem getInventoryItem(int index) {
        if (index >= 0 && index < inventoryCount) {
            return inventory[index];
        }
        return null;
    }
    
    /**
     * Получить текущую комнату игрока.
     * 
     * @return комната, в которой находится игрок
     */
    public TRoom getCurrentRoom() {
        return currentRoom;
    }
    
    /**
     * Установить текущую комнату игрока.
     * 
     * @param currentRoom комната, в которую входит игрок
     */
    public void setCurrentRoom(TRoom currentRoom) {
        this.currentRoom = currentRoom;
    }
    
    /**
     * Получить общую силу атаки игрока.
     * 
     * Общая сила атаки = базовая сила атаки + сила атаки всех оружий в инвентаре.
     * 
     * @return общая сила атаки
     */
    public int getTotalAttackPower() {
        // Начинаем с базовой силы атаки
        int total = baseAttackPower;
        
        // Проходим по всем предметам в инвентаре
        for (int i = 0; i < inventoryCount; i++) {
            TItem item = inventory[i];
            // Если предмет является оружием (TWeapon)
            if (item instanceof TWeapon) {
                // Приводим предмет к типу TWeapon
                TWeapon weapon = (TWeapon) item;
                // Добавляем силу атаки оружия к общей силе атаки
                total = total + weapon.getAttackPower();
            }
        }
        
        // Возвращаем общую силу атаки
        return total;
    }
    
    /**
     * Атаковать NPC.
     * 
     * Игрок атакует NPC в текущей комнате. Урон рассчитывается как:
     * урон = общая_сила_атаки_игрока - защита_NPC
     * 
     * @param npc NPC, которого нужно атаковать
     */
    public void attack(TNPC npc) {
        // Проверяем, что игрок находится в комнате
        if (currentRoom == null) {
            // Если игрок не в комнате, ничего не делаем
            return;
        }
        
        // Проверяем, что NPC жив
        if (!npc.isAlive()) {
            // Если NPC уже мертв, ничего не делаем
            return;
        }
        
        // Получаем общую силу атаки игрока (базовая + оружие из инвентаря)
        int attackPower = getTotalAttackPower();
        
        // Рассчитываем урон: сила атаки игрока минус защита NPC
        int damage = attackPower - npc.getDefense();
        
        // Если урон получился отрицательным или нулевым, устанавливаем минимальный урон = 1
        if (damage <= 0) {
            damage = 1;
        }
        
        // Наносим урон NPC
        npc.takeDamage(damage);
    }
    
    /**
     * Случайно переместиться в пределах текущей комнаты.
     * 
     * Игрок перемещается на случайные координаты внутри комнаты.
     * После перемещения игрока NPC в комнате тоже случайно перемещается.
     */
    public void move() {
        // Проверяем, что игрок находится в комнате
        if (currentRoom == null) {
            // Если игрок не в комнате, ничего не делаем
            return;
        }
        
        // Получаем границы комнаты
        double roomX = currentRoom.getX();
        double roomY = currentRoom.getY();
        double roomWidth = currentRoom.getWidth();
        double roomHeight = currentRoom.getHeight();
        
        // Генерируем случайные координаты внутри комнаты
        // Минимальная координата X = координата комнаты + небольшой отступ
        // Максимальная координата X = координата комнаты + ширина комнаты - отступ
        double minX = roomX + 20;
        double maxX = roomX + roomWidth - 20;
        double newX = minX + random.nextDouble() * (maxX - minX);
        
        // Аналогично для координаты Y
        double minY = roomY + 20;
        double maxY = roomY + roomHeight - 20;
        double newY = minY + random.nextDouble() * (maxY - minY);
        
        // Устанавливаем новые координаты игрока
        setX(newX);
        setY(newY);
        
        // Перемещаем всех NPC в комнате случайным образом
        for (int i = 0; i < currentRoom.getNPCCount(); i++) {
            TNPC npc = currentRoom.getNPC(i);
            // Если NPC жив, перемещаем его
            if (npc.isAlive()) {
                npc.moveRandom();
            }
        }
    }
    
    /**
     * Поднять предмет из текущей комнаты.
     * 
     * Игрок поднимает первый доступный предмет из комнаты и добавляет его в инвентарь.
     * Предмет удаляется из комнаты.
     */
    public void pickUpItem() {
        // Проверяем, что игрок находится в комнате
        if (currentRoom == null) {
            // Если игрок не в комнате, ничего не делаем
            return;
        }
        
        // Проверяем, есть ли предметы в комнате
        if (currentRoom.getItemCount() == 0) {
            // Если предметов нет, ничего не делаем
            return;
        }
        
        // Берем первый предмет
        TItem item = currentRoom.getItem(0);
        
        // Удаляем предмет из комнаты
        currentRoom.removeItem(item);
        
        // Добавляем предмет в инвентарь игрока
        if (inventoryCount < MAX_INVENTORY) {
            inventory[inventoryCount] = item;
            inventoryCount = inventoryCount + 1;
        }
    }
}
