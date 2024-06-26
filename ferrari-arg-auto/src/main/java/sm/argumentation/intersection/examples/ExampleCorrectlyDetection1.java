package sm.argumentation.intersection.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tweetyproject.arg.aspic.reasoner.SimpleAspicReasoner;
import org.tweetyproject.arg.aspic.ruleformulagenerator.PlFormulaGenerator;
import org.tweetyproject.arg.aspic.syntax.AspicArgumentationTheory;
import org.tweetyproject.arg.dung.reasoner.AbstractExtensionReasoner;
import org.tweetyproject.arg.dung.semantics.Semantics;
import org.tweetyproject.commons.InferenceMode;
import org.tweetyproject.commons.ParserException;
import org.tweetyproject.logics.pl.parser.PlParser;
import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.Proposition;
import sm.argumentation.intersection.CrossingCar;
import sm.argumentation.intersection.FourWaysJunctionConfig;
import sm.argumentation.intersection.NumArgsPolicy;
import sm.intersection.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExampleCorrectlyDetection1 {
    /*
     *
     * An example where we can see a Four Ways Junction where three cars have to cross.
     * The RSU system is trustworthy so these three cars are correctly detected.
     * This scenario impose that one car (B) has to decrease its speed due to the strategy implemented by the policy.
     *
     *
     */
    private final static Logger log = LoggerFactory.getLogger(Example1.class);

    public static void main(final String[] args) throws ParserException, IOException {
        final List<Proposition> p = new ArrayList<>();

        List<DIRECTION> route = new ArrayList<>();
        route.add(DIRECTION.RIGHT);
        final Car A = new Car("A", 50);
        A.addRoute(route);
        final UrgentCar U_A = new UrgentCar(A, 0.7);

        route = new ArrayList<>();
        route.add(DIRECTION.STRAIGHT);
        final Car B = new Car("B", 55);
        B.addRoute(route);
        route = new ArrayList<>();
        route.add(DIRECTION.RIGHT);
        B.addRoute(route);
        final UrgentCar U_B = new UrgentCar(B, 0.5);

        route = new ArrayList<>();
        route.add(DIRECTION.LEFT);
        final Car C = new Car("C", 55);
        C.addRoute(route);
        final UrgentCar U_C = new UrgentCar(C, 0.75);

        final NumArgsPolicy nap = new NumArgsPolicy("numArgs");

        final BaseRSU rsu = new BaseRSU("RSU", 0.7);
        final DistanceRSU drsu = new DistanceRSU(rsu, 20);

        final FourWaysJunctionConfig fourWC = new FourWaysJunctionConfig("1", nap, drsu);
        ExampleCorrectlyDetection1.log.info(fourWC.toString());
        fourWC.addCar(U_A, "EAST");
        fourWC.addCar(U_B, "WEST");
        fourWC.addCar(U_C, "NORTH");
        ExampleCorrectlyDetection1.log.info(fourWC.toString());

        final AspicArgumentationTheory<PlFormula> t = new AspicArgumentationTheory<>(new PlFormulaGenerator());
        t.setRuleFormulaGenerator(new PlFormulaGenerator());
        nap.argue(t);
        final Proposition b = drsu.argue(t).get(0);
        p.add(b);
        Proposition a = null;
        for (final CrossingCar element : fourWC.getCars()) {
            a = element.argue(t).get(0);
            p.add(a);
        }
        fourWC.argue(t);
        ExampleCorrectlyDetection1.log.info(t.toString());

        final PlParser plparser = new PlParser();
        final SimpleAspicReasoner<PlFormula> ar = new SimpleAspicReasoner<>(
                AbstractExtensionReasoner.getSimpleReasonerForSemantics(Semantics.GROUNDED_SEMANTICS));
        final PlFormula pf = plparser.parseFormula("Incident");
        ExampleCorrectlyDetection1.log.info("{} --> {}", pf, ar.query(t, pf, InferenceMode.CREDULOUS));

    }

}
