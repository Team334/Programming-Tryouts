package lib;

public class VisionSystem {
    /**
     * Returns the position of a game piece on the field as detected by the vision system.
     */
    public FieldPosition getGamePiecePosition() {
        return new FieldPosition(
            Math.random() * 15,
            Math.random() * 10,
            0
        );
    }
}
