package edu.ucsb.multisnake;

import com.badlogic.gdx.graphics.Color;

import org.mini2Dx.core.graphics.Graphics;

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

    public void render(Graphics gfx) {
        Color c = new Color(r/255f, g/255f, b/255f, 1f);
        gfx.setColor(c);
        int x = position.getX();
        int y = position.getY();
        int size = getSize();
        gfx.fillRect(x, y, (float)size/2, (float)size/2);
    }
}