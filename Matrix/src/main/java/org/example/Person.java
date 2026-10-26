package org.example;

public class Person {
    private int x;
    private int y;
    private int health;

    public Person(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.health = 3;
    }

    public void move(String direction) {
        switch (direction) {
            case "W": y--; break;
            case "S": y++; break;
            case "A": x--; break;
            case "D": x++; break;
        }
    }

    public void decreaseHealth() {
        this.health--;
    }

    public int getHealth() {
        return health;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}