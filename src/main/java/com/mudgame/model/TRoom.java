package com.mudgame.model;

/**
 * Класс локации (комнаты) в игре.
 * 
 * Комната — это контейнер для предметов и NPC, а не игровой объект.
 * Она имеет имя, идентификатор, координаты и размеры для отрисовки на экране.
 * 
 * Взаимодействия (Collaborators):
 * - TItem - хранит предметы в комнате
 * - TNPC - хранит NPC в комнате
 * - TPlayer - игрок взаимодействует с комнатой (поднимает предметы, атакует NPC)
 */
public class TRoom {
    
    // Максимальное количество предметов в комнате
    private static final int MAX_ITEMS = 10;
    
    // Максимальное количество NPC в комнате
    private static final int MAX_NPCS = 10;
    
    // Имя комнаты (например, "Сумеречный лес")
    private String name;
    
    // Уникальный идентификатор комнаты
    private String id;
    
    // Координата X левого верхнего угла комнаты (для отрисовки)
    private double x;
    
    // Координата Y левого верхнего угла комнаты (для отрисовки)
    private double y;
    
    // Массив предметов в комнате
    private TItem[] items;
    
    // Количество предметов в комнате
    private int itemCount;
    
    // Массив NPC в комнате
    private TNPC[] npcs;
    
    // Количество NPC в комнате
    private int npcCount;
    
    // Ширина комнаты в пикселях (для отрисовки на экране)
    private double width;
    
    // Высота комнаты в пикселях (для отрисовки на экране)
    private double height;
    
    /**
     * Конструктор - создает новую комнату.
     * 
     * @param name имя комнаты
     * @param id уникальный идентификатор комнаты
     * @param x координата X левого верхнего угла комнаты
     * @param y координата Y левого верхнего угла комнаты
     * @param width ширина комнаты
     * @param height высота комнаты
     */
    public TRoom(String name, String id, double x, double y, double width, double height) {
        this.name = name;
        this.id = id;
        this.x = x;
        this.y = y;
        this.items = new TItem[MAX_ITEMS];
        this.itemCount = 0;
        this.npcs = new TNPC[MAX_NPCS];
        this.npcCount = 0;
        this.width = width;
        this.height = height;
    }
    
    /**
     * Получить имя комнаты.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Получить координату X (левая граница комнаты).
     */
    public double getX() {
        return x;
    }
    
    /**
     * Получить координату Y (верхняя граница комнаты).
     */
    public double getY() {
        return y;
    }
    
    /**
     * Добавить предмет в комнату.
     * 
     * @param item предмет, который нужно добавить
     */
    public void addItem(TItem item) {
        if (itemCount < MAX_ITEMS) {
            items[itemCount] = item;
            itemCount = itemCount + 1;
            item.setLocation(this);
        }
    }
    
    /**
     * Удалить предмет из комнаты.
     * 
     * @param item предмет, который нужно удалить
     */
    public void removeItem(TItem item) {
        for (int i = 0; i < itemCount; i++) {
            if (items[i] == item) {
                // Сдвигаем элементы после удаляемого на одну позицию влево
                for (int j = i; j < itemCount - 1; j++) {
                    items[j] = items[j + 1];
                }
                items[itemCount - 1] = null;
                itemCount = itemCount - 1;
                item.setLocation(null);
                break;
            }
        }
    }
    
    /**
     * Получить количество предметов в комнате.
     */
    public int getItemCount() {
        return itemCount;
    }
    
    /**
     * Получить предмет по индексу (индекс от 0 до getItemCount()-1).
     */
    public TItem getItem(int index) {
        if (index >= 0 && index < itemCount) {
            return items[index];
        }
        return null;
    }
    
    /**
     * Добавить NPC в комнату.
     * 
     * @param npc NPC, которого нужно добавить
     */
    public void addNPC(TNPC npc) {
        if (npcCount < MAX_NPCS) {
            npcs[npcCount] = npc;
            npcCount = npcCount + 1;
            npc.setLocation(this);
        }
    }
    
    /**
     * Получить количество NPC в комнате.
     */
    public int getNPCCount() {
        return npcCount;
    }
    
    /**
     * Получить NPC по индексу (индекс от 0 до getNPCCount()-1).
     */
    public TNPC getNPC(int index) {
        if (index >= 0 && index < npcCount) {
            return npcs[index];
        }
        return null;
    }
    
    /**
     * Получить ширину комнаты.
     * 
     * @return ширина комнаты
     */
    public double getWidth() {
        return width;
    }
    
    /**
     * Получить высоту комнаты.
     * 
     * @return высота комнаты
     */
    public double getHeight() {
        return height;
    }
}
