import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

/**
 * Write a description of class PropertiesFile here.
 * 
 * @author Ezekiel Elin
 * @version October 6, 2016
 */
public class PropertiesFile {

    /*
     * Dimensions of board
     */
    int width = 0;
    int height = 0;

    /**
     * Light energy
     */
    int light = 0;

    /**
     * Species to add
     */
    ArrayList<SpeciesProperties> species = new ArrayList<>();

    /**
     * Mountains to add
     */
    ArrayList<Mountain> mountains = new ArrayList<>();

    String tourSymbol = "T";
    int tourRadius = 0;
    int tourEnergyDecrease = 0;
    ArrayList<Integer> tourNodeSet = new ArrayList<>();
    
    public PropertiesFile(String path) {
        Scanner sc = null;
        try {
            sc = new Scanner(new File(path));
            while(sc.hasNextLine()) {
                String line = sc.nextLine();
                Scanner lsc = new Scanner(line);
                if (lsc.hasNext()) {
                    String keyword = lsc.next();
                    if (keyword.equals("size")) {
                        try {
                            this.width = lsc.nextInt();
                            this.height = lsc.nextInt();
                        } catch (Exception e) {
                            System.out.println("ConfigError: Unable to load size: " + e);
                        }
                    } else if (keyword.equals("light")) {
                        try {
                            this.light = lsc.nextInt();
                        } catch (Exception e) {
                            System.out.println("ConfigError: Unable to load light: " + e);
                        }
                    } else if (keyword.equals("species")) {
                        SpeciesProperties sp = new SpeciesProperties(line);
                        species.add(sp);
                    } else if (keyword.equals("mountain")) {
                        try {
                            String[] points = lsc.next().split(",");
                            int x1 = Integer.parseInt(points[0]);
                            int y1 = Integer.parseInt(points[1]);
                            int x2 = Integer.parseInt(points[2]);
                            int y2 = Integer.parseInt(points[3]);
                            mountains.add(new Mountain(x1, y1, x2, y2));
                        } catch (Exception e) {
                            System.out.println("ConfigError: Unable to load mountain: " + e);
                        }
                    } else if (keyword.equals("tour")) {
                        tourSymbol = lsc.next();
                        String[] nodes = lsc.next().split(",");
                        for(int i = 0; i < nodes.length; i += 1) {
                            tourNodeSet.add(Integer.parseInt(nodes[i]));
                        }
                        tourRadius = Integer.parseInt(lsc.next());
                        tourEnergyDecrease = Integer.parseInt(lsc.next());
                    }
                    lsc.close();
                }
            }
        } catch (Exception e) {
            System.out.println("ConfigError: Unable to load file: " + e);
        } finally {
            if (sc != null) {
                sc.close();
            }
        }
    }
}
