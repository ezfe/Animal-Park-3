import java.util.ArrayList;
/**
 * @author Ezekiel Elin
 * @version September 29, 2016
 */
class Cell {
    /**
     * Animal this cell holds
     */
    private Animal animal = null;

    /**
     * Plant this cell holds
     */
    private Plant plant = null;

    /**
     * Key of graph node that is @ this cell
     */
    private Integer tnode = null;

    /**
     * Key of the Person that is @ this cell
     */
    private Person person = null;

    /**
     * Location of this cell
     */
    private Point location;

    /**
     * Park this cell resides in
     */
    private Park park;

    /**
     * Whether this cell is a mountain
     */
    private boolean isMountain = false;

    /**
     * Initialize this cell
     * @param p Park to reside in
     * @param k Location to reside at
     */
    public Cell(Park p, Point k) {
        this.park = p;
        this.location = k;
    }

    /**
     * Get the location this cell resides at
     * @return point location
     */
    public Point getLocation() {
        return this.location;
    }

    /**
     * Get the x-position of this cell's location
     * @return int x
     */
    public int getX() {
        return this.location.x;
    }

    /**
     * Get the y-position of this cell's location
     * @return int y
     */
    public int getY() {
        return this.location.y;
    }

    /**
     * Get this cell's neighbor in a given direction
     * @param direction direction to look
     * @return Cell neighbor
     */
    public Cell getNeighbor(CellDirection direction) {
        Point currentLocation = location.copy();
        switch (direction) {
            case UP:
            currentLocation.y -= 1;
            break;
            case DOWN:
            currentLocation.y += 1;
            break;
            case RIGHT:
            currentLocation.x += 1;
            break;
            case LEFT:
            currentLocation.x -= 1;
            break;
            case UPRIGHT:
            currentLocation.y -= 1;
            currentLocation.x += 1;
            break;
            case UPLEFT:
            currentLocation.y -= 1;
            currentLocation.x -= 1;
            break;
            case DOWNRIGHT:
            currentLocation.y += 1;
            currentLocation.x += 1;
            break;
            case DOWNLEFT:
            currentLocation.y += 1;
            currentLocation.x -= 1;
            break;
        }
        return park.getCell(currentLocation);
    }

    /**
     * Get the park this cell resides in
     * @return park park
     */
    public Park getPark() {
        return this.park;
    }

    /**
     * Set the plant field in this cell
     * Updates the plant's cell field automatically
     * @param p plant
     */
    public void setPlant(Plant p) {
        if (!this.isMountain()) {
            plant = p;
            plant.setCell(this);
        }
    }

    /**
     * Remove the plant from the cell
     */
    public void removePlant() {
        plant = null;
    }

    /**
     * Get the plant in this cell
     * @return plant
     */
    public Plant getPlant() {
        return plant;
    }

    /**
     * Set the animal field in this cell
     * Updates the animal's cell field automatically
     * @param a animal
     */
    public void setAnimal(Animal a) {
        if (!this.isMountain()) {
            animal = a;
            animal.setCell(this);
        }
    }

    /**
     * Remove the animal from this cell
     */
    public void removeAnimal() {
        animal = null;
    }

    /**
     * Get the animal in this cell
     * @return animal
     */
    public Animal getAnimal() {
        return animal;
    }

    /**
     * Check whether there is a plant in this cell
     * @return
     */
    public boolean hasPlant() {
        return plant != null;
    }

    /**
     * Check whether there is an animal in this cell
     * @return
     */
    public boolean hasAnimal() {
        return animal != null;
    }

    /**
     * Check whether this cell is a mountain
     */
    public boolean isMountain() {
        return this.isMountain;
    }

    /**
     * Make this cell a mountain
     */
    public void setMountain(boolean b) {
        if (b) {
            this.animal = null;
            this.plant = null;
        }
        this.isMountain = b;
    }

