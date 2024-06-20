/**
 *
 */
package sm.argumentation.intersection;

import java.util.List;

/**
 * @author sm
 *
 */
public interface CrossingPolicy {

    List<CrossingCar> rightOfWay(CrossingCar car1, CrossingCar car2);

    String getName();
}
