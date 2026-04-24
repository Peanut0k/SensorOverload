package entity;

import main.GamePanel;
import main.KeyHandler;
import manager.GameState;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import javax.imageio.ImageIO;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyHandler;
    private BufferedImage spriteSheet;

    private static final int SPRITE_WIDTH = 32;
    private static final int SPRITE_HEIGHT = 48;
    private static final int IDLE_SPRITES = 8;
    private static final int RUNNING_SPRITES = 6;
    private static final int DEATH_SPRITES = 2;
    private static final int SPRITE_SCALE = 3;

    private static final double ACCELERATION = 0.5;
    private static final double MAX_VELOCITY = 8;
    private static final double STOPPING_VELOCITY = 1;
    private static final double GRAVITY = 0.45;
    private static final double JUMP_FORCE = -10;
    private static final double MAX_FALLING_SPEED = 20;

    public enum State { IDLE, RUNNING, DEAD }
    public State currentState = State.IDLE;
    private int animationFrame = 0;
    private double animationTimer = 0;
    private static double animationSpeed = 0.05;
    private int facingDirection = 1;

    public boolean isGrounded = false;

    public Player(GamePanel gp, KeyHandler keyHandler) {
        this.gp = gp;
        this.keyHandler = keyHandler;
        setDefaults();
    }

    @Override
    void setDefaults() {
        int scaledWidth = SPRITE_WIDTH * SPRITE_SCALE;
        int scaledHeight = SPRITE_HEIGHT * SPRITE_SCALE;
        this.coordXY = new int[]{gp.screenWidth /2, 0};
        this.velocityXY = new double[]{0, 0};
        this.collisionBox = new Rectangle(coordXY[0], coordXY[1], gp.tileSize, SPRITE_HEIGHT + 4);
        loadPlayerImage();
    }

    private void loadPlayerImage() {
        try {
            var stream = getClass().getResourceAsStream("/CAT 1.png");
            if (stream != null) {
                spriteSheet = ImageIO.read(stream);
            }
        } catch (Exception e) {
            System.err.println("Failed to load player sprite sheet: " + e.getMessage());
            spriteSheet = null;
        }
    }

    private BufferedImage getSprite(int spriteIndex) {
        if (spriteSheet == null) return null;

        BufferedImage sprite = new BufferedImage(SPRITE_WIDTH, SPRITE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        int col = spriteIndex % (spriteSheet.getWidth() / SPRITE_WIDTH);
        int row = spriteIndex / (spriteSheet.getWidth() / SPRITE_WIDTH);

        for (int y = 0; y < SPRITE_HEIGHT; y++) {
            for (int x = 0; x < SPRITE_WIDTH; x++) {
                int px = col * SPRITE_WIDTH + x;
                int py = row * SPRITE_HEIGHT + y;

                if (px >= 0 && px < spriteSheet.getWidth() && py >= 0 && py < spriteSheet.getHeight()) {
                    sprite.setRGB(x, y, spriteSheet.getRGB(px, py));
                }
            }
        }
        return sprite;
    }

    public void update() {

        double inputX = 0;

        if (keyHandler.leftPressed) {
            inputX = -1;
            facingDirection = -1;
            direction = "left";
        }
        if (keyHandler.rightPressed) {
            inputX = 1;
            facingDirection = 1;
            direction = "right";
        }
        if (keyHandler.spacePressed) {
            direction = "up";
        }
        else {
            direction = "down";
        }

        velocityXY[0] += inputX * ((isGrounded) ? ACCELERATION : ACCELERATION * 0.3);

        if (velocityXY[0] > MAX_VELOCITY)
            velocityXY[0] = MAX_VELOCITY;
        if (velocityXY[0] < -MAX_VELOCITY)
            velocityXY[0] = -MAX_VELOCITY;

        if (inputX == 0) {
            velocityXY[0] = approachZero(velocityXY[0],(isGrounded) ? STOPPING_VELOCITY : STOPPING_VELOCITY / 10);
        }

        if (Math.abs(velocityXY[0]) < 0.03f)
            velocityXY[0] = 0;

        velocityXY[1] += GRAVITY;


        if (velocityXY[1] > MAX_FALLING_SPEED)
            velocityXY[1] = MAX_FALLING_SPEED;


        if (keyHandler.spacePressed && isGrounded && currentState != State.DEAD) {
            velocityXY[1] = JUMP_FORCE;
            isGrounded = false;
            facingDirection = (velocityXY[0] > 0) ? 1 : -1 ;
            animationFrame = 0;
        }

        gp.collisionManager.checkBlock(this);


        direction = (velocityXY[1] < 0) ? "up" : "down";
        gp.collisionManager.checkBlock(this);
        if (!collisionON) {
            coordXY[1] += velocityXY[1];
        } else {
            if (direction.equals("down")) {
                velocityXY[1] = 0;
                isGrounded = true;
            }
        }

        direction = (velocityXY[0] < 0) ? "left" : "right";
        gp.collisionManager.checkBlock(this);
        if (!collisionON) {
            coordXY[0] += velocityXY[0];
        } else {
            velocityXY[0] = 0;
        }


        int scaledWidth = SPRITE_WIDTH * SPRITE_SCALE;
        int scaledHeight = SPRITE_HEIGHT * SPRITE_SCALE;

        if (coordXY[0] < -8)
            coordXY[0] = -8;
        if (coordXY[0] > gp.screenWidth - scaledWidth +8)
            coordXY[0] = gp.screenWidth - scaledWidth +8;

        if (coordXY[1] < -8)
            coordXY[1] = -8;
        if (coordXY[1] > gp.screenHeight - scaledHeight + 18) {
            coordXY[1] = gp.screenHeight - scaledHeight + 18;
            isGrounded = true;
            velocityXY[1] = 0;
        }

        collisionBox.x = coordXY[0] + SPRITE_WIDTH - 6;
        collisionBox.y = coordXY[1] +  SPRITE_HEIGHT + 16;
        collisionBox.width = gp.tileSize;

        if (currentState != State.DEAD) {
            if (Math.abs(velocityXY[0]) > 0.5) {
                currentState = State.RUNNING;
                animationSpeed = (isGrounded) ? 0.05 : 0.1 ;
            } else {
                currentState = (isGrounded) ? State.IDLE : State.RUNNING;
                animationSpeed = 0.1;
            }
        }

    animationTimer += 1.0 / GameState.FPS;
        if (animationTimer >= animationSpeed) {
            animationTimer = 0;

            int maxFrames = (currentState == State.IDLE) ? IDLE_SPRITES :
                    (currentState == State.RUNNING) ? RUNNING_SPRITES :
                            DEATH_SPRITES;

            animationFrame++;
            if (animationFrame >= maxFrames) {
                animationFrame = 0;
            }
        }
    }
    public void draw(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.draw(collisionBox);
        if (spriteSheet != null) {
            int spriteIndex = animationFrame;

            if (currentState == State.RUNNING) {
                spriteIndex = IDLE_SPRITES + animationFrame;
            } else if (currentState == State.DEAD) {
                spriteIndex = IDLE_SPRITES + RUNNING_SPRITES + animationFrame;
            }

            BufferedImage currentSprite = getSprite(spriteIndex);
            if (currentSprite != null) {
                int scaledWidth = SPRITE_WIDTH * SPRITE_SCALE;
                int scaledHeight = SPRITE_HEIGHT * SPRITE_SCALE;

                AffineTransform originalTransform = g2.getTransform();
                AffineTransform transform = new AffineTransform();

                if (facingDirection == -1) {
                    transform.translate(coordXY[0] + scaledWidth, coordXY[1]);
                    transform.scale(-1, 1);
                } else {
                    transform.translate(coordXY[0], coordXY[1]);
                }

                g2.setTransform(transform);
                g2.drawImage(currentSprite, 0, 0, scaledWidth, scaledHeight, null);
                g2.setTransform(originalTransform);
            }
        }
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
