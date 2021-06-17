/**
 * 
 */
package sm.arg.intersection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tweetyproject.arg.aspic.syntax.AspicArgumentationTheory;
import org.tweetyproject.arg.aspic.syntax.DefeasibleInferenceRule;
import org.tweetyproject.arg.aspic.syntax.StrictInferenceRule;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.Proposition;

import sm.arg.general.Debatable;
import sm.intersection.CrossingPolicy;
import sm.intersection.DIRECTION;
import sm.intersection.NoSuitableRSUException;
import sm.intersection.RSU;
import sm.intersection.Road;
import sm.intersection.STATUS;
import sm.intersection.SmartJunction;
import sm.intersection.SmartRoad;
import sm.intersection.UrgentCar;
import sm.intersection.WAY;

/**
 * @author sm
 *
 */
public final class FourWaysJunctionConfig implements Debatable {

	private final Logger log = LoggerFactory.getLogger(FourWaysJunctionConfig.class);
	private final SmartJunction junction;
	private final List<CrossingCar> cars;

	/**
	 * Given RSUs are replicated on each road
	 * 
	 * @param junctionName
	 * @param policy
	 * @param rsus
	 * @return
	 */
	public FourWaysJunctionConfig(String junctionName, CrossingPolicy policy, RSU<?>... rsu) {
		Map<WAY, SmartRoad> roads = new HashMap<>();
		List<DIRECTION> lanes = Arrays.asList(DIRECTION.values()); // each road has all 3 lanes
		List<RSU<?>> rsus = Arrays.asList(rsu); // each road has all given RSUs TODO RSU name no longer unique!
		for (WAY way : WAY.values()) {
			roads.put(way, new SmartRoad(new Road(way.name(), lanes), rsus));
		}
		this.junction = new SmartJunction(junctionName, roads, policy);
		this.cars = new ArrayList<>();
	}

	/**
	 * roadname must exist in junction
	 * 
	 * @param car
	 * @param roadName
	 * @return
	 * @throws NoSuitableRSUException
	 */
	public FourWaysJunctionConfig addCar(UrgentCar car, String roadName) {
		Double d = null;
		for (WAY way : this.junction.getRoads().keySet()) {
			if (this.junction.getRoads().get(way) != null
					&& this.junction.getRoads().get(way).getRoad().getName().equals(roadName)) {
				for (RSU<?> rsu : this.junction.getRoads().get(way).getRsus()) {
					if (rsu instanceof DistanceRSU && rsu.getType().isAssignableFrom(Double.class)) {
						d = rsu.getMeasurement();
					} else {
						log.warn("No RSU instanceof DistanceRSU and assignable from Double found: %s",
								this.junction.getRoads().get(way).getRsus());
						d = Double.NaN;
//						throw new NoSuitableRSUException("No RSU instanceof DistanceRSU and assignable from Double found", this.junction.getRoads().get(way).getRsus());
					}
				}
				this.cars.add(new CrossingCar(car, way, STATUS.APPROACHING, d));
			}
		}
		return this;
	}

	/**
	 * @return the junction
	 */
	public SmartJunction getJunction() {
		return junction;
	}

	/**
	 * @return the cars
	 */
	public List<CrossingCar> getCars() {
		return cars;
	}

	@Override
	public List<Proposition> addAsArgTheory(final AspicArgumentationTheory<PlFormula> t) {
		Proposition a = null;
		Proposition b = null;
		Proposition c = null;
		Proposition d = null;
		Proposition f = null;
		final ArrayList<String> alreadyConsidered = new ArrayList<>();
		boolean Problems = false;
		DefeasibleInferenceRule<PlFormula> r1 = new DefeasibleInferenceRule<>();
		StrictInferenceRule<PlFormula> r2 = new StrictInferenceRule<>();
		for (int i = 0; i < cars.size(); i++) {
			a = new Proposition(cars.get(i).getName());
			c = new Proposition(a + "_CorrectlyDetected");
			d = new Proposition(a + "_WronglyDetected");
			r1 = new DefeasibleInferenceRule<>();
			if (junction.getRoads().get(cars.get(i).getWay()).getRsus().get(0).getConfidence() < 0.5) {
				b = new Proposition("RSU_untrustworthy");
				r1.setConclusion(d);
				r1.addPremise(b);
				r1.addPremise(a);
				t.addRule(r1);
				Problems = true;
			} else {
				b = new Proposition("RSU_trustworthy");
				r1.setConclusion(c);
				r1.addPremise(b);
				r1.addPremise(a);
				t.addRule(r1);
			}
			for (int j = 0; j < cars.size(); j++) {
				b = new Proposition("PossibleIncident_" + cars.get(i).getName() + cars.get(j).getName());
				f = new Proposition(cars.get(j).getName());

				if (!cars.get(i).equals(cars.get(j))
						&& !alreadyConsidered.contains(cars.get(i).getName() + "0" + cars.get(j).getName())) {
					if (noConflicts(i, j)) {
						r2 = new StrictInferenceRule<>();
						r2.setConclusion(new Negation(b));
						r2.addPremise(a);
						r2.addPremise(f);
						t.addRule(r2);
						alreadyConsidered.add(cars.get(j).getName() + "0" + cars.get(i).getName());
						System.out.println(alreadyConsidered);
					}
				}
			}
		}
		for (int i = 0; i < cars.size(); i++) {
			a = new Proposition(cars.get(i).getName());
			b = new Proposition(junction.getPolicy().getName());
			for (int j = 0; j < cars.size(); j++) {
				Proposition e = new Proposition("PossibleIncident_" + cars.get(i).getName() + cars.get(j).getName());
				r1 = new DefeasibleInferenceRule<>();
				r2 = new StrictInferenceRule<>();
				f = new Proposition(cars.get(j).getName());
				if ((!alreadyConsidered.contains(cars.get(j).getName() + "0" + cars.get(i).getName())
						&& !alreadyConsidered.contains(cars.get(i).getName() + "0" + cars.get(j).getName()))
						&& (!cars.get(i).equals(cars.get(j))
								&& !alreadyConsidered.contains(cars.get(i).getName() + "x" + cars.get(j).getName()))) {
					if (Problems) {
						r1.setConclusion(e);
						r1.addPremise(a);
						r1.addPremise(f);
						t.addRule(r1);
						alreadyConsidered.add(cars.get(j).getName() + "x" + cars.get(i).getName());
					}
					if (!Problems && cars.get(j).equals(junction.getPolicy().rightOfWay(cars.get(i), cars.get(j)))) {
						c = new Proposition(f + "_passesFirst");
						r1.setConclusion(c);
						r1.addPremise(b);
						r1.addPremise(f);
						t.add(r1);
						r2.setConclusion(new Negation(e));
						r2.addPremise(c);
						r2.addPremise(a);
						t.add(r2);
						alreadyConsidered.add(cars.get(j).getName() + "x" + cars.get(i).getName());
					}
				}
			}
		}
		return Arrays.asList(new Proposition[] { a, b, c, d, f });
	}

