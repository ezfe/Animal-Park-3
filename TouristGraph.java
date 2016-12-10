import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Collections;
import java.util.Comparator;

/**
 * Undirected Graph of a set of Nodes
 * 
 * @author Ezekiel Elin
 * @version 12/6/2016
 */
public class TouristGraph {
    private HashMap<Integer, Node> nodes = new HashMap<>();

    /**
     * Create graph from a file
     */
    public TouristGraph(String path) {
        Scanner sc = null;
        try {
            sc = new Scanner(new File(path));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Scanner ls = new Scanner(line);
                int k1 = Integer.parseInt(ls.next());
                ls.next();
                int k2 = Integer.parseInt(ls.next());
                int w = Integer.parseInt(ls.next());

                this.addNode(k1);
                this.addNode(k2);
                this.addEdge(k1, k2, w);
            }
        } catch (Exception e) {
            System.out.println("Unable to read " + path);
        } finally {
            if (sc != null) {
                sc.close();
            }
        }
    }

    /**
     * Create a blank graph
     */
    public TouristGraph() {}

    /**
     * Edge in a directed graph
     * Referenced from both outgoing and incoming linked list (of origin and destination)
     */
    private class Edge {
        private Node origin;
        private Node destination;

        private int weight = 1;

        /**
         * Create an new edge for the graph
         * Will not add the node to the graph automatically
         * @param DirectedGraphNode<Key> origin node
         * @param DirectedGraphNode<Key> destination node
         */
        public Edge(Node origin, Node destination, int weight) {
            this.origin = origin;
            this.destination = destination;
            this.weight = weight;
        }

        /**
         * Get the node this edge originates from
         * @return DirectedGraphNode<Key> origin node
         */
        public Node getOrigin() {
            return this.origin;
        }

        /**
         * Get the node this edge points to
         * @return DirectedGraphNode<Key> destination node
         */
        public Node getDestination() {
            return this.destination;
        }

        /**
         * Get the other end
         * If neither are passed, will return the destination
         * @param int this end
         * @return int other end
         */
        public Node otherEnd(Node n) {
            if (this.destination == n) {
                return this.origin;
            } else return this.destination;
        }

        /**
         * Get the weight of this edge
         * @return int weight
         */
        public int getWeight() {
            return this.weight;
        }
    }

    /**
     * Node on the directed graph
     */
    private class Node {
        private int key;
        private Cell cell = null;
        private boolean entrance = false;
        private LinkedList<Edge> edges = new LinkedList<>();

        /**
         * Create a new node for the graph
         * Will not add the node to the graph automatically
         * @param Key key of the node
         */
        public Node(int key) {
            this.key = key;
        }

        /**
         * Get the key of the node
         * @return Key the key
         */
        public int getKey() {
            return this.key;
        }

        /**
         * Set this node's cell
         */
        public void removeCell() {
            this.removeCell(true);
        }

        /**
         * Set this node's cell
         * @param Cell cell
         * @param boolean should update cell key
         */
        public void removeCell(boolean recur) {
            if (recur && this.cell != null) {
                this.cell.removeGraphNode(false);
            }
            this.cell = null;
        }

        /**
         * Set this node's cell
         * @param Cell cell
         */
        public void setCell(Cell c) {
            this.setCell(c, true);
        }

        /**
         * Set this node's cell
         * @param Cell cell
         * @param boolean should update cell key
         */
        public void setCell(Cell c, boolean recur) {
            this.cell = c;
            if (recur && c != null) {
                c.setGraphNode(this.getKey(), false);
            }
        }

        /**
         * Get this node's cell
         * @return Cell cell
         */
        public Cell getCell() {
            return this.cell;
        }

        /**
         * Check if this node is an entrance node
         * @return boolean true if entrance node
         */
        public boolean isEntranceNode() {
            return entrance;
        }

        /**
         * Set entrance node status
         * @param boolean is entrance node
         */
        public void setEntranceNode(boolean e) {
            this.entrance = e;
        }

        /**
         * Add an outgoing edge
         * @param Edge destination edge
         * @return boolean true when edge is added
         */
        public boolean addEdge(Edge edge) {
            if (edge != null) {
                edges.add(edge);
                return true;
            } else {
                return false;
            }
        }

        /**
         * Fetch outgoing iterator
         * @return Iterator outgoing iterator
         */
        public Iterator<Edge> edgeIterator() {
            return edges.iterator();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(getKey() + " --> {");

            Iterator<Edge> edges = edgeIterator();
            while (edges.hasNext()) {
                Edge e = edges.next();
                sb.append(e.otherEnd(this).getKey() + ", ");
            }

            sb.append("}");

            return sb.toString();
        }
    }

    /**
     * Add a node to the graph
     * @param Key used to refer to the node
     * @return boolean true when the node is added
     */
    public void addNode(int key) {
        if (nodes.get(key) == null) {
            nodes.put(key, new Node(key));
        }
    }

    /**
     * Add an edge between two nodes
     * @param Key first key to connect
     * @param Key second key to connect
     * @param int weight of edge
     * @return true when edge is added
     */
    public boolean addEdge(int key1, int key2, int weight) {
        Node node1 = nodes.get(key1);
        Node node2 = nodes.get(key2);

        if (node1 != null || node2 != null) {
            Edge edge = new Edge(node1, node2, weight);
            return node1.addEdge(edge) && node2.addEdge(edge);
        } else {
            return false;
        }
    }

    /**
     * Get all nodes in the graph
     * @return ArrayList<DirectedGraphNode<Key>> nodes
     */
    public ArrayList<Integer> nodes() {
        ArrayList<Integer> returnList = new ArrayList<>();

        Iterator<Map.Entry<Integer, Node>> mapIterator = nodes.entrySet().iterator();
        while (mapIterator.hasNext()) {
            returnList.add(mapIterator.next().getKey());
        }

        return returnList;
    }

    /**
     * Get all entrance nodes in the graph
     * @return ArrayList<DirectedGraphNode<Key>> entrance nodes
     */
    public ArrayList<Integer> entranceNodes() {
        ArrayList<Integer> returnList = new ArrayList<>();

        Iterator<Map.Entry<Integer, Node>> mapIterator = nodes.entrySet().iterator();
        while (mapIterator.hasNext()) {
            Node node = mapIterator.next().getValue();
            if (node.isEntranceNode()) {
                returnList.add(node.getKey());
            }
        }

        return returnList;
    }

    /**
     * Make a node an entrance node
     * @param int key
     * @param boolean entrance node
     */
    public void setEntranceNode(int k, boolean e) {
        Node n = nodes.get(k);
        if (n != null) {
            n.setEntranceNode(e);
        }
    }

    /**
     * Set this node's cell
     * @param Cell cell
     */
    public void removeCell(int k) {
        Node n = nodes.get(k);
        if (n != null) {
            n.removeCell(true);
        }
    }

    /**
     * Set this node's cell
     * @param Cell cell
     * @param boolean should update cell key
     */
    public void removeCell(int k, boolean recur) {
        Node n = nodes.get(k);
        if (n != null) {
            n.removeCell(recur);
        }
    }

    /**
     * Set this node's cell
     * @param Cell cell
     */
    public void setCell(int k, Cell c) {
        Node n = nodes.get(k);
        if (n != null) {
            n.setCell(c, true);
        }
    }

    /**
     * Set this node's cell
     * @param Cell cell
     * @param boolean should update cell key
     */
    public void setCell(int k, Cell c, boolean recur) {
        Node n = nodes.get(k);
        if (n != null) {
            n.setCell(c, recur);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        Iterator<Map.Entry<Integer, Node>> mapIterator = nodes.entrySet().iterator();
        while (mapIterator.hasNext()) {
            Node node = mapIterator.next().getValue();
            builder.append(node.toString() + "\n");
        }

        return builder.toString();
    }

    public ArrayList<Integer> fastestTour(int node) {
        Node start = nodes.get(node);
        if (start == null) return null;

        ArrayList<Integer> newTBV = (ArrayList<Integer>)this.nodes().clone();
        newTBV.remove((Object)start.getKey());

        ArrayList<Integer> path = new ArrayList<Integer>();
        path.add(start.getKey());

        ArrayList<Integer> fpath = TSP(newTBV, path, 0, start,"");
        System.out.println("Distance: " + length(fpath));
        return fpath;
    }

    /**
     * Calculate the length of a path
     * @param ArrayList<Integer> path
     * @return int length
     */
    public int length(ArrayList<Integer> nodes) {
        int score = 0;
        for (int i = 0; i < nodes.size() - 1; i += 1) {
            int thisScore = Integer.MAX_VALUE;
            Node n1 = this.nodes.get(nodes.get(i));
            Node n2 = this.nodes.get(nodes.get(i + 1));
            if (n1 == null || n2 == null) {System.out.println("ERROR NODE NOT FOUND: " + n1 + n2); continue; }
            Iterator<Edge> iterator = n1.edgeIterator();
            while (iterator.hasNext()) {
                Edge e = iterator.next();
                if (e.otherEnd(n1) == n2) {
                    if (thisScore > e.getWeight()) {
                        thisScore = e.getWeight();
                    }
                }
            }
            score += thisScore;
        }
        return score;
    }

    public class PathComparator implements Comparator<ArrayList<Integer>> {
        @Override
        public int compare(ArrayList<Integer> l1, ArrayList<Integer> l2) {
            Integer l1Length = length(l1);
            Integer l2Length = length(l2);
            return l1Length.compareTo(l2Length);
        }
    }

    private ArrayList<Integer> TSP(ArrayList<Integer> toBeVisited, ArrayList<Integer> currentPath, int weight, Node vertex, String indent) {
        /*
        System.out.println(indent + "Starting iteration at " + vertex);
        System.out.println(indent + "To be visited: " + toBeVisited);
        System.out.println(indent + "Current Path: " + currentPath);
        */
        PriorityQueue<ArrayList<Integer>> queue = new PriorityQueue<>(new PathComparator());
        boolean extend = false;
        Iterator<Edge> iterator = vertex.edgeIterator();
        
        if (toBeVisited.size() == 0) {
            return currentPath;
        }
        
        while (iterator.hasNext()) {
            Edge e = iterator.next();
            Node v = e.otherEnd(vertex);
            if (toBeVisited.contains(v.getKey())) {
                ArrayList<Integer> newTBV = (ArrayList<Integer>)toBeVisited.clone();
                newTBV.remove((Object)v.getKey());

                ArrayList<Integer> newCP = (ArrayList<Integer>)currentPath.clone();
                newCP.add(v.getKey());

                //System.out.println(indent + "Calling TSP from node " + vertex + " to node " + v);
                ArrayList<Integer> a = TSP(newTBV, newCP, weight + e.getWeight(), v, indent);// + "\t");
                if (a != null) {
                    //System.out.println(indent + "Result Inserted");
                    queue.add(a);
                } else {
                    //System.out.println(indent + "Null Received");
                }
                extend = true;
            }
        }
        //System.out.println(indent + "Ending iteration at " + vertex);
        if (!extend && toBeVisited.size() > 0) {
            return null;
        } else {
            //System.out.println(indent + "Found option: " + currentPath);
            //System.out.println(indent + "Queue: " + queue);
            return queue.poll();
        }
    }
}
