package edu.ucsb.multisnake.server;

public class Utils { 

    public static class ClientPacketType {
        public static int LOGIN=0;
        public static int MOVE=1;
        public static int QUIT=2;
    }

    public static class ServerPacketType {
        public static int ASSIGN_ID=0;
        public static int BCAST_PLAYERS=1;
        public static int BCAST_FOOD=2;
    }
}