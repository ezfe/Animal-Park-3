import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class WheelTest.
 *
 * @author Ezekiel Elin
 * @version September 6, 2016
 */
public class WheelTest {
    private int length = 20;
    private int seed = 3103652;
    private Wheel wheel1;
    
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp() {
        wheel1 = new Wheel(seed);
    }

    @Test
    public void testSpinGreaterZero() {
        /* Check that the wheel returns only >= 0 */
        for(int i = 0; i < 10000000; i += 1) {
            assertTrue(wheel1.spin() >= 0);
        }
    }

    @Test
    public void testSpinNotRepeatingSequence() {
        /* Store the first item */
        int first = wheel1.spin();
        
        /* Assert that subsequent items are not equal to the first item */
        for(int i = 0; i < length; i += 1) {
            assertTrue(first != wheel1.spin());
        }
    }
    
    @Test
    public void testSpinSeedDifferentiating() {
        /* Create another wheel with a different seed */
        Wheel wheel2 = new Wheel(4214763);
        
        /* Assert that the first `length` items are not equal */
        for(int i = 0; i < length; i += 1) {
            assertTrue(wheel1.spin() != wheel2.spin());
        }
    }
    
    @Test
    public void testSpinSeedIdentical() {
        /* Create another wheel with the same seed */
        Wheel wheel2 = new Wheel(seed);

        /* Assert that the first `length` items are equal */
        for(int i = 0; i < length; i += 1) {
            assertTrue(wheel1.spin() == wheel2.spin());
        }
    }
    
    
    @Test
    public void testSpinParamGreaterZero() {
        /* Check that the wheel returns only >= 0 */
        for(int i = 0; i < 10000000; i += 1) {
            assertTrue(wheel1.spin(100) >= 0);
        }
    }
    
    @Test
    public void testSpinParamLessThanParam() {
        /* Check that the wheel returns only >= 0 */
        for(int i = 0; i < 10000000; i += 1) {
            assertTrue(wheel1.spin(20) <= 20);
        }
    }

    @Test
    public void testSpinParamNotRepeatingSequence() {
        /* Store the first item */
        int first = wheel1.spin(10000);
        
        /* Assert that subsequent items are not equal to the first item */
        for(int i = 0; i < length; i += 1) {
            assertTrue(first != wheel1.spin(10000));
        }
    }
    
    @Test
    public void testSpinParamSeedDifferentiating() {
        /* Create another wheel with a different seed */
        Wheel wheel2 = new Wheel(4214763);
        
        /* Assert that the first `length` items are not equal */
        for(int i = 0; i < length; i += 1) {
            assertTrue(wheel1.spin(100) != wheel2.spin(100));
        }
    }
    
    @Test
    public void testSpinParamSeedIdentical() {
        /* Create another wheel with the same seed */
        Wheel wheel2 = new Wheel(seed);

        /* Assert that the first `length` items are equal */
        for(int i = 0; i < length; i += 1) {
            assertTrue(wheel1.spin(100) == wheel2.spin(100));
        }
    }
}


