package manager;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class ScreenEffects {
    private final ChaosController chaosController;
    private double time;
    private final int screenWidth;
    private final int screenHeight;

    public ScreenEffects(ChaosController chaosController, int screenWidth, int screenHeight) {
        this.chaosController = chaosController;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.time = 0;
    }

    public void update(double dt) {
        // Time scales with intensity to make the sway faster as chaos increases
        time += dt * (1 + chaosController.intensity * 0.15);
    }

    public void applyScreenEffects(Graphics2D g2) {
        // Apply the effects in order
        if (chaosController.floorMovement > 0) {
            applySway(g2);
        }

        if (chaosController.rotation > 0) {
            applyOscillatingRotation(g2);
        }

        if (chaosController.intensity > 5) {
            applyPulse(g2);
        }
    }

    private void applySway(Graphics2D g2) {
        // Swaying the whole coordinate system left and right
        double range = chaosController.floorMovement * 20; // Amplitude of the sway
        double xSway = Math.sin(time * 1.5) * range;

        g2.translate(xSway, 0);
    }

    private void applyOscillatingRotation(Graphics2D g2) {
        double centerX = screenWidth / 2.0;
        double centerY = screenHeight / 2.0;

        // This makes the screen tilt left, then right, like a boat on waves
        // maxAngle increases with chaosController.rotation
        double maxAngle = Math.toRadians(chaosController.rotation * 5);
        double currentAngle = Math.sin(time) * maxAngle;

        g2.rotate(currentAngle, centerX, centerY);
    }

    private void applyPulse(Graphics2D g2) {
        double centerX = screenWidth / 2.0;
        double centerY = screenHeight / 2.0;

        // Subtle zoom pulse to add depth
        double zoom = 1.0 + (Math.sin(time * 2) * 0.03);

        g2.translate(centerX, centerY);
        g2.scale(zoom, zoom);
        g2.translate(-centerX, -centerY);
    }

    public void resetTransform(Graphics2D g2, AffineTransform originalTransform) {
        g2.setTransform(originalTransform);
    }
}