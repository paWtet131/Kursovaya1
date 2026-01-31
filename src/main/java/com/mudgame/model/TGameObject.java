package com.mudgame.model;

/**
 * Базовый абстрактный класс для всех игровых объектов.
 * 
 * Этот класс содержит общие атрибуты, которые есть у всех объектов в игре:
 * - имя объекта
 * - уникальный идентификатор
 * - координаты позиции на экране
 * 
 * Все классы игровых объектов (TPlayer, TItem, TNPC) наследуются от этого класса.
 */
public abstract class TGameObject {
    
    // Имя объекта - например, "Игрок", "Меч", "Враг"
    private String name;
    
    // Уникальный идентификатор объекта - используется для различения объектов
    private String id;
    
    // Координата X - позиция объекта по горизонтали на экране
    private double x;
    
    // Координата Y - позиция объекта по вертикали на экране
    private double y;
    
    /**
     * Конструктор - создает новый игровой объект с заданными параметрами.
     * 
     * @param name имя объекта
     * @param id уникальный идентификатор
     * @param x координата X
     * @param y координата Y
     */
    public TGameObject(String name, String id, double x, double y) {
        // Сохраняем переданные значения в поля объекта
        this.name = name;
        this.id = id;
        this.x = x;
        this.y = y;
    }
    
    /**
     * Получить имя объекта.
     * 
     * @return имя объекта
     */
    public String getName() {
        return name;
    }
    
    /**
     * Получить координату X (горизонтальная позиция).
     * 
     * @return координата X
     */
    public double getX() {
        return x;
    }
    
    /**
     * Установить новую координату X.
     * 
     * @param x новая координата X
     */
    public void setX(double x) {
        this.x = x;
    }
    
    /**
     * Получить координату Y (вертикальная позиция).
     * 
     * @return координата Y
     */
    public double getY() {
        return y;
    }
    
    /**
     * Установить новую координату Y.
     * 
     * @param y новая координата Y
     */
    public void setY(double y) {
        this.y = y;
    }
}
