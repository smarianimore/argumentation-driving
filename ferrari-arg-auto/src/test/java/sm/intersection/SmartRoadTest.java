/**
 * 
 */
package sm.intersection;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author sm
 *
 */
public class SmartRoadTest {

	private Road road;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		BaseRSU rsu = new BaseRSU("a", 1);
		RSU<Double> dRsu = new DistanceRSU(rsu, 50);
		RSU<Boolean> bRSU = new HumanRSU(rsu, false);
		List<RSU<?>> rsus = new ArrayList<>();
		rsus.add(dRsu);
		rsus.add(bRSU);
		road = new Road(new BaseRoad("b", Collections.emptyList()), rsus);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		road = null;
	}

	/**
	 * Test method for {@link Road#getRsus()}.
	 */
	@Test
	public final void testGetRsus() {
		for (RSU<?> rsu : road.getRsus()) {
			if (rsu.getType().isAssignableFrom(Double.class)) {
				Double d = ((DistanceRSU) rsu).getMeasurement();
				assertEquals(50.0, d, 0.1);
			} else if (rsu.getType().isAssignableFrom(Boolean.class)) {
				Boolean b = ((HumanRSU) rsu).getMeasurement();
				assertEquals(false, b);
			}
		}
	}

}
