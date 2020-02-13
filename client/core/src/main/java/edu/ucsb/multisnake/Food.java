package edu.ucsb.multisnake;

public class Food {
    private int x, y;
    private int r, g, b;
    private int size;

    public Food(int x, int y, int size, int r, int g, int b) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getSize() {
        return this.size;
    }

    public int getR() {
        return this.r;
    }

    public int getG() {
        return this.g;
    }

    public int getB() {
        return this.b;
    }
}