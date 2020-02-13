package edu.ucsb.multisnake.server;

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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setG(int g) {
        this.g = g;
    }

    public void setB(int b) {
        this.b = b;
    }
}