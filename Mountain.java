/**
 * Stores information for single mountain range
 * 
 * @author Ezekiel Elin
 * @version 11/02/2016
 */
public class Mountain {
    public int x1;
    public int y1;
    public int x2;
    public int y2;
    
    public Mountain(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
    
    /**
     * Populate a world with this mountain
     * @param World world to populate
     */
    public void populate(Park w) {
        double x1 = (double)this.x1;
        double x2 = (double)this.x2;
        double y1 = (double)this.y1;
        double y2 = (double)this.y2;
        
        boolean s = false;
        
        if (Math.abs(x1 - x2) < Math.abs(y1 - y2)) {
            /* Reverse the line */
            double tx1 = x1;
            x1 = y1;
            y1 = tx1;
            
            double tx2 = x2;
            x2 = y2;
            y2 = tx2;
            
            s = true;
        }
        
        if (x1 > x2) {
            double tx1 = x1;
            x1 = x2;
            x2 = tx1;
            
            double ty1 = y1;
            y1 = y2;
            y2 = ty1;
        }
        
        for(double x = x1; x <= x2; x += 1) {
            double t = (x - x1) / (x2 - x1);
            double y = y1 * (1.0 - t) + y2 * t;
            
            if (s) {
                w.getCell(new Point((int)y, (int)x)).setMountain(true);
            } else {
                w.getCell(new Point((int)x, (int)y)).setMountain(true);
            }
        }
    }
}
