package edu.ucsb.multisnake;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.rmi.UnknownHostException;
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
    private boolean isConnected;
    private int port;
    private String hostname;

    public Connection(String hostname, int port, World world) throws IOException {
        super("Connection");
        this.hostname = hostname;
        this.port = port;
        this.world = world;
        try {
            socket = new Socket(hostname, port);
            isConnected = true;
            input = new BufferedInputStream(socket.getInputStream());
            output = new BufferedOutputStream(socket.getOutputStream());
            System.out.println("Connected to server");
        }
        catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
            throw ex;
        }
        Packet p = new Packet(ClientPacketType.LOGIN, 4);
        p.send(output);

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
        Player me = world.findMe();
        if (!isConnected || me == null)
            return;
        int length = me.getPositions().size();
        Packet p = new Packet(ClientPacketType.MOVE, (3+2*length)*4);
        p.putInt(0); // TODO : need to generate and reuse seqNumber
        p.putInt(length);
        for (int i = 0; i < length; i++) {
            IntPair pos = me.getPositions().get(i);
            p.putInt(pos.getX());
            p.putInt(pos.getY());
        }
        boolean sent = p.send(output);
        System.out.println("sent location!");
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
                Player pl = world.getPlayerWithId(id);
                if (pl == null) {
                    pl = new Player(id, r, g, b);
                    pl.addPositions(new IntPair(x,y));
                    world.addPlayer(pl);
                }
                pl.setMe(true);
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
                    Player p = world.getPlayerWithId(id);
                    if (!p.isMe()){
                        p.setPositions(positions);
                    }
                    System.out.printf("[BCAST] SeqNumber: %d ID: %d r: %d g: %d b: %d pos: %s \n", seqNumber, id, r, g, b, positions.toString());
                }
                break;

            case ServerPacketType.BCAST_FOOD:
                // TODO: add food to food list
                break;
          }
        }
      }
  
}