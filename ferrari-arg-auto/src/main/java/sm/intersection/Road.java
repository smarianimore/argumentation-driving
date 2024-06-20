/**
 *
 */
package sm.intersection;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sm
 *
 */
public final class Road {

    private final BaseRoad baseRoad;
    /**
     * given {@link sm.intersection.RSU#getMeasurement()} implementation, this can
     * be anything extending Object
     */
    private final List<RSU<?>> rsus;

    /**
     * @param baseRoad
     * @param rsus
     */
    public Road(final BaseRoad baseRoad, final List<RSU<?>> rsus) {
        this.baseRoad = baseRoad;
        this.rsus = rsus;
    }

    /**
     * @return the rsus
     */
    public List<RSU<?>> getRsus() {
        return this.rsus;
    }

    public Road addRsu(final RSU<?> rsu) {
        this.rsus.add(rsu);
        return this;
    }

    public int nRsus() {
        return this.rsus.size();
    }

    public List<Class<?>> rsusTypes() {
        return this.rsus.stream().map(RSU::getType).collect(Collectors.toList());
    }

    /**
     * @return the baseRoad
     */
    public BaseRoad getRoad() {
        return this.baseRoad;
    }

    @Override
    public String toString() {
        return String.format("Road [baseRoad=%s, rsus=%s]", this.baseRoad, this.rsus);
    }

}
