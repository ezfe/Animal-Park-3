import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Class that holds the state of the entire park
 * 
 * @author Ezekiel Elin
 * @version 12/6/2016
 */
public class Park  {
    /**
     * Set of species that this park contains
     * Includes died out species
     */
    private HashSet<SpeciesProperties> species = new HashSet<>();

    /**
     * Log of birth and death events for the last tick
     * Cleared at the beginning of each tick
     */
    private ArrayList<EventLogItem> eventLog = new ArrayList<>();

    /**
     * Number of ticks which the eventLog has had zero items
     * Set to zero whenever the eventLog contains an item
     */
    private int noActionTicks = 0;

    /**
     * Data storage mechanism for the park
     * Should be accessed via helper methods to prevent worrying about null cells
     */
    private HashMap<Point, Cell> grid = new HashMap<>();
    public final int width;
    public final int height;

    /**
     * Energy given via `light` each tick
     */
    public final int tickEnergy;

    /**
     * Total number of times tick() has been called
     */
    private int tickCount = 0;

    /**
     * Common wheel shared by entire simulation
     */
    public Wheel wheel;
    
    /**
     * World tourist graph
     * Public because the graph protects everything necessary
     */
    public TouristGraph graph = null;
    
    public Park(int width, int height, int tickEnergy, Wheel w) {
        this.width = width;
        this.height = height;
        this.tickEnergy = tickEnergy;
        this.wheel = w;
    }

