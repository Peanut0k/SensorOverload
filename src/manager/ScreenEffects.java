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
        time += dt;
    }

    /**
     * Applies screen effects (movement, rotation, distortion) to the graphics context
     */
    public void applyScreenEffects(Graphics2D g2) {
        // Apply floor/background movement effect
        if (chaosController.floorMovement > 0) {
            applyFloorMovement(g2);
        }

        // Apply rotation effect
        if (chaosController.rotation > 0) {
            applyRotation(g2);
        }

        // Apply wavy distortion effect
        if (chaosController.intensity > 5) {
            applyWaveDistortion(g2);
        }
    }

    private void applyFloorMovement(Graphics2D g2) {
        double centerX = screenWidth / 2.0;
        double centerY = screenHeight / 2.0;

        double waveX = Math.sin(time * 2) * chaosController.floorMovement * 3;
        double waveY = Math.cos(time * 1.5) * chaosController.floorMovement * 2;

                double swayX = Math.sin(time * 3 + chaosController.intensity) * chaosController.floorMovement * 2;

        g2.translate(waveX + swayX, waveY);

        double tiltAngle = Math.sin(time * 2.5) * chaosController.floorMovement * 0.02;
        g2.rotate(tiltAngle, centerX, centerY);
    }

    private void applyRotation(Graphics2D g2) {
        double centerX = screenWidth / 2.0;
        double centerY = screenHeight / 2.0;

        double baseRotation = chaosController.rotation * 0.05;

        double wobble = Math.sin(time * 2) * chaosController.rotation * 0.03;
        double finalRotation = baseRotation + wobble;

        g2.rotate(finalRotation, centerX, centerY);
    }

    private void applyWaveDistortion(Graphics2D g2) {
        double centerX = screenWidth / 2.0;
        double centerY = screenHeight / 2.0;

        double pulse = Math.sin(time) * 0.01 * chaosController.intensity;
        double scale = 1.0 + pulse;

        g2.scale(scale, scale);


        g2.translate((1 - scale) * centerX, (1 - scale) * centerY);
    }

    public void resetTransform(Graphics2D g2, AffineTransform originalTransform) {
        g2.setTransform(originalTransform);
    }
}




