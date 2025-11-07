package lib;

public class FieldPosition {
    private final double _xMeters;
    private final double _yMeters;
    private final double _headingDegrees;

    public FieldPosition(double xMeters, double yMeters, double headingDegrees) {
        _xMeters = xMeters;
        _yMeters = yMeters;
        _headingDegrees = headingDegrees;
    }

    public FieldPosition() {
        this(0, 0, 0);
    }

    public double getX() {
        return _xMeters;
    }

    public double getY() {
        return _yMeters;
    }

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