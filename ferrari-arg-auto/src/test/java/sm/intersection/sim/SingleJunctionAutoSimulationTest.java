/**
 * 
 */
package sm.intersection.sim;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sm.intersection.DistanceRSU;
import sm.argumentation.intersection.FourWaysJunctionConfig;
import sm.argumentation.intersection.NumArgsPolicy;
import sm.intersection.BaseRSU;
import sm.simulation.RandomStrat;
import sm.simulation.Simulation;
import sm.simulation.VehiclesGenerationStrategy;

/**
 * @author sm
 *
 */
public class SingleJunctionAutoSimulationTest {

    //	private Simulation sim1;
    private Simulation sim2;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        FourWaysJunctionConfig config = new FourWaysJunctionConfig("junction1", new NumArgsPolicy("numArgsPolicy1"),
                new DistanceRSU(new BaseRSU("distance1", 1), 100));
        //VehiclesGenerationStrategy strat = new ConflictCtrlStrat();
        VehiclesGenerationStrategy strat = new RandomStrat();
        strat.configJunction(config.getJunction());
        strat.setSeed(1); // same seed = same random numbers
        //		this.sim1 = new Simulation(config.getJunction(), 1, 10, 20, strat, 1);
        this.sim2 = new Simulation(config.getJunction(), 3, 3, 30, strat, 1);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        //		this.sim1 = null;
        this.sim2 = null;
    }

    /**
     * F Test method for
     * {@link Simulation#step(java.lang.Boolean)}.
     */
    @Test
    public final void testStep() {
        //		this.sim1.step(true);
        this.sim2.step(true);
    }

    /**
     * Test method for
     * {@link Simulation#go(java.lang.Boolean)}.
     */
    @Test
    public final void testGo() {
        //		this.sim1.go(true);
        this.sim2.go(true);
    }

    /**
     * Test method for
     * {@link Simulation#pause()}.
     */
    //	@Test
    //	public final void testPause() {
    //		assertFalse(this.sim2.isGoing());
    //		this.sim2.pause();
    //		assertFalse(this.sim2.isGoing());
    //		this.sim2.step(true/*, 1*/);
    //		assertFalse(this.sim2.isGoing());
    //		this.sim2.pause();
    //		assertFalse(this.sim2.isGoing());
    //		this.sim2.step(true/*, 2*/);
    //		assertFalse(this.sim2.isGoing());
    //		this.sim2.pause();
    //		assertFalse(this.sim2.isGoing());
    //		this.sim2.go(true);
    //		assertTrue(this.sim2.isGoing());
    //		this.sim2.go(true);
    //		assertTrue(this.sim2.isGoing());
    //		this.sim2.step(true/*, 1*/);
    //		assertTrue(this.sim2.isGoing());
    //		this.sim2.pause();
    //		assertFalse(this.sim2.isGoing());
    //		this.sim2.step(true/*, 1*/);
    //		assertFalse(this.sim2.isGoing());
    //		this.sim2.step(true/*, 0*/);
    //	}

}
