import java.util.Comparator;

/**
 * Compares two {@link ElectricBike} objects by their arrival step in ascending order,
 * using bike ID in ascending order as a tie-breaker.
 * @author Oscar
 * @version 2026.11.15
 */
public class ComparatorEBArrivingStep implements Comparator<ElectricBike>
{
    @Override
    public int compare(ElectricBike eb1, ElectricBike eb2)
    {
        if (eb1 == null || eb2 == null) {
            throw new IllegalArgumentException("Bikes to compare cannot be null.");
        }
        int result = Integer.compare(eb1.getArrivingStep(), eb2.getArrivingStep());
        if (result == 0) {
            result = eb1.getIdBike().compareTo(eb2.getIdBike());
        }
        return result;
    }
}