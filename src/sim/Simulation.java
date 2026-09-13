package src.sim;

import lib.Commands;
import lib.FieldPosition;
import lib.VisionSystem;
import src.Constants;
import src.Robot;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A tiny keyboard-controlled simulation — mostly so you can SEE that subsystems really do
 * run at the same time now, instead of just trusting the console output. Manually drive
 * (WASD/arrows) over to the game piece, intake it, index it through the hopper, then drive
 * to the scoring zone and shoot — while all of those commands run concurrently instead of
 * blocking each other.
 *
 * There's no auto-drive-to-target key on purpose: driving there is on you, the same way a
 * real driver would. The game piece and scoring zone are just markers to aim for.
 *
 * This is a debugging/demo tool, not a scored part of the tryout — there's no physics
 * engine and no pickup/collision detection, so intake/hopper/shoot can be pressed any time.
 *
 * Controls:
 *   W / Up     drive forward
 *   S / Down   drive backward
 *   A / Left   rotate counterclockwise
 *   D / Right  rotate clockwise
 *   I          run the intake
 *   H          index the hopper
 *   SPACE      shoot
 */
public class Simulation {
    // VisionSystem.getGamePiecePosition() returns x in [0, 15] and y in [0, 10] — the field
    // needs to be at least that big or the piece can spawn off-screen. A little margin too.
    private static final double FIELD_WIDTH_METERS = 16;
    private static final double FIELD_HEIGHT_METERS = 11;
    private static final int PIXELS_PER_METER = 50;
    private static final double REPAINT_SECONDS = 0.02; // 50 Hz, just for how often we redraw

    // Full-power keyboard input maps directly to Constants' max speeds, and those work out
    // to a ~1031 deg/s turn rate — way too twitchy for WASD. Scale keyboard input down to
    // something drivable; tune these to taste.
    private static final double FORWARD_SENSITIVITY = 0.55;
    private static final double ROTATE_SENSITIVITY = 0.22;

    private final Robot _robot;
    private final Set<Integer> _pressedKeys = ConcurrentHashMap.newKeySet();
    private final FieldPosition _gamePiecePosition = new VisionSystem().getGamePiecePosition();

    public Simulation(Robot robot) {
        _robot = robot;
    }

    /** Opens the simulation window and starts the tick loop. Blocks until the window is closed. */
    public void run() {
        FieldPanel panel = new FieldPanel();

        JFrame frame = new JFrame("Team 334 Tryout Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                _pressedKeys.add(e.getKeyCode());
                onKeyPressed(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                _pressedKeys.remove(e.getKeyCode());
            }
        });

        frame.setFocusable(true);
        frame.setVisible(true);

        // Drive is now its own Command instead of something we call directly every tick —
        // schedule it once, as Drive's default command, and it keeps itself running on its
        // own virtual thread by reading these suppliers each loop iteration.
        Commands.schedule(_robot.drive.driveTeleop(this::currentForwardPower, this::currentRotatePower));

        new Timer((int) (REPAINT_SECONDS * 1000), e -> panel.repaint()).start();
    }

    /** Live forward power in [-1, 1] from whatever drive keys are currently held. */
    private double currentForwardPower() {
        double forward = 0;
        if (_pressedKeys.contains(KeyEvent.VK_W) || _pressedKeys.contains(KeyEvent.VK_UP)) forward += 1;
        if (_pressedKeys.contains(KeyEvent.VK_S) || _pressedKeys.contains(KeyEvent.VK_DOWN)) forward -= 1;
        return forward * FORWARD_SENSITIVITY;
    }

    /** Live rotational power in [-1, 1] from whatever drive keys are currently held. */
    private double currentRotatePower() {
        double rotate = 0;
        if (_pressedKeys.contains(KeyEvent.VK_A) || _pressedKeys.contains(KeyEvent.VK_LEFT)) rotate += 1;
        if (_pressedKeys.contains(KeyEvent.VK_D) || _pressedKeys.contains(KeyEvent.VK_RIGHT)) rotate -= 1;
        return rotate * ROTATE_SENSITIVITY;
    }

    /** Runs once per key press (not held): schedules the one-shot commands. */
    private void onKeyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_I -> Commands.schedule(_robot.intake.intake());
            case KeyEvent.VK_H -> Commands.schedule(_robot.hopper.index());
            case KeyEvent.VK_SPACE -> Commands.schedule(_robot.shooter.shoot());
            default -> { }
        }
    }

    /** Draws the field, the scoring position, and the robot. */
    private class FieldPanel extends JPanel {
        FieldPanel() {
            setPreferredSize(new java.awt.Dimension(
                (int) (FIELD_WIDTH_METERS * PIXELS_PER_METER),
                (int) (FIELD_HEIGHT_METERS * PIXELS_PER_METER)
            ));
            setBackground(new Color(20, 20, 24));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            drawGamePiece(g2);
            drawScoringPosition(g2);
            drawRobot(g2);
            drawControls(g2);
        }

        private void drawGamePiece(Graphics2D g2) {
            int[] px = toPixels(_gamePiecePosition);
            g2.setColor(new Color(230, 160, 40));
            g2.fillOval(px[0] - 8, px[1] - 8, 16, 16);
            g2.setColor(Color.WHITE);
            g2.drawString("PIECE", px[0] + 12, px[1] + 4);
        }

        private void drawScoringPosition(Graphics2D g2) {
            int[] px = toPixels(Constants.scoringPosition);
            g2.setColor(new Color(80, 200, 120));
            g2.fillOval(px[0] - 8, px[1] - 8, 16, 16);
            g2.setColor(Color.WHITE);
            g2.drawString("SCORE", px[0] + 12, px[1] + 4);
        }

        private void drawRobot(Graphics2D g2) {
            FieldPosition pos = _robot.drive.getPosition();
            int[] px = toPixels(pos);

            double sizePx = 0.9 * PIXELS_PER_METER;
            double headingRad = Math.toRadians(-pos.getHeading()); // flip: screen y grows downward

            g2.translate(px[0], px[1]);
            g2.rotate(headingRad);

            g2.setColor(new Color(90, 160, 255));
            g2.fillRoundRect((int) (-sizePx / 2), (int) (-sizePx / 2), (int) sizePx, (int) sizePx, 10, 10);

            // A little nose so you can tell which way is "front"
            g2.setColor(Color.WHITE);
            g2.fillOval((int) (sizePx / 2 - 8), -4, 8, 8);

            g2.rotate(-headingRad);
            g2.translate(-px[0], -px[1]);
        }

        private void drawControls(Graphics2D g2) {
            g2.setColor(Color.LIGHT_GRAY);
            g2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            g2.drawString("WASD/arrows: drive   I: intake   H: hopper   SPACE: shoot", 10, getHeight() - 10);
        }

        /** Converts a field position (meters, origin bottom-left) to panel pixels (origin top-left). */
        private int[] toPixels(FieldPosition position) {
            int x = (int) (position.getX() * PIXELS_PER_METER);
            int y = (int) (getPreferredSize().height - position.getY() * PIXELS_PER_METER);
            return new int[] { x, y };
        }
    }
}