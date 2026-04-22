package main;

import entity.FallingWall;
import entity.Player;
import entity.spawner.WallManager;

import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements Runnable {

    // screen settings
    public final int originalTileSize = 32;
    final int tileScale = 1;
    final int screenScale = 4;
    final int[] resolution = {16,9};
    int FPS = 160;

    int maxEntities = 100;
    double difficulty = 20;

    public final int tileSize = originalTileSize * tileScale;
    final int maxScreenRow = resolution[0] * screenScale;
    final int maxScreenCol = resolution[1] * screenScale;

    public final int screenHeight = originalTileSize * screenScale * resolution[1];
    public final int screenWidth = originalTileSize * screenScale * resolution[0];

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyHandler);

    List<FallingWall> entities = new ArrayList<>();
    WallManager spawner = new WallManager(entities, this, difficulty);

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
                update((double) 1 /FPS);
                repaint();
                delta--;
            }
        }
    }
    public void update(double dt) {
        player.update();
        spawner.update(dt);
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
        player.draw(g2);
        for (FallingWall entity : entities) {
            entity.draw(g2);
        }
        spawner.draw(g2);
        g2.dispose();
    }
}
