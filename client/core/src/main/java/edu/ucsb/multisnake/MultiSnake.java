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

    BufferedInputStream input;
    BufferedOutputStream output;
    int bytesRead;
    Socket socket;
    Player me;
    World world;
    byte buffer[];
        
	@Override
    public void initialise() {

        world = new World();
        world.start();
        String hostname = "localhost";
        int port = 8000;
        buffer = new byte[4096];
        
        try {
            socket = new Socket(hostname, port);
            input = new BufferedInputStream(socket.getInputStream());
            output = new BufferedOutputStream(socket.getOutputStream());
            Packet p = new Packet(ClientPacketType.LOGIN, 4);
            p.send(output);
            if((bytesRead = input.read(buffer)) > 0) {
                System.out.println("Bytes read: " + bytesRead);
                ByteBuffer bb = ByteBuffer.wrap(buffer);
                int packetType = bb.getInt();
                int id,r,g,b,x,y;
                switch (packetType) {
                    case ServerPacketType.ASSIGN_ID:
                        id = bb.getInt();
                        r = bb.getInt();
                        g = bb.getInt();
                        b = bb.getInt();
                        x = bb.getInt();
                        y = bb.getInt();
                        System.out.printf("[ASSIGN_ID] ID: %d x: %d y: %d r: %d g: %d b: %d \n", id, x, y, r, g, b);
                        me = new Player(id, r, g, b);
                        me.addPositions(new IntPair(x,y));
                        world.setMe(me);
                        Connection c = new Connection(socket, me, world);
                        me.setConnection(c);
                        c.start();
                        break;
                    default:
                        System.out.println("Server did not send back ASSIGN_ID packet");
                }    
            }
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
      
    }

    private double shortAngleDist(double a0,double a1) {
        double max = Math.PI*2;
        double da = (a1 - a0) % max;
        return 2*da % max - da;
    }
    
    /*private double angleLerp(a0,a1,t) {
        return a0 + shortAngleDist(a0,a1)*t;
    }*/
    
    
    @Override
    public void update(float delta) {
        Player me = world.getMe();
        int targetX = Gdx.input.getX();
        int targetY = Gdx.input.getY();
        int headX = me.getHead().getX();
        int headY = me.getHead().getY();
        double angle = Math.atan2(targetY - headY, targetX - headX);
        double currentDirection = me.getDirection();
        double dist = shortAngleDist(currentDirection, angle);
        double change = Math.min(me.getTurnSpeed(), Math.abs(dist));
        if (dist < 0) change *= -1;
        me.setDirection(currentDirection + change);
        int newX = (int) Math.floor(headX + Math.cos(me.getDirection()) * me.getSpeed());
        int newY = (int) Math.floor(headY + Math.sin(me.getDirection()) * me.getSpeed());
        if (me != null) {
            me.move(new IntPair(newX, newY));
        }
    }
    
    @Override
    public void interpolate(float alpha) {
    }
    
    @Override
    public void render(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0,0,world.getRadius()*2,world.getRadius()*2);
        g.setColor(Color.WHITE);
        g.fillCircle(width / 2 , height / 2, world.getRadius());
        for(Player pl: world.getPlayers()) {
            if (pl != null){
                if ( me!=null && pl.getId() == me.getId() ) {
                    Color c = new Color(me.getR()/255f, me.getG()/255f, me.getB()/255f, 1f);
                    g.setColor(c);
                    for (int j = 0; j < me.getPositions().size(); j++) {
                        int x = me.getPositions().get(j).getX();
                        int y = me.getPositions().get(j).getY();
                        g.fillCircle(x, y, 40);
                    }
                } else {
                    Color c = new Color(pl.getR()/255f, pl.getG()/255f, pl.getB()/255f, 1f); 
                    g.setColor(c);
                    for (int j = 0; j < pl.getPositions().size(); j++) {
                        int x = pl.getPositions().get(j).getX();
                        int y = pl.getPositions().get(j).getY();
                        g.fillCircle(x, y, 40);
                    }
                }
            }
        }

        List<Food> food = world.getFood();
        for (int i = 0; i < food.size(); i++) {
            Food f = food.get(i);
            if (f != null) {
                Color c = new Color(f.getR()/255f, f.getG()/255f, f.getB()/255f, 1f);
                g.setColor(c);
                int x = f.getX();
                int y = f.getY();
                int size = f.getSize();
                g.fillRect(x, y, (float)size/2, (float)size/2);
            }
        }
    }
}
