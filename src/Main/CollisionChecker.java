package Main;

import Entity.Entity;

import java.lang.foreign.StructLayout;

public class CollisionChecker {
    GamePanel gamePanel;

    public CollisionChecker(GamePanel gp) {
        this.gamePanel = gp;
    }

    public void CheckTile(Entity entity) {

        int entityLeftWorldX = entity.entityWorldXPos + entity.solidArea.x;
        int entityTopWorldY = entity.entityWorldYPos + entity.solidArea.y;
        int entityRightWorldX = entity.entityWorldXPos + entity.solidArea.width;
        int entityBottomWorldY = entity.entityWorldYPos + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gamePanel.finalTileSize;
        int entityRightCol = entityRightWorldX / gamePanel.finalTileSize;
        int entityTopRow = entityTopWorldY / gamePanel.finalTileSize;
        int entityBottomRow = entityBottomWorldY / gamePanel.finalTileSize;

        int tileNum1, tileNum2;

        if(entity.direction.equals("up")){
            entityTopRow = (entityTopWorldY - entity.entitySpeed) / gamePanel.finalTileSize;
            tileNum1 = gamePanel.tileManager.mapTileNumber[entityLeftCol][entityTopRow];
            tileNum2 = gamePanel.tileManager.mapTileNumber[entityRightCol][entityTopRow];
            if (gamePanel.tileManager.tiles[tileNum1].collision || gamePanel.tileManager.tiles[tileNum2].collision) {
                entity.collisionOn = true;
            }
        }
        else if(entity.direction.equals("down")){
            entityBottomRow = (entityBottomWorldY + entity.entitySpeed) / gamePanel.finalTileSize;
            tileNum1 = gamePanel.tileManager.mapTileNumber[entityLeftCol][entityBottomRow];
            tileNum2 = gamePanel.tileManager.mapTileNumber[entityRightCol][entityBottomRow];
            if (gamePanel.tileManager.tiles[tileNum1].collision || gamePanel.tileManager.tiles[tileNum2].collision) {
                entity.collisionOn = true;
            }
        }
        else if(entity.direction.equals("left")){
            entityLeftCol = (entityLeftWorldX - entity.entitySpeed) / gamePanel.finalTileSize;
            tileNum1 = gamePanel.tileManager.mapTileNumber[entityLeftCol][entityTopRow];
            tileNum2 = gamePanel.tileManager.mapTileNumber[entityLeftCol][entityBottomRow];
            if (gamePanel.tileManager.tiles[tileNum1].collision || gamePanel.tileManager.tiles[tileNum2].collision) {
                entity.collisionOn = true;
            }
        }
        else if(entity.direction.equals("right")){
            entityRightCol = (entityRightWorldX + entity.entitySpeed) / gamePanel.finalTileSize;
            tileNum1 = gamePanel.tileManager.mapTileNumber[entityRightCol][entityTopRow];
            tileNum2 = gamePanel.tileManager.mapTileNumber[entityRightCol][entityBottomRow];
            if (gamePanel.tileManager.tiles[tileNum1].collision || gamePanel.tileManager.tiles[tileNum2].collision) {
                entity.collisionOn = true;
            }
        }

    }

    public int checkObject(Entity entity, boolean player) {
        int index = 999;

        for (int i = 0; i < gamePanel.staticObjects.length; i++) {

            if (gamePanel.staticObjects[i] != null) {
                entity.solidArea.x = entity.entityWorldXPos + entity.solidArea.x;
                entity.solidArea.y = entity.entityWorldYPos + entity.solidArea.y;

                gamePanel.staticObjects[i].solidArea.y += gamePanel.staticObjects[i].entityWorldYPos;
                gamePanel.staticObjects[i].solidArea.x += gamePanel.staticObjects[i].entityWorldXPos;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.entitySpeed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.entitySpeed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.entitySpeed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.entitySpeed;

                        break;


                }

                if (entity.solidArea.intersects(gamePanel.staticObjects[i].solidArea)) {
                    if (gamePanel.staticObjects[i].collisionOn) {
                        entity.collisionOn = true;
                    }
                    if (player) {
                        index = i;
                    }
                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gamePanel.staticObjects[i].solidArea.x = gamePanel.staticObjects[i].solidAreaDefaultX;
                gamePanel.staticObjects[i].solidArea.y = gamePanel.staticObjects[i].solidAreaDefaultY;
            }
        }

        return index;
    }

    // NPC collision
    public int checkEntity(Entity entity, Entity[] target) {
        int index = 999;

        for (int i = 0; i < target.length; i++) {

            if (target[i] != null) {
                entity.solidArea.x = entity.entityWorldXPos + entity.solidArea.x;
                entity.solidArea.y = entity.entityWorldYPos + entity.solidArea.y;

                target[i].solidArea.y += target[i].entityWorldYPos;
                target[i].solidArea.x += target[i].entityWorldXPos;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.entitySpeed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.entitySpeed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.entitySpeed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.entitySpeed;
                        break;


                }
                if (entity.solidArea.intersects(target[i].solidArea)) {
                    if (target[i] != entity) {
                        entity.collisionOn = true;
                        index = i;

                    }

                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }
        }

        return index;

    }

    public boolean checkPlayer(Entity entity) {
        boolean contactPlayer = false;
        entity.solidArea.x = entity.entityWorldXPos + entity.solidArea.x;
        entity.solidArea.y = entity.entityWorldYPos + entity.solidArea.y;

        gamePanel.player.solidArea.y += gamePanel.player.entityWorldYPos;
        gamePanel.player.solidArea.x += gamePanel.player.entityWorldXPos;

        switch (entity.direction) {
            case "up":
                entity.solidArea.y -= entity.entitySpeed;
                break;
            case "down":
                entity.solidArea.y += entity.entitySpeed;
                break;
            case "left":
                entity.solidArea.x -= entity.entitySpeed;
                break;
            case "right":
                entity.solidArea.x += entity.entitySpeed;
                break;
        }

        if (entity.solidArea.intersects(gamePanel.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gamePanel.player.solidArea.x = gamePanel.player.solidAreaDefaultX;
        gamePanel.player.solidArea.y = gamePanel.player.solidAreaDefaultY;
        return contactPlayer;
    }
}
