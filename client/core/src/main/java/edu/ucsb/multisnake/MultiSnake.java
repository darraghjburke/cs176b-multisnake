package edu.ucsb.multisnake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;

import edu.ucsb.multisnake.Packet.ClientPacketType;

import java.net.*;
import java.util.Arrays;
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
      try {
        byte buffer[] = new byte[4096];
        
        Packet p = new Packet(ClientPacketType.LOGIN, 20);
        p.putInt(1);
        p.putInt(2);
        p.putInt(3);
        p.putInt(4);

        p.send(output);

        bytesRead = input.read(buffer);
        System.out.println(Arrays.toString(Arrays.copyOfRange(buffer, 0, bytesRead)));

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
}
