package com.mudgame.model;

import java.util.Random;

/**
 * Класс NPC (Non-Player Character) - неигровой персонаж.
 * 
 * NPC - это враг, с которым игрок может сражаться.
 * NPC имеет здоровье, защиту и может случайно перемещаться по комнате.
 * 
 * Взаимодействия (Collaborators):
 * - TRoom - NPC находится в комнате
 * - TPlayer - NPC получает урон от игрока при атаке
 */
public class TNPC extends TGameObject {
    
    // Здоровье NPC - когда становится 0 или меньше, NPC умирает
    private int health;
    
    // Защита NPC - уменьшает получаемый урон от атак
    private int defense;
    
    // Комната, в которой находится NPC
    private TRoom location;
    
    // Объект для генерации случайных чисел (для случайного перемещения)
    private Random random;
    
    /**
     * Конструктор - создает нового NPC.
     * 
     * @param name имя NPC (например, "Враг", "Гоблин")
     * @param id уникальный идентификатор NPC
     * @param x координата X начальной позиции
     * @param y координата Y начальной позиции
     * @param health начальное здоровье
     * @param defense защита NPC
     */
    public TNPC(String name, String id, double x, double y, int health, int defense) {
        // Вызываем конструктор родительского класса TGameObject
        super(name, id, x, y);
        
        // Сохраняем здоровье и защиту
        this.health = health;
        this.defense = defense;
        
        // Изначально NPC не находится ни в одной комнате
        this.location = null;
        
        // Создаем объект для генерации случайных чисел
        this.random = new Random();
    }
    
    /**
     * Получить защиту NPC.
     * 
     * @return защита NPC
     */
    public int getDefense() {
        return defense;
    }
    
    /**
     * Получить комнату, в которой находится NPC.
     * 
     * @return комната, где находится NPC
     */
    public TRoom getLocation() {
        return location;
    }
    
    /**
     * Установить комнату, в которой находится NPC.
     * 
     * @param location комната, где находится NPC
     */
    public void setLocation(TRoom location) {
        this.location = location;
    }
    
    /**
     * Получить урон от атаки.
     * 
     * Этот метод вызывается игроком, когда он атакует NPC.
     * Урон рассчитывается как: урон = сила_атаки_игрока - защита_NPC
     * 
     * @param damage количество урона, которое нужно нанести
     */
    public void takeDamage(int damage) {
        // Уменьшаем здоровье на полученный урон
        health = health - damage;
        
        // Если здоровье стало отрицательным, устанавливаем его в 0
        if (health < 0) {
            health = 0;
        }
    }
    
    /**
     * Проверить, жив ли NPC.
     * 
     * @return true если здоровье больше 0, false если NPC мертв
     */
    public boolean isAlive() {
        return health > 0;
    }
    
    /**
     * Случайно переместить NPC в пределах комнаты.
     * 
     * Этот метод генерирует случайные координаты внутри комнаты и перемещает NPC туда.
     * Вызывается автоматически при перемещении игрока.
     */
    public void moveRandom() {
        // Проверяем, что NPC находится в комнате
        if (location == null) {
            // Если NPC не в комнате, ничего не делаем
            return;
        }
        
        // Получаем границы комнаты
        double roomX = location.getX();
        double roomY = location.getY();
        double roomWidth = location.getWidth();
        double roomHeight = location.getHeight();
        
        // Генерируем случайные координаты внутри комнаты
        // Минимальная координата X = координата комнаты + небольшой отступ (чтобы не вплотную к стене)
        // Максимальная координата X = координата комнаты + ширина комнаты - отступ
        double minX = roomX + 20;
        double maxX = roomX + roomWidth - 20;
        double newX = minX + random.nextDouble() * (maxX - minX);
        
        // Аналогично для координаты Y
        double minY = roomY + 20;
        double maxY = roomY + roomHeight - 20;
        double newY = minY + random.nextDouble() * (maxY - minY);
        
        // Устанавливаем новые координаты NPC
        setX(newX);
        setY(newY);
    }
}
