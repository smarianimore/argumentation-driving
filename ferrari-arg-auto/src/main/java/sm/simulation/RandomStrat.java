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
 * @author sm
 */
public final class RandomStrat implements VehiclesGenerationStrategy {

    private final Logger log = LoggerFactory.getLogger(RandomStrat.class);
    private Junction junction;
    private boolean setup = false;

    private List<WAY> values;
    private int size;
    private Random random;
    private long seed;
    private boolean seedSet = false;

    private long nCars;
    private int minSpeed;
    private int maxSpeed;
    private int minUrgency;
    private int maxUrgency;

    @Override
    public VehiclesGenerationStrategy configJunction(final Junction junction) {
        this.junction = junction;
        this.values = List.copyOf(this.junction.getRoads().keySet());
        this.size = this.values.size();
        this.nCars = 0;
        if (!this.seedSet) {
            this.random = new Random();
        }
        this.minSpeed = Defaults.MIN_SPEED;
        this.maxSpeed = Defaults.MAX_SPEED;
        this.minUrgency = Defaults.MIN_URGENCY;
        this.maxUrgency = Defaults.MAX_URGENCY;
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
            final UrgentCar car = new UrgentCar(
                    new Car(String.format("%s_%d", way, this.nCars),
                            this.random.nextDouble() * (this.maxSpeed - this.minSpeed) + this.minSpeed),
                    this.random.nextDouble() * (this.maxUrgency - this.minUrgency) + this.minUrgency);
            List<DIRECTION> route = new ArrayList<>();
            route.add(DIRECTION.random());
            route = deepRoute(route);
            car.getCar().addRoute(route);
            for (int i = 1; i < Defaults.MAX_ROUTES - 1; i++) {
                route = new ArrayList<>();
                if (this.random.nextDouble() < Defaults.P_ADD_NEW_ROUTE) { // randomly generate alternative routes for some vehicles
                    route.add(DIRECTION.random());
                    car.getCar().addRoute(deepRoute(route));
                }
            }
            return Collections.singletonList(new CrossingCar(car, way, STATUS.APPROACHING, d));
        } else {
            this.log.warn("REFERENCE JUNCTION NOT YET CONFIGURED");
            return null;
        }
    }

    /**
     * @return a deep route
     */
    public List<DIRECTION> deepRoute(final List<DIRECTION> route) {
        for (int i = 1; i < Defaults.MAX_ROUTE_DEPTH; i++) {
            if (this.random.nextDouble() < Defaults.P_ADD_DEPTH) { // randomly generate second direction for some route
                route.add(DIRECTION.random());
            }
        }
        return route;
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
        this.minSpeed = min;
        this.maxSpeed = max;
        return this;
    }

    @Override
    public VehiclesGenerationStrategy setUrgencyRange(final int min, final int max) {
        this.minUrgency = min;
        this.maxUrgency = max;
        return this;
    }

}
