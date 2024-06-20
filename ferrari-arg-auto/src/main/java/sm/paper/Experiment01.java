/**
 *
 */
package sm.paper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sm.argumentation.intersection.*;
import sm.intersection.BaseRSU;
import sm.intersection.DistanceRSU;
import sm.intersection.Junction;
import sm.intersection.JunctionsNetwork;
import sm.simulation.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author sm
 *
 */
public final class Experiment01 {

    private static final Logger log = LoggerFactory.getLogger(Experiment01.class);

    private static final String LOG_P = "log";
    private static final String POLICY_P = "policy";
    private static final String STRAT_P = "strat";
    private static final String MAX_STEPS_P = "maxSteps";
    private static final String GEN_STEPS_P = "genSteps";
    private static final String COLS_P = "cols";
    private static final String ROWS_P = "rows";
    private static final int GEN_X_S = 1;
    private static final int SEED = 42;
    private static final int SIM_STEP = 1;
    private static final int RSU_CONFIDENCE = 1;
    private static final int RSU_DISTANCE = 2 * Defaults.SAFETY_DISTANCE_SOFT;

    /**
     * @param args filepath to settings file
     *
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String propsPath;
        if (args.length < 1) {
            log.warn("NO PROPERTIES FILE GIVEN, USING DEFAULT");
            propsPath = Thread.currentThread().getContextClassLoader().getResource("").getPath()
                    + "sim_settings.properties";
        } else {
            log.info("Properties file given, using {}", args[0]);
            propsPath = System.getProperty("user.dir") + "/" + args[0];
        }

        Properties simProps = new Properties();
        log.info("Properties file: {}", propsPath);
        simProps.load(new FileInputStream(propsPath));
        Defaults defs = Defaults.getInstance(propsPath);
        log.info("Defaults: {}", defs);

        for (int i = 0; i < Integer.parseInt(simProps.getProperty("nRuns")); i++) {
            VehiclesGenerationStrategy strat = null;
            if ("RandomStrat".equals(simProps.getProperty(STRAT_P))) {
                strat = new RandomStrat();
            } else if ("ConflictRandomStrat".equals(simProps.getProperty(STRAT_P))) {
                strat = new ConflictRandomStrat();
            } else {
                log.error("UNSUPPORTED STRATEGY: choose <RandomStrat> or <ConflictRandomStrat>");
                System.exit(-1);
            }
            CrossingPolicy pol = null;
            if ("AltRoutesPolicy".equals(simProps.getProperty(POLICY_P))) {
                pol = new AltRoutesPolicy("alt_routes");
            } else if ("NumArgsPolicy".equals(simProps.getProperty(POLICY_P))) {
                pol = new NumArgsPolicy("num_args");
            } else if ("UrgencyPolicy".equals(simProps.getProperty(POLICY_P))) {
                pol = new UrgencyPolicy("urgency");
            } else {
                log.error("UNSUPPORTED POLICY: choose <AltRoutesPolicy> or <NumArgsPolicy>");
                System.exit(-1);
            }

            Junction[][] junctions = new Junction[Integer.parseInt(simProps.getProperty(ROWS_P))][Integer
                    .parseInt(simProps.getProperty(COLS_P))];
            FourWaysJunctionConfig j4;
            List<SimulationAPI> simulationAPIS = new ArrayList<>();
            strat.setSeed(SEED);
            Simulation s;
            for (int r = 0; r < junctions.length; r++) {
                for (int c = 0; c < junctions[r].length; c++) {
                    j4 = new FourWaysJunctionConfig(String.format("J_%d_%d", r, c), pol,
                            new DistanceRSU(new BaseRSU("distance", RSU_CONFIDENCE), RSU_DISTANCE));
                    junctions[r][c] = j4.getJunction();
                    strat.configJunction(junctions[r][c]);
                    s = new Simulation(junctions[r][c], GEN_X_S,
                            Integer.parseInt(simProps.getProperty(GEN_STEPS_P)),
                            Integer.parseInt(simProps.getProperty(MAX_STEPS_P)), strat, SIM_STEP);
                    simulationAPIS.add(s);
                }
            }
            JunctionsNetwork network = new JunctionsNetwork(junctions);
            NetworkSimulation sim = new NetworkSimulation(network, simulationAPIS,
                    String.format("performance-%d.csv", i));
            sim.go(Boolean.parseBoolean(simProps.getProperty(LOG_P)));
            log.info("Props: {}", simProps);
        }
    }

}
