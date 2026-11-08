package src.sim;

import lib.Commands;
import lib.FieldPosition;
import lib.Mechanism;
import lib.VisionSystem;
import src.Constants;
import src.Robot;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
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
 * This is a debugging/demo tool, not a scored part of the tryout. The game piece has to go
 * intake -> hopper -> shooter, in that order:
 *   1. Run the intake while the BACK of the robot touches the piece to grab it.
 *   2. Run the hopper to move it from the intake to the middle of the robot.
 *   3. Run the shooter to fire it out the FRONT. Land it on SCORE to score.
 * A stage only moves the piece if the piece is in the stage before it and that stage's command
 * ran for its full time in Constants — so a command with the wrong duration shows up here.
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
    private static final int PIXELS_PER_METER = 60;
    private static final int FIELD_WIDTH_PX = (int) (FIELD_WIDTH_METERS * PIXELS_PER_METER);
    private static final int FIELD_HEIGHT_PX = (int) (FIELD_HEIGHT_METERS * PIXELS_PER_METER);
    private static final int SIDEBAR_WIDTH_PX = 290;

    // Full-power keyboard input maps directly to Constants' max speeds, and those work out
    // to a ~1031 deg/s turn rate — way too twitchy for WASD. Scale keyboard input down to
    // something drivable; tune these to taste.
    private static final double FORWARD_SENSITIVITY = 0.55;
    private static final double ROTATE_SENSITIVITY = 0.22;

    // Where things sit along the robot's heading, in meters from its center.
    private static final double ROBOT_SIZE_METERS = 0.9;
    private static final double INTAKE_EDGE_OFFSET = -0.45;
    private static final double INTAKE_OFFSET = -0.3;
    private static final double HOPPER_OFFSET = 0;
    private static final double SHOOTER_OFFSET = 0.3;

    private static final double PIECE_DIAMETER_METERS = 0.3;
    private static final double PICKUP_RADIUS_METERS = 0.35;
    private static final double SCORE_RADIUS_METERS = 0.5;
    private static final double SHOT_SPEED_METERS_PER_SECOND = 8;
    private static final double SHOT_RANGE_METERS = 7;

    // Slack for tick timing when checking a command ran for its full duration.
    private static final double DURATION_TOLERANCE_SECONDS = 0.25;

    private static final int TRAIL_LENGTH = 14;
    private static final double SCORE_FLASH_SECONDS = 0.8;

    // Palette. Mechanism colors match the LED colors in LED.update().
    private static final Color BACKGROUND = new Color(18, 20, 26);
    private static final Color CARPET = new Color(30, 34, 42);
    private static final Color GRID = new Color(40, 45, 55);
    private static final Color GRID_MAJOR = new Color(56, 62, 74);
    private static final Color PANEL = new Color(24, 27, 34);
    private static final Color CARD = new Color(34, 38, 48);
    private static final Color TEXT = new Color(230, 233, 240);
    private static final Color MUTED = new Color(135, 143, 160);
    private static final Color ACCENT = new Color(90, 160, 255);
    private static final Color GOOD = new Color(80, 220, 120);
    private static final Color BAD = new Color(240, 90, 90);
    private static final Color PIECE = new Color(255, 170, 40);
    private static final Color PIECE_EDGE = new Color(170, 100, 15);
    private static final Color BUMPER = new Color(40, 100, 220);
    private static final Color CHASSIS = new Color(38, 42, 52);
    private static final Color WHEEL = new Color(12, 12, 15);
    private static final Color INTAKE_COLOR = new Color(80, 220, 120);
    private static final Color HOPPER_COLOR = new Color(240, 200, 60);
    private static final Color SHOOTER_COLOR = new Color(240, 90, 90);

    private static final Font TITLE_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);
    private static final Font BODY_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    private static final Font BOLD_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);
    private static final Font SMALL_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 10);
    private static final Font SCORE_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 34);
    private static final Font MONO_FONT = new Font("Consolas", Font.PLAIN, 12);

    private enum PieceState { ON_FIELD, INTAKING, IN_INTAKE, IN_HOPPER, IN_FLIGHT }

    private final Robot _robot;
    private final Set<Integer> _pressedKeys = ConcurrentHashMap.newKeySet();
    private final VisionSystem _vision = new VisionSystem();

    // Piece state is only touched on the Swing thread (tick + paint), so no locking needed.
    private PieceState _pieceState = PieceState.ON_FIELD;
    private FieldPosition _piecePosition = _vision.getGamePiecePosition(); // used when ON_FIELD / IN_FLIGHT
    private final Deque<FieldPosition> _trail = new ArrayDeque<>();
    private double _shotHeadingDegrees;
    private double _shotDistanceMeters;
    private int _score;
    private long _scoreFlashNanos;
    private String _status = "Back the robot onto the piece while intaking";

    private final RunTracker _intakeRun = new RunTracker();
    private final RunTracker _hopperRun = new RunTracker();
    private final RunTracker _shooterRun = new RunTracker();

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

        Commands.schedule(_robot.drive.driveTeleop(this::readForward, this::readRotate));

        new Timer((int) (Constants.TICK_SECONDS * 1000), e -> {
            tick();
            panel.repaint();
        }).start();
    }

    /** Reads currently-held drive keys into a forward power, scaled down to something drivable. */
    private double readForward() {
        double forward = 0;

        if (_pressedKeys.contains(KeyEvent.VK_W) || _pressedKeys.contains(KeyEvent.VK_UP)) forward += 1;
        if (_pressedKeys.contains(KeyEvent.VK_S) || _pressedKeys.contains(KeyEvent.VK_DOWN)) forward -= 1;

        return forward * FORWARD_SENSITIVITY;
    }

    /** Reads currently-held turn keys into a rotate power, scaled down to something drivable. */
    private double readRotate() {
        double rotate = 0;

        if (_pressedKeys.contains(KeyEvent.VK_A) || _pressedKeys.contains(KeyEvent.VK_LEFT)) rotate += 1;
        if (_pressedKeys.contains(KeyEvent.VK_D) || _pressedKeys.contains(KeyEvent.VK_RIGHT)) rotate -= 1;

        return rotate * ROTATE_SENSITIVITY;
    }

    /** Runs every simulation tick — driving itself now happens on Drive's own command loop. */
    private void tick() {
        _robot.led.update();
        updatePiece();
    }

    /** Moves the game piece through intake -> hopper -> shooter based on which commands ran. */
    private void updatePiece() {
        Double intakeRan = _intakeRun.update(_robot.intake);
        Double hopperRan = _hopperRun.update(_robot.hopper);
        Double shooterRan = _shooterRun.update(_robot.shooter);

        if (_pieceState == PieceState.ON_FIELD
                && Commands.owner(_robot.intake) != null
                && robotPoint(INTAKE_EDGE_OFFSET).distanceTo(_piecePosition) <= PICKUP_RADIUS_METERS) {
            _pieceState = PieceState.INTAKING;
            _status = "Grabbed the piece - intaking...";
        }

        if (intakeRan != null) {
            finishStage("Intake", intakeRan, Constants.intakeRunTimeSeconds, PieceState.INTAKING, PieceState.IN_INTAKE);
        }
        if (hopperRan != null) {
            finishStage("Hopper", hopperRan, Constants.hopperIndexTimeSeconds, PieceState.IN_INTAKE, PieceState.IN_HOPPER);
        }
        if (shooterRan != null) {
            finishStage("Shooter", shooterRan, Constants.shooterRunTimeSeconds, PieceState.IN_HOPPER, PieceState.IN_FLIGHT);
        }

        if (_pieceState == PieceState.IN_FLIGHT) {
            flyPiece();
        }
    }

    /**
     * Called when a mechanism's command stops running. Hands the piece to the next stage only if
     * it was in this stage and the command ran for its full expected duration.
     */
    private void finishStage(String name, double ranSeconds, double expectedSeconds, PieceState from, PieceState to) {
        if (_pieceState != from) {
            _status = String.format("%s ran %.1fs, but the piece is %s - nothing moved", name, ranSeconds, describe(_pieceState));
            return;
        }

        if (!durationOk(ranSeconds, expectedSeconds)) {
            _status = String.format("%s ran %.1fs, expected %.1fs - piece not fed", name, ranSeconds, expectedSeconds);
            if (from == PieceState.INTAKING) {
                _piecePosition = robotPoint(INTAKE_EDGE_OFFSET);
                _pieceState = PieceState.ON_FIELD;
            }
            return;
        }

        _pieceState = to;
        switch (to) {
            case IN_INTAKE -> _status = "Piece in intake - run the hopper";
            case IN_HOPPER -> _status = "Piece in hopper - aim and shoot";
            case IN_FLIGHT -> {
                _piecePosition = robotPoint(SHOOTER_OFFSET);
                _shotHeadingDegrees = _robot.drive.getPosition().getHeading();
                _shotDistanceMeters = 0;
                _trail.clear();
                _status = "Shot!";
            }
            default -> { }
        }
    }

    /** Advances a shot piece; it scores if it passes over SCORE, otherwise lands back on the field. */
    private void flyPiece() {
        double step = SHOT_SPEED_METERS_PER_SECOND * Constants.TICK_SECONDS;
        double headingRad = Math.toRadians(_shotHeadingDegrees);
        _piecePosition = new FieldPosition(
            _piecePosition.getX() + step * Math.cos(headingRad),
            _piecePosition.getY() + step * Math.sin(headingRad),
            0
        );
        _shotDistanceMeters += step;
        _trail.addFirst(_piecePosition);
        if (_trail.size() > TRAIL_LENGTH) {
            _trail.removeLast();
        }

        if (_piecePosition.distanceTo(Constants.scoringPosition) <= SCORE_RADIUS_METERS) {
            _score++;
            _scoreFlashNanos = System.nanoTime();
            _piecePosition = _vision.getGamePiecePosition();
            _pieceState = PieceState.ON_FIELD;
            _trail.clear();
            _status = "Scored! New piece spawned";
            return;
        }

        boolean offField = _piecePosition.getX() < 0 || _piecePosition.getX() > FIELD_WIDTH_METERS
            || _piecePosition.getY() < 0 || _piecePosition.getY() > FIELD_HEIGHT_METERS;
        if (_shotDistanceMeters >= SHOT_RANGE_METERS || offField) {
            _piecePosition = new FieldPosition(
                Math.max(0, Math.min(FIELD_WIDTH_METERS, _piecePosition.getX())),
                Math.max(0, Math.min(FIELD_HEIGHT_METERS, _piecePosition.getY())),
                0
            );
            _pieceState = PieceState.ON_FIELD;
            _trail.clear();
            _status = "Missed - go pick it up again";
        }
    }

    /** Whether a shot fired right now would pass over SCORE (drives the aim guide's color). */
    private boolean shotWouldScore() {
        FieldPosition start = robotPoint(SHOOTER_OFFSET);
        double headingRad = Math.toRadians(_robot.drive.getPosition().getHeading());
        double dx = Constants.scoringPosition.getX() - start.getX();
        double dy = Constants.scoringPosition.getY() - start.getY();
        double along = dx * Math.cos(headingRad) + dy * Math.sin(headingRad);
        double across = Math.abs(dx * Math.sin(headingRad) - dy * Math.cos(headingRad));
        // A little under the score radius since flight is sampled once per tick.
        return along >= 0 && along <= SHOT_RANGE_METERS && across <= SCORE_RADIUS_METERS * 0.9;
    }

    private static boolean durationOk(double ranSeconds, double expectedSeconds) {
        return Math.abs(ranSeconds - expectedSeconds) <= DURATION_TOLERANCE_SECONDS;
    }

    /** A point offsetMeters ahead of (positive) or behind (negative) the robot's center. */
    private FieldPosition robotPoint(double offsetMeters) {
        FieldPosition pos = _robot.drive.getPosition();
        double headingRad = Math.toRadians(pos.getHeading());
        return new FieldPosition(
            pos.getX() + offsetMeters * Math.cos(headingRad),
            pos.getY() + offsetMeters * Math.sin(headingRad),
            0
        );
    }

    private static String describe(PieceState state) {
        return switch (state) {
            case ON_FIELD -> "on the field";
            case INTAKING, IN_INTAKE -> "in the intake";
            case IN_HOPPER -> "in the hopper";
            case IN_FLIGHT -> "in the air";
        };
    }

    /**
     * Watches Commands.owner(mechanism) once per tick and reports how long each command
     * that used the mechanism actually ran, once it stops (finishes or gets interrupted).
     */
    private static class RunTracker {
        private Commands.Command _current;
        private long _startNanos;
        private Double _lastRanSeconds;

        /** Returns the run time in seconds of a command that just stopped, or null if none did. */
        Double update(Mechanism mechanism) {
            Commands.Command owner = Commands.owner(mechanism);
            if (owner == _current) {
                return null;
            }

            long now = System.nanoTime();
            Double ranSeconds = _current == null ? null : (now - _startNanos) / 1e9;
            if (ranSeconds != null) {
                _lastRanSeconds = ranSeconds;
            }
            _current = owner;
            _startNanos = now;
            return ranSeconds;
        }

        boolean isRunning() {
            return _current != null;
        }

        /** Seconds the current command has been running (0 when idle). */
        double runningSeconds() {
            return _current == null ? 0 : (System.nanoTime() - _startNanos) / 1e9;
        }

        /** How long the last stopped command ran, or null if none has yet. */
        Double lastRanSeconds() {
            return _lastRanSeconds;
        }
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

    /** Draws the field on the left and a status sidebar on the right. */
    private class FieldPanel extends JPanel {
        FieldPanel() {
            setPreferredSize(new Dimension(FIELD_WIDTH_PX + SIDEBAR_WIDTH_PX, FIELD_HEIGHT_PX));
            setBackground(BACKGROUND);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            double t = System.nanoTime() / 1e9;

            g2.setClip(0, 0, FIELD_WIDTH_PX, FIELD_HEIGHT_PX);
            drawField(g2);
            drawScoringZone(g2, t);
            drawAimGuide(g2);
            drawPickupHint(g2);
            drawRobot(g2, t);
            drawGamePiece(g2, t);
            g2.setClip(null);

            drawSidebar(g2);
            g2.dispose();
        }

        // ---------------------------------------------------------------- field

        private void drawField(Graphics2D g2) {
            g2.setColor(CARPET);
            g2.fillRect(0, 0, FIELD_WIDTH_PX, FIELD_HEIGHT_PX);

            for (int m = 1; m < FIELD_WIDTH_METERS; m++) {
                g2.setColor(m % 5 == 0 ? GRID_MAJOR : GRID);
                g2.drawLine(m * PIXELS_PER_METER, 0, m * PIXELS_PER_METER, FIELD_HEIGHT_PX);
            }
            for (int m = 1; m < FIELD_HEIGHT_METERS; m++) {
                int y = FIELD_HEIGHT_PX - m * PIXELS_PER_METER;
                g2.setColor(m % 5 == 0 ? GRID_MAJOR : GRID);
                g2.drawLine(0, y, FIELD_WIDTH_PX, y);
            }

            g2.setColor(GRID_MAJOR);
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(1, 1, FIELD_WIDTH_PX - 3, FIELD_HEIGHT_PX - 3);
            g2.setStroke(new BasicStroke(1));
        }

        private void drawScoringZone(Graphics2D g2, double t) {
            double[] c = toPixels(Constants.scoringPosition);
            double r = SCORE_RADIUS_METERS * PIXELS_PER_METER;
            double pulse = 0.5 + 0.5 * Math.sin(t * 3);

            g2.setColor(withAlpha(GOOD, 25));
            g2.fill(circle(c[0], c[1], r + 6 + pulse * 5));
            g2.setColor(withAlpha(GOOD, 50));
            g2.fill(circle(c[0], c[1], r));
            g2.setColor(GOOD);
            g2.setStroke(new BasicStroke(2));
            g2.draw(circle(c[0], c[1], r));
            g2.fill(circle(c[0], c[1], 3));
            g2.setStroke(new BasicStroke(1));

            g2.setFont(SMALL_FONT);
            drawCentered(g2, "SCORE", c[0], c[1] + r + 16);

            double age = (System.nanoTime() - _scoreFlashNanos) / 1e9;
            if (_scoreFlashNanos != 0 && age < SCORE_FLASH_SECONDS) {
                double f = age / SCORE_FLASH_SECONDS;
                int alpha = (int) (255 * (1 - f));
                g2.setColor(withAlpha(GOOD, alpha));
                g2.setStroke(new BasicStroke(3));
                g2.draw(circle(c[0], c[1], r + f * 60));
                g2.setStroke(new BasicStroke(1));
                g2.setFont(SCORE_FONT.deriveFont(22f));
                g2.setColor(withAlpha(TEXT, alpha));
                drawCentered(g2, "+1", c[0], c[1] - r - 10 - f * 30);
            }
        }

        /** While holding a piece, shows where a shot would go — green when it'd score. */
        private void drawAimGuide(Graphics2D g2) {
            if (_pieceState != PieceState.IN_HOPPER) {
                return;
            }

            FieldPosition start = robotPoint(SHOOTER_OFFSET);
            double headingRad = Math.toRadians(_robot.drive.getPosition().getHeading());
            FieldPosition end = new FieldPosition(
                start.getX() + SHOT_RANGE_METERS * Math.cos(headingRad),
                start.getY() + SHOT_RANGE_METERS * Math.sin(headingRad),
                0
            );
            double[] a = toPixels(start);
            double[] b = toPixels(end);

            g2.setColor(withAlpha(shotWouldScore() ? GOOD : MUTED, 190));
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1, new float[] { 8, 8 }, 0));
            g2.draw(new Line2D.Double(a[0], a[1], b[0], b[1]));
            g2.setStroke(new BasicStroke(2));
            g2.draw(circle(b[0], b[1], 5));
            g2.setStroke(new BasicStroke(1));
        }

        /** While the intake runs, shows the area behind the robot that can grab the piece. */
        private void drawPickupHint(Graphics2D g2) {
            if (_pieceState != PieceState.ON_FIELD || !_intakeRun.isRunning()) {
                return;
            }

            double[] c = toPixels(robotPoint(INTAKE_EDGE_OFFSET));
            double r = PICKUP_RADIUS_METERS * PIXELS_PER_METER;
            g2.setColor(withAlpha(INTAKE_COLOR, 40));
            g2.fill(circle(c[0], c[1], r));
            g2.setColor(withAlpha(INTAKE_COLOR, 140));
            g2.draw(circle(c[0], c[1], r));
        }

        // ---------------------------------------------------------------- robot

        private void drawRobot(Graphics2D g2, double t) {
            FieldPosition pos = _robot.drive.getPosition();
            double[] c = toPixels(pos);
            double s = ROBOT_SIZE_METERS * PIXELS_PER_METER;
            double half = s / 2;

            AffineTransform saved = g2.getTransform();
            g2.translate(c[0], c[1]);
            g2.rotate(Math.toRadians(-pos.getHeading())); // flip: screen y grows downward
            // From here on +x is the robot's front.

            // LED glow
            Color led = colorFor(_robot.led.getColor());
            for (int i = 3; i >= 1; i--) {
                double grow = 3 + i * 3;
                g2.setColor(withAlpha(led, 28));
                g2.fill(new RoundRectangle2D.Double(-half - grow, -half - grow, s + grow * 2, s + grow * 2, 16 + grow, 16 + grow));
            }

            // Wheels, one per corner
            g2.setColor(WHEEL);
            double wheelLength = s * 0.3;
            double wheelWidth = s * 0.14;
            for (int sx = -1; sx <= 1; sx += 2) {
                for (int sy = -1; sy <= 1; sy += 2) {
                    g2.fill(new RoundRectangle2D.Double(sx * half * 0.55 - wheelLength / 2, sy * half - wheelWidth / 2,
                        wheelLength, wheelWidth, 4, 4));
                }
            }

            // Bumpers + chassis
            g2.setColor(BUMPER);
            g2.fill(new RoundRectangle2D.Double(-half, -half, s, s, 12, 12));
            g2.setColor(CHASSIS);
            g2.fill(new RoundRectangle2D.Double(-half + 5, -half + 5, s - 10, s - 10, 8, 8));

            // Mechanisms, back to front
            drawMechanism(g2, t, -0.40, -0.23, half, _intakeRun.isRunning(), INTAKE_COLOR);
            drawMechanism(g2, t, -0.18, 0.18, half, _hopperRun.isRunning(), HOPPER_COLOR);
            drawMechanism(g2, t, 0.23, 0.40, half, _shooterRun.isRunning(), SHOOTER_COLOR);

            // Front marker
            Path2D.Double nose = new Path2D.Double();
            nose.moveTo(half + 7, 0);
            nose.lineTo(half - 1, -6);
            nose.lineTo(half - 1, 6);
            nose.closePath();
            g2.setColor(TEXT);
            g2.fill(nose);

            g2.setTransform(saved);
        }

        /**
         * One mechanism zone in robot-local pixels, from fromMeters to toMeters along the robot.
         * Dim when idle; bright with rollers spinning toward the front when its command is running.
         */
        private void drawMechanism(Graphics2D g2, double t, double fromMeters, double toMeters, double half,
                boolean running, Color color) {
            double x0 = fromMeters * PIXELS_PER_METER;
            double x1 = toMeters * PIXELS_PER_METER;
            Rectangle2D.Double zone = new Rectangle2D.Double(x0, -half + 8, x1 - x0, 2 * (half - 8));

            g2.setColor(withAlpha(color, running ? 110 : 45));
            g2.fill(zone);

            if (running) {
                Shape oldClip = g2.getClip();
                g2.clip(zone);
                g2.setColor(withAlpha(color, 220));
                g2.setStroke(new BasicStroke(2));
                double spacing = 7;
                double offset = (t * 70) % spacing;
                for (double x = x0 - zone.height; x < x1 + spacing; x += spacing) {
                    g2.draw(new Line2D.Double(x + offset, zone.y, x + offset - 6, zone.y + zone.height));
                }
                g2.setStroke(new BasicStroke(1));
                g2.setClip(oldClip);
            }

            g2.setColor(withAlpha(color, running ? 255 : 120));
            g2.draw(zone);
        }

        // ---------------------------------------------------------------- piece

        /** Draws the piece on the field, in the air, or inside the robot at whichever stage holds it. */
        private void drawGamePiece(Graphics2D g2, double t) {
            double r = PIECE_DIAMETER_METERS / 2 * PIXELS_PER_METER;

            switch (_pieceState) {
                case ON_FIELD -> {
                    double[] c = toPixels(_piecePosition);
                    double pulse = 0.5 + 0.5 * Math.sin(t * 4);
                    g2.setColor(withAlpha(PIECE, (int) (60 + 60 * pulse)));
                    g2.draw(circle(c[0], c[1], r + 4 + pulse * 3));
                    drawShadow(g2, c[0], c[1] + 2, r);
                    drawBall(g2, c[0], c[1], r);
                }
                case INTAKING, IN_INTAKE -> {
                    double[] c = toPixels(robotPoint(INTAKE_OFFSET));
                    drawBall(g2, c[0], c[1], r * 0.85);
                }
                case IN_HOPPER -> {
                    double[] c = toPixels(robotPoint(HOPPER_OFFSET));
                    drawBall(g2, c[0], c[1], r * 0.85);
                }
                case IN_FLIGHT -> drawFlyingPiece(g2, r);
            }
        }

        /** Trail, ground shadow, and a ball that rises and falls along the shot's arc. */
        private void drawFlyingPiece(Graphics2D g2, double r) {
            int i = 0;
            for (FieldPosition p : _trail) {
                double f = (double) i / TRAIL_LENGTH;
                double[] c = toPixels(p);
                g2.setColor(withAlpha(PIECE, (int) (140 * (1 - f))));
                g2.fill(circle(c[0], c[1], r * (0.7 - 0.45 * f)));
                i++;
            }

            double progress = Math.min(1, _shotDistanceMeters / SHOT_RANGE_METERS);
            double height = 4 * progress * (1 - progress);
            double[] c = toPixels(_piecePosition);
            drawShadow(g2, c[0], c[1], r * (1 - 0.3 * height));
            drawBall(g2, c[0], c[1] - height * 22, r * (1 + 0.6 * height));
        }

        private void drawBall(Graphics2D g2, double x, double y, double r) {
            g2.setColor(PIECE);
            g2.fill(circle(x, y, r));
            g2.setColor(withAlpha(Color.WHITE, 120));
            g2.fill(circle(x - r / 3, y - r / 3, r / 3));
            g2.setColor(PIECE_EDGE);
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(circle(x, y, r));
            g2.setStroke(new BasicStroke(1));
        }

        private void drawShadow(Graphics2D g2, double x, double y, double r) {
            g2.setColor(new Color(0, 0, 0, 90));
            g2.fill(new Ellipse2D.Double(x - r, y - r * 0.5, r * 2, r));
        }

        // ---------------------------------------------------------------- sidebar

        private void drawSidebar(Graphics2D g2) {
            int x = FIELD_WIDTH_PX;
            g2.setColor(PANEL);
            g2.fillRect(x, 0, SIDEBAR_WIDTH_PX, getHeight());
            g2.setColor(GRID_MAJOR);
            g2.drawLine(x, 0, x, getHeight());

            int left = x + 14;
            int width = SIDEBAR_WIDTH_PX - 28;
            int y = 14;

            // Score + where the piece is
            card(g2, left, y, width, 112);
            g2.setFont(SMALL_FONT);
            g2.setColor(MUTED);
            g2.drawString("SCORE", left + 12, y + 18);
            g2.setFont(SCORE_FONT);
            g2.setColor(TEXT);
            g2.drawString(String.valueOf(_score), left + 12, y + 52);
            drawPipeline(g2, left + 12, y + 74, width - 24);
            y += 122;

            // Mechanisms
            y = drawMechanismCard(g2, left, y, width, "Intake", "I", _intakeRun,
                Constants.intakeRunTimeSeconds, INTAKE_COLOR,
                String.format("motor %+4.0f%%", _robot.intake.getMotorPower() * 100));
            y = drawMechanismCard(g2, left, y, width, "Hopper", "H", _hopperRun,
                Constants.hopperIndexTimeSeconds, HOPPER_COLOR,
                String.format("L %+4.0f%%  R %+4.0f%%",
                    _robot.hopper.getLeftMotorPower() * 100, _robot.hopper.getRightMotorPower() * 100));
            y = drawMechanismCard(g2, left, y, width, "Shooter", "SPACE", _shooterRun,
                Constants.shooterRunTimeSeconds, SHOOTER_COLOR,
                String.format("motor %+4.0f%%", _robot.shooter.getMotorPower() * 100));

            // Status message
            g2.setFont(BODY_FONT);
            List<String> lines = wrap(_status, g2.getFontMetrics(), width - 24);
            int statusHeight = 14 + lines.size() * 16;
            card(g2, left, y, width, statusHeight);
            g2.setColor(ACCENT);
            g2.fillRect(left, y, 3, statusHeight);
            g2.setColor(TEXT);
            for (int i = 0; i < lines.size(); i++) {
                g2.drawString(lines.get(i), left + 12, y + 19 + i * 16);
            }

            // Controls, pinned to the bottom
            String[] controls = { "W A S D / arrows   drive", "I   intake (back)      H   hopper", "SPACE   shoot (front)" };
            g2.setFont(MONO_FONT);
            g2.setColor(MUTED);
            int controlsY = getHeight() - 14 - (controls.length - 1) * 15;
            for (int i = 0; i < controls.length; i++) {
                g2.drawString(controls[i], left, controlsY + i * 15);
            }
        }

        /** FIELD -> INTAKE -> HOPPER -> SHOT, with the piece's current stage highlighted. */
        private void drawPipeline(Graphics2D g2, int x, int y, int width) {
            String[] stages = { "FIELD", "INTAKE", "HOPPER", "SHOT" };
            int current = switch (_pieceState) {
                case ON_FIELD -> 0;
                case INTAKING, IN_INTAKE -> 1;
                case IN_HOPPER -> 2;
                case IN_FLIGHT -> 3;
            };
            double step = (width - 20) / (double) (stages.length - 1);

            g2.setStroke(new BasicStroke(2));
            for (int i = 0; i < stages.length - 1; i++) {
                g2.setColor(i < current ? ACCENT : GRID_MAJOR);
                g2.draw(new Line2D.Double(x + 10 + i * step, y, x + 10 + (i + 1) * step, y));
            }
            g2.setStroke(new BasicStroke(1));

            g2.setFont(SMALL_FONT);
            for (int i = 0; i < stages.length; i++) {
                double cx = x + 10 + i * step;
                if (i == current) {
                    g2.setColor(withAlpha(ACCENT, 60));
                    g2.fill(circle(cx, y, 10));
                }
                g2.setColor(i <= current ? ACCENT : GRID_MAJOR);
                g2.fill(circle(cx, y, 6));
                g2.setColor(i == current ? TEXT : MUTED);
                drawCentered(g2, stages[i], cx, y + 24);
            }
        }

        /** Name, key, running state, a timing bar against the expected duration, and the last run's result. */
        private int drawMechanismCard(Graphics2D g2, int x, int y, int width, String name, String key,
                RunTracker run, double expectedSeconds, Color color, String motorText) {
            int height = 66;
            card(g2, x, y, width, height);
            boolean running = run.isRunning();

            g2.setColor(color);
            g2.fill(circle(x + 16, y + 17, 4));
            g2.setFont(BOLD_FONT);
            g2.setColor(TEXT);
            g2.drawString(name, x + 26, y + 21);

            // Key chip
            g2.setFont(SMALL_FONT);
            FontMetrics fm = g2.getFontMetrics();
            int chipX = x + 26 + g2.getFontMetrics(BOLD_FONT).stringWidth(name) + 8;
            int chipWidth = fm.stringWidth(key) + 10;
            g2.setColor(GRID_MAJOR);
            g2.fill(new RoundRectangle2D.Double(chipX, y + 9, chipWidth, 15, 6, 6));
            g2.setColor(TEXT);
            g2.drawString(key, chipX + 5, y + 20);

            g2.setColor(running ? color : MUTED);
            drawRight(g2, running ? "RUNNING" : "IDLE", x + width - 12, y + 21);

            // Timing bar: fills over the expected duration, turns red if it runs long
            int barX = x + 12;
            int barY = y + 32;
            int barWidth = width - 24;
            g2.setColor(GRID);
            g2.fill(new RoundRectangle2D.Double(barX, barY, barWidth, 6, 6, 6));
            if (running) {
                double elapsed = run.runningSeconds();
                double fill = Math.min(1, elapsed / expectedSeconds);
                boolean tooLong = elapsed > expectedSeconds + DURATION_TOLERANCE_SECONDS;
                g2.setColor(tooLong ? BAD : color);
                g2.fill(new RoundRectangle2D.Double(barX, barY, barWidth * fill, 6, 6, 6));
            }

            // Last result + live motor output
            g2.setFont(MONO_FONT);
            Double last = run.lastRanSeconds();
            if (running) {
                g2.setColor(MUTED);
                g2.drawString(String.format("%.1fs / %.1fs", run.runningSeconds(), expectedSeconds), barX, y + 55);
            } else if (last == null) {
                g2.setColor(MUTED);
                g2.drawString(String.format("needs %.1fs", expectedSeconds), barX, y + 55);
            } else {
                g2.setColor(durationOk(last, expectedSeconds) ? GOOD : BAD);
                g2.drawString(String.format("last %.1fs / %.1fs", last, expectedSeconds), barX, y + 55);
            }
            g2.setColor(MUTED);
            drawRight(g2, motorText, x + width - 12, y + 55);

            return y + height + 8;
        }

        private void card(Graphics2D g2, int x, int y, int width, int height) {
            g2.setColor(CARD);
            g2.fill(new RoundRectangle2D.Double(x, y, width, height, 10, 10));
        }

        // ---------------------------------------------------------------- helpers

        private Ellipse2D.Double circle(double cx, double cy, double r) {
            return new Ellipse2D.Double(cx - r, cy - r, r * 2, r * 2);
        }

        private Color withAlpha(Color color, int alpha) {
            return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, alpha)));
        }

        private void drawCentered(Graphics2D g2, String text, double cx, double baselineY) {
            int w = g2.getFontMetrics().stringWidth(text);
            g2.drawString(text, (float) (cx - w / 2.0), (float) baselineY);
        }

        private void drawRight(Graphics2D g2, String text, int rightX, int baselineY) {
            g2.drawString(text, rightX - g2.getFontMetrics().stringWidth(text), baselineY);
        }

        private List<String> wrap(String text, FontMetrics fm, int width) {
            List<String> lines = new ArrayList<>();
            String line = "";
            for (String word : text.split(" ")) {
                String candidate = line.isEmpty() ? word : line + " " + word;
                if (!line.isEmpty() && fm.stringWidth(candidate) > width) {
                    lines.add(line);
                    line = word;
                } else {
                    line = candidate;
                }
            }
            if (!line.isEmpty()) {
                lines.add(line);
            }
            return lines;
        }

        /** Maps an LED color name (whatever the LED subsystem hands it) to something drawable. */
        private static Color colorFor(String name) {
            return switch (name.toUpperCase()) {
                case "RED" -> SHOOTER_COLOR;
                case "GREEN" -> INTAKE_COLOR;
                case "BLUE" -> ACCENT;
                case "ORANGE" -> PIECE;
                case "YELLOW" -> HOPPER_COLOR;
                case "PURPLE" -> new Color(170, 100, 220);
                case "WHITE" -> Color.WHITE;
                case "OFF" -> Color.DARK_GRAY;
                default -> Color.GRAY;
            };
        }

        /** Converts a field position (meters, origin bottom-left) to panel pixels (origin top-left). */
        private double[] toPixels(FieldPosition position) {
            return new double[] {
                position.getX() * PIXELS_PER_METER,
                FIELD_HEIGHT_PX - position.getY() * PIXELS_PER_METER
            };
        }
    }
}
