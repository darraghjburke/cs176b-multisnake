package edu.ucsb.multisnake.server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class Packet {

    public static class ClientPacketType {
        public static final int LOGIN = 1;
        public static final int MOVE = 2;
        public static final int QUIT = 3;
    }

    public static class ServerPacketType {
        public static final int ASSIGN_ID = -1;
        public static final int BCAST_PLAYERS = -2;
        public static final int BCAST_FOOD = -3;
    }
    
    ByteBuffer bb;
    int length;

    public Packet(int packet_type, int length) {
        bb = ByteBuffer.allocate(length);
        bb.putInt(packet_type);
        this.length = length;
    }

    public void putInt(int i) {
        bb.putInt(i);
    }

    public boolean send(BufferedOutputStream output) {
        try {
            output.write(bb.array(), 0, length);
            output.flush(); 
            return true;
        }
        catch (SocketException e) {
            System.out.println("(Warning) Could not send packet because client disconnected!");
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

}