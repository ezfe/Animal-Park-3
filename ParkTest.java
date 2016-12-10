import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class ParkTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class ParkTest {
    Park park;
    SpeciesProperties plantprop = new SpeciesProperties("species wheat vegetable h light 100,5,20 20.1,18 10 1000 0 0 2 0");
    SpeciesProperties animalprop = new SpeciesProperties("species cow herbivore c wheat,banana 70,3,30 40.3,38 8 70 1 0 1 1");
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp() {
        park = new Park(20, 20, 5, new Wheel(10), "T", 0, 0);
    }
    
    @Test
    public void testValidPoints() {
        assertTrue("Valid point is not marked valid", park.validPoint(new Point(0,0)));
        assertTrue("Valid point is not marked valid", park.validPoint(new Point(19,0)));
        assertTrue("Valid point is not marked valid", park.validPoint(new Point(0,19)));
        assertTrue("Valid point is not marked valid", park.validPoint(new Point(19,19)));
        assertTrue("Valid point is not marked valid", park.validPoint(new Point(10,10)));
        
        assertTrue("Invalid point is marked valid", !park.validPoint(new Point(-1,0)));
        assertTrue("Invalid point is marked valid", !park.validPoint(new Point(0,-1)));
        assertTrue("Invalid point is marked valid", !park.validPoint(new Point(-1,-1)));
        assertTrue("Invalid point is marked valid", !park.validPoint(new Point(20,20)));
        assertTrue("Invalid point is marked valid", !park.validPoint(new Point(10,20)));
    }
    
    @Test
    public void testGetCell() {
        Cell c = park.getCell(new Point(5, 6));
        assertTrue(c != null);
        assertTrue(c.getX() == 5);
        assertTrue(c.getY() == 6);
    }
    
    @Test
    public void testPlant() {
        assertTrue(!park.hasPlant(new Point(5, 6)));
        Plant p = new Plant(plantprop, new Wheel(10));
        park.setPlant(new Point(5, 6), p);
        assertTrue(park.getPlant(new Point(5, 6)) == p);
        assertTrue(park.hasPlant(new Point(5, 6)));
    }
    
    @Test
    public void testAnimal() {
        assertTrue(!park.hasAnimal(new Point(5, 6)));
        Animal p = new Animal(animalprop, new Wheel(10));
        park.setAnimal(new Point(5, 6), p);
        assertTrue(park.getAnimal(new Point(5, 6)) == p);
        assertTrue(park.hasAnimal(new Point(5, 6)));
    }
    
    @Test
    public void testFindPlantSpace() {
        Point p = park.findPlantSpace();
        
        Plant pl = new Plant(plantprop, new Wheel(10));
        Plant pl2 = pl.copy();
        
        assertTrue(!park.hasPlant(p));
        
        park.setPlant(p, pl);
        
        Point p2 = park.findPlantSpace();
        
        assertTrue(!p.equals(pl2));
    }
    
    @Test
    public void testFindAnimalSpace() {
        Point p = park.findAnimalSpace();
        
        Animal pl = new Animal(animalprop, new Wheel(10));
        Animal pl2 = pl.copy();
        
        assertTrue(!park.hasAnimal(p));
        
        park.setAnimal(p, pl);
        
        Point p2 = park.findAnimalSpace();
        
        assertTrue(!p.equals(p2));
    }
}
