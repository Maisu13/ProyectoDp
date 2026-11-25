import java.util.*;

/**
 * Provides a demonstration and simulation environment for the 
 * Electric Bike (EB) and Bike Station model.
 * It sets up the environment and runs the simulation for a fixed number of steps.
 *
 * Scenarios: 
 * <ul>
 * <li>Demo SIMPLE (demo=DemoType.SIMPLE): Two bikes are created.</li>
 * <li>Demo MEDIUM (demo=DemoType.MEDIUM): Five bikes are created.</li>
 * <li>Demo ADVANCED (demo=DemoType.ADVANCED): Eight bikes are created.</li>
 * </ul>
 * 
 * @author DP Classes
 * @version 2026.11.15
 */
public class EBikeDemo
{
    /** The maximum X coordinate for the grid (number of rows). */
    public static final int MAXX = 20; 
    /** The maximum Y coordinate for the grid (number of columns). */
    public static final int MAXY = 20; 
    
    /** The maximum number of turns. */
    public static final int MAXSTEPS = 50;
    
    /** The company managing the EBs and Bike stations. */
    private EBCompany company;
    
    /** 
     * The simulation's bikes; they are the electric bikes of the company. 
     * @see ElectricBike 
     */
    private List<ElectricBike> bikes; 
    
    /** 
     * The bike stations available in the city. 
     * @see BikeStation 
     */
    private List<BikeStation> stations;
    
    /** Constant for selecting the demo scenario, using the {@link DemoType} enumeration. */
    private static final DemoType DEMO = DemoType.ADVANCED;
        
    /**
     * Constructor for objects of class EBikeDemo.
     * Initializes the company, bikes, and stations lists, and calls {@code reset()}.
     */
    public EBikeDemo()
    {
        this.company = new EBCompany("EBBike Cáceres");
        this.bikes = new ArrayList<>();
        this.stations = new ArrayList<>();
        
        reset();
    }

    /**
     * Runs the demo for a fixed number of steps (50 steps).
     * In each step, all bikes perform their action, and final information is displayed at the end.
     */
    public void run()
    {        
        for (int step = 0; step < MAXSTEPS; step++) {
            step(step);
        }
        showFinalInfo();
    }

    /**
     * Runs the demo for one step by requesting all bikes to act in ascending order of their ID.
     * @param step The current step number of the simulation.
     */
    public void step(int step)
    {
        for (ElectricBike bike : bikes) {
            String eventMsg = bike.act(step);
            if (eventMsg != null) {
                System.out.println(eventMsg);
            }
            System.out.println("(step: " + step + "  " + bike.toString().substring(1));
        }
    }

    /**
     * Resets the demo to a starting point.
     * Clears existing bikes and stations, resets the company, re-creates
     * the initial setup, and shows the initial information.
     */
    public void reset()
    {
        bikes.clear();
        stations.clear();
        
        if (company != null) {
            company.reset();
        }
        
        createElectricBikes();
        createStations(); 
        createDocks();
        configureRoutes();
        showInitialInfo();
    }
    
    /**
     * Creates the {@link ElectricBike}s based on {@code DEMO}, assigns them
     * starting and target {@link Location}s, and adds them to the company.
     * The bikes list is sorted by ID in ascending order using {@link ComparatorEBIdBike}.
     */
    private void createElectricBikes()
    {
        Location[] locations = {
            new Location(10, 13), new Location(8, 4), new Location(8, 4), new Location(15, 10), 
            new Location(1, 1), new Location(2, 2), new Location(11, 13), new Location(14, 16)
        };
        Location[] targetLocations = {
            new Location(1, 1), new Location(19, 19), new Location(12, 17), new Location(4, 4), 
            new Location(1, 10), new Location(5, 5), new Location(8, 7), new Location(19, 19)
        };
                                        
        for (int i = 0; i < DEMO.getNumBikesToCreate(); i++) {
            ElectricBike eb = new ElectricBike(company, locations[i], targetLocations[i], ("EBCC" + i), (i + 1) * 15);
            bikes.add(eb);
            company.addElectricBike(eb);
        }
        
        bikes.sort(new ComparatorEBIdBike());
    }

