/**
 *
 */
package sm.argumentation.general;

import org.tweetyproject.arg.aspic.syntax.AspicArgumentationTheory;
import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.Proposition;

import java.util.List;

/**
 * @author sm
 */
public interface Debatable {

    List<Proposition> argue(final AspicArgumentationTheory<PlFormula> t);

}
