package edu.ucsb.multisnake.server;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Server {   
    List<Connection> connections;
    BufferedInputStream input;
    BufferedOutputStream output;

    Server()  {
        connections = new ArrayList<Connection>();
    }

    void init(){
        int port = 8000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
 
            System.out.println("Server is listening on port " + port);
 
            while (true) {
                Socket socket = serverSocket.accept();
                Connection c = new Connection(socket);
                c.start();
                connections.add(c);
            }
 
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}