    /**
     * Set this cell's graph node
     */
    public void removeGraphNode() {
        this.removeGraphNode(true);
    }

    /**
     * Set this cell's graph node
     * @param boolean should update node
     */
    public void removeGraphNode(boolean recur) {
        if (recur && this.tnode != null) {
            Park k = this.getPark();
            if (k != null) {
                TouristGraph g = k.graph;
                if (g != null) {
                    g.removeCell(this.tnode, false);
                }
            }
        }
        this.tnode = null;
        this.person = null;
    }

    /**
     * Set this cell's graph node
     * @param int node key
     */
    public void setGraphNode(int key) {
        this.setGraphNode(key, true);
    }

    /**
     * Set this cell's graph node
     * @param int node key
     * @param boolean should update node
     */
    public void setGraphNode(int key, boolean recur) {
        this.tnode = key;
        if (recur) {
            Park k = this.getPark();
            if (k != null) {
                TouristGraph g = k.graph;
                if (g != null) {
                    g.setCell(key, this, false);
                }
            }
        }
    }

    /**
     * Get this cell's graph node
     * @return int key
     */
    public Integer getGraphNode() {
        return this.tnode;
    }

    /**
     * Get this node's person
     * @return Person person
     */
    public Person getPerson() {
        return this.person;
    }

    /**
     * Set this node's person
     * @param Person person
     */
    public boolean setPerson(Person p) {
        if (this.tnode == null) {
            return false;
        } else {
            this.person = p;
            return true;
        }
    }

    /**
     * Fetch all cells within a certain range
     * @param int range to fetch
     * @param boolean whether animals obstruct
     */
    public ArrayList<Cell> getLOSCells(int range, boolean animalsObstruct) {
        range += 1;
        ArrayList<Cell> valids = new ArrayList<Cell>();
        Point current = this.getLocation();
        for(int i = current.x - range + 1; i < current.x + range; i++) {
            if (i >= 0 && i < this.getPark().width) {
                for(int j = current.y - range + 1; j < current.y + range; j++) {
                    if (j >= 0 && j < this.getPark().height) {
                        Point p = new Point(i, j);

                        /* Line Algorithm */

                        double x1 = (double)current.x;
                        double x2 = (double)p.x;
                        double y1 = (double)current.y;
                        double y2 = (double)p.y;

                        boolean s = false;

                        if (Math.abs(x1 - x2) < Math.abs(y1 - y2)) {
                            /* Reverse the line */
                            double tx1 = x1;
                            x1 = y1;
                            y1 = tx1;

                            double tx2 = x2;
                            x2 = y2;
                            y2 = tx2;

                            s = true;
                        }

                        if (x1 > x2) {
                            double tx1 = x1;
                            x1 = x2;
                            x2 = tx1;

                            double ty1 = y1;
                            y1 = y2;
                            y2 = ty1;
                        }

                        Cell previous = null;
                        boolean success = true;
                        for(double x = x1; x <= x2; x += 1) {
                            double t = (x - x1) / (x2 - x1);
                            double y = y1 * (1.0 - t) + y2 * t;

                            Cell currC = null;
                            if (s) {
                                currC = getPark().getCell(new Point((int)y, (int)x));
                            } else {
                                currC = getPark().getCell(new Point((int)x, (int)y));
                            }

                            if (currC == previous) {
                                continue;
                            }

                            if (currC.isMountain() && (!animalsObstruct || currC.getAnimal() == null)) {
                                success = false;
                            } else if (previous != null) {
                                /* check there's a path through mountain range */
                                Point currCLoc = currC.getLocation();
                                Point prevLoc = previous.getLocation();
                                try {
                                    if (prevLoc.x == currCLoc.x - 1 && prevLoc.y == currCLoc.y - 1) {
                                        if (getPark().isMountain(new Point(currCLoc.x, currCLoc.y - 1)) && getPark().isMountain(new Point(currCLoc.x - 1, currCLoc.y))) {
                                            success = false;
                                        }
                                        if (animalsObstruct && getPark().hasAnimal(new Point(currCLoc.x, currCLoc.y - 1)) && getPark().hasAnimal(new Point(currCLoc.x - 1, currCLoc.y))) {
                                            success = false;
                                        }
                                    }
                                    if (prevLoc.x == currCLoc.x - 1 && prevLoc.y == currCLoc.y + 1) {
                                        if (getPark().isMountain(new Point(currCLoc.x, currCLoc.y + 1)) && getPark().isMountain(new Point(currCLoc.x - 1, currCLoc.y))) {
                                            success = false;
                                        }
                                        if (animalsObstruct && getPark().hasAnimal(new Point(currCLoc.x, currCLoc.y + 1)) && getPark().hasAnimal(new Point(currCLoc.x - 1, currCLoc.y))) {
                                            success = false;
                                        }
                                    }
                                    if (prevLoc.x == currCLoc.x + 1 && prevLoc.y == currCLoc.y - 1) {
                                        if (getPark().isMountain(new Point(currCLoc.x, currCLoc.y - 1)) && getPark().isMountain(new Point(currCLoc.x + 1, currCLoc.y))) {
                                            success = false;
                                        }
                                        if (animalsObstruct && getPark().hasAnimal(new Point(currCLoc.x, currCLoc.y - 1)) && getPark().hasAnimal(new Point(currCLoc.x + 1, currCLoc.y))) {
                                            success = false;
                                        }
                                    }
                                    if (prevLoc.x == currCLoc.x + 1 && prevLoc.y == currCLoc.y + 1) {
                                        if (getPark().isMountain(new Point(currCLoc.x, currCLoc.y + 1)) && getPark().isMountain(new Point(currCLoc.x + 1, currCLoc.y))) {
                                            success = false;
                                        }
                                        if (animalsObstruct && getPark().hasAnimal(new Point(currCLoc.x, currCLoc.y + 1)) && getPark().hasAnimal(new Point(currCLoc.x + 1, currCLoc.y))) {
                                            success = false;
                                        }
                                    }
                                } catch(Exception e) {}
                            }

                            previous = currC;
                        }

                        if (success) {
                            valids.add(getPark().getCell(new Point(p.x, p.y)));
                        }

                        /* End Line Algorithm */

                    }
                }
            }
        }
        return valids;
    }

