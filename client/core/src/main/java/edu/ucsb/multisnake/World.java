package edu.ucsb.multisnake;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.graphics.Color;

import org.mini2Dx.core.graphics.Graphics;

import edu.ucsb.multisnake.Utils.IntPair;

public class World extends Thread {
    private List<Player> players;
    private int numOfPlayers = 0;
    private List<Food> food;
    private int numOfFood = 0;
    private int radius = 400;
    private IntPair center = new IntPair(400,400);

    public World() {
        players = new CopyOnWriteArrayList<Player>();
        food = new ArrayList<Food>();
        // run();
    }

    public void addPlayer(Player p) {
        players.add(p);
        numOfPlayers++;
    }

    public void setFood(List<Food> foodList) {
        food = foodList;
        numOfFood = foodList.size();
    }

    public void deletePlayerWithId(int id) {
        for (Player player : players) {
            if (player.getId() == id) {
                players.remove(player);
                break;
            }
        }
        numOfPlayers--;
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
        System.out.println("World loop started");
        while (true) {
            long now = System.currentTimeMillis();
            long updateLength = now - lastLoopTime;
            if (updateLength >= 17) {
                lastLoopTime = now;
                if (MultiSnake.getConnection() != null)
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
        return players;
    }

    public List<Food> getFood() {
        return this.food;
    }

    public int getRadius() {
        return radius;
    }

    public void render(Graphics gfx) {
        gfx.setColor(Color.DARK_GRAY);
        gfx.fillRect(0,0,center.getX() * 2,center.getY() * 2);
        gfx.setColor(Color.WHITE);
        gfx.fillCircle(center.getX(), center.getY(), radius); // circle size
        for(Player pl: getPlayers()) {
            pl.render(gfx);
        }

        for (Food f: getFood()) {
            f.render(gfx);
        }
    }
}