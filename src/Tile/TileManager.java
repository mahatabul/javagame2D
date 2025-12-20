package Tile;

import Main.GamePanel;
import Main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class TileManager {
    GamePanel gamePanel;
    public Tiles[] tiles;

    public int[][] mapTileNumber;
    public String[] maps;
    public int currentMapIdx;

    ArrayList<String> fileName = new ArrayList<>();
    ArrayList<String> collisionStatus = new ArrayList<>();

    //area bound
    public int[][] areaBounds = new int[4][4];

    public TileManager(GamePanel gp) {
        this.gamePanel = gp;
        
        // reading tile collision data
        InputStream is = getClass().getResourceAsStream("/Collision Data/collision.txt");
        assert is != null;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        // getting tile names and collision info fron file
        String line;

        try{
            while((line = br.readLine()) != null){
                fileName.add(line);
                collisionStatus.add(br.readLine());
            }
            br.close();
        }catch(Exception e){
            throw new RuntimeException(e);
        }

        // initialize tile array
        tiles = new Tiles[fileName.size()];
        getTileImage();

        // reading one line to get max tile numbers
        is = getClass().getResourceAsStream("/Mapping Data/forest.txt");
        assert is != null;
        br = new BufferedReader(new InputStreamReader(is));


        try{
            line = br.readLine();
            String[] maxTile = line.split(" ");
            gamePanel.maxWorldCol = gamePanel.maxWorldRow = maxTile.length;
            br.close();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        
        mapTileNumber = new int[gamePanel.maxWorldCol][gamePanel.maxWorldRow];

        // storing map names
        maps = new String[1];

        maps[0] = "/Mapping Data/forest.txt";

        loadMap(maps[0]);
        currentMapIdx = 0;

        //setting area bounds
        setAreaBounds();
    }

    public void getTileImage() {

        for(int i = 0; i < fileName.size(); ++i){
            String file;
            boolean collision;

            file = fileName.get(i);
            collision = !collisionStatus.get(i).equals("false");

            setUp(i, file, collision);
        }
    }

    public void setUp(int index, String imagePath, boolean collisionON) {
        UtilityTool utool = new UtilityTool();
        try {
            tiles[index] = new Tiles();
            //tiles[index].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Tiles/Roswaal Mansion Outside/" + imagePath + ".png")));
            tiles[index].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Tiles/" + imagePath)));
            tiles[index].image = utool.scaleImage(tiles[index].image, gamePanel.finalTileSize, gamePanel.finalTileSize);
            tiles[index].collision = collisionON;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void loadMap(String mapPath) {
        try {
            InputStream is = getClass().getResourceAsStream(mapPath);
            assert is != null;
            BufferedReader buffRdr = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow) {
                String line = buffRdr.readLine();
                while (col < gamePanel.maxWorldCol) {
                    String[] numbers = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNumber[col][row] = num;
                    col++;
                }
                if (col == gamePanel.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            buffRdr.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(Graphics2D g2d) {
        int worldCol, worldRow, worldX, worldY, scrX, scrY;
        worldCol = worldRow = 0;

        while (worldCol < gamePanel.maxWorldCol && worldRow < gamePanel.maxWorldRow) {
            int tileNum = mapTileNumber[worldCol][worldRow];

            worldX = gamePanel.finalTileSize * worldCol;
            worldY = gamePanel.finalTileSize * worldRow;
            scrX = worldX - gamePanel.player.entityWorldXPos + gamePanel.player.scrXpos;
            scrY = worldY - gamePanel.player.entityWorldYPos + gamePanel.player.scrYpos;

            // stop moving the camera at the edges
            if (gamePanel.player.scrXpos > gamePanel.player.entityWorldXPos) {
                scrX = worldX;
            }
            if (gamePanel.player.scrYpos > gamePanel.player.entityWorldYPos) {
                scrY = worldY;
            }

            int rightOffset = gamePanel.scrWidth - gamePanel.player.scrXpos;
            if (rightOffset > gamePanel.worldTotalWidth - gamePanel.player.entityWorldXPos) {
                scrX = gamePanel.scrWidth - (gamePanel.worldTotalWidth - worldX);
            }

            int bottomOffset = gamePanel.scrHeight - gamePanel.player.scrYpos;
            if (bottomOffset > gamePanel.worldTotalHeight - gamePanel.player.entityWorldYPos) {
                scrY = gamePanel.scrHeight - (gamePanel.worldTotalHeight - worldY);
            }


            if (worldX + gamePanel.finalTileSize > gamePanel.player.entityWorldXPos - gamePanel.player.scrXpos &&
                    worldX - gamePanel.finalTileSize < gamePanel.player.entityWorldXPos + gamePanel.player.scrXpos &&
                    worldY + gamePanel.finalTileSize > gamePanel.player.entityWorldYPos - gamePanel.player.scrYpos &&
                    worldY - gamePanel.finalTileSize < gamePanel.player.entityWorldYPos + gamePanel.player.scrYpos
            ) {
                g2d.drawImage(tiles[tileNum].image, scrX, scrY, gamePanel.finalTileSize, gamePanel.finalTileSize, null);
            } else if (gamePanel.player.scrXpos > gamePanel.player.entityWorldXPos
                    || gamePanel.player.scrYpos > gamePanel.player.entityWorldYPos
                    || rightOffset > gamePanel.worldTotalWidth - gamePanel.player.entityWorldXPos
                    || bottomOffset > gamePanel.worldTotalHeight - gamePanel.player.entityWorldYPos) {

                g2d.drawImage(tiles[tileNum].image, scrX, scrY, gamePanel.finalTileSize, gamePanel.finalTileSize, null);

            }


            worldCol++;

            if (worldCol == gamePanel.maxWorldCol) {
                worldCol = 0;

                worldRow++;

            }


        }


    }

    private void setAreaBounds(){
        //farm
        areaBounds[0][0] = 912;
        areaBounds[0][1] = 1880;
        areaBounds[0][2] = 1852;
        areaBounds[0][3] = 2496;
        //village
        areaBounds[1][0] = 2352;
        areaBounds[1][1] = 1284;
        areaBounds[1][2] = 3940;
        areaBounds[1][3] = 1712;
        //boss arena
        areaBounds[2][0] = 3340;
        areaBounds[2][1] = 3152;
        areaBounds[2][2] = 4148+48;
        areaBounds[2][3] = 3920+48;
        //forest
//        areaBounds[3][0] =;
//        areaBounds[3][1] =;
//        areaBounds[3][2] =;
//        areaBounds[3][3] =;
    }


    public int getCurrMapIdx(){
        return currentMapIdx;
    }
}
