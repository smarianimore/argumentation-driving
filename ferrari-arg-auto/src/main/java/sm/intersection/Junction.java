/**
 *
 */
package sm.intersection;

import sm.argumentation.intersection.CrossingPolicy;

import java.util.Map;
import java.util.Set;

/**
 * @author sm
 */
public final class Junction {

    private final String id;
    private final Map<WAY, Road> roads;
    private final CrossingPolicy policy;
    int nServed;
    int nArgProc;
    int nAltRoutesUsed;

    /**
     * @param id    MUST BE UNIQUE
     * @param roads
     */
    public Junction(final String id, final Map<WAY, Road> roads, final CrossingPolicy policy) {
        this.id = id; // MUST BE UNIQUE
        this.roads = roads;
        this.policy = policy;
        this.nServed = 0;
        this.nArgProc = 0;
        this.nAltRoutesUsed = 0;
    }

    /**
     * @return the name
     */
    public String getId() {
        return this.id;
    }

    /**
     * @return the roads
     */
    public Map<WAY, Road> getRoads() {
        return this.roads;
    }

    public int nRoads() {
        return this.roads.size();
    }

    public int incServed() {
        this.nServed += 1;
        return this.nServed;
    }

    public int incArgProc() {
        this.nArgProc += 1;
        return this.nArgProc;
    }

    public int incAltRoutesUsed(final int nAltRoutes) {
        this.nAltRoutesUsed += nAltRoutes;
        return this.nAltRoutesUsed;
    }

    public int getServed() {
        return this.nServed;
    }

    public int getAltRoutesUsed() {
        return this.nAltRoutesUsed;
    }

    public int getArgProc() {
        return this.nArgProc;
    }

    public Set<WAY> ways() {
        return this.roads.keySet();
    }

    public CrossingPolicy getPolicy() {
        return this.policy;
    }

    @Override
    public String toString() {
        return String.format("Junction [name=%s, roads=%s, policy=%s]", this.id, this.roads, this.policy);
    }

}
