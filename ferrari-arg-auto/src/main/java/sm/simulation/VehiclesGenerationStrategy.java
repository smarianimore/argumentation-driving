/**
 *
 */
package sm.simulation;

import sm.argumentation.intersection.CrossingCar;
import sm.intersection.Junction;

import java.util.List;

/**
 * @author sm
 */
public interface VehiclesGenerationStrategy {

    List<CrossingCar> newCars();

    VehiclesGenerationStrategy configJunction(final Junction junction);

    VehiclesGenerationStrategy setSeed(final long seed);

    VehiclesGenerationStrategy setSpeedRange(final int min, final int max);

    VehiclesGenerationStrategy setUrgencyRange(final int min, final int max);

}
