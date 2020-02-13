package edu.ucsb.multisnake.server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import edu.ucsb.multisnake.server.Utils.IntPair;


import edu.ucsb.multisnake.server.Packet.ClientPacketType;
import edu.ucsb.multisnake.server.Packet.ServerPacketType;

public class Connection extends Thread {
    public BufferedInputStream input;
    public BufferedOutputStream output;
    public Socket socket;
    private World world;
    private Player player;
    private boolean isConnected;

    public Connection(Socket socket, Player p, World w) throws IOException {
        super("Connection");
        System.out.println("New client connected");
        this.socket = socket;
        this.player = p;
        this.world = w;
        isConnected = true;
        input = new BufferedInputStream(socket.getInputStream());
        output = new BufferedOutputStream(socket.getOutputStream());
    }
  
    public void run() {
        if (!isConnected) return;
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
                        p.putInt(player.getHead().getX());
                        p.putInt(player.getHead().getY());
                        p.send(output);
                    break;
                    case ClientPacketType.MOVE:
                        int seqNumber = bb.getInt();
                        int currLength = bb.getInt();
                        List<IntPair> positions = new ArrayList<IntPair>();
                        for (int i = 0; i < currLength; i++) {
                            positions.add(new IntPair(bb.getInt(), bb.getInt()));
                        }
                        player.setPositions(positions);
                        // System.out.printf("[MOVE] SeqNumber: %d x: %d y: %d \n", seqNumber, x, y);
                    // TODO : update player position
                    break;
                    case ClientPacketType.QUIT:
                    // TODO : delete player
                    disconnect();
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
        if (!isConnected) return;
        Packet p = new Packet(ServerPacketType.BCAST_PLAYERS, 12 + (24*world.getPlayers().size()));
        p.putInt(0);
        p.putInt(world.getPlayers().size());
        for(int i = 0; i < world.getPlayers().size(); i++) {
            Player pl = world.getPlayers().get(i);
            p.putInt(pl.getId());
            p.putInt(pl.getR());
            p.putInt(pl.getG());
            p.putInt(pl.getB());
            p.putInt(pl.getTargetLength());
            p.putInt(pl.getPosition().size());
            for(IntPair position : pl.getPosition()) {
                p.putInt(position.getX());
                p.putInt(position.getY());
            }
        }
        boolean sent = p.send(output);
        if (!sent) {
            disconnect();
            return;
        }
    }

    public void broadcastFood() {
        if(!isConnected) return;
        Packet p = new Packet(ServerPacketType.BCAST_FOOD, 6*world.getFood().size()+4);
        for(int i = 0; i < world.getFood().size(); i++) {
            Food f = world.getFood().get(i);
            p.putInt(f.getSize());
            p.putInt(f.getR());
            p.putInt(f.getG());
            p.putInt(f.getB());
            p.putInt(f.getPosition().getX());
            p.putInt(f.getPosition().getY());
        }
        boolean sent = p.send(output);
        if (!sent) {
            disconnect();
            return;
        }
    }

    private void disconnect() {
        isConnected = false;
        world.deletePlayerWithId(player.getId());
    }
}