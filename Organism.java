import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * @author Ezekiel Elin
 * @version September 29, 2016
 */
public abstract class Organism {
	/**
	 * The cell that this Organism resides in
	 * Can and will be null at some points
	 */
    protected Cell cell = null;

    /*
     * Basic informatoin about this organism
     */
    
    public final String SYMBOL;
    public final String TYPE;
    public final String NAME;
    
    /**
     * The types of food this organism can consume
     * Should include `light` for plants
     */
    public final Set<String> FOOD_SOURCES;
    
    /**
     * This organisms current energy level
     */
    private int energy;
    
    /**
     * The threshold at which this organism can reproduce
     */
    public final int REPRODUCE_THRESHOLD;

    /**
     * Total number of times tick() has been called
     */
    protected int tickCount = 0;
    
    /**
     * The threshold for this organism to die of old age
     */
    public final int MAX_TICK_COUNT;

    /**
     * The max energy this organism can have
     * Enforced by usage of get and set methods on `energy` variable
     */
    public final int MAX_ENERGY;
    
    /**
     * Distance the organism can move
     */
    public final int MOVE_RANGE;
    
    /**
     * Distance the organism can see
     */
    public final int DETECT_RANGE;
    
    /**
     * The cost per tick to live
     * Deducted at the END of the tick
     */
    public final int LIVING_COST;
    
    /**
     * Threshold for AI-esque movement
     */
    public final int HUNGER_THRESHOLD;
    
    /*
     * Directions this organism can reproduce or eat in, relative to its current locations
     */
    public final ArrayList<CellDirection> NEIGHBOR_DIRECTIONS = new ArrayList<CellDirection>(Arrays.asList(CellDirection.UP, CellDirection.DOWN, CellDirection.RIGHT, CellDirection.LEFT, CellDirection.UPLEFT, CellDirection.UPRIGHT, CellDirection.DOWNLEFT, CellDirection.DOWNRIGHT));
    
    /**
     * The original property field for this organism. Used to clone the organism and reinitialize the clone.
     * Can also be used to find out information about this organism
     * This information should not be changed
     */
    protected SpeciesProperties originalProperties;
    
    /**
     * Common wheel shared by entire simulation
     */
    public Wheel wheel;
    
    /**
     * Initializer for any organism
     * @param properties the properties field to initialize the organism
     * @param w the common wheel shared by entire simulation
     */
    public Organism(SpeciesProperties properties, Wheel w) {
    	this.wheel = w;
        this.NAME = properties.name;
        this.TYPE = properties.type;
        this.SYMBOL = properties.symbol;
        this.FOOD_SOURCES = properties.sources;
        this.energy = (int)properties.initialEnergy;
        this.MAX_TICK_COUNT = properties.rollDeath(this.wheel);
        this.REPRODUCE_THRESHOLD = properties.reproduceThreshold;
        this.MAX_ENERGY = properties.maxEnergy;
        this.MOVE_RANGE = properties.moveRange;
        this.DETECT_RANGE = properties.detectRange;
        this.LIVING_COST = properties.livingCost;
        this.HUNGER_THRESHOLD = properties.hungerThreshold;
        this.originalProperties = properties;
    }
    
    /**
     * Create a new organism of the proper class
     * This is used when you don't know whether you are creating a plant or an animal
     * @param properties the properties field to initialize the organism
     * @param w the common wheel shared by entire simulation
     * @return organism the organism created
     */
    public static Organism initialize(SpeciesProperties properties, Wheel w) {
        Organism o = null;
        if (properties.isAnimal()) {
            o = new Animal(properties, w);
        } else if (properties.isPlant()) {
            o = new Plant(properties, w);
        }
        return o;
    }

    public Park getPark() {
        if (this.cell != null) {
            return this.cell.getPark();
        } else {
            return null;
        }
    }

    /**
     * Get the cell instance this organism resides in
     * @return cell the cell
     */
    public Cell getCell() {
		return cell;
	}

    /**
     * Set the cell instance that this organism reside in
     * THIS will not update the park data structure and
     * should only be used to tell the organism where it
     * currently lives
     * @param cell the cell
     */
	public void setCell(Cell cell) {
		this.cell = cell;
	}

	/**
	 * Get the tick count of this organism
	 * @return int the tick count
	 */
	public int getTickCount() {
        return this.tickCount;
    }
    
	/**
	 * Get the energy level of this organism
	 * @return int the energy level
	 */
    public int getEnergy() {
    	return this.energy;
    }
    
    /**
     * Set the energy level of this organism
     * @param newEnergy the new energy level
     */
    public void setEnergy(int newEnergy) {
    	this.energy = newEnergy;
    	this.processEnergyDelta();
    }
    
    /**
     * Increment the energy level
     * @param deltaEnergy amount to increment by
     */
    public void addEnergy(int deltaEnergy) {
    	this.setEnergy(this.getEnergy() + deltaEnergy);
    }
    
    /**
     * Decrement the energy level
     * @param deltaEnergy amount to decrement by
     */
    public void removeEnergy(int deltaEnergy) {
    	this.addEnergy(deltaEnergy * -1);
    }
    
