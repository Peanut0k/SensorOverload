package main;

import entity.FallingWall;
import entity.Player;
import manager.ChaosController;
import manager.GameState;
import manager.ScreenEffects;
import manager.WallManager;

import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.geom.AffineTransform;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static manager.GameState.time;

public class GamePanel extends JPanel implements Runnable {

    // screen settings
    public final int originalTileSize = 32;
    final int tileScale = 1;
    final int screenScale = 4;
    final int[] resolution = {16,9};
    int FPS = 165;

    int maxEntities = 100;
    double intensity = 0;

    public final int tileSize = originalTileSize * tileScale;

    public final int screenHeight = originalTileSize * screenScale * resolution[1];
    public final int screenWidth = originalTileSize * screenScale * resolution[0];
    Image backgroundGif;

    double speedCorrection = (double) 60 / FPS;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyHandler, speedCorrection);
    GameState gameState = new GameState(intensity, FPS, speedCorrection);
    ChaosController chaosController = new ChaosController();
    ScreenEffects screenEffects = new ScreenEffects(chaosController, screenWidth, screenHeight);

    List<FallingWall> entities = new ArrayList<>();
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

        backgroundGif = new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/bg.gif"))).getImage();
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
        gameState.update(dt);
        chaosController.update();
        screenEffects.update(dt);
        player.update();
        wallManager.update(dt);

            for (FallingWall entity : entities) {
                entity.update();
            }
            if (entities.size() > maxEntities) {
                entities.remove(0);
            }

    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform originalTransform = g2.getTransform();
        screenEffects.applyScreenEffects(g2);
        g2.drawImage(backgroundGif, 0, 0, screenWidth, screenHeight, null);
        player.draw(g2);
            for (FallingWall entity : entities) {
                entity.draw(g2);
            }
        screenEffects.resetTransform(g2, originalTransform);
        gameState.draw(g2);
        wallManager.draw(g2);
        g2.dispose();
    }

    private void drawBackground(Graphics2D g2) {
        Color[] colors = getPhaseColors();
        Color darkStrip = colors[0];
        Color lightStrip = colors[1];
        Color accentColor = colors[2];

        double t = time;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        g2.setColor(darkStrip);
        g2.fillRect(-1000, -1000, screenWidth+1000, screenHeight+1000);

        g2.setColor(lightStrip);
        int stripWidth = 60;

        double scrollOffset = (t * 50) % (stripWidth * 2);

        for (double x = -scrollOffset - (stripWidth * 2); x < screenWidth + (stripWidth * 2); x += stripWidth * 2) {
            Path2D.Double path = new Path2D.Double();

            for (int y = 0; y <= screenHeight + 120; y += 20) {
                double warp = Math.sin(y * 0.015 + t * 2.5) * 40;
                if (y == 0) {
                    path.moveTo(x + warp, y);
                } else {
                    path.lineTo(x + warp, y);
                }
            }

            for (int y = screenHeight + 20; y >= 0; y -= 20) {
                double warp = Math.sin(y * 0.015 + t * 2.5) * 40;
                path.lineTo(x + stripWidth + warp, y);
            }

            path.closePath();
            g2.fill(path);
        }

        g2.setColor(accentColor);
        int gridSize = 120;

        for (int x = -gridSize; x <= screenWidth + gridSize; x += gridSize) {
            for (int y = -gridSize; y <= screenHeight + gridSize; y += gridSize) {

                double localT = t + (x * 0.005) + (y * 0.005);

                double size = 25 + Math.sin(localT * 3) * 15;

                double driftX = Math.cos(localT * 1.5) * 30;
                double driftY = Math.sin(localT * 2.0) * 30;

                AffineTransform oldTransform = g2.getTransform();

                g2.translate(x + driftX, y + driftY);
                g2.rotate(localT * 1.2);

                g2.fillRoundRect((int)(-size / 2), (int)(-size / 2), (int)size, (int)size, 15, 15);

                g2.setTransform(oldTransform);
            }
        }
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
