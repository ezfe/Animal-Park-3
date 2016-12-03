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
     * Location of this cell
     */
    private Point location;

    /**
     * Park this cell resides in
     */
    private Park park;

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
        plant = p;
        plant.setCell(this);
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
        animal = a;
        animal.setCell(this);
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
     * Tick the cell
     */
    public void tick() {
        if (this.animal != null) {
            animal.tick();
        }
        if (this.plant != null) {
            plant.tick();
        }
    }

    @Override
    public String toString() {
        return "{P" + (this.plant == null ? "_" : ("`"+this.plant.toString()+"`")) + ", " + "A" + (this.animal == null ? "_" : ("`"+this.animal.toString()+"`")) + "}";
    }
}