    /**
     * Creates predefined {@link BikeStation}s and adds them to the company.
     * The stations list is sorted by ID in ascending order using {@link ComparatorBikeStationId}.
     */
    private void createStations()
    {  
        Location[] locations = {
            new Location(10, 5), new Location(10, 11), new Location(14, 16), new Location(8, 4)
        };
                                
        for (int i = 0; i < DEMO.getNumStationsToCreate(); i++) {
            BikeStation station = new BikeStation("CC0" + i, locations[i]);
            stations.add(station);
            company.addBikeStation(station);
        }
        
        stations.sort(new ComparatorBikeStationId());
    }

    /**
     * Creates a fixed number of {@link Dock} units for each {@link BikeStation}
     * and orders the Docks within each station.
     */
    private void createDocks()
    {  
        for (BikeStation station : stations) {
            for (int i = 0; i < DEMO.getNumDocksToCreate(); i++) {
                Dock d = new Dock(station.getId() + "_00" + i, ((i + 1) * 20), ((i + 1) * 0.20f));
                station.addDock(d);
            }
        }    
    }
    
    /**
     * Instructs each {@link ElectricBike} to calculate its initial route.
     * This determines if an intermediate recharging stop is necessary.
     */
    private void configureRoutes()
    {
        for (ElectricBike bike : bikes) {
            bike.calculateRoute();
        }
    }

    /**
     * Displays the initial information for the simulation, including the company name
     * and the details of all {@link ElectricBike}s and {@link BikeStation}s.
     */
    private void showInitialInfo()
    {
        System.out.println("(Compañía " + company.getName() + " )");
        System.out.println("(-------------------)");
        System.out.println("( Electric Bikes )");
        System.out.println("(-------------------)");

        for (ElectricBike bike : bikes) {
            System.out.println(bike.toString());
        }

        System.out.println("(-------------------)");
        System.out.println("( Bike Stations )");
        System.out.println("(-------------------)");
       
        for (BikeStation station : stations) {
            System.out.println(station.toString());
            for (Dock dock : station.getDocks()) {
                System.out.println(dock.toString());
            }
        }

        System.out.println("(------------------)");
        System.out.println("( Simulation start )");
        System.out.println("(------------------)");
    }

    /**
     * Displays the final information after the simulation has run.
     * Bikes are sorted by arrival step using {@link ComparatorEBArrivingStep}.
     * Stations are sorted by the number of recharges using {@link ComparatorBikeStationNumberRecharged}.
     */
    private void showFinalInfo()
    {
        System.out.println("(-------------------)");
        System.out.println("( Final information )");        
        System.out.println("(-------------------)");
        
        System.out.println("(-------------------)");
        System.out.println("( Electric Bikes )");
        System.out.println("(-------------------)");
        
        bikes.sort(new ComparatorEBArrivingStep());
        for (ElectricBike bike : bikes) {
            System.out.println(bike.toString());
            for (Dock dock : bike.getDocks()) {
                System.out.println("(Dock: " + dock.getId() + ")");
            }
        }
        
        System.out.println("(-------------------)");
        System.out.println("( Bike Stations )");
        System.out.println("(-------------------)");
        
        stations.sort(new ComparatorBikeStationNumberRecharged());
        for (BikeStation station : stations) {
            System.out.println(station.toString());
            for (Dock dock : station.getDocks()) {
                System.out.println(dock.toString());
                for (ElectricBike rechargedBike : dock.getBikesRecharged()) {
                    System.out.println(rechargedBike.toString());
                }
            }
        }
    }
    
    /**
     * The main entry point for running the EBikeDemo simulation.
     * Creates an instance of {@code EBikeDemo} and starts the simulation.
     */
    public static void main()
    {
        EBikeDemo demo = new EBikeDemo();
        demo.run();
    }
}