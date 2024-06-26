/**
 *
 */
package sm.simulation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sm.argumentation.intersection.CrossingCar;
import sm.intersection.JunctionsNetwork;

import java.io.IOException;
import java.util.List;

/**
 * @author sm
 */
public final class NetworkSimulation extends ManualNetworkSimulation {

    private final Logger log = LoggerFactory.getLogger(NetworkSimulation.class);
    private final long maxSteps;

    /**
     * @param network
     * @param simulationAPIS MUST HAVE SAME STEP, SAME MAX STEPS
     */
    public NetworkSimulation(final JunctionsNetwork network, final List<SimulationAPI> simulationAPIS,
                             final String performancePath) {
        super(network, simulationAPIS, performancePath);
        this.maxSteps = simulationAPIS.get(0).getMaxSteps();
    }

    @Override
    public List<CrossingCar> step(final Boolean log) {
        if (!this.going) {
            if (super.getSteps() >= this.maxSteps) {
                this.log.warn("MAXIMUM STEPS REACHED: {}", this.maxSteps);
            } else {
                /*if (super.getSteps() < this.genSteps) {
                    for (int i = 0; i < this.gXs; i++) {
                        this.addCars(this.gen.newCars());
                    }
                }*/
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
                //          super.go(log); // check which step() is called therein: this, or superclass
            }
            if (super.getSteps() >= this.maxSteps) {
                this.log.warn("MAXIMUM STEPS REACHED: {}", this.maxSteps);
            } else {
                this.log.warn("ALL CARS SERVED");
            }
        } else {
            this.log.warn("SIMULATION ALREADY GOING");
        }
        int nServed = 0;
        int nArgProc = 0;
        int nAltRoutesUsed = 0;
        for (int i = 0; i < this.network.nRows(); i++) {
            for (int j = 0; j < this.network.nCols(); j++) {
                nServed += this.network.getJunction(i, j).getServed();
                nArgProc += this.network.getJunction(i, j).getArgProc();
                nAltRoutesUsed += this.network.getJunction(i, j).getAltRoutesUsed();
            }
        }
        if (super.writer != null) {
            try {
                super.writer.close();
            } catch (IOException e) {
                this.log.warn("<CSVWriter> NOT CLOSED");
                e.printStackTrace();
            }
        }
        double elapsedTime = System.currentTimeMillis() - super.start;
        //        if (log) {
        this.log.info("##### PERFORMANCE SUMMARY #####");
        this.log.info("{} cars generated", this.generated.size());
        this.log.info("\t{} cars left the network", this.nLeftNet);
        this.log.info("\t{} cars arrived at destination", this.nArrived);
        //        this.log.info("{} cars crossed a junction", nServed);
        this.log.info("{} crossings happened", nServed);
        this.log.info("{} argumentation processes done", nArgProc);
        this.log.info("SimulationAPI time: {} millis ({} seconds)", elapsedTime, elapsedTime / 1000d);
        this.log.info("{} argumentation processes per second", (double) nArgProc * 1000 / (elapsedTime));
        this.log.info("{} alternative routes adopted", nAltRoutesUsed);
        this.log.info("##### #####");
        //        }

    }

    @Override
    public long getMaxSteps() {
        return this.maxSteps;
    }

}
