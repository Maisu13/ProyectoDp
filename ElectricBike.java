import java.util.List;
import java.util.ArrayList;

/**
 * Models an Electric Bike (EB) that operates within the simulation,
 * moving towards a target location and recharging at stations if necessary.
 * 
 * @author David J. Barnes and Michael Kölling
 * @author DP classes 
 * @version 2026.11.15
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
    private int arrivingStep;
    
    private List<Dock> docks;

    /**
     * Constructor of class ElectricBike.
     * @param company The EB's operating company. Must not be null.
     * @param location The EB's starting {@link Location}. Must not be null.
     * @param targetLocation The EB's final destination {@link Location}. Must not be null.
     * @param id The identifier of the bike.
     * @param batteryCapacity The maximum capacity of the battery in Wh.
     * @throws IllegalArgumentException If location, targetLocation, or id is null/empty, or batteryCapacity <= 0.
     */
    public ElectricBike(EBCompany company, Location location, Location targetLocation, String id, int batteryCapacity)
    {
        if (location == null || targetLocation == null) {
            throw new IllegalArgumentException("Locations cannot be null.");
        }
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Bike ID cannot be null or empty.");
        }
        if (batteryCapacity <= 0) {
            throw new IllegalArgumentException("Battery capacity must be greater than zero.");
        }
        
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
        this.arrivingStep = Integer.MAX_VALUE;
           
        this.docks = new ArrayList<>();
        
        calculateRoute();
    }

    /**
     * @return The unique identifier of the electric bike.
     */
    public String getIdBike()
    {
        return idBike;    
    }

    /**
     * @return The number of turns the bike has been idle.
     */
    public int getIdleCount()
    {
        return idleCount;
    }

    /**
     * @return The current location of the electric bike.
     */
    public Location getLocation()
    {
        return location;
    }

    /**
     * Sets the current location of the electric bike.
     * @param location Where the bike is located. Must not be null.
     * @throws IllegalArgumentException If location is null.
     */
    public void setLocation(Location location)
    {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null.");
        }
        this.location = location;
    }
    
    /**
     * @return The final target location of the bike.
     */
    public Location getTargetLocation()
    {
        return targetLocation;
    }

    /**
     * Sets the required final target location and recalculates the route.
     * @param location Where to go. Must not be null.
     * @throws IllegalArgumentException If location is null.
     */
    public void setTargetLocation(Location location)
    {
        if (location == null) {
            throw new IllegalArgumentException("Target location cannot be null.");
        }
        this.targetLocation = location;
        calculateRoute();
    }

    /**
     * @return The {@link Location} of the intermediate {@link BikeStation} to visit, or null if none is planned.
     */
    public Location getRechargingLocation()
    {
        return rechargingLocation;
    }

    /**
     * @return The simulation step when the bike arrived at its target destination, or Integer.MAX_VALUE if not arrived.
     */
    public int getArrivingStep()
    {
        return arrivingStep;
    }

    /**
     * @return The list of {@link Dock} units where this bike has recharged.
     */
    public List<Dock> getDocks()
    {
        return docks;
    }

    /**
     * @return The maximum battery capacity in Wh.
     */
    public int getBatteryCapacity()
    {
        return batteryCapacity;
    }

    /**
     * @return The current battery level in Wh.
     */
    public int getBatteryLevel()
    {
        return batteryLevel;
    }

    /**
     * @return The total Wh charged across all recharges.
     */
    public int getWsCharged()
    {
        return wsCharged;
    }

    /**
     * @return The total number of recharges performed.
     */
    public int getChargesCount()
    {
        return chargesCount;
    }

    /**
     * @return The total cost in euros of all recharges.
     */
    public double getChargesCost()
    {
        return chargesCost;
    }

    /**
     * Calculates the optimal route for the bike.
     * If there is not enough battery to reach the final target, searches for an intermediate charging station.
     */
    public void calculateRoute()
    {
        if (enoughBattery(distanceToTheTargetLocation())) {
            this.rechargingLocation = null;
        } else {
            calculateRechargingPosition();
        }
    }

    /**
     * Gets a string representation of the planned route.
     * @return A string showing {@code currentLocation -> [rechargingLocation ->] targetLocation}.
     */
    public String getStringRoute()
    {
        String route;
        if (this.rechargingLocation != null) {
            route = this.location.toString() + " -> " + this.rechargingLocation.toString() + " -> " + this.targetLocation.toString();
        } else {
            route = this.location.toString() + " -> " + this.targetLocation.toString();
        }
        return route;
    }

    /**
     * Checks if the current battery level is sufficient to cover a given distance (5 Wh per step).
     * @param distance The distance in steps to cover.
     * @return {@code true} if battery is enough, {@code false} otherwise.
     */
    public boolean enoughBattery(int distance)
    {
        return this.batteryLevel >= (distance * 5);
    }

    /**
     * Determines the optimal intermediate {@link BikeStation} to visit for recharging
     * if the bike cannot reach the final target directly.
     */
    public void calculateRechargingPosition()
    {
        int minTotalDist = Integer.MAX_VALUE;
        BikeStation bestStation = null;
        
        if (company != null) {
            for (BikeStation station : company.getStations()) {
                int distToStation = location.distance(station.getLocation());
                if (distToStation > 0 && enoughBattery(distToStation)) {
                    int totalDist = distToStation + station.getLocation().distance(targetLocation);
                    if (totalDist < minTotalDist) {
                        minTotalDist = totalDist;
                        bestStation = station;
                    }
                }
            }
        }
        
        if (bestStation != null) {
            rechargingLocation = bestStation.getLocation();
        } else {
            rechargingLocation = null;
        }
    }

    /**
     * Calculates the Chebyshev distance from the current location to the final target location.
     * @return The number of steps to the target location.
     */
    public int distanceToTheTargetLocation()
    {
        return location.distance(targetLocation);
    }

    /**
     * Simulates the recharging process when the bike is at a charging station.
     */
    public void recharge()
    {
        rechargeWithMessage(0);
    }

    /**
     * Simulates the recharging process at the current station and formats the simulation event message.
     * @param currentStep The current simulation step.
     * @return A formatted recharge message according to Annex I, or {@code null} if no recharge occurred.
     */
    private String rechargeWithMessage(int currentStep)
    {
        String message = null;
        
        if (company != null) {
            BikeStation station = company.getBikeStation(location);
            if (station != null) {
                Dock dock = station.getFreeDock();
                if (dock != null) {
                    int whToCharge = batteryCapacity - batteryLevel;
                    float cost = dock.recharge(this, whToCharge);
                    
                    docks.add(dock);
                    batteryLevel = batteryCapacity;
                    wsCharged += whToCharge;
                    incrementCharges();
                    incrementChargesCost(cost);
                    calculateRoute();
                    
                    float costRounded = Math.round(cost * 10.0f) / 10.0f;
                    message = "(step: " + currentStep + "  ElectricBike: " + idBike + 
                              " recharges: " + whToCharge + "wh at dock: " + dock.getId() + 
                              " with cost: " + costRounded + "€ ********)";
                } else {
                    rechargingLocation = null;
                    targetLocation = location;
                }
            }
        }
        
        return message;
    }

    /**
     * Increments the count of recharges performed by this bike.
     */
    public void incrementCharges()
    {
        chargesCount++;
    }

    /**
     * Adds a cost amount to the total charges cost.
     * @param cost The cost in euros to add.
     */
    public void incrementChargesCost(double cost)
    {
        chargesCost += cost;
    }   

    /**
     * Carries out a single step of the bike's actions without step tracking.
     */
    public void act()
    {
        act(0);
    }

    /**
     * Carries out a single step of the bike's actions during the simulation.
     * Moves one step towards the target, recharges at a station, or stays idle.
     * @param currentStep The current simulation step number.
     * @return An event message if the bike recharged or reached its destination, or {@code null} otherwise.
     */
    public String act(int currentStep)
    {
        String eventMessage = null;
        
        if (location.equals(targetLocation)) {
            idleCount++;
        } else if (rechargingLocation == null && !enoughBattery(distanceToTheTargetLocation())) {
            idleCount++;
        } else {
            Location target;
            if (rechargingLocation != null) {
                target = rechargingLocation;
            } else {
                target = targetLocation;
            }
            
            setLocation(location.nextLocation(target));
            reduceBatteryLevel();
            
            if (rechargingLocation != null && location.equals(rechargingLocation)) {
                eventMessage = rechargeWithMessage(currentStep);
            } else if (location.equals(targetLocation)) {
                arrivingStep = currentStep;
                eventMessage = "(step: " + currentStep + "  ElectricBike: " + idBike + " at target destination ********)";
            }
        }
        
        return eventMessage;
    }

    /**
     * Reduces the battery level by 5 Wh (the cost of one movement step).
     * Ensures the battery level does not drop below zero.
     */
    public void reduceBatteryLevel()
    {
        batteryLevel = batteryLevel - 5;
        if (batteryLevel < 0) {
            batteryLevel = 0;
        }
    }

    /**
     * Returns a formatted string representation of the electric bike according to Annex I.
     * @return A string containing the bike's ID, battery info, charge count, cost, idle count, and locations.
     */
    @Override
    public String toString()
    {
        String formatted;
        double costRounded = Math.round(chargesCost * 10.0) / 10.0;
        
        if (rechargingLocation != null) {
            formatted = "(ElectricBike: " + idBike + ", " + batteryCapacity + "wh, " + batteryLevel + "wh, " +
                        chargesCount + ", " + costRounded + "€, " + idleCount + ", " +
                        location.toString() + ", " + rechargingLocation.toString() + ", " + targetLocation.toString() + ")";
        } else {
            formatted = "(ElectricBike: " + idBike + ", " + batteryCapacity + "wh, " + batteryLevel + "wh, " +
                        chargesCount + ", " + costRounded + "€, " + idleCount + ", " +
                        location.toString() + ", " + targetLocation.toString() + ")";
        }
        
        return formatted;
    }
}

