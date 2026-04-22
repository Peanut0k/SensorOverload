package main;

import entity.Entity;
import entity.Player;
import manager.*;

import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class GamePanel extends JPanel implements Runnable {

    // screen settings
    public final int originalTileSize = 32;
    final double tileScale = 1.2;
    final int screenScale = 5;
    final int[] resolution = {16,9};
    int FPS = 90;
    double bgTimer = 0;

    int maxEntities = 100;
    double intensity = 1;

    public final int tileSize = (int) (originalTileSize * tileScale);

    public final int screenHeight = originalTileSize * screenScale * resolution[1];
    public final int screenWidth = originalTileSize * screenScale * resolution[0];

    double speedCorrection = (double) 60 / FPS;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;
    public Player player = new Player(this, keyHandler, speedCorrection);
    ChaosController chaosController = new ChaosController();
    GameState gameState = new GameState(intensity, FPS, speedCorrection, chaosController);
    ScreenEffects screenEffects = new ScreenEffects(chaosController, screenWidth, screenHeight);

    Color[] colors = getPhaseColors();

    public List<Entity> entities = new ArrayList<>();
    WallManager wallManager = new WallManager(entities, this, speedCorrection, chaosController);

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

        double drawInterval = 1000000000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update((double) 1 /FPS);
                repaint();
                delta--;
            }
        }
    }
    public void update(double dt) {
        if (false) {
            if (keyHandler.rPressed) {
                resetGame();
            }
            return;
        }

        // Use the controller's specific illusion speed
        bgTimer += chaosController.illusionSpeed * dt * 5;

        colors = getPhaseColors();
        gameState.update(dt);
        chaosController.update();
        screenEffects.update(dt);
        player.update();
        wallManager.update(dt);

        for (Entity entity : entities) {
            entity.update();
        }
        if (entities.size() > maxEntities) {
            entities.remove(0);
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // SPEED OPTIMIZATIONS
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);

        AffineTransform originalTransform = g2.getTransform();
        screenEffects.applyScreenEffects(g2);
        drawBackground(g2);

        player.draw(g2);
            for (Entity entity : entities) {
                entity.draw(g2);
            }
        screenEffects.resetTransform(g2, originalTransform);
        gameState.draw(g2);
        if (GameState.gameOver) {
            g2.setColor(Color.RED);
            g2.setFont(new Font("Times New Roman", Font.BOLD, 60));
            g2.drawString("GAME OVER", (screenWidth -350) /2, screenHeight /2);
        }
        g2.dispose();
    }

    private void drawBackground(Graphics2D g2) {
        // 1. Calculate the diagonal to ensure the screen is ALWAYS covered during rotation
        int bufferSize = (int) Math.sqrt(screenWidth * screenWidth + screenHeight * screenHeight) + 200;
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        // 25 layers is the "sweet spot" for a dense look without killing the FPS
        int totalLayers = 40;

        for (int i = 0; i < totalLayers; i++) {
            // Alternate colors
            g2.setColor((i % 2 == 0) ? colors[0] : colors[1]);

            // Perspective scaling: squares get smaller as they go "deeper"
            double ratio = (double) (totalLayers - i) / totalLayers;
            int size = (int) (bufferSize * ratio);

            // ILLUSION MATH
            // We use different frequencies for X and Y to create a "figure-8" or "oval" path
            double intensityFactor = chaosController.intensity * 0.1;
            double speed = bgTimer * (1 + intensityFactor);
            double layerOffset = i * 0.2; // The "snake" delay between squares

            // Vertical and Horizontal movement of the illusion tunnel
            int offsetX = (int) (Math.sin(speed + layerOffset) * chaosController.illusionAmplitude);
            int offsetY = (int) (Math.cos(speed * 0.8 + layerOffset) * (chaosController.illusionAmplitude * 0.6));

            // Center the square and apply the sway
            int x = (centerX - size / 2) + offsetX;
            int y = (centerY - size / 2) + offsetY;

            g2.fillRect(x, y, size, size);
        }
    }

    public void resetGame() {
        GameState.gameOver = false;
        entities.clear();
        chaosController = new ChaosController();
        gameState = new GameState(intensity, FPS, speedCorrection, chaosController);
        screenEffects = new ScreenEffects(chaosController, screenWidth, screenHeight);
    }

    private Color[] getPhaseColors() {
        return switch (chaosController.phase) {
            case 1 -> new Color[]{
                    new Color(20, 30, 50),      // Dark blue
                    new Color(40, 50, 80),      // Light blue
                    new Color(30, 50, 100, 80)  // Blue accent
            };
            case 2 -> new Color[]{
                    new Color(35, 20, 50),      // Dark purple
                    new Color(60, 40, 80),      // Light purple
                    new Color(80, 40, 120, 80)  // Purple accent
            };
            case 3 -> new Color[]{
                    new Color(50, 20, 40),      // Dark magenta
                    new Color(90, 40, 70),      // Light magenta
                    new Color(150, 50, 100, 80) // Magenta accent
            };
            case 4 -> new Color[]{
                    new Color(80, 20, 30),      // Dark red
                    new Color(140, 50, 60),     // Light red
                    new Color(200, 70, 80, 80)  // Red accent
            };
            case 5 -> new Color[]{
                    new Color(100, 10, 50),     // Dark magenta-red
                    new Color(180, 30, 100),    // Light magenta-red
                    new Color(255, 100, 150, 100) // Bright magenta accent
            };
            default -> new Color[]{
                    new Color(120, 10, 60),     // Extreme dark magenta
                    new Color(220, 50, 120),    // Extreme bright magenta
                    new Color(255, 150, 200, 120) // Extreme accent
            };
        };
    }
}
