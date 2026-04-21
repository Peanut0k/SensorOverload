package entity.spawner;

import entity.FallingWall;
import main.GamePanel;

import java.awt.*;
import java.util.List;

public class WallManager {
    private List<FallingWall> existingEntities;
    GamePanel gp;
    double spawnTimer = 0;
    double spawnInterval = 0.25; // seconds (spawn every 1s)
    double difficulty;

    public WallManager(List<FallingWall> entities, GamePanel gp, double difficulty) {
        this.existingEntities = entities;
        this.gp = gp;
        this.difficulty = difficulty;
    }

    public void update(double dt) {
        spawnTimer += dt;
        difficulty += dt;

        if (spawnTimer >= spawnInterval - difficulty * 0.0015) {
            existingEntities.add(new FallingWall(gp, difficulty));
            spawnTimer = 0;
        }
    }
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.drawString(difficulty + "%", 100, (int) (gp.screenHeight / 1.5) -15);
    }
}