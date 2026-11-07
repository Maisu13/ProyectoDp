import java.util.Comparator;
/**
 * Compares two (Dock) objects based on:
 * 1. Charging speed in descendig order.
 * 2. Charging fee in descending order.
 * 3 Dock ID in ascending order(final tie-breaker).
 * @author (Óscar) 
 * @version (5.11.26)
 */
public class ComparatorDockOrder implements Comparator<Dock>
{
    @Override 
    
    public int compare(Dock d1, Dock d2)
    {
        int result = 0;
        if(d1 == null || d2 == null){
            throw new IllegalArgumentException("Docks to compare cannot be null");
        }
        
        int speedComp = Integer.compare(d2.getChargingSpeed(), d1.getChargingSpeed());
        if(speedComp !=0){
            result = speedComp;
        } else{
            int feeComp = Float.compare(d1.getChargingFee(), d2.getChargingFee());
            if(feeComp!=0){
                result = feeComp;
            } else{
                result = d1.getId().compareTo(d2.getId());
            }
        }
        return result;
    }

}