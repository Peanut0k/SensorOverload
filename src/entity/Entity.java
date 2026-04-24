package entity;

import java.awt.*;

public abstract class Entity {
    public int[] coordXY;
    public double[] velocityXY;
    public Rectangle collisionBox;
    public double hp;
    public double gravity;
    public boolean collisionON =  false;
    public String direction;
    public boolean isGrounded;

    abstract void setDefaults();

    public abstract void update();

    public abstract void draw(Graphics2D g2);
}
