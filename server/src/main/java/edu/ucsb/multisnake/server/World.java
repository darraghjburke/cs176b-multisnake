package edu.ucsb.multisnake.server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
import java.util.Random;
import java.awt.Color;

public class World extends Thread{
    private List<Player> players;
    private int numOfPlayers = 0;
    private List<Food> food;
    private int radius = 640;

    public World() {
        players = new ArrayList<Player>();
        food = new ArrayList<Food>();
        for (int i = 0; i < 10; i++) {
            spawnFood();
        }
        // run();
    }

    public Player spawnPlayer() {
        Random rand = new Random();
        int x, y;
        float saturation = 175;
        float brightness = 175;
        int EXPECTED_MAX = 15;
        int HUE_FACTOR = 255 / EXPECTED_MAX;
        float hue = (numOfPlayers * HUE_FACTOR) % 255;

        int rgb = Color.HSBtoRGB(hue/256, saturation/256, brightness/256);
        System.out.println("Hue: " + hue + ", RGB: " + rgb);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        do {
            x = rand.nextInt(radius);
            y = rand.nextInt(radius);
        } while (Math.sqrt(x * x + y * y) < radius);
        Player p = new Player(numOfPlayers++, x, y, r, g, b);

        players.add(p);
        return p;
    }

    public Food spawnFood() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(radius);
            y = rand.nextInt(radius);
        } while (Math.sqrt(x * x + y * y) < radius);
        Food f = new Food(x, y);
        food.add(f);
        return f;
    }

    public void deletePlayerWithId(int id) {
        for(int i = 0; i < players.size(); i++) {
            if (players.get(i).getId() == id) {
                players.remove(i);
                break;
            }
        }
    }

    public void printWorld() {
        for (int i = 0; i < players.size(); i++) {
            System.out.println(players.get(i).toString());
        }
    }

    public void run() {
        long lastLoopTime = System.currentTimeMillis();
        while (true) {
            long now = System.currentTimeMillis();
            long updateLength = now - lastLoopTime;
            if (updateLength >= 100) {
                lastLoopTime = now;
                for(int i = 0; i < players.size(); i++) {
                    players.get(i).getConnection().broadcast();
                }
                printWorld();
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
        return this.players;
    }




    
}