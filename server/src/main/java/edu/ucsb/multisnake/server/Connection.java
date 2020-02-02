package edu.ucsb.multisnake.server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;

import edu.ucsb.multisnake.server.Packet.ClientPacketType;
import edu.ucsb.multisnake.server.Packet.ServerPacketType;

public class Connection extends Thread {
    public BufferedInputStream input;
    public BufferedOutputStream output;
    public Socket socket;
    private World world;
    private Player player;

    public Connection(Socket socket, Player p, World w) throws IOException {
        super("Connection");
        System.out.println("New client connected");
        this.socket = socket;
        this.player = p;
        this.world = w;
        input = new BufferedInputStream(socket.getInputStream());
        output = new BufferedOutputStream(socket.getOutputStream());
    }
  
    public void run() {
        int bytesRead;
        byte buffer[] = new byte[4096];
        try {
            while ((bytesRead = input.read(buffer)) > 0) {
                // System.out.println("Bytes read: " + bytesRead);
                ByteBuffer bb = ByteBuffer.wrap(buffer);
                int packetType = bb.getInt();
                switch (packetType) {
                    case ClientPacketType.LOGIN:
                        Packet p = new Packet(ServerPacketType.ASSIGN_ID, 28);
                        p.putInt(player.getId());
                        p.putInt(player.getR());
                        p.putInt(player.getG());
                        p.putInt(player.getB());
                        p.putInt(player.getX());
                        p.putInt(player.getY());
                        p.send(output);
                    break;
                    case ClientPacketType.MOVE:
                    // TODO : update player position
                    break;
                    case ClientPacketType.QUIT:
                    // TODO : delete player
                    world.deletePlayerWithId(player.getId());
                    break;
                }
            }
        } catch (SocketException e) {
            System.out.println("Client disconnected");
            world.deletePlayerWithId(player.getId());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void broadcast() {
        for(int i = 0; i < world.getPlayers().size(); i++) {
            Player pl = world.getPlayers().get(i);
            Packet p = new Packet(ServerPacketType.BCAST_PLAYERS, 32);
            p.putInt(0);
            p.putInt(pl.getId());
            p.putInt(pl.getR());
            p.putInt(pl.getG());
            p.putInt(pl.getB());
            p.putInt(pl.getX());
            p.putInt(pl.getY());
            p.send(output);
        }
    }
}