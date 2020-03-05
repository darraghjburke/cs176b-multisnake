package edu.ucsb.multisnake.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.Color;
import edu.ucsb.multisnake.server.Utils.IntPair;
import java.lang.Object;

public class World extends Thread{
    private List<Player> players;
    private int numOfPlayers = 0, numOfFood = 0;
    private List<Food> food;
    private int radius = 400;
    private IntPair center = new IntPair(400,400);

    private final int FOOD_AMOUNT = 10;
    private final int UPDATE_TIME = 48;

    public World() {
        players = new CopyOnWriteArrayList<Player>();
        food = new ArrayList<Food>();
        while (food.size() < FOOD_AMOUNT) {
            spawnFood();
        }
        // run();
    }

    private double distanceToCenter(IntPair pos) {
        return pos.distanceTo(center);
    }

    public Player spawnPlayer() {
        Random rand = new Random();
        int x, y;
        float saturation = 175;
        float brightness = 175;
        int HUE_FACTOR = 30;
        float hue = (numOfPlayers * HUE_FACTOR) % 255;

        int rgb = Color.HSBtoRGB(hue/255, saturation/255, brightness/255);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        // System.out.println("Hue: " + hue + ", RGB: " + r + "," + g + "," + b);
        do {
            x = rand.nextInt(radius);
            y = rand.nextInt(radius);
        } while (distanceToCenter(new IntPair(x,y)) > radius);
        Player p = new Player(numOfPlayers++, x, y, r, g, b);

        players.add(p);
        return p;
    }
    
    public void interpolate(Player p) { 
        //The client needs to know his last two position-updates
        //AND the time since the last update from the server
        IntPair newPosition = p.getHead();
        IntPair oldPosition = p.getPositions().get(1);
        int x_pos = oldPosition.getX(); 
        int y_pos = oldPosition.getY();
        int differenceX = newPosition.getX() - x_pos;
        int differenceY = oldPosition.getY() - y_pos;

        x_pos += differenceX / UPDATE_TIME;
        y_pos += differenceY / UPDATE_TIME;
        IntPair updatedPosition = new IntPair(x_pos, y_pos);
        p.setPosition(updatedPosition);
    }

    public Food spawnFood() {
        Random rand = new Random();
        int x, y, size;
        float saturation = 225;
        float brightness = 255;
        int HUE_FACTOR = 30;
        float hue = (numOfFood++ * HUE_FACTOR) % 255;

        int rgb = Color.HSBtoRGB(hue/255, saturation/255, brightness/255);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        do {
            x = rand.nextInt(radius*2);
            y = rand.nextInt(radius*2);
            size = rand.nextInt(20) + 5; // size is random from 5 to 25 (inclusive)
        } while (distanceToCenter(new IntPair(x,y)) > radius);
        Food f = new Food(x, y, size, r, g, b);
        food.add(f);
        return f;
    }

    public void deletePlayerWithId(int id) {
        for(Player player: players) {
            if (player.getId() == id) {
                players.remove(player);
                break;
            }
        }
    }

    public void printWorld() {
        System.out.println("PLAYERS");
        for (Player player: getPlayers()) {
            System.out.printf("{%s},", player.toString());
        }
        System.out.printf("/n");
    }

    public void run() {
        long lastLoopTime = System.currentTimeMillis();
        while (true) {
            long now = System.currentTimeMillis();
            long updateLength = now - lastLoopTime;
            if (updateLength >= UPDATE_TIME) {
                lastLoopTime = now;
                if (food.size() < FOOD_AMOUNT) {
                    spawnFood();
                }
                for(Player player : getPlayers()) {
                    if (player.getConnection() != null) {
                        player.getConnection().broadcast();
                        player.getConnection().broadcastFood();
                        // interpolate(player);
                    }
                }
                // printWorld();
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public List<Player> getPlayers() {
        return players;
    }
    
    public List<Food> getFood() {
        return this.food;
    }
}