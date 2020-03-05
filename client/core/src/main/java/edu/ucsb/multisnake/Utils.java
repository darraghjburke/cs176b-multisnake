package edu.ucsb.multisnake;

public class Utils {
    public static class Sequence {
        /* empty condition: acked == unacked, means no unacked pkts
           full condition: (unacked+1)%SEQNUMSIZE == acked, means all (SEQNUMSIZE-1) pkts are unacked yet */

        private final int SEQNUMSIZE = 10001; // 1 more slot needed to distinct between empty vs full
        private int num[];
        private int acked;   // pointing to the last acked seq num
        private int unacked; // pointing to the last used seq num (wait to be acked)
        private long time[];
        private int numAcked; // number of pkts acked so far
        private long totalTime; // acumulative time for acked pkts

        public Sequence() { 
            num = new int[SEQNUMSIZE]; 
            time = new long[SEQNUMSIZE];
            acked = 0; 
            unacked = 0; 
            numAcked = 0;
            totalTime = 0;
        } 

        public int getNextSeqNum(){
            int next = (unacked + 1) % SEQNUMSIZE;
            if (next != acked) { // not full, can fit more unacked pkts
                num[next] = 1;
                unacked = next;
                return unacked;
            } else {
                System.out.println("Too many unacked pkts, ran out of sequence numbers!");
                return -1;
            }
        }

        public int acknowledge(int n){
            int next = (acked + 1) % SEQNUMSIZE;
            if (unacked != acked && num[n]==1){ // not empty, there are more pkts to be acked
                while (acked != n && num[next]==1) { // ack pkts up to the n-th one (including n)
                    num[next] = 0;
                    acked = next;
                    next = (acked + 1) % SEQNUMSIZE;
                }
                return acked;
            } else { // empty
                //System.out.println("No packets needed acknowledgement...");
                return -1;
            }
        }

        public void setSendTime(int i, long current){
            time[i] = current;
        }

        public void calAckTime(int i) {
            totalTime += (System.currentTimeMillis()-time[i]);
            numAcked += 1;
        }

        public long getAvgAckTime() {
            if (numAcked > 0) {
                return totalTime/numAcked;
            } else {
                return (Long) null;
            }
        }
    }

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