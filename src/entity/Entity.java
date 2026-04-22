package entity;

import java.awt.*;

public abstract class Entity {
    public int[] coordXY;
    public double[] velocityXY;
    public Rectangle collisionBox =  new Rectangle();



    abstract void setDefaults();

    public abstract void update();

    public abstract void draw(Graphics2D g2);
}
