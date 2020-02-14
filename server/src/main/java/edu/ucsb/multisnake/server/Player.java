package edu.ucsb.multisnake.server;

import java.util.ArrayList;
import java.util.List;
import edu.ucsb.multisnake.server.Utils.IntPair;


public class Player {
  private int id;
  private int r, b, g;
  private int targetLength;
  private List<IntPair> positions;
  private Connection conn;

  public Player(int id, int x, int y, int r, int g, int b) {
      this.id = id;
      this.positions = new ArrayList<IntPair>();
      this.positions.add(new IntPair(x, y));
      this.r = r;
      this.g = g;
      this.b = b;
      this.targetLength = 1;
  }

    public int getId() {
        return this.id;
    }

    public int getR()
    {
		return this.r;
    }
    
    public int getG()
    {
		return this.g;
	}

    public int getB()
    {
		return this.b;
    }
    
    public int getTargetLength() {
      return this.targetLength;
    }

    public List<IntPair> getPositions() {
      return positions;
    }

    public void setColor(int r, int g, int b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public String toString() {
        return id + "," + positions.toString() + "," + r + "," + g + "," + b;
    }

    public void setPositions(List<IntPair> pos) {
      positions = pos;
    }
  
    public IntPair getHead() {
      return positions.get(0);
    }

    public void grow(int amt) {
      targetLength+=amt;
    }

    public void setConnection(Connection c) {
      this.conn = c;
    }

    public Connection getConnection() {
      return this.conn;
    }

}