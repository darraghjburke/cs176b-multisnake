package edu.ucsb.multisnake;

public class Player {
        private int id;
        private int x, y;
        private int r, b, g;
        private Connection conn;
    
        public Player(int id, int x, int y, int r, int g, int b) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.r = r;
            this.g = g;
            this.b = b;
        }
    
        public int getId() {
            return this.id;
        }

        public void setId(int id) {
            this.id = id;
        }
    
        public int getX()
        {
            return this.x;
        }
        
        public int getY() {
            return this.y;
        }
    
        public void setX(int x)
        {
            this.x = x;
        }
        
        public void setY(int y) {
            this.y = y;
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
        
        public void setColor(int r, int g, int b)
        {
            this.r = r;
            this.g = g;
            this.b = b;
        }
        
        public String toString() {
            return id + "," + x + "," + y + "," + r + "," + g + "," + b;
        }
    
        public void setConnection(Connection c) {
          this.conn = c;
        }
    
        public Connection getConnection() {
          return this.conn;
        }
    
    }