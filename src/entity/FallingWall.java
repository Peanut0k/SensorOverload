package entity;

import main.GamePanel;

import java.awt.*;
import java.util.Random;

public class FallingWall extends Entity {

    GamePanel gp;
    double difficulty;

    public FallingWall(GamePanel gp, double difficulty) {
        this.gp = gp;
        this.difficulty = difficulty /10;
        setDefaults();
    }

    @Override
    void setDefaults() {
        Random rand = new Random();
        this.coordXY = new int[]{rand.nextInt(0,gp.screenWidth), rand.nextInt(0,gp.screenHeight /2)};
        this.velocityXY = new double[]{rand.nextDouble(0,10.0 + difficulty/ 1.5), rand.nextDouble(5, 10 + difficulty /1.5)};
    }

    public void update() {
        coordXY[0] += velocityXY[0];
        coordXY[1] += velocityXY[1];
    }
    public void draw(Graphics2D g2) {
        g2.setColor(Color.red);
        g2.fillRect(coordXY[0], coordXY[1], gp.tileSize *2, gp.tileSize *2);
//        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight /2);
    }
}
