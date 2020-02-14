package edu.ucsb.multisnake;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.graphics.Color;

import org.mini2Dx.core.graphics.Graphics;

import edu.ucsb.multisnake.Utils.IntPair;

public class Player {
    public static final int MINIMUM_DISTANCE = 10; // how close we can get to our goal before we stop rotating
    private int id;
    private int r, b, g;
    private int targetLength;
    private List<IntPair> positions;
    private Connection conn;
    private double direction;
    private boolean isMe;

    public Player(int id, int r, int g, int b) {
        this.id = id;
        this.positions = new CopyOnWriteArrayList<IntPair>();
        this.r = r;
        this.g = g;
        this.b = b;
        this.targetLength = 1;
        isMe = false;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTargetLength() {
        return this.targetLength;
    }

    public void setTargetLength(int length) {
        this.targetLength = length;
    }

    public void setDirection(double dir) {
        direction = dir;
    }

    public double getDirection() {
        return direction;
    }

    public int getR()
    {
        return this.r;
    }
    
    public int getG()
    {
        return this.g;
    }

    public int getB()
    {
        return this.b;
    }
    
    public void setColor(int r, int g, int b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public List<IntPair> getPositions() {
        return positions;
    }

    public IntPair getHead() {
        return this.positions.get(0);
    }

    public void addPositions(IntPair p) {
        this.positions.add(p);
    }

    public void setPositions(List<IntPair> p){
        this.positions = p;
    }

    // delete the last position and append to the head
    public void move(IntPair p) { 
        this.positions.remove(this.positions.size()-1);
        this.positions.add(p);
    }

    public void setConnection(Connection c) {
        this.conn = c;
    }

    public Connection getConnection() {
        return this.conn;
    }

    public double getSpeed() {
        return 3;
    }

    public double getTurnSpeed() {
        return 0.1;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean isMe) {
        this.isMe = isMe;
    }

    public void moveTowards(IntPair target) {
        double angle = getHead().angleTo(target); // compute the angle to that position
        double dist = IntPair.shortAngleDist(direction, angle); // figure out whether it's faster to rotate clockwise or counter clockwise to match that angle
        double change = Math.min(getTurnSpeed(), Math.abs(dist)); // limit our rotation to our max turn speed
        if (dist < 0) change *= -1;
        if (getHead().distanceTo(target) > MINIMUM_DISTANCE) {
            direction = direction + change; // update our direction
        }
        move(getHead().moveDirection(direction, getSpeed())); // calculate our new position based on our direction and speed
    }

    public void render(Graphics gfx) {
        Color c = new Color(r/255f, g/255f, b/255f, 1f);
        gfx.setColor(c);
        for (IntPair pos: positions) {
            gfx.fillCircle(pos.getX(), pos.getY(), 40);
        }
    }

}