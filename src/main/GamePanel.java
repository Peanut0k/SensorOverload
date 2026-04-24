package main;

import entity.Entity;
import entity.Player;
import manager.*;

import java.awt.RenderingHints;
import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static entity.Player.State.IDLE;


public class GamePanel extends JPanel implements Runnable {

    // screen settings
    public final int originalTileSize = 32;
    final double tileScale = 1.25;
    final int screenScale = 5;
    final int[] resolution = {16,9};
    int FPS = 120;

    int maxEntities = 100;

    public final int tileSize = (int) (originalTileSize * tileScale);

    public final int screenHeight = originalTileSize * screenScale * resolution[1];
    public final int screenWidth = originalTileSize * screenScale * resolution[0];

    public final int maxScreenCol = screenWidth / tileSize;
    public final int maxScreenRow = screenHeight / tileSize;

    double speedCorrection = (double) 60 / FPS;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;
    public Player player = new Player(this, keyHandler);
    GameState gameState = new GameState(FPS, speedCorrection);
    public BlockManager blockManager = new BlockManager(this);
    public CollisionManager collisionManager = new CollisionManager(this);

    public List<Entity> entities = new ArrayList<>();

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
        player.update();
        if (GameState.gameOver) {
            if (keyHandler.rPressed) {
                resetGame();
            }
            return;
        }

        gameState.update(dt);

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

        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);


        blockManager.draw(g2);
        player.draw(g2);
        gameState.draw(g2);
        if (GameState.gameOver) {
            g2.setColor(Color.RED);
            g2.setFont(new Font("Times New Roman", Font.BOLD, 60));
            g2.drawString("GAME OVER", (screenWidth -350) /2, screenHeight /2);
        }
        g2.dispose();
    }

    private void drawBackground(Graphics2D g2) {
        g2.setColor(Color.green);
        g2.fillRect(0, screenHeight - screenHeight /4, screenWidth, screenHeight/4);
    }

    public void resetGame() {
        GameState.gameOver = false;
        player.currentState = IDLE;
        entities.clear();
        gameState = new GameState(FPS, speedCorrection);
    }
}
