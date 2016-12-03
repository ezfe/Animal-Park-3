import java.util.Scanner;

/**
 * @author Ezekiel Elin
 * @version (September 29, 2016
 */
public class Controller {
    public static void main(String[] args) throws InterruptedException {

    	String configPath = "/Users/ezekielelin/Library/Mobile Documents/com~apple~CloudDocs/Developer/Lafayette/CS150/AnimalPark 3/config.txt"; 
    	//String configPath = args[0];
    	
    	int steps = -1;
    	//int steps = Integer.parseInt(args[1]);
    	
        PropertiesFile config = new PropertiesFile(configPath);
        
        /* SET SEED HERE */
        Wheel wheel = new Wheel(5);
        
        Park park = new Park(config.width, config.height, config.light, wheel);
        
        for (int i = 0; i < config.mountains.size(); i += 1) {
            Mountain m = config.mountains.get(i);
            m.populate(park);
        }
       
        for (int z = 0; z < config.species.size(); z += 1) {
            SpeciesProperties itemProperties = config.species.get(z);
            //System.out.println("Doing " + itemProperties.name);
            Organism s = Organism.initialize(itemProperties, wheel);
            for (int i = 0; i < itemProperties.rollInitialPopulation(park.wheel); i += 1) {
                Point p = null;
                if (itemProperties.isAnimal()) {
                    p = park.findAnimalSpace();
                    if (p == null) System.out.println("PopError: PARK IS FULL");
                    if (p != null) {
                        park.setAnimal(p, ((Animal)s).copy());
                    }
                } else if (itemProperties.isPlant()) {
                    p = park.findPlantSpace();
                    if (p == null) System.out.println("PopError: PARK IS FULL");
                    if (p != null) {
                        park.setPlant(p, ((Plant)s).copy());
                    }
                }
            }
        }

        Scanner controller = new Scanner(System.in);
        System.out.println("p: map, c: tick & delta report, i: continue to end; r: print report");
    	System.out.print("Command: ");
        while (controller.hasNext()) {
        	if (controller.hasNext()) {
        		String command = controller.next();
        		if (command.toLowerCase().equals("p")) {
        			System.out.println(park.mapString());
        		} else if (command.toLowerCase().equals("c")) {
        			park.tick();
        			System.out.println(park.deltaSummaryString());
        		} else if (command.toLowerCase().equals("i")) {
        			System.out.println("Processing...");
        			while (!park.ended(steps)) {
        				park.tick();
        			}
        			System.out.println("Finished simulation...");
        		} else if (command.toLowerCase().equals("r")) {
        			System.out.println(park.speciesSummaryString());
        		} else if (command.toLowerCase().equals("a")) {
        		    int next = Integer.parseInt(controller.next());
        		    for (int i = 0; i < next; i += 1) {
        		        park.tick();
        		    }
        		} else if (command.toLowerCase().equals("m")) {
        		    while (true) {
        		        System.out.println(park.mapString());
        		        park.tick();
        		        Thread.sleep(250);
        		    }
        		}
        	}
            System.out.println("p: map, c: tick & delta report, i: continue to end; r: print report");
        	System.out.print("Command: ");
        }
        controller.close();
    }
}