    /**
     * Check whether the passed point is within the bounds of the grid
     * @param key point to check
     * @return boolean whether or not the point is valid
     */
    public boolean validPoint(Point key) {
        if (key.x >= 0 && key.x < this.width && key.y >= 0 && key.y < this.height) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get the cell that occupies the passed point
     * Will only return null for out-of-bounds points
     * @param key point to fetch
     * @return Cell that occupies that point
     */
    public Cell getCell(Point key) {
        if (validPoint(key)) {
            Cell c = grid.get(key);
            if (c == null) {
                c = new Cell(this, key);
                grid.put(key, c);
            }
            return c;
        } else {
            return null;
        }
    }

    /**
     * Update the cell at the passed point with a new plant
     * @param key point to set
     * @param p plant to set
     */
    public void setPlant(Point key, Plant p) {
        this.species.add(p.originalProperties);
        getCell(key).setPlant(p);
    }

    /**
     * Get the plant at the passed point
     * @param key point to get
     * @return plant or null
     */
    public Plant getPlant(Point key) {
        return getCell(key).getPlant();
    }

    /**
     * Update the cell at the passedp oint with a new animal
     * @param key point to set
     * @param a animal to set
     */
    public void setAnimal(Point key, Animal a) {
        this.species.add(a.originalProperties);
        getCell(key).setAnimal(a);
    }

    /**
     * Get the animal at the passed point
     * @param key point to get
     * @return animal or null
     */
    public Animal getAnimal(Point key) {
        return getCell(key).getAnimal();
    }

    /**
     * Check whether the passed point contains a plant
     * @param key point to check
     * @return boolean has plant
     */
    public boolean hasPlant(Point key) {
        Cell c = getCell(key);
        if (c != null) {
            return c.hasPlant();
        }
        return false;
    }

    /**
     * Check whether the passed point is a mountain
     * @param key point to check
     * @return boolean is a mountain
     */
    public boolean isMountain(Point key) {
        Cell c = getCell(key);
        if (c != null) {
            return c.isMountain();
        }
        return false;
    }

    /**
     * Check whether the passed point contains an animal
     * @param key point to check
     * @return boolean has plant
     */
    public boolean hasAnimal(Point key) {
        Cell c = getCell(key);
        if (c != null) {
            return c.hasAnimal();
        }
        return false;
    }

    /**
     * Find a space in the grid for an animal
     * @return point valid point for an animal
     */
    public Point findAnimalSpace() {
        for (int i = 0; i < width * height * 10; i += 1) {
            Point p = Point.randomPoint(width - 1, height - 1);
            if (!hasAnimal(p) && !isMountain(p)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Find a space in the grid for a plant
     * @return point valid point for a plant
     */
    public Point findPlantSpace() {
        for (int i = 0; i < width * height * 10; i += 1) {
            Point p = Point.randomPoint(width - 1, height - 1);
            if (!hasPlant(p) && !isMountain(p)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Tick the entire board
     */
    public void tick() {
        this.clearEventLog();

        this.tickCount += 1;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                Cell c = this.getCell(new Point(x, y));
                if (c != null) c.tick();
            }
        }
        if (this.eventLog.size() == 0) {
            this.noActionTicks += 1;
        } else {
            this.noActionTicks = 0;
        }
    }

    /**
     * Log a death event of a certain species
     * @param species species of organism that died
     */
    public void logDeath(String species) {
        this.eventLog.add(new EventLogItem(EventLogItem.EventLogType.DEATH, species));
    }

    /**
     * Log a birth event of a certain species
     * @param species species of organism that was born
     */
    public void logBirth(String species) {
        this.eventLog.add(new EventLogItem(EventLogItem.EventLogType.BIRTH, species));
    }

    /**
     * Reset the event log
     */
    private void clearEventLog() {
        this.eventLog.clear();
    }

    @Override
    public String toString() {
        return "[Park Object]";
    }

    /**
     * Build the map
     * @return string the map
     */
    public String mapString() {
        StringBuilder b = new StringBuilder();
        for (int r = 0; r < (3*width)+1; r += 1) {
            b.append("-");
        }
        b.append("\n");
        for (int y = 0; y < height; y += 1) {
            b.append("|");
            for (int x = 0; x < width; x += 1) {
                Cell c = this.getCell(new Point(x, y));
                if (c.isMountain()) {
                    b.append("MT");
                } else {
                    if (c.getAnimal() != null) {
                        b.append(c.getAnimal().SYMBOL);
                    } else {
                        b.append(" ");
                    }
                    if (c.getPlant() != null) {
                        b.append(c.getPlant().SYMBOL);
                    } else {
                        b.append(" ");
                    }
                }
                b.append("|");
            }
            b.append("\n");
        }
        for (int r = 0; r < (3*width)+1; r += 1) {
            b.append("-");
        }
        return b.toString();
    }

    /**
     * Build the delta summary
     * @return string delta summary
     */
    public String deltaSummaryString() {
        StringBuilder builder = new StringBuilder();

        Iterator<SpeciesProperties> iter = this.species.iterator();
        while (iter.hasNext()) {
            SpeciesProperties workingSpecies = iter.next();

            int deaths = 0;
            int births = 0;

            for (EventLogItem item : eventLog) {
                if (item.species.equals(workingSpecies.name)) {
                    if (item.type == EventLogItem.EventLogType.BIRTH) {
                        births += 1;
                    } else if (item.type == EventLogItem.EventLogType.DEATH) {
                        deaths += 1;
                    }
                }
            }

            builder.append("---" + workingSpecies.name + "---\n");
            builder.append("  Births: " + births + "\n");
            builder.append("  Deaths: " + deaths + "\n");
            builder.append("   Delta: " + (births-deaths) + "\n");

        }

        return builder.toString();
    }

    /**
     * Build the species summary
     * @return string species summary
     */
    public String speciesSummaryString() {
        StringBuilder builder = new StringBuilder();

        Iterator<SpeciesProperties> iter = this.species.iterator();
        while (iter.hasNext()) {
            SpeciesProperties workingSpecies = iter.next();
            ArrayList<Integer> energyLevels = new ArrayList<>();
            ArrayList<Integer> ages = new ArrayList<>();
            int count = 0;
            for (int x = 0; x < this.width; x += 1) {
                for (int y = 0; y < this.height; y += 1) {
                    Cell c = this.getCell(new Point(x, y));
                    if (c.hasPlant()) {
                        if (c.getPlant().originalProperties.equals(workingSpecies)) {
                            count += 1;
                            energyLevels.add(c.getPlant().getEnergy());
                            ages.add(c.getPlant().tickCount);
                        }
                    }
                    if (c.hasAnimal()) {
                        if (c.getAnimal().originalProperties.equals(workingSpecies)) {
                            count += 1;
                            energyLevels.add(c.getAnimal().getEnergy());
                            ages.add(c.getAnimal().tickCount);
                        }
                    }
                }
            }
            Collections.sort(ages);
            Collections.sort(energyLevels);

            int medianAge = (ages.size() > 0) ? ages.get(ages.size() / 2) : 0;
            int medianEng = (energyLevels.size() > 0) ? energyLevels.get(ages.size() / 2) : 0;

            builder.append("---" + workingSpecies.name + "---\n");
            builder.append("  Count: " + count + "\n");
            builder.append("  Median Age: " + medianAge + "\n");
            builder.append("  Median Engergy: " + medianEng + "\n");
        }

        return builder.toString();
    }

    /**
     * Check whether the simulation should halt
     * @param limit simulation tick limit
     * @return boolean should end
     */
    public boolean ended(int limit) {
        /*
         * If the limit is greater than zero (-1 == ∞) and the
         * tick count is greater than the limit, end it
         * 
         * If there has been no action for 50 or more ticks,
         * end it
         * 
         * If there has been no action for more than 1 tick,
         * check if the board has any animals. If there are
         * any animals, DO NOT end it. otherwise, end it.
         * 
         * If there was action last tick, then do not end it.
         * 
         * otherwise, end it.
         */
        if (limit > 0 && this.tickCount >= limit) {
            return true;
        } else if (this.noActionTicks >= 50) {
            return true;
        } else if (this.noActionTicks > 0) {
            for (int x = 0; x < this.width; x += 1) {
                for (int y = 0; y < this.height; y += 1) {
                    if (this.hasPlant(new Point(x, y)) || this.hasAnimal(new Point(x, y))) {
                        return false;
                    }
                }
            }
            return true;
        } else if (this.noActionTicks == 0) {
            return false;
        } else {
            return true;
        }
    }
}