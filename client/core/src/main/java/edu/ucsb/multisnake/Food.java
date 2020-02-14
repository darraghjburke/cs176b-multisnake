package edu.ucsb.multisnake;

import edu.ucsb.multisnake.Utils.IntPair;

public class Food {
    private IntPair position;
    private int r, g, b;
    private int size;

    public Food(int x, int y, int size, int r, int g, int b) {
        this.position = new IntPair(x,y);
        this.size = size;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public IntPair getPosition() {
        return this.position;
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