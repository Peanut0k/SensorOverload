package manager;

import java.awt.*;

public class GameState {
    public static double time;
    public static double intensity;
    public static int FPS;
    public static double speedCorrection;
    public static boolean gameOver;
    public ChaosController chaosController;

    public GameState(double intensity, int FPS, double speedCorrection, ChaosController chaosController) {
        time = 0;
        GameState.intensity = intensity;
        GameState.FPS = FPS;
        GameState.speedCorrection = speedCorrection;
        this.chaosController = chaosController;
    }


    public void update(double dt) {
        time += dt;
        if (Math.floor(time) % 10 == 0 && Math.floor(time) != 0)
            intensity += (double) 1 / FPS;

    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.drawString("Intensity: " + Math.round(intensity), 10, 900);
        g2.drawString("Time: " + Math.round(time) + " s", 10, 930);
        g2.setColor(Color.RED);
        g2.drawString("Phase: " + chaosController.phase + " | Spawn Interval: " + String.format("%.2f", chaosController.spawnInterval), 10, 960);

    }
}
