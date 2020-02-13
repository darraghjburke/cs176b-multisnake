package edu.ucsb.multisnake;

import java.util.ArrayList;
import java.util.List;

public class World extends Thread {
    private List<Player> Players;
    private int numOfPlayers = 0;
    private List<Food> food;
    private int radius = 400;
    private Player me;

    public World() {
        Players = new ArrayList<Player>();
        food = new ArrayList<Food>();
        me = null;
        // run();
    }

    public void addPlayer(Player p) {
        Players.add(p);
        numOfPlayers++;
    }

    public void deletePlayerWithId(int id) {
        for (int i = 0; i < Players.size(); i++) {
            if (Players.get(i).getId() == id) {
                Players.remove(i);
                break;
            }
        }
    }

    public Player getPlayerWithId(int id) {
        for (int i = 0; i < Players.size(); i++) {
            Player pl = Players.get(i);
            if (pl.getId()==id) {
                return pl;
            }
        }
        return null;
    }

    /*
    public void printWorld() {
        for (int i = 0; i < otherPlayers.size(); i++) {
            System.out.println(otherPlayers.get(i).toString());
        }
        System.out.println(me.toString());
    }*/

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
        return this.Players;
    }

    public List<Food> getFood() {
        return this.food;
    }

    public int getRadius() {
        return radius;
    }
}