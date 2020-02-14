package edu.ucsb.multisnake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;

import edu.ucsb.multisnake.Packet.ClientPacketType;
import edu.ucsb.multisnake.Packet.ServerPacketType;
import edu.ucsb.multisnake.Utils.IntPair;

import java.net.*;
import java.nio.ByteBuffer;
import java.util.List;
import java.io.*;

public class MultiSnake extends BasicGame {
    public static final String GAME_IDENTIFIER = "edu.ucsb.multisnake";

    World world;
    byte buffer[];
    private static Connection conn;
    

    @Override
    public void initialise() {

        world = new World();
        world.start();
        String hostname = "localhost";
        int port = 8000;
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
        if (conn == null) System.out.println("CONNETION IS NULL");
    }
    
    @Override
    public void interpolate(float alpha) {
    }
    
    private void drawPlayer(Player pl, Graphics g) {
        Color c = new Color(pl.getR()/255f, pl.getG()/255f, pl.getB()/255f, 1f);
        g.setColor(c);
        /* Extremely hacky fix because we kinda messed up player management! */
        List<IntPair> positions = pl.getPositions();
        for (IntPair pos: positions) {
            g.fillCircle(pos.getX(), pos.getY(), 40);
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0,0,world.getRadius()*2,world.getRadius()*2);
        g.setColor(Color.WHITE);
        g.fillCircle(width / 2 , height / 2, world.getRadius());
        for(Player pl: world.getPlayers()) {
            if (pl != null){
                drawPlayer(pl, g);
            }
        }

        for (Food f: world.getFood()) {
            if (f != null) {
                Color c = new Color(f.getR()/255f, f.getG()/255f, f.getB()/255f, 1f);
                g.setColor(c);
                int x = f.getPosition().getX();
                int y = f.getPosition().getY();
                int size = f.getSize();
                g.fillRect(x, y, (float)size/2, (float)size/2);
            }
        }
    }

    public static Connection getConnection() {
        return conn;
    }
}
