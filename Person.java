import java.util.ArrayList;

/**
 * Person on the path
 * 
 * @author Ezekiel Elin
 * @version 12/10/2016
 */
public class Person {
    private ArrayList<Integer> intendedPath;
    private int index = 0;
    private Park park;

    private int ltid = -1;
    
    /**
     * Constructor for objects of class Person
     */
    public Person(Park park, ArrayList<Integer> path) {
        this.park = park;
        this.intendedPath = path;
        Cell tryStart = park.graph.getCell(intendedPath.get(0));
        if (tryStart.getPerson() != null) {
            System.out.println("PERSON ERROR: UNABLE TO CREATE PERSON, CELL IS OCCUPIED");
        } else {
            tryStart.setPerson(this);
        }
    }
    
    /**
     * Process this person and advance
     * @return boolean did advance
     */
    public boolean advance(int tickID) {
        if (tickID == ltid) {
            return false;
        }
        
        Cell at = park.graph.getCell(intendedPath.get(index));
        
        int nextIndex = index + 1;
        if (nextIndex >= intendedPath.size()) {
            at.setPerson(null); /* exit the park */
            return true;
        }

        Cell tryGo = park.graph.getCell(intendedPath.get(nextIndex));
        if (tryGo.getPerson() != null) {
            return false;
        } else {
            tryGo.setPerson(this);
            at.setPerson(null);
            this.index = nextIndex;
            return true;
        }
    }
    
    /**
     * Get path
     * @return ArrayList<Integer> path
     */
    public ArrayList<Integer> path() {
        return this.intendedPath;
    }
    
    /**
     * Get current node
     * @return int node
     */
    public Integer node() {
        return this.intendedPath.get(index);
    }
}
