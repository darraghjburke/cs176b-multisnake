package edu.ucsb.multisnake.server;

import java.io.*;
import java.net.*;

public class Server {   
    BufferedInputStream input;
    BufferedOutputStream output;
    World world;
    int port = 8000;

    void init(){
        World w = new World();
        w.start();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
 
            System.out.println("Server is listening on port " + port);
 
            while (true) {
                Socket socket = serverSocket.accept();
                Player p = w.spawnPlayer();
                Connection c = new Connection(socket, p, w);
                p.setConnection(c);
                c.start();
            }
 
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}