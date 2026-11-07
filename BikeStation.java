import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Models an Electric Vehicle Charging Station.
 * A charging station contains multiple {@link Dock} units.
 * @author DP classes 
 * @version 2026.10.13
 */
public class BikeStation
{
    private String idStation;
    private Location location;
    private List<Dock> docks;

    /**
     * Default constructor for class BikeStation
     * Initializes a default station with ID "CC00" at location (0,0)
     */
    public BikeStation(  ) {
        this("CC00", new Location (0,0));
        }

    /**
     * Constructor for objects of class BikeStation.
     * @param id The unique identifier of the station (e.j, "CC00").
     * @param location The georgrafic location fo the station.
     * @throws IllegalArgumentException If id is null or empty, or location is null.
     */
    public BikeStation(String id, Location location)
    {
        if( id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Station ID connot be null or empty.");
        }
        if(location == null){
            throw new IllegalArgumentException("Location cannot be null");
        }
        this.idStation = id;
        this.location = location;
        this.docks = new ArrayList<>();    
        }
    
    /**
     * Adds a new (Dock) to the station and mountains the list ordered
     * according to the sorting criteria (charging speed desc, fee asc, id asc).
     * @param dock The new Dock unit to add
     */    
    public void addDock (Dock dock){
        if(dock != null){
            this.docks.add(dock);
            Collections.sort(this.docks, new ComparatorDockOrder());
        }
    }
    
    /**
     * Retrieves the first free {@link Dock} found at the station.
     * @return A free {@link Dock}, or possibly throws an exception if none are found (depending on stream implementation details).
     */
    public Dock getFreeDock()
    {
        Dock freeDock= null;
        boolean found = false;
        int i = 0;
        while(i< docks.size() && !found){
            Dock current = docks.get(i);
            if(current.isFree()){
                freeDock = current;
                found = true;
            }
            i++;
        }
        return freeDock;     
    }

    /**
     * @return The unique identifier of the charging station.
     */
    public String getId()
    {
        
        return idStation;
    }
    
    /**
     * @return The current location of the charging station
     */
    public Location getLocation(){
        return location;
    }
    
    /**
     * Set the current location of the charging station.
     * @param location Where it is. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setLocation(Location location)
    {
       if(location == null){
           throw new NullPointerException("Location connot be null");
       }
       this.location = location;
    }

    /**
     * @return The list of dock units in this station
     */
    public List<Dock> getDocks(){
        return docks;
    }
    
    /**
     * Calculates the total number of electir bikes recharged across all docks in the station
     * @return Total count of recharged bikes
     */
    public int getTotalBikesRecharged(){
        int total = 0;
        for(Dock dock : docks){
            total += dock.getNumerEVRecharged();
        }
        return total;
    }
    
    /**
     * @return A formatted string with station ID, total recharged bikes, and location
     */
    @Override
    public String toString(){
        return "(Biketation: " + idStation + ", " + getTotalBikesRecharged() + ", " + location + ")";
    }
    
    /**
     * Compares two BikeStation objects for equality based on station ID
     * @param obj The object to compare with
     * @return (true) if both stations have the same ID, (false) otherwise
     */
    @Override
    public boolean equals(Object obj){
        boolean isEqual = false;
        if(this == obj){
            isEqual = true;
        } else{
            if(obj!= null && getClass() == obj.getClass()){
                BikeStation other = (BikeStation) obj;
                isEqual = this.idStation.equals(other.idStation);
            }
        }
        return isEqual;
    }
    
    /**
     * Generates a hash code based on the station ID
     * @return Hash code for the station
     */
    @Override
    public int hashCode(){
        return idStation.hashCode();
    }
    
}
