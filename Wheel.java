import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
/**
 * @author Ezekiel Elin
 * @version September 6, 2016
 */
public class Wheel {
    private Random r;

    public Wheel() {
        this.r = new Random();
    }
    
    /**
     * Constructor for objects of class Wheel
     */
    public Wheel(int seed) {
        /* Create a random object */
        this.r = new Random(seed);
    }

    /**
     * Generate a random number from [0, Integer.MAX_VALUE)
     */
    public int spin() {
        /* Spin the random object */
        return this.r.nextInt(Integer.MAX_VALUE);
    }
    
    /**
     * Generate a random number from [0, max]
     */
    public int spin(int max) {
        /* Spin the random object */
        return this.r.nextInt(max + 1);
    }
    
    /**
     * Generate a random number with a gaussian distribution
     * @param median median result
     * @param std standard deviation
     * @return double resulting number
     */
    public double gaussian(double median, double std) {
    	return median + this.r.nextGaussian() * std;
    }
    
    /**
     * Randomize the passed ArrayList in place
     * @param list list to shuffle
     */
    public void randomDirections(ArrayList<CellDirection> list) {
    	Collections.shuffle(list, r);
    }
}
