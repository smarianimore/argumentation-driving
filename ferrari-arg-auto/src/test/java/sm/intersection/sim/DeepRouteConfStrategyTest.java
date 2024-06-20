package sm.intersection.sim;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

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
import sm.simulation.ConflictRandomStrat;
import sm.simulation.NoAltRoutesStrat;
import sm.simulation.Defaults;

public class DeepRouteConfStrategyTest {

    
    private static final int ITER = 10;
    private ConflictRandomStrat strat;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.strat = new ConflictRandomStrat();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.strat = null;
    }

    /**
     * Test method for {@link NoAltRoutesStrat#newCars()}.
     */
    @Test
    public final void testNewCars() {
        FourWaysJunctionConfig config = new FourWaysJunctionConfig("junction1", new NumArgsPolicy("numArgsPolicy1"),
                new DistanceRSU(new BaseRSU("distance1", 1), 100));
        this.strat.configJunction(config.getJunction());
        this.strat.setSeed(1); // if needed, otherwise can skip
        List<CrossingCar> newCars = new ArrayList<>();
        List<CrossingCar> cars = new ArrayList<>();
        for (int i = 0; i < DeepRouteConfStrategyTest.ITER; i++) {
            newCars.addAll(this.strat.newCars());
           // System.out.println(newCars.size());
            
            for (CrossingCar car : newCars) {
                assertTrue(car.getCar().getCar().getSpeed() < Defaults.MAX_SPEED);
                assertTrue(car.getCar().getCar().getSpeed() > Defaults.MIN_SPEED);
                assertTrue(car.getCar().getUrgency() < Defaults.MAX_URGENCY);
                assertTrue(car.getCar().getUrgency() > Defaults.MIN_URGENCY);
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

