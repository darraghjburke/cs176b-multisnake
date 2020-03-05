package edu.ucsb.multisnake.desktop;

import org.mini2Dx.desktop.DesktopMini2DxConfig;

import com.badlogic.gdx.backends.lwjgl.DesktopMini2DxGame;

import edu.ucsb.multisnake.MultiSnake;

public class DesktopLauncher {
	public static void main (String[] arg) {
		DesktopMini2DxConfig config = new DesktopMini2DxConfig(MultiSnake.GAME_IDENTIFIER);
		config.vSyncEnabled = true;
		config.width = 800;
		config.height = 800;
		config.useHDPI = true;
		new DesktopMini2DxGame(new MultiSnake(arg), config);
	}
}
