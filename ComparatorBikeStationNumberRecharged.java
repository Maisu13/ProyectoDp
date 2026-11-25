import java.util.Comparator;

/**
 * Compares two {@link BikeStation} objects by total recharged bikes in descending order,
 * using station ID in ascending order as a tie-breaker.
 * @author Rober
 * @version 2026.11.15
 */
public class ComparatorBikeStationNumberRecharged implements Comparator<BikeStation>
{
    @Override
    public int compare(BikeStation st1, BikeStation st2)
    {
        if (st1 == null || st2 == null) {
            throw new IllegalArgumentException("Stations to compare cannot be null.");
        }
        int result = Integer.compare(st2.getTotalBikesRecharged(), st1.getTotalBikesRecharged());
        if (result == 0) {
            result = st1.getId().compareTo(st2.getId());
        }
        return result;
    }
}