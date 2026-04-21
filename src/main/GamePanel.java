package main;

import javax.swing.JPanel;
import java.awt.*;
import java.util.Arrays;

public class GamePanel extends JPanel implements Runnable {

    // screen settings
    final int originalTileSize = 32;
    final int tileScale = 1;
    final int screenScale = 2;
    final int[] resolution = {16,9};
    int FPS = 60;

    // player settings
    int[] playerCoordsXY = {100,100};
    double[] playerVelocityXY = {0,0};
    double playerMaxVelocity = 6;
    double acceleration = 0.3;
    double stoppingPower = 0.3;

    final int tileSize = originalTileSize * tileScale;
    final int maxScreenRow = resolution[0] * screenScale;
    final int maxScreenCol = resolution[1] * screenScale;

    final int screenHeight = originalTileSize * screenScale * maxScreenCol;
    final int screenWidth = originalTileSize * screenScale * maxScreenRow;

    KeyHandler  keyHandler = new KeyHandler();
    Thread gameThread;


    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }
    public void update() {

        double inputX = 0;
        double inputY = 0;

        if (keyHandler.leftPressed)  inputX = -1;
        if (keyHandler.rightPressed) inputX =  1;
        if (keyHandler.upPressed)    inputY = -1;
        if (keyHandler.downPressed)  inputY =  1;

        playerVelocityXY[0] += inputX * acceleration;
        playerVelocityXY[1] += inputY * acceleration;

        if (playerVelocityXY[0] > playerMaxVelocity)
            playerVelocityXY[0] = playerMaxVelocity;
        if (playerVelocityXY[0] < -playerMaxVelocity)
            playerVelocityXY[0] = -playerMaxVelocity;

        if (playerVelocityXY[1] > playerMaxVelocity)
            playerVelocityXY[1] = playerMaxVelocity;
        if (playerVelocityXY[1] < -playerMaxVelocity)
            playerVelocityXY[1] = -playerMaxVelocity;

        if (inputX == 0) {
            playerVelocityXY[0] = approachZero(playerVelocityXY[0], stoppingPower);
        }

        if (inputY == 0) {
            playerVelocityXY[1] = approachZero(playerVelocityXY[1], stoppingPower);
        }

        if (Math.abs(playerVelocityXY[0]) < 0.03f)
            playerVelocityXY[0] = 0;

        if (Math.abs(playerVelocityXY[1]) < 0.03f)
            playerVelocityXY[1] = 0;

        playerCoordsXY[0] += playerVelocityXY[0];
        playerCoordsXY[1] += playerVelocityXY[1];

        if (playerCoordsXY[0] < 0) {
            playerCoordsXY[0] = 0;
        }
        if (playerCoordsXY[0] > screenWidth - originalTileSize) {
            playerCoordsXY[0] = screenWidth - originalTileSize;
        }

        if (playerCoordsXY[1] < 0) {
            playerCoordsXY[1] = 0;
        }
        if (playerCoordsXY[1] > screenHeight - originalTileSize) {
            playerCoordsXY[1] = screenHeight - originalTileSize;
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.white);
        g2.fillRect(playerCoordsXY[0], playerCoordsXY[1], tileSize, tileSize);
        g2.drawString(Arrays.toString(playerCoordsXY), 100, 100);
        g2.dispose();
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