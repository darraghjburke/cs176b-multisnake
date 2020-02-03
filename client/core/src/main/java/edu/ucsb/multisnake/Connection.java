package edu.ucsb.multisnake;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;

import edu.ucsb.multisnake.Packet.ClientPacketType;
import edu.ucsb.multisnake.Packet.ServerPacketType;

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
        if (!isConnected)
            return;
        int bytesRead;
        byte buffer[] = new byte[4096];
        try {
            while ((bytesRead = input.read(buffer)) > 0) {
                // System.out.println("Bytes read: " + bytesRead);
                ByteBuffer bb = ByteBuffer.wrap(buffer);
                int packetType = bb.getInt();
                int seqNumber, id, r, g, b, x, y;
                switch (packetType) {
                case ServerPacketType.BCAST_PLAYERS:
                    seqNumber = bb.getInt();
                    id = bb.getInt();
                    r = bb.getInt();
                    g = bb.getInt();
                    b = bb.getInt();
                    x = bb.getInt();
                    y = bb.getInt();
                    System.out.printf("[BCAST] SeqNumber: %d ID: %d x: %d y: %d r: %d g: %d b: %d \n", seqNumber, id, x,
                            y, r, g, b);
                    // TODO : update player position
                    break;
                case ServerPacketType.BCAST_FOOD:
                    // TODO : display food
                    // disconnect();
                    break;
                }
            }
        } catch (SocketException e) {
            System.out.println("Server disconnected");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void send_location() {
        if (!isConnected)
            return;
        Packet p = new Packet(ClientPacketType.MOVE, 16);
        p.putInt(0); // TODO : need to generate and reuse seqNumber
        p.putInt(this.player.getX());
        p.putInt(this.player.getY());
        boolean sent = p.send(output);
        if (!sent) {
            System.out.println("Player cannot send location");
            disconnect();
            return;
        }
    }

    private void disconnect() {
        isConnected = false;
        try {
            socket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}