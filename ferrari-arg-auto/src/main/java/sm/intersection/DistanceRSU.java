/**
 *
 */
package sm.intersection;

import org.tweetyproject.arg.aspic.syntax.AspicArgumentationTheory;
import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.Proposition;
import sm.argumentation.general.Debatable;
import sm.argumentation.intersection.ArgKeys;

import java.util.Collections;
import java.util.List;

/**
 * @author sm
 */
public final class DistanceRSU implements RSU<Double>, Debatable {

    private final BaseRSU rsu;
    private final double distance;

    /**
     * @param rsu
     * @param distance
     */
    public DistanceRSU(final BaseRSU rsu, final double distance) {
        this.rsu = rsu;
        this.distance = distance;
    }

    /**
     * @return the rsu
     */
    @Override
    public BaseRSU getRsu() {
        return this.rsu;
    }

    @SuppressWarnings("unchecked") // there will be nothing but Double here
    @Override
    public Double getMeasurement() {
        return this.distance;
    }

    @Override
    public String toString() {
        return String.format("PositionRSU [rsu=%s, distance=%s]", this.rsu, this.distance);
    }

    @Override
    public List<Proposition> argue(final AspicArgumentationTheory<PlFormula> t) { // TODO threshold as param
        Proposition a = null;
        if (this.rsu.getConfidence() > 0.5) {
            a = new Proposition("" + ArgKeys.RSU_trustworthy);
            t.addAxiom(a);
        } else {
            a = new Proposition("" + ArgKeys.RSU_untrustworthy);
            t.addAxiom(a);
        }
        return Collections.singletonList(a);
    }

    @Override
    public Class<Double> getType() {
        return Double.class;
    }

    @Override
    public double getConfidence() {
        return this.rsu.getConfidence();
    }

}
