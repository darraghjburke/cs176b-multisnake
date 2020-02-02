package edu.ucsb.multisnake.server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;

import edu.ucsb.multisnake.server.Packet.ClientPacketType;

public class Connection extends Thread {
    public BufferedInputStream input;
    public BufferedOutputStream output;
    public Socket socket;
    private Player player;

    public Connection(Socket socket, Player p) throws IOException {
        super("Connection");
        System.out.println("New client connected");
        this.socket = socket;
        this.player = p;
        input = new BufferedInputStream(socket.getInputStream());
        output = new BufferedOutputStream(socket.getOutputStream());
    }
  
    public void run() {
        int bytesRead;
        byte buffer[] = new byte[4096];
        try {
            while ((bytesRead = input.read(buffer)) > 0) {
                System.out.println("Bytes read: " + bytesRead);
                ByteBuffer bb = ByteBuffer.wrap(buffer);
                int packetType = bb.getInt();
                switch (packetType) {
                    case ClientPacketType.LOGIN:
                    // TODO : randomize x and y and color
                    break;
                    case ClientPacketType.MOVE:
                    // TODO : update player position
                    break;
                    case ClientPacketType.QUIT:
                    // TODO : delete player
                    break;
                }
                // for (int i=0; i<=3; i++){
                //     int a = bb.getInt();
                //     System.out.println(a);
                // }
                output.write(buffer, 0, bytesRead); // write it back
                output.flush();    // flush the output buffer
            }
        } catch (SocketException e) {
            System.out.println("Client disconnected");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}