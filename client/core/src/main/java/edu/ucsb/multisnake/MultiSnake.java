package edu.ucsb.multisnake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;

public class MultiSnake extends BasicGame {
	public static final String GAME_IDENTIFIER = "edu.ucsb.multisnake";

	private Texture texture;
	
	@Override
    public void initialise() {
    	texture = new Texture("mini2Dx.png");
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
      g.drawCircle(800,800,64);
		  g.drawTexture(texture, 0f, 0f);
    }
}
