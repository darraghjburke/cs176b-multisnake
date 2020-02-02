package edu.ucsb.multisnake.server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

public class Food {
    private int x, y;
    private int r, b, g;

    public Food(int x, int y) {
        this.x = x;
        this.y = y;
        this.r = 255;
        this.b = 255;
        this.g = 255;
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