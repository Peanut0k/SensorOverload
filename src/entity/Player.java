package entity;

import main.GamePanel;
import main.KeyHandler;

import java.awt.*;
import java.util.Arrays;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyHandler;
    private double maxVelocity;
    private double acceleration;
    private double stoppingPower;

    public Player(GamePanel gp, KeyHandler keyHandler) {
        this.gp = gp;
        this.keyHandler = keyHandler;
        setDefaults();
    }


    @Override
    void setDefaults() {
        this.coordXY = new int[]{gp.screenWidth/2 - gp.tileSize /2, (int) (gp.screenHeight /1.25)};
        this.velocityXY = new double[]{0, 0};
        this.maxVelocity = 10;
        this.acceleration = 0.9;
        this.stoppingPower = 0.8;
    }

    public void update() {

        double inputX = 0;
        double inputY = 0;

        if (keyHandler.leftPressed)  inputX = -1;
        if (keyHandler.rightPressed) inputX =  1;
        if (keyHandler.upPressed)    inputY = -1;
        if (keyHandler.downPressed)  inputY =  1;

        velocityXY[0] += inputX * acceleration;
        velocityXY[1] += inputY * acceleration;

        if (velocityXY[0] > maxVelocity)
            velocityXY[0] = maxVelocity;
        if (velocityXY[0] < -maxVelocity)
            velocityXY[0] = -maxVelocity;

        if (velocityXY[1] > maxVelocity)
            velocityXY[1] = maxVelocity;
        if (velocityXY[1] < -maxVelocity)
            velocityXY[1] = -maxVelocity;

        if (inputX == 0) {
            velocityXY[0] = approachZero(velocityXY[0], stoppingPower);
        }

        if (inputY == 0) {
            velocityXY[1] = approachZero(velocityXY[1], stoppingPower);
        }

        if (Math.abs(velocityXY[0]) < 0.03f)
            velocityXY[0] = 0;

        if (Math.abs(velocityXY[1]) < 0.03f)
            velocityXY[1] = 0;

        coordXY[0] += velocityXY[0];
        coordXY[1] += velocityXY[1];

        if (coordXY[0] < 0) {
            coordXY[0] = 0;
        }
        if (coordXY[0] > gp.screenWidth - gp.originalTileSize) {
            coordXY[0] = gp.screenWidth - gp.originalTileSize;
        }

        if (coordXY[1] < 0) {
            coordXY[1] = 0;
        }
        if (coordXY[1] > gp.screenHeight - gp.originalTileSize) {
            coordXY[1] = gp.screenHeight - gp.originalTileSize;
        }
    }
    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.fillRect(coordXY[0], coordXY[1], gp.tileSize, gp.tileSize);
        g2.drawString(Arrays.toString(coordXY), 100, (int) (gp.screenHeight / 1.5));
    }

    private double approachZero(double value, double amount) {
        if (value > 0) {
            value -= amount;
            if (value < 0) return 0;
        } else if (value < 0) {
            value += amount;
            if (value > 0) return 0;
        }
        return value;
    }
}