    /**
     * Tick the cell
     */
    public void tick(int tickID) {
        if (this.animal != null) {
            animal.tick(tickID);
        }
        if (this.plant != null) {
            plant.tick(tickID);
        }

        if (this.tnode != null) {
            Park k = this.getPark();
            if (k != null) {
                TouristGraph g = k.graph;
                if (g != null) {
                    if (this.person != null) {
                        this.person.advance(tickID);
                    } else if (g.isEntranceNode(this.tnode)) {
                        System.out.println("Node: " + this.tnode + ", creating person");
                        Person p = new Person(k, g.tour(this.tnode));
                    }
                }
            }

            int range = park.tourRadius + 1;
            int decrease = park.tourEnergyDecrease;

            for(int i = getLocation().x - range + 1; i < getLocation().x + range; i++) {
                if (i >= 0 && i < this.getPark().width) {
                    for(int j = getLocation().y - range + 1; j < getLocation().y + range; j++) {
                        if (j >= 0 && j < this.getPark().height) {
                            Animal a = this.getPark().getAnimal(new Point(i, j));
                            if (a != null) {
                                a.removeEnergy(decrease);
                            }
                            Plant p = this.getPark().getPlant(new Point(i, j));
                            if (p != null) {
                                p.removeEnergy(decrease);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        if (this.isMountain) {
            return "{MNTAIN}";
        }
        return "{P" + (this.plant == null ? "_" : ("`"+this.plant.toString()+"`")) + ", " + "A" + (this.animal == null ? "_" : ("`"+this.animal.toString()+"`")) + "}";
    }
}