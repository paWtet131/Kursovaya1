package com.mudgame.model;

/**
 * Класс оружия - специальный тип предмета.
 * 
 * Оружие наследуется от TItem, но имеет дополнительный атрибут - силу атаки.
 * Когда игрок поднимает оружие, он может использовать его силу атаки для нанесения урона врагам.
 * 
 * Взаимодействия (Collaborators):
 * - TPlayer - игрок использует оружие для атаки (сила атаки оружия увеличивает урон игрока)
 */
public class TWeapon extends TItem {
    
    // Сила атаки оружия - определяет, сколько дополнительного урона наносит оружие
    private int attackPower;
    
    /**
     * Конструктор - создает новое оружие.
     * 
     * @param name имя оружия (например, "Меч", "Кинжал")
     * @param id уникальный идентификатор оружия
     * @param x координата X позиции оружия
     * @param y координата Y позиции оружия
     * @param attackPower сила атаки оружия
     */
    public TWeapon(String name, String id, double x, double y, int attackPower) {
        // Вызываем конструктор родительского класса TItem
        super(name, id, x, y);
        
        // Сохраняем силу атаки оружия
        this.attackPower = attackPower;
    }
    
    /**
     * Получить силу атаки оружия.
     * 
     * @return сила атаки
     */
    public int getAttackPower() {
        return attackPower;
    }
}
