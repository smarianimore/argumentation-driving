/**
 *
 */
package sm.intersection;

import java.util.List;

/**
 * @author sm
 *
 */
public final class BaseRoad {

    private final String id;
    private final List<DIRECTION> lanes;

    /**
     * @param id
     * @param lanes
     */
    public BaseRoad(final String id, final List<DIRECTION> lanes) {
        this.id = id;
        this.lanes = lanes;
    }

    /**
     * @return the name
     */
    public String getId() {
        return this.id;
    }

    /**
     * @return the lanes
     */
    public List<DIRECTION> getLanes() {
        return this.lanes;
    }

    public int nLanes() {
        return this.lanes.size();
    }

    @Override
    public String toString() {
        return String.format("BaseRoad [name=%s, lanes=%s]", this.id, this.lanes);
    }

}
