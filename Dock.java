import java.util.ArrayList;
import java.util.List;
/**
 * Model a dock unit within a {@link BikeStation}.
 * It tracks its charging capabilities, fee, and the electric bikes it has recharged.
 * @author David J. Barnes and Michael Kölling
 * @author DP classes 
 * @version 2026.10.13
 */
public class Dock
{
    private String idDock;
    private int chargingSpeed;
    private float chargingFee;
    private List<ElectricBike> bikesRecharged;
    private float amountCollected;
    private boolean free;

    /**
     * Constructor for objects of class Dock.
     * @param id The unique identifier of the dock(e.j "CC00_001").
     * @param speed The charging speed in Wh.
     * @param fee The charging fee in €/Wh.
    */
    public Dock(String id, int speed, float fee)
    {
        if(id == null || id.trim().isEmpty()){
            throw new IllegalArgumentException("El ID del dock no puede ser nulo o vacio.");
            
        }
        if(speed <= 0) {
            throw new IllegalArgumentException("La velocidad de carga debe ser mayor que 0");
            
        }
        if(fee < 0){
            throw new IllegalArgumentException("La tarifa no puede ser negativa");
        }
        
        this.idDock = id;
        this.chargingSpeed = speed;
        this.chargingFee = fee;
        this.bikesRecharged = new ArrayList<>();
        this.amountCollected= 0.0f;
        this.free = true;
    }

    /**
     * @return The total number of {@link ElectricBike}s that have been recharged by this dock.
     */
    public int getNumerEVRecharged(){
       
        return bikesRecharged.size();
    }
    
    
    /**
     * Simulates the charging process for an {@link ElectricBike}.
     * Increases the amount collected and registers the bike as recharged.
     * @param bike The electric bike to be recharged. Must not be null
     * @param energyToCharge The amount of energy in Wh needed to complete the battery charge
     * @return The total cost of the recharge in euros
     */
    public float recharge( ElectricBike bike, int energyToCharge  ){
        if( bike == null){
            throw new IllegalArgumentException("The bike to recharge cannot be null");
        }
        float cost = energyToCharge * this.chargingFee;
        this.amountCollected += cost;
        this.bikesRecharged.add(bike);
        return cost;
    }

    /**
     * @return The unique identifier of the dock.
     */
    public String getId(){
        return idDock;
    }
    
    /**
     * @return The charging speed of the dock in Wh.
     */
    public int getChargingSpeed(){
        return chargingSpeed;
    }
    
    /**
     * @return The charging Fee of the dock in €/Wh.
     */
    public float getChargingFee(){
        return chargingFee;
    }
    /**
     * @return The total amount of money collected by this dock in euros.
     */
    public float getAmountCollected(){
        return amountCollected;
    }
    
    /**
     * @return The list of (ElectricBike) recharged by this lock
     */
    public List<ElectricBike> getBikesRecharged(){
        return bikesRecharged;
    }
    
    /**
     * @return The total numbre of (ElectricBike) that have been recharged by this dock
     */
    public int getNumberEVRecharged(){
        return bikesRecharged.size();
    }
    
    /**
     * return true if the dock is currently available, false if occupied 
    */
    public boolean isFree(){
        return free;
    }
    
    /**
     * Returns a string representation of the dock, including its ID, speed, fee, and the number of EVs recharged.
     * @return A string representation of the dock.
     */
    @Override
    public String toString()
    {
        String feeStr = (chargingFee + "").replace(',','.');
        String amountStr =(amountCollected + "").replace(',','.');
        return "Dock: " + idDock + "," + chargingSpeed + "wh,  " + feeStr +"€, " + bikesRecharged.size() + ", " + amountStr + "€)";
    }    
}
