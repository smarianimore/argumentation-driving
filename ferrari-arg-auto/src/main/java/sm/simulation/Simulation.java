/**
 *
 */
package sm.simulation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sm.argumentation.intersection.CrossingCar;
import sm.intersection.Junction;

/**
 * @author sm
 *
 */
public final class Simulation extends ManualSimulation {

    private final Logger log = LoggerFactory.getLogger(Simulation.class);
    //	private final ManualSimulation sim;
    private final double gXs;
    private final long genSteps;
    private final long maxSteps;
    private final VehiclesGenerationStrategy genStrategy;

    /**
     *
     * @param junction
     * @param genPerSecond
     * @param genSteps
     * @param maxSteps
     * @param strategy
     * @param simStep
     */
    public Simulation(final Junction junction, final int genPerSecond, final long genSteps,
                      final long maxSteps, final VehiclesGenerationStrategy strategy, final double simStep) {
        super(junction, new ArrayList<>(), simStep);
        this.gXs = genPerSecond;
        this.genSteps = genSteps;
        this.maxSteps = maxSteps;
        this.genStrategy = strategy;
    }

    @Override
    public List<CrossingCar> step(final Boolean log) {
        if (!this.going) {
            if (super.getSteps() >= this.maxSteps) {
                this.log.warn("MAXIMUM STEPS REACHED: {}", this.maxSteps);
            } else {
                if (super.getSteps() < this.genSteps) {
                    for (int i = 0; i < this.gXs; i++) {
                        this.addCars(this.genStrategy.newCars());
                    }
                }
                return super.step(log);
            }
        } else {
            this.log.warn("SIMULATION GOING, PAUSE IT FIRST");
        }
        return null;
    }

    @Override
    public void go(final Boolean log) {
        if (!this.going) {
            this.step(log); // to avoid premature termination of super.go() due to cars being empty
            while (super.getSteps() < this.maxSteps && !super.getCars().isEmpty()) {
                this.going = false;
                this.step(log);
                this.going = true;
                //			super.go(log); // check which step() is called therein: this, or superclass

            }
            if (super.getSteps() >= this.maxSteps) {
                this.log.warn("MAXIMUM STEPS REACHED: {}", this.maxSteps);
            } else {
                this.log.warn("ALL CARS SERVED");
            }
        } else {
            this.log.warn("SIMULATION ALREADY GOING");
        }
        //        this.log.info("##### PERFORMANCE SUMMARY #####");
        //        int ContArg = super.getJunctions().get(0).getArgProc();
        //        this.log.info("{} argumentation processes done", ContArg);
        //        this.log.info("simulation time: {} millis",System.currentTimeMillis()-super.start);
        //        this.log.info("argumentation processes in one second: {}",(double)ContArg*1000/(System.currentTimeMillis()-super.start));
        //        this.log.info("##### #####");
    }

    @Override
    public long getMaxSteps() {
        return this.maxSteps;
    }

}
