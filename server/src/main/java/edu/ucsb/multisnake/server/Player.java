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
    positions = new ArrayList<IntPair>();
    positions.add(new IntPair(x, y));
    this.r = r;
    this.g = g;
    this.b = b;
    targetLength = 1;
  }

  public int getId() {
    return id;
  }

  public int getR() {
    return r;
  }

  public int getG() {
    return g;
  }

  public int getB() {
    return b;
  }

  public int getTargetLength() {
    return targetLength;
  }

  public List<IntPair> getPositions() {
    return positions;
  }

  public void setColor(int r, int g, int b) {
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

  public void setPosition(IntPair pos) {
    positions.add(0, pos);
  }

  public IntPair getHead() {
    return positions.get(0);
  }

  public void grow(int amt) {
    targetLength += amt;
  }

  public void setConnection(Connection c) {
    conn = c;
  }

  public Connection getConnection() {
    return conn;
  }

}