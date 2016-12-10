import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class CellTest.
 *
 * @author Ezekiel Elin
 * @version October 15, 2016
 */
public class CellTest {
    Cell cell;
    Park park;
    SpeciesProperties plantprop = new SpeciesProperties("species wheat vegetable h light 100,5,20 20.1,18 10 1000 0 0 2 0");
        
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp() {
        park = new Park(20, 20, 5, new Wheel(10), "T", 0, 0);
        Plant plant = new Plant(plantprop, new Wheel(10));
        park.setPlant(new Point(5, 5), plant);
        cell = plant.getCell();
    }

    @Test
    public void testLocation() {
        assertTrue(cell.getLocation().equals(new Point(5, 5)));
        assertTrue(cell.getX() == 5);
        assertTrue(cell.getY() == 5);
    }
    
    @Test
    public void testGetNeighbor() {
        assertTrue(cell.getNeighbor(CellDirection.UP).getLocation().equals(new Point(5, 4)));
        assertTrue(cell.getNeighbor(CellDirection.DOWN).getLocation().equals(new Point(5, 6)));
        assertTrue(cell.getNeighbor(CellDirection.LEFT).getLocation().equals(new Point(4, 5)));
        assertTrue(cell.getNeighbor(CellDirection.RIGHT).getLocation().equals(new Point(6, 5)));
        assertTrue(cell.getNeighbor(CellDirection.UPRIGHT).getLocation().equals(new Point(6, 4)));
        assertTrue(cell.getNeighbor(CellDirection.UPLEFT).getLocation().equals(new Point(4, 4)));
        assertTrue(cell.getNeighbor(CellDirection.DOWNRIGHT).getLocation().equals(new Point(6, 6)));
        assertTrue(cell.getNeighbor(CellDirection.DOWNLEFT).getLocation().equals(new Point(4, 6)));
    }
    
    @Test
    public void testGetPark() {
        assertTrue(cell.getPark() == park);
    }
}
