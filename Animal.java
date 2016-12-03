import java.util.Arrays;
import java.util.HashSet;

/**
 * @author Ezekiel Elin
 * @version September 29, 2016
 */
public class Animal extends Organism {
	/**
	 * Types of animals that are animals
	 */
    public final static HashSet<String> ANIMAL_TYPES = new HashSet<>(Arrays.asList("herbivore", "omnivore", "carnivore"));
    
    public Animal(SpeciesProperties properties, Wheel w) {
        super(properties, w);
    }
    
    @Override
    public boolean randomMove() {
        this.wheel.randomDirections(NEIGHBOR_DIRECTIONS);
        for (int i = 0; i < NEIGHBOR_DIRECTIONS.size(); i += 1) {
            CellDirection direction = NEIGHBOR_DIRECTIONS.get(i);
            if (this.getCell() != null && this.getCell().getNeighbor(direction) != null && !this.getCell().getNeighbor(direction).hasAnimal()) {
            	this.getCell().getNeighbor(direction).setAnimal(this);
            	this.getCell().removeAnimal();
            	return true;
            }
        }
        return false;
    }
    
    @Override
    public Animal copy() {
        SpeciesProperties c = this.originalProperties.copy();
        return new Animal(c, this.wheel);
    }
    
    @Override
    public Animal createOffspring() {
        Animal a = copy();
        a.setEnergy(a.getEnergy() / 2);
        return a;
    }
}
