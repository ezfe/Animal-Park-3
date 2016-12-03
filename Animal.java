import java.util.Arrays;
import java.util.ArrayList;
import java.util.PriorityQueue;
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
        
        ArrayList<Cell> visibleCells = getCell().getLOSCells((int)DETECT_RANGE, false);
        ArrayList<Cell> destinationCells = getCell().getLOSCells((int)MOVE_RANGE, true);
        PriorityQueue<CellSc> scoredDestinations = new PriorityQueue<>(new CellScComparator());
        
        for(int i = 0; i < destinationCells.size(); i+= 1) {
            CellSc eval = new CellSc(destinationCells.get(i));
            
            if (this.getEnergy() < this.HUNGER_THRESHOLD) {
                double nearestPredator = 10000;
                double nearestPrey = 10000;
                for(int j = 0; j < visibleCells.size(); j += 1) {
                    Cell look = visibleCells.get(j);
                    Point lloc = look.getLocation();
                    Point cloc = eval.cell.getLocation();
                    double distance = Math.sqrt(Math.pow(lloc.x-cloc.x, 2)+Math.pow(lloc.y-cloc.y, 2));
                    if (look != null && ((look.getAnimal() != null && this.FOOD_SOURCES.contains(look.getAnimal().TYPE)) || (look.getPlant() != null && this.FOOD_SOURCES.contains(look.getPlant().TYPE)))) {
                        if (nearestPrey > distance) {
                            nearestPrey = distance;
                        }
                    }
                    if (look != null && look.getAnimal() != null && look.getAnimal().FOOD_SOURCES.contains(this.TYPE)) {
                        if (nearestPredator > distance) {
                            nearestPredator = distance;
                        }
                    }
                }
                //Smaller is better. If the nearest predator is further than the nearest prey
                //this will be negative
                eval.score = (int)(nearestPrey - nearestPredator);
            } else {
                eval.score = 0;
            }
            
            scoredDestinations.add(eval);
        }
        
        if (scoredDestinations.isEmpty()) {
            System.out.println("NO DESTINATIONS");
        }
        
        while(!scoredDestinations.isEmpty()) {
            Cell temp = scoredDestinations.poll().cell;
            if (temp != null && !temp.isMountain() && temp.getAnimal() == null) {
                Cell old = this.getCell();
                temp.setAnimal(this);
                old.removeAnimal();
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
