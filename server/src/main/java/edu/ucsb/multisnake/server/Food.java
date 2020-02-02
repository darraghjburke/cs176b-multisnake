package edu.ucsb.multisnake.server;

public class Food {
    private int x, y;
    private int r, g, b;

    public Food(int x, int y) {
        this.x = x;
        this.y = y;
        this.r = 255;
        this.g = 255;
        this.b = 255;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


}