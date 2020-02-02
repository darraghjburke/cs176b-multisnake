package edu.ucsb.multisnake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;

import edu.ucsb.multisnake.Packet.ServerPacketType;

import java.net.*;
import java.nio.ByteBuffer;
import java.io.*;

public class MultiSnake extends BasicGame {
  public static final String GAME_IDENTIFIER = "edu.ucsb.multisnake";

  BufferedInputStream input;
  BufferedOutputStream output;
  int bytesRead;
  Socket socket;
	
	@Override
    public void initialise() {
 
      String hostname = "localhost";
      int port = 8000;
      
      try {
          socket = new Socket(hostname, port);
          input = new BufferedInputStream(socket.getInputStream());
          output = new BufferedOutputStream(socket.getOutputStream());


      } catch (UnknownHostException ex) {

          System.out.println("Server not found: " + ex.getMessage());

      } catch (IOException ex) {

          System.out.println("I/O error: " + ex.getMessage());
      }
      
    }
    
    @Override
    public void update(float delta) {
        byte buffer[] = new byte[4096];

        try {
          System.out.println(input.available());
            while (input.available() > 0) {
              bytesRead = input.read(buffer);
              System.out.println(bytesRead);
              if (bytesRead > 0) {
                ByteBuffer bb = ByteBuffer.wrap(buffer);
                processPacket(bb);
              }
          }
      } catch (IOException ex) {
        System.out.println("I/O error: " + ex.getMessage());
      }
    }
    
    @Override
    public void interpolate(float alpha) {
    
    }
    
    @Override
    public void render(Graphics g) {
      g.setColor(Color.GREEN);
      g.fillCircle(Gdx.input.getX(), Gdx.input.getY(), 40);;
      g.setTranslation(-Gdx.input.getX(), -Gdx.input.getY());
    }

    public void processPacket(ByteBuffer bb) {
      while(bb.hasRemaining()) {
        int packetType = bb.getInt();
        int seqNumber,id,r,g,b,x,y;
        switch (packetType) {
          case ServerPacketType.ASSIGN_ID:
            id = bb.getInt();
            r = bb.getInt();
            g = bb.getInt();
            b = bb.getInt();
            x = bb.getInt();
            y = bb.getInt();
            System.out.printf("[ASSIGN_ID] ID: %d x: %d y: %d r: %d g: %d b: %d \n", id, x, y, r, g, b);
            break;

          case ServerPacketType.BCAST_PLAYERS:
            seqNumber = bb.getInt();
            id = bb.getInt();
            r = bb.getInt();
            g = bb.getInt();
            b = bb.getInt();
            x = bb.getInt();
            y = bb.getInt();
            System.out.printf("[BCAST] SeqNumber: %d ID: %d x: %d y: %d r: %d g: %d b: %d \n", seqNumber, id, x, y, r, g, b);
            break;
        }
      }
    }
}
