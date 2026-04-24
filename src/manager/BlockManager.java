package manager;

import Blocks.Block;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BlockManager {
    GamePanel gp;
    Block[] block;
    public int[][] mapBlockNum;

    public BlockManager(GamePanel gp) {
        this.gp = gp;
        block = new Block[10];
        mapBlockNum = new int[gp.maxScreenCol][gp.maxScreenRow];
        loadBlockImages();
        loadMap();
    }

    public void loadBlockImages() {
        try {
            block[1] = new Block();
            block[1].image = ImageIO.read(getClass().getResourceAsStream("/Rock.png"));
            block[1].collision = true;

            block[0] = new Block();
            block[0].image = ImageIO.read(getClass().getResourceAsStream("/Sky.png"));
        } catch (Exception e) {
            System.err.println("Failed to load block texture: " + e.getMessage());
        }
    }

    public void loadMap() {
        try {
            InputStream is = getClass().getResourceAsStream("/map.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int row = 0;
            String line;

            while ((line = br.readLine()) != null && row < gp.maxScreenRow) {
                String[] numbers = line.split(" ");

                for (int col = 0; col < gp.maxScreenCol && col < numbers.length; col++) {
                    int num = Integer.parseInt(numbers[col]);
                    mapBlockNum[col][row] = num;
                }
                row++;
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void draw(Graphics2D g2) {
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
            int blockNum = mapBlockNum[col][row];
            if (block[blockNum] != null && block[blockNum].image != null) {
                g2.drawImage(block[blockNum].image, x, y, gp.tileSize, gp.tileSize, null);
            }
            col++;
            x += gp.tileSize;

            if (col == gp.maxScreenCol) {
                row++;
                col = 0;
                x = 0;
                y += gp.tileSize;
            }
        }
    }
}
