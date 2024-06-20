/**
 * 
 */
package sm.intersection.sim;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sm.argumentation.intersection.CrossingCar;
import sm.intersection.DistanceRSU;
import sm.argumentation.intersection.FourWaysJunctionConfig;
import sm.argumentation.intersection.NumArgsPolicy;
import sm.intersection.BaseRSU;
import sm.simulation.FlatRouteMaxStrategy;
import sm.simulation.ControlledStrat;

/**
 * @author sm
 *
 */
public class NoConflictStrategyTest {
    
    private static final int ITER = 10;
    private ControlledStrat strat;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.strat = new ControlledStrat();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.strat = null;
    }

    /**
     * Test method for {@link FlatRouteMaxStrategy#newCars()}.
     */
    @Test
    public final void testNewCars() {
        FourWaysJunctionConfig config = new FourWaysJunctionConfig("junction1", new NumArgsPolicy("numArgsPolicy1"),
                new DistanceRSU(new BaseRSU("distance1", 1), 100));
        this.strat.configJunction(config.getJunction());
        this.strat.setSeed(1); // if needed, otherwise can skip
        List<CrossingCar> newCars = new ArrayList<>();
        List<CrossingCar> cars = new ArrayList<>();
        for (int i = 0; i < NoConflictStrategyTest.ITER; i++) {
            newCars.addAll(this.strat.newCars());
           // System.out.println(newCars.size());
            
            for (CrossingCar car : newCars) {
                assertTrue(this.strat.getJunction().getRoads().containsKey(car.getWay()));
                assertTrue(this.strat.getJunction().getRoads().get(car.getWay()) != null);
                for (CrossingCar otherCar : cars) {
                	
                    assertNotEquals(car, otherCar);
                }
                cars.add(car);
               
               
            }   
            System.out.println(cars);
            assertEquals(this.strat.getnCars(), cars.size());
            newCars = new ArrayList<>();
        }
    }
}
