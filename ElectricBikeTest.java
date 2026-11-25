import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link ElectricBike} class.
 * @author Oscar
 * @version 2026.11.15
 */
public class ElectricBikeTest
{
    private EBCompany eBCompan1;
    private Location location1;
    private Location location2;
    private ElectricBike electric1;
    private BikeStation stationStart;
    private BikeStation stationMid;

    @BeforeEach
    public void setUp()
    {
        eBCompan1 = new EBCompany("E-Bike Tour");
        location1 = new Location(0, 0);
        location2 = new Location(10, 10);
        
        stationStart = new BikeStation("CC00", new Location(0, 0));
        stationStart.addDock(new Dock("CC00_001", 80, 0.8f));
        
        stationMid = new BikeStation("CC01", new Location(5, 5));
        stationMid.addDock(new Dock("CC01_001", 60, 0.6f));
        
        eBCompan1.addBikeStation(stationStart);
        eBCompan1.addBikeStation(stationMid);
        
        electric1 = new ElectricBike(eBCompan1, location1, location2, "EBCC01", 100);
    }

    @AfterEach
    public void tearDown()
    {
    }

    @Test
    public void testCreation()
    {
        assertEquals("EBCC01", electric1.getIdBike());
        assertEquals(location1, electric1.getLocation());
        assertEquals(location2, electric1.getTargetLocation());
        assertTrue(electric1.enoughBattery(20));
    }

    @Test
    public void testEnoughBattery()
    {
        assertTrue(electric1.enoughBattery(10));
        assertFalse(electric1.enoughBattery(21));
    }

    @Test
    public void testCalculateRoute()
    {
        
        electric1.calculateRoute();
        assertNull(electric1.getRechargingLocation());
        
        
        ElectricBike lowBatteryBike = new ElectricBike(eBCompan1, location1, location2, "EBCC02", 30);
        assertEquals(new Location(5, 5), lowBatteryBike.getRechargingLocation());
    }
    
    @Test
    public void testRecharge()
    {
    
        electric1.reduceBatteryLevel();
        assertFalse(electric1.enoughBattery(20));
        
        
        electric1.recharge();
        assertTrue(electric1.enoughBattery(20));
    }
}