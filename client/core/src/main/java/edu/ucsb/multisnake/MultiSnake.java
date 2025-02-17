package edu.ucsb.multisnake;

import com.badlogic.gdx.Gdx;

import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.viewport.FitViewport;

import edu.ucsb.multisnake.Utils.IntPair;

import java.io.*;

public class MultiSnake extends BasicGame {
    public static final String GAME_IDENTIFIER = "edu.ucsb.multisnake";

    private World world;
    private static Connection conn;
    private final String hostname = "csil.cs.ucsb.edu";
    private final int port = 8000;
    private FitViewport viewport;
    private long lastUpdateTime = System.currentTimeMillis();
    float gameWidth = 800;
    float gameHeight = 800;
    private int clientUpdateRate = 17;
    public boolean interpolation = true, prediction = true, reconciliation = true;
    public static MultiSnake GAME;

    public MultiSnake(String[] args) {
        super();
        for(int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("clientUpdateRate")) {
                clientUpdateRate = Integer.parseInt(args[++i]);
            }
            if (arg.equals("noInterpolate")) {
                interpolation = false;
            }
            if (arg.equals("noPredict")) {
                prediction = false;
            }
            if (arg.equals("noReconcile")) {
                reconciliation = false;
            }
            System.out.printf("\nSettings: %d %s %s %s\n\n", clientUpdateRate, interpolation ? "true" : "false", prediction ? "true" : "false", reconciliation ? "true" : "false");
        }
        GAME = this;
    }

    @Override
    public void initialise() {
        viewport = new FitViewport(gameWidth, gameHeight);
        world = new World();
        world.start();
        try {
            conn = new Connection(hostname, port, world);
            conn.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
      
    }
    
    @Override
    public void update(float delta) {
        Player me = world.findMe();
        if (me != null) me.moveTowards(new IntPair(Gdx.input.getX(), Gdx.input.getY()));
        if (conn == null) System.out.println("CONNECTION IS NULL");
        long now = System.currentTimeMillis();
        if (now - lastUpdateTime >= clientUpdateRate) {
            getConnection().send_location();
        }
        
    }
    
    @Override
    public void interpolate(float alpha) {
    }

    @Override
    public void render(Graphics g) {
        viewport.apply(g);
        world.render(g);
    }

    public static Connection getConnection() {
        return conn;
    }
}
