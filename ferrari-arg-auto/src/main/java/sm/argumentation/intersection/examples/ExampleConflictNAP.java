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

public class ExampleConflictNAP {

    private final static Logger log = LoggerFactory.getLogger(Example1.class);

    public static void main(final String[] args) throws ParserException, IOException {
        final List<Proposition> p = new ArrayList<>();

        List<DIRECTION> route = new ArrayList<>();
        route.add(DIRECTION.LEFT);
        final Car A = new Car("A", 50);
        A.addRoute(route);
        final UrgentCar U_A = new UrgentCar(A, 0.7);

        route = new ArrayList<>();
        route.add(DIRECTION.LEFT);
        final Car B = new Car("B", 55);
        B.addRoute(route);
        final UrgentCar U_B = new UrgentCar(B, 0.5);

        final NumArgsPolicy nap = new NumArgsPolicy("numArgs");

        final BaseRSU rsu = new BaseRSU("RSU", 0.7);
        final DistanceRSU drsu = new DistanceRSU(rsu, 20);

        final FourWaysJunctionConfig fourWC = new FourWaysJunctionConfig("1", nap, drsu);
        ExampleConflictNAP.log.info(fourWC.toString());
        fourWC.addCar(U_A, "EAST");
        fourWC.addCar(U_B, "NORTH");
        ExampleConflictNAP.log.info(fourWC.toString());

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
        ExampleConflictNAP.log.info(t.toString());

        final PlParser plparser = new PlParser();
        final SimpleAspicReasoner<PlFormula> ar = new SimpleAspicReasoner<>(
                AbstractExtensionReasoner.getSimpleReasonerForSemantics(Semantics.GROUNDED_SEMANTICS));
        final PlFormula pf0 = plparser.parseFormula("A_PassesFirst");
        final PlFormula pf1 = plparser.parseFormula("B_PassesFirst");
        final PlFormula pf2 = plparser.parseFormula("Incident");
        ExampleConflictNAP.log.info("{} --> {}", pf0, ar.query(t, pf0, InferenceMode.CREDULOUS));
        ExampleConflictNAP.log.info("{} --> {}", pf1, ar.query(t, pf1, InferenceMode.CREDULOUS));
        ExampleConflictNAP.log.info("{} --> {}", pf2, ar.query(t, pf2, InferenceMode.CREDULOUS));
    }

}
