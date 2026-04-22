package manager;

import entity.Entity;
import entity.FallingWall;
import entity.Player;
import main.GamePanel;

import java.awt.*;
import java.util.List;

public class CollisionManager {
    public Player player;
    public GamePanel gp;

    public CollisionManager(Player player, GamePanel gp) {
        this.player = player;
        this.gp = gp;
    }

    public boolean checkCollision() {
        for (Entity e : gp.entities) {
            if (e instanceof FallingWall) {
                FallingWall f = (FallingWall) e;
                if (f.collisionBox.intersects(player.playerArea)) {
                    return true;
                }
            }
        }
        return false;
    }

}
