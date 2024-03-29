import java.util.Scanner;
import java.util.ArrayList;

/**
 * @author Ezekiel Elin
 * @version (September 29, 2016
 */
public class Controller {
    public static void main(String[] args) throws InterruptedException {

        //String configPath = "/Users/ezekielelin/Library/Mobile Documents/com~apple~CloudDocs/Developer/Lafayette/CS150/AnimalPark 3/config.txt"; 
        String configPath = args[0];

        //String graphPath = "/Users/ezekielelin/Library/Mobile Documents/com~apple~CloudDocs/Developer/Lafayette/CS150/AnimalPark 3/graph.txt";

        String graphPath = args[1];
        
        //int steps = -1;
        int steps = Integer.parseInt(args[2]);

        PropertiesFile config = new PropertiesFile(configPath);

        /* SET SEED HERE */
        Wheel wheel = new Wheel(5);

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

        Scanner controller = new Scanner(System.in);
        printControls();
        while (controller.hasNext()) {
            String command = controller.next();
            if (command.toLowerCase().equals("p")) {
                System.out.println(park.mapString());
            } else if (command.toLowerCase().equals("c")) {
                park.tick(wheel.spin(10000));
                System.out.println(park.deltaSummaryString());
            } else if (command.toLowerCase().equals("i")) {
                System.out.println("Processing...");
                while (!park.ended(steps)) {
                    park.tick(wheel.spin(10000));
                }
                System.out.println("Finished simulation...");
            } else if (command.toLowerCase().equals("r")) {
                System.out.println(park.speciesSummaryString());
            } else if (command.toLowerCase().equals("a")) {
                int next = Integer.parseInt(controller.next());
                for (int i = 0; i < next; i += 1) {
                    park.tick(wheel.spin(10000));
                }
            } else if (command.toLowerCase().equals("m")) {
                while (true) {
                    System.out.println(park.mapString());
                    park.tick(wheel.spin(10000));
                    Thread.sleep(250);
                }
            } else if (command.toLowerCase().equals("q")) {
                String t = controller.next();
                String[] point = controller.next().split(",");
                Point p = new Point(Integer.parseInt(point[0]), Integer.parseInt(point[1]));
                if (t.equals("cell")) {
                    if (park.validPoint(p)) {
                        Cell c = park.getCell(p);
                        if (c.hasAnimal()) {
                            System.out.println("Animal: " + c.getAnimal().toString());
                        } else {
                            System.out.println("Animal: None");
                        }

                        if (c.hasPlant()) {
                            System.out.println(" Plant: " + c.getPlant().toString());
                        } else {
                            System.out.println(" Plant: None");
                        }

                        if (c.isMountain()) {
                            System.out.println("Mountain");
                        }

                        if (c.getGraphNode() != null) {
                            System.out.println("Graph node " + c.getGraphNode());
                        }

                        if (c.getPerson() != null) {
                            System.out.println("Person " + c.getPerson().path() + " @@ " + c.getPerson().node());
                        }
                    } else {
                        System.out.println("Invalid location");
                    }
                } else {
                    System.out.println("Please enter a valid analysis type");
                }
            }

            printControls();
        }
        controller.close();
    }

    public static void printControls() {
        System.out.println("---");
        System.out.println("p: map, c: tick & delta report i: continue to end r: print report");
        //System.out.println("\tm: print & tick on loop\n\tq: <cell> <x>,<y>: examine data");
        System.out.print("Command: ");
    }
}
