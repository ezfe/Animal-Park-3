import java.util.Scanner;
import java.util.ArrayList;

/**
 * @author Ezekiel Elin
 * @version (September 29, 2016
 */
public class Controller {
    public static void main(String[] args) throws InterruptedException {

        String configPath = "/Users/ezekielelin/Library/Mobile Documents/com~apple~CloudDocs/Developer/Lafayette/CS150/AnimalPark 3/config.txt"; 
        //String configPath = args[0];

        String graphPath = "/Users/ezekielelin/Library/Mobile Documents/com~apple~CloudDocs/Developer/Lafayette/CS150/AnimalPark 3/graph.txt";

        //String graphPath = args[1];

        int steps = 500;
        //int steps = Integer.parseInt(args[2]);

        PropertiesFile config = new PropertiesFile(configPath);

        for (int seed =  0; seed < 5; seed += 1) {

            System.out.println("Starting seed " + seed);

            /* SET SEED HERE */
            Wheel wheel = new Wheel(seed);

            Park park = new Park(config.width, config.height, config.light, wheel, config.tourSymbol, config.tourRadius, config.tourEnergyDecrease);

            park.graph = new TouristGraph(graphPath);

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

            park.rebindGraph();

            /* Mark Starting Nodes */
            ArrayList<Integer> startingNodes = config.tourNodeSet;
            for (Integer n: startingNodes) {
                park.graph.setEntranceNode(n, true);
            }

            /* Print out their tours */
            ArrayList<Integer> nodes = park.graph.entranceNodes();
            for (Integer n: nodes) {
                System.out.println(park.graph.tour(n));
            }

            int y = 0;
            System.out.println(y + " tick report");
            System.out.println(park.speciesSummaryString());
            while (!park.ended(steps)) {
                y += 1;
                park.tick(wheel.spin(10000));
                if (y % 50 == 0) {
                    System.out.println(y + " tick report");
                    System.out.println(park.speciesSummaryString());
                }
            }
            System.out.println(y + " tick report");
            System.out.println(park.speciesSummaryString());
            System.out.println("Finished simulation...");
            System.out.println("Press enter to repeat simulation");
            Scanner controller = new Scanner(System.in);
            while (!controller.hasNextLine()) {};
            controller.close();
        }
    }

    public static void printControls() {
        System.out.println("---");
        System.out.println("\tp: map\n\tc: tick & delta report\n\ti: continue to end\n\tr: print report");
        System.out.println("\tm: print & tick on loop\n\tn: Go to end, report every 50\n\tq: <cell> <x>,<y>: examine data");
        System.out.print("Command: ");
    }
}
