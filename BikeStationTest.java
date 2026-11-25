import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link BikeStation} class.
 * @author Oscar
 * @version 2026.11.15
 */
public class BikeStationTest
{
    private BikeStation station;
    private Dock slowDock;
    private Dock fastDock;

    @BeforeEach
    public void setUp()
    {
        station = new BikeStation("CC00", new Location(10, 5));
        slowDock = new Dock("CC00_000", 20, 0.2f);
        fastDock = new Dock("CC00_003", 80, 0.8f);
    }

    @Test
    public void testAddDock()
    {
        assertEquals(0, station.getDocks().size());
        
        station.addDock(slowDock);
        station.addDock(fastDock);
        
        assertEquals(2, station.getDocks().size());
        assertEquals(fastDock, station.getDocks().get(0));
        assertEquals(slowDock, station.getDocks().get(1));
    }
}