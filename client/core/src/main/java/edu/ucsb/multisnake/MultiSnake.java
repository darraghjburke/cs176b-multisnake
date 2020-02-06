package edu.ucsb.multisnake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;

import edu.ucsb.multisnake.Packet.ClientPacketType;
import edu.ucsb.multisnake.Packet.ServerPacketType;

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
                        me = new Player(id, x, y, r, g, b);
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
    
    @Override
    public void update(float delta) {
    }
    
    @Override
    public void interpolate(float alpha) {
    }
    
    @Override
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillCircle(width / 2 , height / 2, world.getRadius());
        List<Player> otherPlayers = world.getPlayers();
        for(int i = 0; i < otherPlayers.size(); i++) {
            Player pl = otherPlayers.get(i);
            if (pl != null){
                Color c = new Color(pl.getR()/255f, pl.getG()/255f, pl.getB()/255f, 1f); // TODO : need to send alpha?
                g.setColor(c);
                int x = pl.getX();
                int y = pl.getY();
                g.fillCircle(x, y, 40);
            }
        }
        me = world.getMe();
        if (me != null){
            Color c = new Color(me.getR()/255f, me.getG()/255f, me.getB()/255f, 1f); // TODO : need to send alpha?
            g.setColor(c);
            int x = Gdx.input.getX();
            int y = Gdx.input.getY();
            g.fillCircle(x, y, 40);
            me.setX(x);
            me.setY(y);
        }
    }
}
