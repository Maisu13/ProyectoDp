import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class ElectricBikeTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class ElectricBikeTest
{
    private EBCompany eBCompan1;
    private Location location1;
    private Location location2;
    private ElectricBike electric1;

    /**
     * Default constructor for test class ElectricBikeTest
     */
    public ElectricBikeTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
        eBCompan1 = new EBCompany("E-Bike Tour");
        location1 = new Location(0, 0);
        location2 = new Location(10, 10);
        electric1 = new ElectricBike(eBCompan1, location1, location2, "Bike1", 100);
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
    }

    @Test
    public void testCreation()
    {
        assertEquals("Bike1", electric1.getIdBike());
        assertEquals(true, electric1.enoughBattery(20));
    }

    @Test
    public void testEnoughBattery()
    {
        assertEquals(true, electric1.enoughBattery(10));
        assertEquals(false, electric1.enoughBattery(21));
    }

    @Test
    public void testCalculateRoute()
    {
        electric1.calculateRoute();
        assertEquals(null, electric1.getRechargingLocation());
    }
}


