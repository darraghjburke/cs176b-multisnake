package edu.ucsb.multisnake.server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

public class Player {
    private int id;
    private int x, y;
    private int r, b, g;

    public Player(int id, int x, int y, int r, int b, int g) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.r = r;
        this.b = b;
        this.g = g;
    }

    public int getId() {
        return this.id;
    }

    public int getX()
    {
		return this.x;
	}
    
    public int getY() {
        return this.y;
    }

    public void setX(int x)
    {
		this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }

    public int getR()
    {
		return this.r;
    }
    
    public int getB()
    {
		return this.b;
	}

    public int getG()
    {
		return this.g;
    }
    
    public void setColor(int r, int b, int g)
    {
        this.r = r;
        this.b = b;
        this.g = g;
    }
    
    public String toString() {
        return id + "," + x + "," + y + "," + r + "," + b + "," + g;
    }

}