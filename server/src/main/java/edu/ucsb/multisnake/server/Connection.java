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
    private int seqNumber = 0;

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
                        seqNumber = bb.getInt();
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
        int packetLength = 12;
        for(Player p: world.getPlayers()) {
            packetLength += 24 + p.getPositions().size() * 8;
        }
        Packet p = new Packet(ServerPacketType.BCAST_PLAYERS, packetLength);
        p.putInt(seqNumber);
        p.putInt(world.getPlayers().size());
        for(Player player: world.getPlayers()) {
            p.putInt(player.getId());
            p.putInt(player.getR());
            p.putInt(player.getG());
            p.putInt(player.getB());
            p.putInt(player.getTargetLength());
            p.putInt(player.getPositions().size());
            for(IntPair position : player.getPositions()) {
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
        Packet p = new Packet(ServerPacketType.BCAST_FOOD, 24*world.getFood().size()+8);
        p.putInt(world.getFood().size());
        for(Food food: world.getFood()) {
            p.putInt(food.getSize());
            p.putInt(food.getR());
            p.putInt(food.getG());
            p.putInt(food.getB());
            p.putInt(food.getPosition().getX());
            p.putInt(food.getPosition().getY());
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