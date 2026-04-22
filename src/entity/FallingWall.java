package entity;

import main.GamePanel;

import java.awt.*;
import java.util.Random;

public class FallingWall extends Entity {

    GamePanel gp;
    double speedCorrection;
    double speedMultiplier;
    public Rectangle collisionBox;

    public FallingWall(GamePanel gp, double speedCorrection, double speedMultiplier) {
        this.gp = gp;
        this.speedCorrection = speedCorrection;
        this.speedMultiplier = speedMultiplier;
        setDefaults();
    }

    @Override
    void setDefaults() {
        Random rand = new Random();
        this.coordXY = new int[]{rand.nextInt(0,gp.screenWidth), rand.nextInt(0,gp.screenHeight /2)};
        this.velocityXY = new double[]{rand.nextDouble(-10 * speedCorrection * speedMultiplier,10.0 * speedCorrection * speedMultiplier), rand.nextDouble(5 * speedCorrection * speedMultiplier, 10 * speedCorrection * speedMultiplier)};
        this.collisionBox = new Rectangle(this.coordXY[0], this.coordXY[1], gp.tileSize, gp.tileSize);
    }

    public void update() {
        coordXY[0] += velocityXY[0];
        coordXY[1] += velocityXY[1];
        collisionBox.x = this.coordXY[0];
        collisionBox.y = this.coordXY[1];
    }
    public void draw(Graphics2D g2) {
        g2.setColor(Color.red);
        g2.fillRect(coordXY[0], coordXY[1], gp.tileSize *2, gp.tileSize *2);
    }
}
