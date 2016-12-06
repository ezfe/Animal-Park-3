import java.util.Comparator;
/**
 * Write a description of class CellScComparator here.
 * 
 * @author Ezekiel Elin
 * @version 11/11/2016
 */
public class CellScComparator implements Comparator<CellSc> {
    public int compare(CellSc o1, CellSc o2) {
        return ((Integer)o1.score).compareTo(o2.score);
    }
}