	private boolean noConflicts(int i, int j) {
		return (cars.get(i).getWay().equals(WAY.SOUTH) && cars.get(j).getWay().equals(WAY.NORTH)
				&& !cars.get(i).getRoutes().get(0).contains(DIRECTION.LEFT)
				&& !cars.get(j).getRoutes().get(0).contains(DIRECTION.LEFT))
				|| (cars.get(i).getWay().equals(WAY.EAST) && cars.get(j).getWay().equals(WAY.WEST)
						&& !cars.get(i).getRoutes().get(0).contains(DIRECTION.LEFT)
						&& !cars.get(j).getRoutes().get(0).contains(DIRECTION.LEFT))
				|| (cars.get(i).getWay().equals(WAY.NORTH) && cars.get(j).getWay().equals(WAY.SOUTH)
						&& cars.get(i).getRoutes().get(0).contains(DIRECTION.LEFT)
						&& cars.get(j).getRoutes().get(0).contains(DIRECTION.LEFT))
				|| (cars.get(i).getWay().equals(WAY.EAST) && cars.get(j).getWay().equals(WAY.WEST)
						&& cars.get(i).getRoutes().get(0).contains(DIRECTION.LEFT)
						&& cars.get(j).getRoutes().get(0).contains(DIRECTION.LEFT))
				|| (cars.get(i).getWay().equals(WAY.NORTH) && cars.get(j).getWay().equals(WAY.EAST)
						&& cars.get(i).getRoutes().get(0).contains(DIRECTION.RIGHT))
				|| (cars.get(i).getWay().equals(WAY.NORTH) && cars.get(j).getWay().equals(WAY.EAST)
						&& cars.get(j).getRoutes().get(0).contains(DIRECTION.RIGHT)
						&& cars.get(i).getRoutes().get(0).contains(DIRECTION.LEFT))
				|| (cars.get(i).getWay().equals(WAY.WEST) && cars.get(j).getWay().equals(WAY.NORTH)
						&& cars.get(i).getRoutes().get(0).contains(DIRECTION.RIGHT))
				|| (cars.get(i).getWay().equals(WAY.WEST) && cars.get(j).getWay().equals(WAY.NORTH)
						&& cars.get(i).getRoutes().get(0).contains(DIRECTION.RIGHT)
						&& cars.get(i).getRoutes().get(0).contains(DIRECTION.LEFT))
				|| (cars.get(i).getWay().equals(WAY.SOUTH) && cars.get(j).getWay().equals(WAY.WEST)
						&& cars.get(i).getRoutes().get(0).contains(DIRECTION.RIGHT))
				|| (cars.get(i).getWay().equals(WAY.SOUTH) && cars.get(j).getWay().equals(WAY.WEST)
						&& cars.get(i).getRoutes().get(0).contains(DIRECTION.RIGHT)
						&& cars.get(i).getRoutes().get(0).contains(DIRECTION.LEFT))
				|| (cars.get(i).getWay().equals(WAY.EAST) && cars.get(j).getWay().equals(WAY.SOUTH)
						&& cars.get(i).getRoutes().get(0).contains(DIRECTION.RIGHT))
				|| (cars.get(i).getWay().equals(WAY.EAST) && cars.get(j).getWay().equals(WAY.SOUTH)
						&& cars.get(i).getRoutes().get(0).contains(DIRECTION.RIGHT)
						&& cars.get(i).getRoutes().get(0).contains(DIRECTION.LEFT))
				|| (cars.get(i).getWay().equals(cars.get(j).getWay()));
	}

	@Override
	public String toString() {
		return String.format("FourWaysJunctionConfig [junction=%s, cars=%s]", junction, cars);
	}

}