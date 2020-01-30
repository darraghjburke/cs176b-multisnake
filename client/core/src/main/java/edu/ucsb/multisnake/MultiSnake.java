package edu.ucsb.multisnake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;
import java.net.*;
import java.io.*;

public class MultiSnake extends BasicGame {
	public static final String GAME_IDENTIFIER = "edu.ucsb.multisnake";

  private Texture texture;
  private float x,y;
	
	@Override
    public void initialise() {
 
      String hostname = "localhost";
      int port = 8000;
      Socket socket;
      try {
          socket = new Socket(hostname, port);
          BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
          BufferedReader reader = new BufferedReader(new InputStreamReader(input));

          String time = reader.readLine();

          System.out.println(time);


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
      g.setColor(Color.GREEN);
      g.drawCircle(Gdx.input.getX(), Gdx.input.getY(), 40);;
      g.setTranslation(-Gdx.input.getX(), -Gdx.input.getY());
    }
}
