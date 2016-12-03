

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class PointTest.
 *
 * @author Ezekiel Elin
 * @version October 15, 2016
 */
public class PointTest {

    @Test
    public void testEquality() {
        Point p = new Point(0, 0);
        Point p2 = new Point(0, 0);
        Point p3 = new Point(1, 0);
        Point p4 = new Point(0, 1);
        
        assertTrue("Identical valued points aren't equal", p.equals(p2));
        assertTrue("Different valued points are equal", !p.equals(p3));
        assertTrue("Different valued points are equal", !p.equals(p4));
    }
    
    @Test
    public void testCopy() {
        Point p = new Point(5, 10);
        assertTrue("Copy isn't equal", p.equals(p.copy()));
    }
    
}
