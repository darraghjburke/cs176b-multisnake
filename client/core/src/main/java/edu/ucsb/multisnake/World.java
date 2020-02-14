package edu.ucsb.multisnake;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class World extends Thread {
    private List<Player> players;
    private int numOfPlayers = 0;
    private List<Food> food;
    private int radius = 400;

    public World() {
        players = new ArrayList<Player>();
        food = new ArrayList<Food>();
        // run();
    }

    public void addPlayer(Player p) {
        players.add(p);
        numOfPlayers++;
    }

    public void deletePlayerWithId(int id) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId() == id) {
                players.remove(i);
                break;
            }
        }
    }

    public Player getPlayerWithId(int id) {
        for (Player pl: getPlayers()) {
            if (pl.getId()==id) {
                return pl;
            }
        }
        return null;
    }

    public Player findMe() {
        for (Player pl: getPlayers()) {
            if (pl.isMe()) return pl;
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
            if (updateLength >= 17 && MultiSnake.getConnection() != null) {
                lastLoopTime = now;
                MultiSnake.getConnection().send_location();
            }
        }
    }

    public int getNumOfPlayers(){
        return this.numOfPlayers;
    }

    public void setNumofPlayers(int num){
        this.numOfPlayers = num;
    }

    public List<Player> getPlayers() {
        return Collections.synchronizedList(players);
    }

    public List<Food> getFood() {
        return this.food;
    }

    public int getRadius() {
        return radius;
    }
}