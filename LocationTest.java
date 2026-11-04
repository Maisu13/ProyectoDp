import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test implementation of the {@link Location} class.
 * Provides unit tests for methods like {@code distance()} and {@code nextLocation()}.
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29
 * @version 2026.10.13 DP classes (adaptado a Java 8+)
 */
public class LocationTest
{
    private Location p0;
        private Location p1;
        private Location p2;
        private Location p3;
    
    /**
     * Default constructor for test class LocationTest.
     */
    public LocationTest()
    {
    }
    
    /**
     * Sets up the test fixture.
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
        p0 = new Location (0,0);
        p1 = new Location (1,5);
        p2 = new Location (6,6);
        p3 = new Location (5,2);
    }

    /**
     * Tears down the test fixture.
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
        // No teardown required for Location tests
    }
    
    /**
     * Test the {@code distance} method of the {@link Location} class.
     * Checks distances from a central location to surrounding points.
     */
    @Test
    public void testDistance()
    {
     assertEquals(5, p0.distance(p1));
     assertEquals(4, p2.distance(p3));
     assertEquals(0, p0.distance(p3));
    }
    
    /**
     * Test the {@code nextLocation} method when the destination is adjacent 
     * (one step away in any direction).
     */
    @Test
    public void testAdjacentLocations()
    {
        Location loc = new Location (1,1);
        
    }
    
    /**
     * Test the {@code nextLocation} method when the destination is not adjacent 
     * (more than one step away).
     */
    @Test
    public void testNonAdjacentLocations()
    {
        //TODO: Complete this code
    }
}