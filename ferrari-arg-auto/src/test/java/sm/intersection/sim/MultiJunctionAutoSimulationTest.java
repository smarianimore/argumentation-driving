package sm.intersection.sim;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sm.argumentation.intersection.AltRoutesPolicy;
import sm.intersection.DistanceRSU;
import sm.argumentation.intersection.FourWaysJunctionConfig;
import sm.intersection.BaseRSU;
import sm.intersection.JunctionsNetwork;
import sm.intersection.Junction;
import sm.simulation.*;
import sm.simulation.ConflictRandomStrat;
import sm.simulation.NetworkSimulation;
import sm.simulation.Simulation;
import sm.simulation.SingleJunctionAutoSimulation;
import sm.simulation.VehiclesGenerationStrategy;

public class MultiJunctionAutoSimulationTest {

    private NetworkSimulation ms;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        List<SimulationAPI> sims = new ArrayList<>();
        FourWaysJunctionConfig fourWays;
        Junction[][] junctions = new Junction[2][2];
        VehiclesGenerationStrategy strat = new ConflictRandomStrat();
        /*
         * For each junction in network...
         */
        for (int r = 0; r < junctions.length; r++) {
            for (int c = 0; c < junctions[r].length; c++) {
                /*
                 * ...create 4 ways junction config...
                 */
                fourWays = new FourWaysJunctionConfig(String.format("4ways %d,%d", r, c),
                        new AltRoutesPolicy("altPolicy"), new DistanceRSU(new BaseRSU("rsu", 1), 50));
                /*
                 * ...and add it to the network...
                 */
                junctions[r][c] = fourWays.getJunction();

                strat.configJunction(junctions[r][c]);
                strat.setSeed(3); // same seed = same random numbers
                if (r == 0 && c == 0)
                    sims.add(new Simulation(junctions[r][c], 1, 3, 20, strat, 1)); // vehicles generated during first 3 steps only junction[0][0]
                else
                    sims.add(new Simulation(junctions[r][c], 0, 0, 20, strat, 1)); // vehicles generated during first 3 steps only junction[0][0]

            }
        }

        this.ms = new NetworkSimulation(new JunctionsNetwork(junctions), sims, "perfs.csv");
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.ms = null;
    }

    /**
     * Test method for
     * {@link NetworkSimulation#step(java.lang.Boolean)}.
     */
    @Test
    public final void testStep() {
        this.ms.step(true);
        this.ms.step(true);
    }

    /**
     * Test method for
     * {@link NetworkSimulation#go(java.lang.Boolean)}.
     */
    @Test
    public final void testGo() {
        this.ms.go(true);
    }

}
