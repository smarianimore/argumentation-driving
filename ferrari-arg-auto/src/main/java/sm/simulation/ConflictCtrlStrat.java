package sm.simulation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sm.argumentation.intersection.CrossingCar;
import sm.intersection.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ConflictCtrlStrat implements VehiclesGenerationStrategy {
    private final Logger log = LoggerFactory.getLogger(FlatRouteRandomStrategy.class);
    private Junction junction;
    private boolean setup = false;

    private List<WAY> values;
    private int size;
    private Random random;
    private long seed;
    private boolean seedSet;

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
        this.random = new Random();
        this.seedSet = false;
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
        return this;
    }

    @Override
    public List<CrossingCar> newCars() {
        if (this.setup) {
            if (!this.seedSet) {
                this.log.warn("SEED NOT SET, USING NON-REPRODUCIBLE STRATEGY");
            }

            final List<CrossingCar> lc = new ArrayList<>();

            final WAY way1 = this.values.get(this.random.nextInt(this.size));
            WAY way2 = null;
            Double d = null;

            for (final RSU<?> rsu : this.junction.getRoads().get(way1).getRsus()) {
                if (rsu instanceof DistanceRSU && rsu.getType().isAssignableFrom(Double.class)) {
                    d = ((DistanceRSU) rsu).getMeasurement();
                } else {
                    this.log.warn("No RSU instanceof DistanceRSU and assignable from Double found: {}",
                            this.junction.getRoads().get(way1).getRsus());
                    d = Double.NaN;
                }
            }
            this.nCars++;
            final UrgentCar car1 = new UrgentCar(
                    new Car(String.format("%s_%d", way1, this.nCars),
                            this.random.nextDouble() * (this.maxSpeed - this.minSpeed) + this.minSpeed),
                    this.random.nextDouble() * (this.maxUrgency - this.minUrgency) + this.minUrgency);
            final List<DIRECTION> route = new ArrayList<>();
            route.add(DIRECTION.random());
            for (int i = 1; i < Defaults.MAX_ROUTE_DEPTH; i++) {
                if (this.random.nextDouble() < Defaults.P_ADD_DEPTH) { // randomly generate second direction for some route
                    route.add(DIRECTION.random());
                }
            }
            car1.getCar().addRoute(route);
            int i = 0;
            while (i < 1) {
                if (car1.getCar().getRoutes().get(car1.getCar().getCurrentRoute()).get(0).equals(DIRECTION.RIGHT)) {
                    car1.getCar().setR(Collections.singletonList(DIRECTION.random()));
                } else {
                    i++;
                }

            }
            this.nCars++;
            final UrgentCar car2 = new UrgentCar(
                    new Car(String.format("%s_%d", way2, this.nCars),
                            this.random.nextDouble() * (this.maxSpeed - this.minSpeed) + this.minSpeed),
                    this.random.nextDouble() * (this.maxUrgency - this.minUrgency) + this.minUrgency);
            final double alpha = this.random.nextDouble();
            if (car1.getCar().getRoutes().get(car1.getCar().getCurrentRoute()).get(0).equals(DIRECTION.STRAIGHT)) {

                if (alpha <= 0.25) {
                    if (way1.intValue() + 1 <= 3) {
                        way2 = WAY.VALUES.get(way1.intValue() + 1);
                        car2.getCar().addRoute(Collections.singletonList(DIRECTION.STRAIGHT));
                    } else {
                        final int v = way1.intValue() + 1 - 4;

                        way2 = WAY.VALUES.get(v);
                        car2.getCar().addRoute(Collections.singletonList(DIRECTION.STRAIGHT));

                    }
                }

                if (alpha <= 0.50 && alpha > 0.25) {
                    if (way1.intValue() + 2 <= 3) {
                        way2 = WAY.VALUES.get(way1.intValue() + 2);
                        car2.getCar().addRoute(Collections.singletonList(DIRECTION.LEFT));
                    } else {
                        final int v = way1.intValue() + 2 - 4;

                        way2 = WAY.VALUES.get(v);
                        car2.getCar().addRoute(Collections.singletonList(DIRECTION.LEFT));
                    }
                }

                if (alpha <= 0.75 && alpha > 0.50) {
                    if (way1.intValue() + 3 <= 3) {
                        way2 = WAY.VALUES.get(way1.intValue() + 3);
                        car2.getCar().addRoute(Collections.singletonList(DIRECTION.LEFT));
                    } else {
                        final int v = way1.intValue() + 3 - 4;

                        way2 = WAY.VALUES.get(v);
                        car2.getCar().addRoute(Collections.singletonList(DIRECTION.LEFT));
                    }
                }
                if (alpha > 0.75) {
                    if (way1.intValue() + 3 <= 3) {
                        way2 = WAY.VALUES.get(way1.intValue() + 3);
                        car2.getCar().addRoute(Collections.singletonList(DIRECTION.STRAIGHT));
                    } else {
                        final int v = way1.intValue() + 3 - 4;

                        way2 = WAY.VALUES.get(v);
                        car2.getCar().addRoute(Collections.singletonList(DIRECTION.STRAIGHT));
                    }
                }
            }

            //            if (car1.getCar().getRoutes().get(car1.getCar().getCurrentRoute()).get(0).toString().equals(DIRECTION.RIGHT.toString())) {
            //            	way2 = values.get(random.nextInt(size));
            //            	car2.getCar().addRoute(Collections.singletonList(DIRECTION.random()));
            //            }

            if (car1.getCar().getRoutes().get(car1.getCar().getCurrentRoute()).get(0).equals(DIRECTION.LEFT)) {
                if (alpha <= 0.33) {

                    if (way1.intValue() + 1 <= 3) {
                        way2 = WAY.VALUES.get(way1.intValue() + 1);
                        car2.getCar().addRoute(Collections.singletonList(DIRECTION.STRAIGHT));
                    } else {
                        final int v = way1.intValue() + 1 - 4;
                        way2 = WAY.VALUES.get(v);
                        car2.getCar().addRoute(Collections.singletonList(DIRECTION.STRAIGHT));
                    }
                }
                if (alpha <= 0.66 && alpha > 0.33) {

                    if (way1.intValue() + 1 <= 3) {
                        way2 = WAY.VALUES.get(way1.intValue() + 1);
                        car2.getCar().addRoute(Collections.singletonList(DIRECTION.LEFT));
                    } else {
                        final int v = way1.intValue() + 1 - 4;
                        way2 = WAY.VALUES.get(v);
                        car2.getCar().addRoute(Collections.singletonList(DIRECTION.LEFT));
                    }
                }
                if (alpha > 0.66) {

                    if (way1.intValue() + 2 <= 3) {
                        way2 = WAY.VALUES.get(way1.intValue() + 2);
                        car2.getCar().addRoute(Collections.singletonList(DIRECTION.STRAIGHT));
                    } else {
                        final int v = way1.intValue() + 2 - 4;
                        way2 = WAY.VALUES.get(v);
                        car2.getCar().addRoute(Collections.singletonList(DIRECTION.STRAIGHT));
                    }
                }

            }
            car2.getCar().setName(String.format("%s_%d", way2, this.nCars));
            lc.add(new CrossingCar(car1, way1, STATUS.APPROACHING, d));
            lc.add(new CrossingCar(car2, way2, STATUS.APPROACHING, d));
            return lc;
        } else {
            this.log.warn("REFERENCE JUNCTION NOT YET CONFIGURED");
            return null;
        }

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