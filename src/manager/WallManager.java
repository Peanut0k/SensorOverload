package manager;

import entity.FallingWall;
import main.GamePanel;

import java.awt.*;
import java.util.List;

public class WallManager {
    private List<FallingWall> existingEntities;
    GamePanel gp;
    ChaosController chaosController;
    double spawnTimer = 0;
    double speedCorrection;

    public WallManager(List<FallingWall> entities, GamePanel gp, double speedCorrection, ChaosController chaosController) {
        this.existingEntities = entities;
        this.gp = gp;
        this.speedCorrection = speedCorrection;
        this.chaosController = chaosController;
    }

    public void update(double dt) {
        spawnTimer += dt;

        // Use chaos controller's spawn interval
        if (spawnTimer >= chaosController.spawnInterval) {
            existingEntities.add(new FallingWall(gp, GameState.intensity, speedCorrection, chaosController.speedMultiplier));
            spawnTimer = 0;
        }
    }
    public void draw(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.drawString("Phase: " + chaosController.phase + " | Spawn Interval: " + String.format("%.2f", chaosController.spawnInterval), 10, 960);
    }
}