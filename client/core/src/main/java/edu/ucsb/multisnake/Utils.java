package edu.ucsb.multisnake;

public class Utils {

    public static class ClientPacketType {
        public static int LOGIN = 1;
        public static int MOVE = 2;
        public static int QUIT = 3;
    }

    public static class ServerPacketType {
        public static int ASSIGN_ID = -1;
        public static int BCAST_PLAYERS = -2;
        public static int BCAST_FOOD = -3;
    }


}