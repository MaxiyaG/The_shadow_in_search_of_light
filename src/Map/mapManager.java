package Map;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class mapManager {
    GamePanel gamePanel;
    public map[] map;
    public int[][] tileNum;


    public mapManager(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        map = new map[3]; // Kind tiles
        tileNum = new int[gamePanel.maxScreenRow][gamePanel.maxScreenCol];
        getTileImage();
        loadMap("map.txt");
    }

    public void getTileImage(){
        try {
            map[0] = new map();
            map[0].image = ImageIO.read(getClass().getResourceAsStream("/Image/tiles/grass.png"));

            map[1] = new map();
            map[1].image = ImageIO.read(getClass().getResourceAsStream("/Image/tiles/wall.png"));
            map[1].collision = true;

            map[2] = new map();
            map[2].image = ImageIO.read(getClass().getResourceAsStream("/Image/tiles/tree.png"));
            map[2].collision = true;

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void loadMap(String path){
        try {
            InputStream is = getClass().getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int row = 0;
            String line;
            while ((line = br.readLine()) != null && row < gamePanel.maxScreenRow) {
                String[] numbers = line.split(" ");
                for (int col = 0; col < numbers.length && col < gamePanel.maxScreenCol; col++) {
                    tileNum[row][col] = Integer.parseInt(numbers[col]);
                }
                row++;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isWallTile(int row, int col) {
        int numTile = tileNum[row][col];
        return numTile == 1; // Si le numéro de tuile correspond à un mur
    }


    public void draw(Graphics2D g2) {
        int tileWidth = gamePanel.titeSize;
        int tileHeight = gamePanel.titeSize;

        for (int row = 0; row < gamePanel.maxScreenRow; row++) {
            for (int col = 0; col < gamePanel.maxScreenCol; col++) {
                int x = col * tileWidth;
                int y = row * tileHeight;
                int numTile = tileNum[row][col]; // Get the tile number

                // Ensure that the tile number is within the range of the map array
                if (numTile >= 0 && numTile < map.length) {
                    g2.drawImage(map[numTile].image, x, y, tileWidth, tileHeight, null);
                }
            }
        }
    }
}
