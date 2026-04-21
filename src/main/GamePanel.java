package main;

import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    // screen settings
    final int originalTileSize = 32;
    final int scale = 4;

    final int tileSize = originalTileSize * scale;
    final int maxScreenRow = 16;
    final int maxScreenCol = 9;

    final int screenHeight = tileSize * maxScreenCol;
    final int screenWidth = tileSize * maxScreenRow;

    Thread gameThread;

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
    }

    @Override
    public void run() {

    }
}
