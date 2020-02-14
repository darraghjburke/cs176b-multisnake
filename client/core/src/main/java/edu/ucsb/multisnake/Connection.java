package edu.ucsb.multisnake;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.EllipseShapeBuilder;

import edu.ucsb.multisnake.Packet.ClientPacketType;
import edu.ucsb.multisnake.Packet.ServerPacketType;
import edu.ucsb.multisnake.Utils.IntPair;

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
                processPacket(bb);
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
        int length = player.getPositions().size();
        Packet p = new Packet(ClientPacketType.MOVE, (3+2*length)*4);
        p.putInt(0); // TODO : need to generate and reuse seqNumber
        p.putInt(length);
        for (IntPair ip: player.getPositions()) {
            p.putInt(ip.getX());
            p.putInt(ip.getY());
        }
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

    public void processPacket(ByteBuffer bb) {
        while(bb.hasRemaining()) {
          int packetType = bb.getInt();
          int seqNumber,numPlayers,id,r,g,b,x,y,target_length, current_length;
          switch (packetType) {
            case ServerPacketType.ASSIGN_ID:
                id = bb.getInt();
                r = bb.getInt();
                g = bb.getInt();
                b = bb.getInt();
                x = bb.getInt();
                y = bb.getInt();
                System.out.printf("[ASSIGN_ID] ID: %d x: %d y: %d r: %d g: %d b: %d \n", id, x, y, r, g, b);
                break;
  
            case ServerPacketType.BCAST_PLAYERS:
                seqNumber = bb.getInt();
                numPlayers = bb.getInt();
                for (int j=0; j<numPlayers; j++) {
                    id = bb.getInt();
                    r = bb.getInt();
                    g = bb.getInt();
                    b = bb.getInt();
                    target_length = bb.getInt();
                    current_length = bb.getInt();
                    List<IntPair> positions = new ArrayList<IntPair>();
                    if (world.getPlayerWithId(id) == null) {
                        world.addPlayer(new Player(id, r, g, b));        
                    // update position for other players
                    }
                    for (int i = 0; i < current_length; i++){
                        x = bb.getInt();
                        y = bb.getInt();
                        IntPair p = new IntPair(x,y);
                        positions.add(p);
                    }
                    if (id != world.getMe().getId()){
                        Player p = world.getPlayerWithId(id);
                        p.setPositions(positions);
                    }
                }
                // System.out.printf("[BCAST] SeqNumber: %d ID: %d x: %d y: %d r: %d g: %d b: %d \n", seqNumber, id, x, y, r, g, b);
                break;

            case ServerPacketType.BCAST_FOOD:
                // TODO: add food to food list
                break;
          }
        }
      }
  
}