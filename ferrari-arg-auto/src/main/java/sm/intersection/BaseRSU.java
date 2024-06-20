/**
 *
 */
package sm.intersection;

/**
 * @author sm
 *
 */
public final class BaseRSU {

    private final String id;
    //	private final WAY position;
    private final double confidence;

    /**
     * @param id
     * @param position
     * @param confidence
     */
    public BaseRSU(final String id, final double confidence) {
        this.id = id;
        //		this.position = position;
        this.confidence = confidence;
    }

    /**
     * @return the name
     */
    public String getId() {
        return this.id;
    }

    /**
     * @return the position
     */
    //	public WAY getPosition() {
    //		return position;
    //	}

    /**
     * @return the confidence
     */
    public double getConfidence() {
        return this.confidence;
    }

    @Override
    public String toString() {
        return String.format("RSU [name=%s, confidence=%s]", this.id, this.confidence);
    }

}
