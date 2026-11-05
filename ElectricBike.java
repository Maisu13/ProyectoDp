import java.util.List;
import java.util.ArrayList;

/**
 * Model the common elements of an Electric bike (EB) that operates 
 * within the simulation, moving towards a target and potentially recharging.
 * * @author David J. Barnes and Michael Kölling
 * @author DP classes 
 * @version 2026.10.13
 */
public class ElectricBike 
{
    private String idBike;
    private EBCompany company;
    private Location location;
    private Location targetLocation;
    private Location rechargingLocation;
    
    private int idleCount;
    private int batteryCapacity;
    private int batteryLevel;
    private int wsCharged;
    private int chargesCount;
    private double chargesCost;
    
    private List<Dock> docks;

    /**
     * Constructor of class ElectricBike.
     * @param company The EB's operating company. Must not be null.
     * @param location The EB's starting {@link Location}. Must not be null.
     * @param targetLocation The EB's final destination {@link Location}. Must not be null.
     * @param id The identificator of the bike.
     * @param batteryCapacity The maximum capacity of the battery.
     * @throws NullPointerException If company, location, or targetLocation is null.
     */
    public ElectricBike(EBCompany company, Location location, Location targetLocation, String id, int batteryCapacity)
    {
        this.company = company;
        this.location = location;
        this.targetLocation = targetLocation;
        this.idBike = id;
        this.batteryCapacity = batteryCapacity;
        this.batteryLevel = batteryCapacity;
           
        this.rechargingLocation = null;
        this.idleCount = 0;
        this.wsCharged = 0;
        this.chargesCount = 0;
        this.chargesCost = 0.0;
           
        this.docks = new ArrayList<Dock>();
        
        calculateRoute();
    }

    /**
     * @return The id of the electric bike.
     */
    public String getIdBike()
    {
        return idBike;    
    }

    /**
     * Set the current location.
     * @param location Where it is. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setLocation(Location location)
    {
        this.location = location;
    }
    
    /**
     * Get the final target location.
     * @return Where this bike is ultimately headed.
     */
    public Location getTargetLocation()
    {
        return targetLocation;
    }

    /**
     * Get the temporary recharging location.
     * @return The {@link Location} of the next {@link BikeStation} to visit, or null if no recharge is planned.
     */
    public Location getRechargingLocation()
    {
        return rechargingLocation;
    }
    
    /**
     * Set the required final target location.
     * @param location Where to go. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setTargetLocation(Location location)
    {
        this.targetLocation = location;
        calculateRoute();
    }

    /**
     * Calculates the optimal route for the bike. 
     * If there isn't enough battery to reach the target, it attempts to find an intermediate 
     * {@link BikeStation} and sets it as the {@code rechargingLocation}.
     */
    public void calculateRoute()
    {
        if (enoughBattery(distanceToTheTargetLocation())){
            this.rechargingLocation = null;
        }
        else{
            calculateRechargingPosition();
        }
    }

    /**
     * Gets a string representation of the planned route, including the recharging stop if one exists.
     * @return A string showing the route: {@code currentLocation -> [rechargingLocation ->] targetLocation}.
     */
    public String getStringRoute()
    {
       if (this.rechargingLocation != null){
           return this.location.toString() + " -> " + this.rechargingLocation.toString() + " -> " + this.targetLocation.toString();
       }
       return this.location.toString() + " -> " + this.targetLocation.toString();
    }

    /**
     * Checks if the current battery level is sufficient to cover a given distance.
     * @param distance The distance to cover.
     * @return true if battery is enough.
     */
    public boolean enoughBattery(int distance)
    {
        return this.batteryLevel >= (distance * 5);
    }

    /**
     * Determines the optimal intermediate {@link BikeStation} to visit for recharging
     * if the bike cannot reach the final target directly.
     * Sets {@code rechargingLocation} to the chosen station's location.
     */
    public void calculateRechargingPosition()
    {
        int minTotalDist = Integer.MAX_VALUE;
        BikeStation bestStation = null;
        
        for (BikeStation station : company.getStations()){
            int distToStation = location.distance(station.getLocation());
            if(enoughBattery(distToStation)){
                int totalDist = distToStation + station.getLocation().distance(targetLocation);
                if (totalDist < minTotalDist){
                    minTotalDist = totalDist;
                    bestStation = station;
                }
            }
        }
        
        if (bestStation != null){
            rechargingLocation = bestStation.getLocation();
        }
    }

    /**
     * Get the Manhattan-like distance to the final target location from the current location.
     * @return The distance to the target location.
     */
    public int distanceToTheTargetLocation()
    {
        return location.distance(targetLocation);
    }

    /**
     * Simulates the recharging process when the bike arrives at a {@code rechargingLocation}.
     * The battery is charged to full capacity, the cost is calculated, and the route is recalculated.
     * //TODO complete the JavaDoc comments
     */
    public void recharge(   )
    {
        BikeStation station = company.getStationAt(location);
        if (station != null){
            Dock dock = station.getFreeDock();
            
            if (dock != null){
                int whToCharge = batteryCapacity - batteryLevel;
                        
                dock.addRechargedBike();
                docks.add(dock);
                batteryLevel = batteryCapacity;
                wsCharged += whToCharge;
                incrementCharges();
                incrementChargesCost(whToCharge * dock.getTariff());
                
                calculateRoute();
            }
        }
    }

    /**
     * Increments the count of recharges performed by this bike.
     * //TODO complete the JavaDoc comments
     */
    public void incrementCharges()
    {
        chargesCount++;
    }

    /**
     * Adds a cost amount to the total charges cost.
     * //TODO complete the JavaDoc comments
     */
    public void incrementChargesCost(double cost)
    {
        chargesCost += cost;
    }   

    /**
     * Carries out a single step of the bike's actions.
     * Moves one step towards the target (recharging or final) or stays idle.
     * //TODO complete the JavaDoc comments
     */
    public void act(   )
    {
       if (location.equals(targetLocation)){
           idleCount++;
       }
       
       Location target;
       if(rechargingLocation != null){
           target = rechargingLocation;
       }
       else{
           target = targetLocation;
       }
       
       setLocation(location.nextLocation(target));
       reduceBatteryLevel();
       
       if (rechargingLocation != null && location.equals(rechargingLocation)){
           recharge();
       }
    }

    /**
     * Reduces the battery level by the cost of one movement step (defined in {@link EBCompany#MOVINGCOST}).
     * Ensures the battery level does not go below zero.
     */
    public void reduceBatteryLevel(){
       batteryLevel = batteryLevel - 5;
       
       if (batteryLevel < 0){
           batteryLevel = 0;
       }
    }

    /**
     * Returns a detailed string representation of the electric bike.
     * @return A string containing the bike's name, plate, battery info, charge counts, costs, idle count, and route.
     */
    @Override
    public String toString(){
        String rechargeStr;
        
        if (rechargingLocation != null){
            rechargeStr = rechargingLocation.toString();
        }
        else{
            rechargeStr = "";
        }
        
        return String.format("ElectricBike: %s, %dwh, %dwh, %d, %s€, %d, %s, %s, %s",
            idBike, batteryCapacity, batteryLevel, chargesCount, chargesCost, idleCount,
            location.toString(), rechargeStr, targetLocation.toString());
    }

}