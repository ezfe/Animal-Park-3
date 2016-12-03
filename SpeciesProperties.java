import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ezekiel Elin
 * @version October 6, 2016
 */
public class SpeciesProperties {
    public String name;
    public String type;
    public String symbol;
    
    public Set<String> sources;
    
    public double medianInitialPopulation;
    public double standardDeviationInitialPopulation;
    public double initialEnergy;
    
    public double medianDeath;
    public double standardDeviationDeath;
    
    public int reproduceThreshold;
    
    public int maxEnergy;
    
    public int livingCost;
    
    public SpeciesProperties(String name, String type, String symbol, Set<String> sources, double medianInitialPopulation, double standardDeviationInitialPopulation, double initialEnergy, double medianDeath, double standardDeviationDeath, int reproduceThreshold, int maxEnergy, int livingCost) {
        this.name = name;
        this.type = type;
        this.symbol = symbol;
        
        this.sources = sources;
        
        this.medianInitialPopulation = medianInitialPopulation;
        this.standardDeviationInitialPopulation = standardDeviationInitialPopulation;
        this.initialEnergy = initialEnergy;
        
        this.medianDeath = medianDeath;
        this.standardDeviationDeath = standardDeviationDeath;
        
        this.reproduceThreshold = reproduceThreshold;
        
        this.maxEnergy = maxEnergy;
        
        this.livingCost = livingCost;
    }
    
    /**
     * Create the property file from a line in a config
     * @param s
     */
    public SpeciesProperties(String s) {
        Scanner sc = new Scanner(s);

        sc.next(); /* remove `species` */

        name = sc.next();
        type = sc.next();
        symbol = sc.next();

        sources = new HashSet<>();
        String[] sourcesArray = sc.next().split(",");
        for (int i = 0; i < sourcesArray.length; i += 1) {
            sources.add(sourcesArray[i]);
        }
        
        String[] inpoparray = sc.next().split(",");
        medianInitialPopulation = Double.parseDouble(inpoparray[0]);
        standardDeviationInitialPopulation = Double.parseDouble(inpoparray[1]);
        initialEnergy = Double.parseDouble(inpoparray[2]);
        
        String[] deatharray = sc.next().split(",");
        medianDeath = Double.parseDouble(deatharray[0]);
        standardDeviationDeath = Double.parseDouble(deatharray[1]);
        
        reproduceThreshold = sc.nextInt();
        maxEnergy = sc.nextInt();
        livingCost = sc.nextInt();
        /*
        System.out.println("Name: " + name);
        System.out.println("Type: " + type);
        System.out.println("Symbol: " + symbol);
        System.out.println("sources: " + sources);
        
        System.out.println("Median Initial Population: " + medianInitialPopulation);
        System.out.println("std. dev of Initial Pop.: " + standardDeviationInitialPopulation);
        System.out.println("Initial Energy: " + initialEnergy);
        
        System.out.println("Median Death: " + medianDeath);
        System.out.println("std. dev of Death: " + standardDeviationDeath);
        
        System.out.println("Reproduce Threshold: " + reproduceThreshold);
        
        System.out.println("Max Energy: " + maxEnergy);
        
        System.out.println("Living Cost: " + livingCost);
        */
        sc.close();
    }
    
    /**
     * Copy the config items
     * @return SpeciesProperties copy
     */
    public SpeciesProperties copy() {
        Set<String> newsources = new HashSet<>();
        newsources.addAll(sources);
        
        SpeciesProperties c = new SpeciesProperties(name, type, symbol, newsources, medianInitialPopulation, standardDeviationInitialPopulation, initialEnergy, medianDeath, standardDeviationDeath, reproduceThreshold, maxEnergy, livingCost);
        
        return c;
    }
    
    /**
     * Check whether this species property item is for a plant or animal
     * @return boolean isPlant
     */
    public boolean isPlant() {
    	return Plant.PLANT_TYPES.contains(this.type);
    }
    
    /**
     * Check whether this species property item is for a plant or animal
     * @return boolean isAnimal
     */
    public boolean isAnimal() {
    	return Animal.ANIMAL_TYPES.contains(this.type);
    }
    
    /**
     * Find an initial population
     * @param w Wheel to use
     * @return int population
     */
    public int rollInitialPopulation(Wheel w) {
    	return (int) w.gaussian(this.medianInitialPopulation, this.standardDeviationInitialPopulation);
    }
    
    /**
     * Find a death limit
     * @param w Wheel to use
     * @return int death limit
     */
    public int rollDeath(Wheel w) {
    	return (int) w.gaussian(this.medianDeath, this.standardDeviationDeath);
    }
    
    @Override
    public String toString() {
    	return "[Species: " + this.name + "]";
    }
    
    @Override
    public boolean equals(Object o) {
    	if (o instanceof SpeciesProperties) {
    		return this.name.equals(((SpeciesProperties)o).name);
    	} else {
    		return false;
    	}
    }
    
    @Override
    public int hashCode() {
    	return this.name.hashCode();
    }
}
