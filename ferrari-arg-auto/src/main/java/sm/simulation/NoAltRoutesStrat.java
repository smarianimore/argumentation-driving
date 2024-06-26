/**
 *
 */
package sm.simulation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sm.argumentation.intersection.CrossingCar;
import sm.intersection.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Random ways and routes, fixed speed and urgency (MAX), multi-step route
 * possible. - WAY is random - speed is 50 - urgency is 1 - 1 route only, with
 * random DIRECTION depth
 *
 * @author sm
 */
public class NoAltRoutesStrat implements VehiclesGenerationStrategy {

    private final Logger log = LoggerFactory.getLogger(NoAltRoutesStrat.class);
    private Junction junction;
    private boolean setup = false;

    private List<WAY> values;
    private int size;
    private Random random;
    private long seed;
    private boolean seedSet;

    private long nCars;

    @Override
    public List<CrossingCar> newCars() {
        if (this.setup) {
            if (!this.seedSet) {
                this.log.warn("SEED NOT SET, USING NON-REPRODUCIBLE STRATEGY");
            }
            final WAY way = this.values.get(this.random.nextInt(this.size));
            Double d = null;
            for (final RSU<?> rsu : this.junction.getRoads().get(way).getRsus()) {
                if (rsu instanceof DistanceRSU && rsu.getType().isAssignableFrom(Double.class)) {
                    d = ((DistanceRSU) rsu).getMeasurement();
                } else {
                    this.log.warn("No RSU instanceof DistanceRSU and assignable from Double found: {}",
                            this.junction.getRoads().get(way).getRsus());
                    d = Double.NaN;
                }
            }
            this.nCars++;
            final UrgentCar car = new UrgentCar(new Car(
                    String.format("%s_%d_%s", way, this.nCars, this.getJunction().getId()).replace("ways ", "W"), // TODO sono curioso: perche questo cambio di nome?
                    Defaults.MAX_SPEED), Defaults.MAX_URGENCY);
            final List<DIRECTION> route = new ArrayList<>();
            route.add(DIRECTION.random());
            for (int i = 1; i < Defaults.MAX_ROUTE_DEPTH; i++) {
                if (this.random.nextDouble() < Defaults.P_ADD_DEPTH) { // randomly generate second direction for some route
                    route.add(DIRECTION.random());
                }
            }
            car.getCar().addRoute(route);
            return Collections.singletonList(new CrossingCar(car, way, STATUS.APPROACHING, d));
        } else {
            this.log.warn("REFERENCE JUNCTION NOT YET CONFIGURED");
            return null;
        }
    }

    @Override
    public VehiclesGenerationStrategy configJunction(final Junction junction) {
        this.junction = junction;
        this.values = List.copyOf(this.junction.getRoads().keySet());
        this.size = this.values.size();
        this.nCars = 0;
        this.random = new Random();
        this.seedSet = false;
        this.setup = true;
        return this;
    }

    @Override
    public VehiclesGenerationStrategy setSeed(final long seed) {
        this.seed = seed;
        this.seedSet = true;
        this.random = new Random(this.seed);
        DIRECTION.setSeed(this.seed);
        WAY.setSeed(this.seed);
        return this;
    }

    /**
     * @return the junction
     */
    public Junction getJunction() {
        return this.junction;
    }

    /**
     * @return the setup
     */
    public boolean isSetup() {
        return this.setup;
    }

    /**
     * @return the nCars
     */
    public long getnCars() {
        return this.nCars;
    }

    @Override
    public VehiclesGenerationStrategy setSpeedRange(final int min, final int max) {
        throw new UnsupportedOperationException(
                "This strategy always generates max speed and max urgency. To get random values, use, e.g., FlatRouteRandomStrategy");
    }

    @Override
    public VehiclesGenerationStrategy setUrgencyRange(final int min, final int max) {
        throw new UnsupportedOperationException(
                "This strategy always generates max speed and max urgency. To get random values, use, e.g., FlatRouteRandomStrategy");
    }

}
