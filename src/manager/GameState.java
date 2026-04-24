package manager;

import java.awt.*;

public class GameState {
    public static double time;
    public static int FPS;
    public static double speedCorrection;
    public static boolean gameOver;

    public GameState(int FPS, double speedCorrection) {
        time = 0;
        GameState.FPS = FPS;
        GameState.speedCorrection = speedCorrection;
    }


    public void update(double dt) {
        time += dt;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.drawString("Time: " + Math.round(time) + " s", 10, 930);
    }
}
