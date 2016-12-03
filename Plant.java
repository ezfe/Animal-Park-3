import java.util.Arrays;
import java.util.HashSet;

/**
 * @author Ezekiel Elin
 * @version September 29, 2016
 */
public class Plant extends Organism {
	/**
	 * Types of organisms that are plants
	 */
    public final static HashSet<String> PLANT_TYPES = new HashSet<>(Arrays.asList("plant", "fruit", "vegetable"));

    public Plant(SpeciesProperties properties, Wheel w) {
        super(properties, w);
    }
    
    @Override
    public Plant copy() {
        SpeciesProperties c = this.originalProperties.copy();
        return new Plant(c, this.wheel);
    }
    
    @Override
    public Plant createOffspring() {
        Plant p = copy();
        p.setEnergy(p.getEnergy() / 2);
        return p;
    }
}
