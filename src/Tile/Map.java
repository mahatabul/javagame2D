package Tile;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Map extends TileManager{
    GamePanel gamePanel;
    BufferedImage[] worldMap;
    public boolean miniMapOn = false;

    public Map(GamePanel gp){
        super(gp);
        this.gamePanel = gp;
    }
    public void createWorldMap(){
        worldMap = new BufferedImage[gamePanel.tileManager.maps.length];
        int worldMapWidth = gamePanel.worldTotalWidth;
        int worldMapHeight = gamePanel.worldTotalHeight;

        for(int i = 0;   i < gamePanel.tileManager.maps.length; i++){
            worldMap[i] = new BufferedImage(worldMapWidth, worldMapHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = (Graphics2D) worldMap[i].createGraphics();

            int col = 0;int row = 0;

            while (col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow){
                int tilenum = gamePanel.tileManager.mapTileNumber[col][row]; // issues
            }
        }
    }
}
