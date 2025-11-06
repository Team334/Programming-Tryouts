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
     * Calculates the distance between two FieldPositions.
     */
    public static double distance(FieldPosition a, FieldPosition b) {
        double deltaX = b.getX() - a.getX();
        double deltaY = b.getY() - a.getY();

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}