package edu.ucsb.multisnake.server;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;

public class Server {   
    
    BufferedInputStream input;
    BufferedOutputStream output;
    void init(){
        int port = 8000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
 
            System.out.println("Server is listening on port " + port);
 
            while (true) {
                Socket socket = serverSocket.accept();
 
                System.out.println("New client connected");
                input = new BufferedInputStream(socket.getInputStream());
                output = new BufferedOutputStream(socket.getOutputStream());

                byte buffer[] = new byte[4096];

                int bytesRead;

                // read until "eof" returned
                while ((bytesRead = input.read(buffer)) > 0) {
                    //System.out.println(Arrays.toString(buffer));
                    System.out.println("bytesRead: " +bytesRead);
                    ByteBuffer bb = ByteBuffer.wrap(buffer);
                    for (int i=0; i<=3; i++){
                        int a = bb.getInt();
                        System.out.println(a);
                    }
                    
                    output.write(buffer, 0, bytesRead); // write it back
                    output.flush();    // flush the output buffer
                }
            }
 
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}