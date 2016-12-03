import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class PlantTest.
 *
 * @author Ezekiel Elin
 * @version October 15, 2016
 */
public class PlantTest {

    SpeciesProperties prop = new SpeciesProperties("species wheat vegetable h light 300,5,20 10.1,5.1 30 50 2");
    Plant plant;
    
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp() {
        plant = new Plant(prop, new Wheel(10));
    }
    
    @Test
    public void testCopy() {
        Plant plant2 = plant.copy();
        plant.removeEnergy(5);
        assertTrue(plant != plant2 && plant2 instanceof Plant);
    }
    
    @Test
    public void testOffSpring() {
        Plant plant2 = plant.createOffspring();
        assertTrue(plant2.getEnergy() == plant.getEnergy()/2);
    }
    
    @Test
    public void testCell() {
        assertTrue(plant.getCell() == null);
    }
    
    @Test
    public void testEnergy() {
        assertTrue("Energy not initial", plant.getEnergy() == plant.originalProperties.initialEnergy);
        plant.setEnergy(5);
        assertTrue("Energy not 5", plant.getEnergy() == 5);
        plant.addEnergy(10);
        assertTrue("Energy not 15", plant.getEnergy() == 15);
        plant.removeEnergy(3);
        assertTrue("Energy not 12", plant.getEnergy() == 12);
    }
    
    @Test
    public void testMaxEnergy() {
        plant.setEnergy(plant.originalProperties.maxEnergy + 30);
        assertTrue("Energy above max", plant.getEnergy() == plant.originalProperties.maxEnergy);
    }
    
    @Test
    public void testMinEnergy() {
        plant.setEnergy(-5);
        assertTrue("Energy below min", plant.getEnergy() == 0);
    }
}
