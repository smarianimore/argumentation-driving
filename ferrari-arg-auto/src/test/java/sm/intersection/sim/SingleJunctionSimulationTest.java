/**
 * 
 */
package sm.intersection.sim;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sm.intersection.DistanceRSU;
import sm.argumentation.intersection.FourWaysJunctionConfig;
import sm.argumentation.intersection.NumArgsPolicy;
import sm.intersection.BaseRSU;
import sm.intersection.Car;
import sm.intersection.DIRECTION;
import sm.intersection.UrgentCar;
import sm.intersection.WAY;
import sm.simulation.ManualSimulation;
import sm.simulation.SimulationAPI;

/**
 * @author sm
 *
 */
public class SingleJunctionSimulationTest {

	private SimulationAPI simulationAPI;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		FourWaysJunctionConfig config = new FourWaysJunctionConfig("junction1", new NumArgsPolicy("numArgsPolicy1"),
				new DistanceRSU(new BaseRSU("distance1", 1), 100));
		UrgentCar car = new UrgentCar(new Car("car1", 50), 0);
		car.getCar().addRoute(Collections.singletonList(DIRECTION.STRAIGHT));
		config.addCar(car, WAY.NORTH.toString());
		this.simulationAPI = new ManualSimulation(config.getJunction(), config.getCars(), 1);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.simulationAPI = null;
	}

	/**
	 * Test method for {@link ManualSimulation#step()}.
	 */
	@Test
	public final void testStep() {
		this.simulationAPI.step(true/*, 1*/);
	}

	/**
	 * Test method for {@link ManualSimulation#go()}.
	 */
	@Test
	public final void testGo() {
		this.simulationAPI.go(true);
	}

	/**
	 * Test method for {@link ManualSimulation#pause()}.
	 */
	@Test
	public final void testPause() {
		assertFalse(this.simulationAPI.isGoing());
		this.simulationAPI.pause();
		assertFalse(this.simulationAPI.isGoing());
		this.simulationAPI.step(true/*, 1*/);
		assertFalse(this.simulationAPI.isGoing());
		this.simulationAPI.pause();
		assertFalse(this.simulationAPI.isGoing());
		this.simulationAPI.step(true/*, 2*/);
		assertFalse(this.simulationAPI.isGoing());
		this.simulationAPI.pause();
		assertFalse(this.simulationAPI.isGoing());
		this.simulationAPI.go(true);
		assertTrue(this.simulationAPI.isGoing());
		this.simulationAPI.go(true);
		assertTrue(this.simulationAPI.isGoing());
		this.simulationAPI.step(true/*, 1*/);
		assertTrue(this.simulationAPI.isGoing());
		this.simulationAPI.pause();
		assertFalse(this.simulationAPI.isGoing());
		this.simulationAPI.step(true/*, 1*/);
		assertFalse(this.simulationAPI.isGoing());
		this.simulationAPI.step(true/*, 0*/);
	}

	/**
	 * Test method for {@link ManualSimulation#logSituation()}.
	 */
	@Test
	public final void testLogSituation() {
		this.simulationAPI.logSituation();
	}

}
