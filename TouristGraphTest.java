import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class DirectedGraphTest.
 *
 * @author Ezekiel Elin
 * @version 11/17/2016
 */
public class TouristGraphTest {
    TouristGraph graph = null;

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp() {
        graph = new TouristGraph();
    }

    @Test
    public void testEmptyCreation() {
        assertTrue("New graph is not empty", graph.nodes().size() == 0);
    }

    @Test
    public void testAddNode() {
        graph.addNode(1);
        assertTrue("Not one node after adding node", graph.nodes().size() == 1);
        graph.addNode(1);
        assertTrue("Duplicate shouldn't add", graph.nodes().size() == 1);
        graph.addNode(2);
        assertTrue("Not two nodes after adding 2 nodes", graph.nodes().size() == 2);
    }

    @Test
    public void testEntranceNodes() {
        graph.addNode(1);
        graph.addNode(2);
        graph.setEntranceNode(2, true);

        assertTrue("Entrance node != b", graph.entranceNodes().get(0).equals(2));
        assertTrue("Entrance nodes >1 found", graph.entranceNodes().size() == 1);
    }
}