    /**
     * Process a change in energy
     * Makes sure that the energy is still within bounds
     */
    private void processEnergyDelta() {
    	if (this.energy > this.MAX_ENERGY) {
    		this.energy = this.MAX_ENERGY;
    	}
    	if (this.energy < 0) {
    		this.energy = 0;
    	}
    }

    @Override
    public String toString() {
        return this.NAME;
    }

    /**
     * Tick the organism
     */
    public void tick() {
        this.tickCount += 1;
        
        /*
         * Check if this organism should die
         * If so, die
         * 
         * Check if this organism can reproduce
         * If so, reproduce
         * 
         * Else, check if this organism can eat
         * If so, eat
         * 
         * Else, check if this orgnaism can move
         * If so, move
         * 
         * Decrement by living cost
         */
        if (tryDie()) {
        	//Died
        } else if (tryReproduce()) {
        	this.getPark().logBirth(this.originalProperties.name);
        } else if (eat()) {
            //Ate
        } else if (randomMove()) {
        	//Moved
        } else {
        	//Cannot do anything!
        }
        
        this.removeEnergy(LIVING_COST);
    }

    /**
     * Try to die. Will die if too old or doesn't have enough energy
     * @return boolean whether the organism died
     */
    public boolean tryDie() {
        if (this.getEnergy() <= 0 || this.tickCount > this.MAX_TICK_COUNT) {
        	this.die();
        	return true;
        }
        return false;
    }

    /**
     * Kill the organism
     */
    public void die() {
        if (this.getCell() != null) {

            if (this.getCell().getAnimal() == this) {
                this.getCell().removeAnimal();
            }

            if (this.getCell().getPlant() == this) {
                this.getCell().removePlant();
            }
            
            if (this.getPark() != null) {
            	this.getPark().logDeath(this.originalProperties.name);
            }
        } else { 
            System.out.println("DeathError: Unable to locate cell container");
        }
    }

    /**
     * Copy the organism
     * @return Organism copy of the organism
     */
    abstract public Organism copy();
    
    /**
     * Create offspring organism
     * @return Organism organism to add back to the board as an offspring
     */
    abstract public Organism createOffspring();
    
    /**
     * Try to reproduce
     * @return boolean whether successfully reproduced
     */
    public boolean tryReproduce() {
    	if (this.getEnergy() < this.REPRODUCE_THRESHOLD) {
    		return false;
    	}
    	
        Organism a = this.createOffspring();
        Cell c = this.getCell();
        
        if (c == null) { return false; }
        
        this.wheel.randomDirections(NEIGHBOR_DIRECTIONS);
        for (int i = 0; i < NEIGHBOR_DIRECTIONS.size(); i += 1) {
            //System.out.println("Direction: " + directions.get(i));
            Cell n = c.getNeighbor(NEIGHBOR_DIRECTIONS.get(i));
            if (n != null && !n.isMountain()) {
                if (a instanceof Animal && !n.hasAnimal()) {
                    n.setAnimal((Animal) a);
                    this.energy /= 2;
                    return true;
                } else if (a instanceof Plant && !n.hasPlant()) {
                    n.setPlant((Plant) a);
                    this.energy /= 2;
                    return true;
                } else {
                    //System.out.println("Unable to fit into a cell");
                }
            } else {
                //System.out.println("No neighbor in that direction");
            }
        }

        return false;
    }
   
    /**
     * Try to eat
     * @return boolean whether ate or failed
     */
    public boolean eat() {
    	/*
    	 * If this organism can eat `ligth`
    	 * then increment by parks tickEnergy
    	 * and exit `true`
    	 * 
    	 * Else, if this organism is an Animal,
    	 * Check if there a plant in the current
    	 * cell, and whether this organism can eat that plant
    	 * If so, eat the plant and exit
    	 * 
    	 * Else, if this organism is an Animal,
    	 * Check (in random order) surrounding
    	 * cells for other animals this
    	 * organism can eat
    	 * If so, eat the animal and exit
    	 */
        if (FOOD_SOURCES.contains("light")) {
            this.energy += getPark().tickEnergy;
            return true;
        } else if (this instanceof Animal) {
            Cell c = getCell();
            if (c.getPlant() != null && this.FOOD_SOURCES.contains(c.getPlant().NAME)) {
            	this.addEnergy(c.getPlant().getEnergy());
                c.getPlant().die();
                return true;
            } else {
            	this.wheel.randomDirections(NEIGHBOR_DIRECTIONS);
                for (int i = 0; i < NEIGHBOR_DIRECTIONS.size(); i += 1) {
                    CellDirection direction = NEIGHBOR_DIRECTIONS.get(i);
                    Cell n = c.getNeighbor(direction);
                    if (n != null && n.getAnimal() != null && this.FOOD_SOURCES.contains(n.getAnimal().NAME)) {
                        this.addEnergy(n.getAnimal().getEnergy());
                        n.getAnimal().die();
                        return true;
                    }
                }
                return false;
            }
        } else {
            return false;
        }
    }
    
    /**
     * Try to move in a random direction
     * @return boolean whether successfully moved
     */
    public boolean randomMove() {
        return false;
    }
}
