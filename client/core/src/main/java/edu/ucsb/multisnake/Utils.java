package edu.ucsb.multisnake;

public class Utils {
    public static class Sequence {
        private final int SEQNUMSIZE = 10000;
        private int num[];
        private int ack;
        private int send;
        public Sequence(){ num = new int[SEQNUMSIZE]; ack = 0; send = 0; } 
        public int getNextSeqNum(){
            int next = (send + 1) % SEQNUMSIZE;
            if (next != ack) { // not full, can fit more unack pkts
                num[next] = 1;
                send = next;
                return send;
            } else {
                System.out.println("Ran out of sequence numbers, waiting for acknowledgements...");
                return -1;
            }
        }
        public int acknowledge(int n){
            int next;
            if (send != ack && num[n]==1){ // not empty, or there are pkts to be acked
                while ((next = (ack + 1) % SEQNUMSIZE) != n) { // ack pkts up to the n-th one
                    num[next] = 0;
                    ack = next;
                }
                return ack;
            } else { // empty
                System.out.println("No packets needed acknowledgement...");
                return -1;
            }
        }
    }

    private static double deltaDist = 5.0;

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

        /* This prob shouldn't be in here but lol w/e */
        public static double shortAngleDist(double a0,double a1) {
            double max = Math.PI*2;
            double da = (a1 - a0) % max;
            return 2*da % max - da;
        }
    }
}