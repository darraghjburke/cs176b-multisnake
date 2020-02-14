package edu.ucsb.multisnake.server;

public class Utils {
    public static class IntPair {
        private final int x;
        private final int y;
        public IntPair(int x, int y) { this.x = x; this.y = y; }
        public int getX() { return this.x; }
        public int getY() { return this.y; }

        @Override
        public String toString() {
            return x + "," + y;
        }

        public double distanceTo(IntPair otherPoint) {
            return Math.sqrt(Math.pow(x - otherPoint.getX(), 2) + Math.pow(y - otherPoint.getY(), 2));
        }

        public double angleTo(IntPair otherPoint) {
            return Math.atan2(otherPoint.y - y, otherPoint.x - x);
        }

        /* Returns the position if you moved in a direction by a certain distance, starting from this position */
        public IntPair moveDirection(double direction, double distance) {
            int newX = (int) Math.floor(x + Math.cos(direction) * distance);
            int newY = (int) Math.floor(y + Math.sin(direction) * distance);
            return new IntPair(newX, newY);
        }
    }
}