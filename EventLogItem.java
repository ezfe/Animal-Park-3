public class EventLogItem {
	public enum EventLogType {
		BIRTH, DEATH
	}
	
	/*
	 * Event type and species
	 */
	public EventLogType type;
	public String species;
	
	public EventLogItem(EventLogType type, String species) {
		this.type = type;
		this.species = species;
	}
}
