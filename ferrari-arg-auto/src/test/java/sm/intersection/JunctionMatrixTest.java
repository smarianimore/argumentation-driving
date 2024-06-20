/**
 * 
 */
package sm.intersection;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import sm.argumentation.intersection.FourWaysJunctionConfig;
import sm.argumentation.intersection.NumArgsPolicy;

/**
 * @author sm
 *
 */
public class JunctionMatrixTest {
    
    private JunctionsNetwork jm;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        FourWaysJunctionConfig fourWays;
        Junction[][] junctions = new Junction[2][2];
        for (int r = 0; r < junctions.length; r++) {
            for (int c = 0; c < junctions[r].length; c++) {
                fourWays = new FourWaysJunctionConfig(
                        String.format("4ways %d,%d", r, c), 
                        new NumArgsPolicy("numArgs"), 
                        new DistanceRSU(
                                new BaseRSU(
                                        "rsu", 
                                        1), 
                                50));
                junctions[r][c] = fourWays.getJunction();
            }
        }
        this.jm = new JunctionsNetwork(junctions);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.jm = null;
    }

    /**
     * Test method for {@link JunctionsNetwork#getJunction(int, int)}.
     */
    @Ignore
    @Test
    public final void testGetJunction() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link JunctionsNetwork#setJunction(int, int, Junction)}.
     */
    @Ignore
    @Test
    public final void testSetJunction() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link JunctionsNetwork#next(int[], sm.intersection.WAY, sm.intersection.DIRECTION)}.
     */
    @Test
    public final void testNext() {
        for (int r = 0; r < this.jm.nRows(); r++) {
            for (int c = 0; c < this.jm.nCols(); c++) {
//                assertEquals(this.jm.getJunction(r, c+1), this.jm.next(new int[] {r, c}, WAY.WEST, DIRECTION.STRAIGHT));
//                assertEquals(this.jm.getJunction(r+1, c), this.jm.next(new int[] {r, c}, WAY.WEST, DIRECTION.RIGHT));
//                assertEquals(this.jm.getJunction(r-1, c), this.jm.next(new int[] {r, c}, WAY.WEST, DIRECTION.LEFT));
//                assertEquals(this.jm.getJunction(r+1, c), this.jm.next(new int[] {r, c}, WAY.NORTH, DIRECTION.STRAIGHT));
//                assertEquals(this.jm.getJunction(r, c-1), this.jm.next(new int[] {r, c}, WAY.NORTH, DIRECTION.RIGHT));
//                assertEquals(this.jm.getJunction(r, c+1), this.jm.next(new int[] {r, c}, WAY.NORTH, DIRECTION.LEFT));
//                assertEquals(this.jm.getJunction(r, c-1), this.jm.next(new int[] {r, c}, WAY.EAST, DIRECTION.STRAIGHT));
//                assertEquals(this.jm.getJunction(r-1, c), this.jm.next(new int[] {r, c}, WAY.EAST, DIRECTION.RIGHT));
//                assertEquals(this.jm.getJunction(r+1, c), this.jm.next(new int[] {r, c}, WAY.EAST, DIRECTION.LEFT));
//                assertEquals(this.jm.getJunction(r-1, c), this.jm.next(new int[] {r, c}, WAY.SOUTH, DIRECTION.STRAIGHT));
//                assertEquals(this.jm.getJunction(r, c+1), this.jm.next(new int[] {r, c}, WAY.SOUTH, DIRECTION.RIGHT));
//                assertEquals(this.jm.getJunction(r, c-1), this.jm.next(new int[] {r, c}, WAY.SOUTH, DIRECTION.LEFT));
                Optional<Junction> opt = this.jm.next(new int[] {r, c}, WAY.WEST, DIRECTION.STRAIGHT);
                printNext(r, c, opt);
                opt = this.jm.next(new int[] {r, c}, WAY.WEST, DIRECTION.RIGHT);
                printNext(r, c, opt);
                opt = this.jm.next(new int[] {r, c}, WAY.WEST, DIRECTION.LEFT);
                printNext(r, c, opt);
                opt = this.jm.next(new int[] {r, c}, WAY.NORTH, DIRECTION.STRAIGHT);
                printNext(r, c, opt);
                opt = this.jm.next(new int[] {r, c}, WAY.NORTH, DIRECTION.RIGHT);
                printNext(r, c, opt);
                opt = this.jm.next(new int[] {r, c}, WAY.NORTH, DIRECTION.LEFT);
                printNext(r, c, opt);
                opt = this.jm.next(new int[] {r, c}, WAY.EAST, DIRECTION.STRAIGHT);
                printNext(r, c, opt);
                opt = this.jm.next(new int[] {r, c}, WAY.EAST, DIRECTION.RIGHT);
                printNext(r, c, opt);
                opt = this.jm.next(new int[] {r, c}, WAY.EAST, DIRECTION.LEFT);
                printNext(r, c, opt);
                opt = this.jm.next(new int[] {r, c}, WAY.SOUTH, DIRECTION.STRAIGHT);
                printNext(r, c, opt);
                opt = this.jm.next(new int[] {r, c}, WAY.SOUTH, DIRECTION.RIGHT);
                printNext(r, c, opt);
                opt = this.jm.next(new int[] {r, c}, WAY.SOUTH, DIRECTION.LEFT);
                printNext(r, c, opt);
            }
        }
    }

    private void printNext(int r, int c, Optional<Junction> opt) {
        if (!opt.isEmpty()) {
            System.out.println(String.format("Current: (%d,%d) \t next: %s", r, c, opt.get().getId()));
        }
    }

    /**
     * Test method for {@link JunctionsNetwork#toString()}.
     */
    @Test
    public final void testToString() {
        System.out.println(this.jm.toString());
    }

}
