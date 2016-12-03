class Point {
    
	/**
	 * X location
	 */
    public int x;
    /**
     * Y location
     */
    public int y;
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Copy the point
     * @return point copy
     */
    public Point copy() {
        return new Point(x, y);
    }
    
    @Override
    public int hashCode() {
        int hash = x;
        hash = y + (hash * 31);
        return hash;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Point) {
            Point p = (Point) o;
            return p != null && p.x == this.x && p.y == this.y;
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
    
    /**
     * Generate a random point
     * @param maxx max x locatoin
     * @param maxy max y location
     * @return point random point
     */
    public static Point randomPoint(int maxx, int maxy) {
        Wheel w = new Wheel();
        int y = w.spin(maxy);
        int x = w.spin(maxx);
        return new Point(x, y);
    }
}