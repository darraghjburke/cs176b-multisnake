package edu.ucsb.multisnake.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.Color;
import edu.ucsb.multisnake.server.Utils.IntPair;


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

        // System.out.println("Hue: " + hue + ", RGB: " + r + "," + g + "," + b);
        IntPair pos = randomizePosition();
        RGBColor color = makeNewPlayerColor();
        
        Player p = new Player(numOfPlayers, pos.getX(), pos.getY(), color.r, color.g, color.b);

        players.add(p);
        return p;
    }

    public void killPlayer(Player p) {
        System.out.println("KILLED PLAYER " + p.getId());
        spawnFood(p.getHead(), p.getTargetLength(), p.getR(), p.getG(), p.getB());
        List<IntPair> newPosition = new ArrayList<IntPair>();
        newPosition.add(randomizePosition());
        p.setPositions(newPosition);
        RGBColor color = makeNewPlayerColor();
        p.setColor(color.r, color.g, color.b);
    }

    public IntPair randomizePosition() {
        Random rand = new Random();
        IntPair pair;
        do {
            pair = new IntPair(rand.nextInt(radius), rand.nextInt(radius));
        } while (distanceToCenter(pair) > radius);
        return pair;
    }

    public Food spawnFood() {
        Random rand = new Random();
        int size;
        size = rand.nextInt(20) + 5;
        
        float saturation = 225;
        float brightness = 255;
        int HUE_FACTOR = 30;
        float hue = (numOfFood++ * HUE_FACTOR) % 255;

        int rgb = Color.HSBtoRGB(hue/255, saturation/255, brightness/255);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return spawnFood(randomizePosition(), size, r, g, b);
    }

    public class RGBColor {
        public int r;
        public int g;
        public int b;

        public RGBColor(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

    }

    public RGBColor makeNewPlayerColor() {
        Random rand = new Random();
        
        float saturation = 175;
        float brightness = 175;
        int HUE_FACTOR = 30;
        float hue = (numOfPlayers++ * HUE_FACTOR) % 255;

        int rgb = Color.HSBtoRGB(hue/255, saturation/255, brightness/255);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return new RGBColor(r,g,b);
    }

    public Food spawnFood(IntPair position, int size, int r, int g, int b) {        
        Food f = new Food(position.getX(), position.getY(), size, r, g, b);
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
                for (Player player: getPlayers()) {
                    if (player.getPositions().size() > 0 && distanceToCenter(player.getHead()) > radius) {
                        killPlayer(player);
                    }
                }
                
                for(Player player : getPlayers()) {
                    if (player.getConnection() != null) {
                        player.getConnection().broadcast();
                        player.getConnection().broadcastFood();
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