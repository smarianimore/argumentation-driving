package sm.simulation;

import java.util.List;

import sm.argumentation.intersection.CrossingCar;
import sm.intersection.Junction;

public interface SimulationAPI {

    List<CrossingCar> step(Boolean log /* , final long steps */);

    void go(Boolean log);

    void pause();

    void logSituation();

    boolean isGoing();

    /**
     * @return the junction
     */
    List<Junction> getJunctions();

    /**
     * @return the cars
     */
    List<CrossingCar> getCars();

    SimulationAPI addCars(List<CrossingCar> cars);

    /**
     * @return the step
     */
    double getStep();

    /**
     * @return the steps
     */
    long getSteps();

    long getMaxSteps();
    
    long getNWaiting();

}