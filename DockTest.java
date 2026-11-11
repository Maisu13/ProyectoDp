import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
/**
 * Write a description of class DockTest here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DockTest
{
    private Dock dock;
    private ElectricBike bike;
    
    @Before
    
    public void setUp(){
        dock = new Dock ("CC00_003", 80, 0.8f);
        bike = new ElectricBike(null, new Location(0,0), new Location (5,5), "EBCC0", 60);
    }

    @Test 
    public void testRecharge(){
        assertEquals(0, dock.getNumerEVRecharged());
        assertEquals(0.0f, dock.getAmountCollected(), 0.001f);
        
        float cost = dock.recharge(bike,10);
        
        assertEquals(8.0f, cost, 0.001f);
        assertEquals(8.0f, dock.getAmountCollected(), 0.001f);
        assertEquals(1, dock.getNumberEVRecharged());
        assertTrue(dock.getBikesRecharged().contains(bike));
    }
}