package lib;

public class VisionSystem {
    /**
     * Returns the position of a game piece on the field as detected by the vision system. Note that heading is unimportant and 
     * only the x/y values of the position should be considered.
     */
    public FieldPosition getGamePiecePosition() {
        return new FieldPosition(
            Math.random() * 15,
            Math.random() * 10,
            0 // unimportant here
        );
    }
}
