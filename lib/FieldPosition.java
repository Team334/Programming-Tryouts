package lib;

/**
 * Represents a position on the field with x, y coordinates and heading.
 */
public class FieldPosition {
    private final double _xMeters;
    private final double _yMeters;
    private final double _headingDegrees;

    /**
     * Creates a new FieldPosition.
     * @param xMeters X coordinate in meters.
     * @param yMeters Y coordinate in meters.
     * @param headingDegrees Heading in degrees.
     */
    public FieldPosition(double xMeters, double yMeters, double headingDegrees) {
        _xMeters = xMeters;
        _yMeters = yMeters;
        _headingDegrees = headingDegrees;
    }

    /**
     * Creates a new FieldPosition at the origin with 0 degrees heading.
     */
    public FieldPosition() {
        this(0, 0, 0);
    }

    /**
     * Gets the X coordinate in meters.
     */
    public double getX() {
        return _xMeters;
    }

    /**
     * Gets the Y coordinate in meters.
     */
    public double getY() {
        return _yMeters;
    }

    /**
     * Gets the heading in degrees.
     */
    public double getHeading() {
        return _headingDegrees;
    }

    /**
     * Calculates the distance between this FieldPosition and another FieldPosition.
     * 
     * @param other The other FieldPosition.
     * 
     * @return The distance in meters.
     */
    public double distanceTo(FieldPosition other) {
        double deltaX = other.getX() - getX();
        double deltaY = other.getY() - getY();

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * Calculates the angle from this FieldPosition to another FieldPosition.
     * 
     * @param other The other FieldPosition.
     * 
     * @return The angle in degrees.
     */
    public double angleTo(FieldPosition other) {
        double deltaX = other.getX() - getX();
        double deltaY = other.getY() - getY();

        double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));

        if (angle < 0) {
            angle += 360;
        }

        return angle - getHeading();
    }
}