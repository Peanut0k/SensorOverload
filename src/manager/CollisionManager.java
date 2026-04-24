package manager;

import entity.Entity;
import main.GamePanel;

public class CollisionManager {
    GamePanel gp;

    public CollisionManager(GamePanel gp) {
        this.gp = gp;
    }

    public void checkBlock(Entity entity) {
        int entityLeftX = entity.collisionBox.x;
        int entityRightX = entity.collisionBox.x + entity.collisionBox.width;
        int entityTopY = entity.collisionBox.y;
        int entityBottomY = entity.collisionBox.y + entity.collisionBox.height;

        int entityLeftCol = entityLeftX / gp.tileSize;
        int entityRightCol = entityRightX / gp.tileSize;
        int entityTopRow = entityTopY / gp.tileSize;
        int entityBottomRow = entityBottomY / gp.tileSize;

        int tileNum1, tileNum2;

        entity.collisionON = false;

        switch (entity.direction) {
            case "up":
                entityTopRow = (int) ((entityTopY + entity.velocityXY[1]) / gp.tileSize);
                if (entityTopRow >= 0 && entityTopRow < gp.maxScreenRow) {
                    tileNum1 = gp.blockManager.mapBlockNum[entityLeftCol][entityTopRow];
                    tileNum2 = gp.blockManager.mapBlockNum[entityRightCol][entityTopRow];
                    if (gp.blockManager.block[tileNum1].collision || gp.blockManager.block[tileNum2].collision) {
                        entity.collisionON = true;
                    }
                }
                break;
            case "down":
                entityBottomRow = (int) ((entityBottomY + entity.velocityXY[1]) / gp.tileSize);
                if (entityBottomRow >= 0 && entityBottomRow < gp.maxScreenRow) {
                    tileNum1 = gp.blockManager.mapBlockNum[entityLeftCol][entityBottomRow];
                    tileNum2 = gp.blockManager.mapBlockNum[entityRightCol][entityBottomRow];
                    if (gp.blockManager.block[tileNum1].collision || gp.blockManager.block[tileNum2].collision) {
                        entity.collisionON = true;
                    }
                }
                break;
            case "left":
                entityLeftCol = (int) ((entityLeftX + entity.velocityXY[0]) / gp.tileSize);
                if (entityLeftCol >= 0 && entityLeftCol < gp.maxScreenCol) {
                    tileNum1 = gp.blockManager.mapBlockNum[entityLeftCol][entityTopRow];
                    tileNum2 = gp.blockManager.mapBlockNum[entityLeftCol][entityBottomRow];
                    if (gp.blockManager.block[tileNum1].collision || gp.blockManager.block[tileNum2].collision) {
                        entity.collisionON = true;
                    }
                }
                break;
            case "right":
                entityRightCol = (int) ((entityRightX + entity.velocityXY[0]) / gp.tileSize);
                if (entityRightCol >= 0 && entityRightCol < gp.maxScreenCol) {
                    tileNum1 = gp.blockManager.mapBlockNum[entityRightCol][entityTopRow];
                    tileNum2 = gp.blockManager.mapBlockNum[entityRightCol][entityBottomRow];
                    if (gp.blockManager.block[tileNum1].collision || gp.blockManager.block[tileNum2].collision) {
                        entity.collisionON = true;
                    }
                }
                break;
        }
    }
}