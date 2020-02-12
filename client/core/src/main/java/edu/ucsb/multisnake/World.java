package edu.ucsb.multisnake;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
import java.util.Random;
import java.awt.Color;

public class World extends Thread {
    private List<Player> otherPlayers;
    private int numOfPlayers = 0;
    private List<Food> food;
    private int radius = 400;
    private Player me;

    public World() {
        otherPlayers = new ArrayList<Player>();
        food = new ArrayList<Food>();
        me = null;
        // run();
    }

    public void deletePlayerWithId(int id) {
        for (int i = 0; i < otherPlayers.size(); i++) {
            if (otherPlayers.get(i).getId() == id) {
                otherPlayers.remove(i);
                break;
            }
        }
    }

    public void printWorld() {
        for (int i = 0; i < otherPlayers.size(); i++) {
            System.out.println(otherPlayers.get(i).toString());
        }
        System.out.println(me.toString());
    }

    public void run() {
        long lastLoopTime = System.currentTimeMillis();
        while (true) {
            long now = System.currentTimeMillis();
            long updateLength = now - lastLoopTime;
            if (updateLength >= 17 && me != null && me.getConnection() != null) {
                lastLoopTime = now;
                me.getConnection().send_location();
            }
        }
    }

    public int getNumOfPlayers(){
        return this.numOfPlayers;
    }

    public void setNumofPlayers(int num){
        this.numOfPlayers = num;
    }

    public Player getMe(){
        return this.me;
    }

    public void setMe(Player p){
        this.me = p;
    }

    public List<Player> getPlayers() {
        return this.otherPlayers;
    }

    public List<Food> getFood() {
        return this.food;
    }

    public int getRadius() {
        return radius;
    }
}