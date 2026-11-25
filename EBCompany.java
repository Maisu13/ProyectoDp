import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
/**
 * Models the operation of an Electric Bike (EB) Company. 
 * It manages a fleet of {@link ElectricBike}s and a network of {@link BikeStation}s.
 * * @author David J. Barnes and Michael Kölling
 * @author DP classes
 * @version 2026.10.13
 */
public class EBCompany  
{
    private String name;
    private List<ElectricBike> subscribedBikes;
    private List<BikeStation> stations;

    /**
     * Constructor for objects of class EVCompany.
     * @param name The name of the company.
     */
    public EBCompany(String name)
    {
        this.name = name;
        this.subscribedBikes = new ArrayList<>();
        this.stations = new ArrayList<>();
    }

    /**
     * Adds an {@link ElectricBike} to the company's fleet.
     * @param bike The electric bike to add.
     */
    public void addElectricBike(ElectricBike bike)
    {       
        this.subscribedBikes.add(bike);
    }

    
    /**
     * Adds a {@link BikeStation} to the company's network.
     * @param station The bike station to add.
     */
    public void addBikeStation(BikeStation station)
    {       
        this.stations.add(station);
    }
    
    
    /**
     * Retrieves a {@link BikeStation} by its unique ID using an Iterator.
     * @param id The ID of the station to find.
     * @return The {@link BikeStation} with the matching ID, or {@code null} if not found.
     */
    public BikeStation getBikeStation(String id)
    {
        BikeStation foundStation = null;
        Iterator<BikeStation> it = stations.iterator();
        while (it.hasNext() && foundStation == null) {
            BikeStation current = it.next();
            if (current.getId().equals(id)) {
                foundStation = current;
            }
        }
        return foundStation;
    }

    /**
     * Retrieves a {@link BikeStation} by its {@link Location} using an Iterator.
     * @param location The {@link Location} of the station to find.
     * @return The {@link BikeStation} at the matching location, or {@code null} if not found.
     */
    public BikeStation getBikeStation(Location location)
    {
        BikeStation foundStation = null;
        Iterator<BikeStation> it = stations.iterator();
        while (it.hasNext() && foundStation == null) {
            BikeStation current = it.next();
            if (current.getLocation().equals(location)) {
                foundStation = current;
            }
        }
        return foundStation;
    }
    /**
     * @return The total number of managed {@link BikeStation}s.
     */
    public int getNumberOfStations(){
        return stations.size();
    }
    
    /**
     * Clears all managed bikes and stations, resetting the company to an empty state.
     */
    public void reset(){
        this.subscribedBikes.clear();
        this.stations.clear();
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public List<ElectricBike> getSuscribedBikes(){
        return subscribedBikes;
    }
    
    public void setSubscribedBikes(List<ElectricBike> subscribedBikes){
        this.subscribedBikes = subscribedBikes;
    }
    
    public List<BikeStation> getStations(){
        return stations;
    }
    
    public void setStations(List<BikeStation> stations){
        this.stations = stations;
    }
}