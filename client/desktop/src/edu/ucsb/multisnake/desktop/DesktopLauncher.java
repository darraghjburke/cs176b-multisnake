package edu.ucsb.multisnake.desktop;

import org.mini2Dx.desktop.DesktopMini2DxConfig;

import com.badlogic.gdx.backends.lwjgl.DesktopMini2DxGame;

import edu.ucsb.multisnake.MultiSnake;

public class DesktopLauncher {
	public static void main (String[] arg) {
		DesktopMini2DxConfig config = new DesktopMini2DxConfig(MultiSnake.GAME_IDENTIFIER);
		config.vSyncEnabled = true;
		config.width = 1280;
		config.height = 1280;
		new DesktopMini2DxGame(new MultiSnake(), config);
	}
}
