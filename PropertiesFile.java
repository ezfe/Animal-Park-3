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
                            System.out.println("ConfigError: Unable to load size");
                        }
                    } else if (keyword.equals("light")) {
                        try {
                            this.light = lsc.nextInt();
                        } catch (Exception e) {
                            System.out.println("ConfigError: Unable to load light");
                        }
                    } else if (keyword.equals("species")) {
                        SpeciesProperties sp = new SpeciesProperties(line);
                        species.add(sp);
                    } else if (keyword.equals("mountain")) {
                        int x1 = lsc.nextInt();
                        int y1 = lsc.nextInt();
                        int x2 = lsc.nextInt();
                        int y2 = lsc.nextInt();
                    }
                    lsc.close();
                }
            }
        } catch (Exception e) {
            System.out.println("ConfigError: Unable to load file");
        } finally {
            if (sc != null) {
                sc.close();
            }
        }
    }
}
