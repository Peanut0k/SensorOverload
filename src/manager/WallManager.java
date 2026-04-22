package manager;

import entity.Entity;
import entity.FallingWall;
import main.GamePanel;

import java.awt.*;
import java.util.List;

public class WallManager {
    private List<Entity> existingEntities;
    GamePanel gp;
    ChaosController chaosController;
    double spawnTimer = 0;
    double speedCorrection;

    public WallManager(List<Entity> entities, GamePanel gp, double speedCorrection, ChaosController chaosController) {
        this.existingEntities = entities;
        this.gp = gp;
        this.speedCorrection = speedCorrection;
        this.chaosController = chaosController;
    }

    public void update(double dt) {
        spawnTimer += dt;

        if (spawnTimer >= chaosController.spawnInterval) {
            existingEntities.add(new FallingWall(gp, speedCorrection, chaosController.speedMultiplier));
            spawnTimer = 0;
        }
    }
}