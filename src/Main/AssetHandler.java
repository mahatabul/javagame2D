package Main;

import Entity.NPC_Emilia;
import Entity.NPC_rem;
import Monster.Betelgeuse;
import Monster.Monster_Slime;

public class AssetHandler {
    GamePanel gamePanel;

    public AssetHandler(GamePanel gp) {
        this.gamePanel = gp;
    }

    public void setObjects() {
        // Removing these objects bcz we have to rewrite them anyway
    }

    public void setNPC() {
        gamePanel.npc[0] = new NPC_rem(gamePanel,gamePanel.finalTileSize*33, gamePanel.finalTileSize*33);
//        gamePanel.npc[0].entityWorldXPos = gamePanel.finalTileSize * 13;
//        gamePanel.npc[0].entityWorldYPos = gamePanel.finalTileSize * 23;

        gamePanel.npc[1] = new NPC_Emilia(gamePanel,gamePanel.finalTileSize*50, gamePanel.finalTileSize*50);
    }
    public void setMonster() {
        
        gamePanel.monster[0] = new Monster_Slime(gamePanel);
        gamePanel.monster[0].entityWorldXPos = gamePanel.finalTileSize * 26;
        gamePanel.monster[0].entityWorldYPos = gamePanel.finalTileSize * 30;

        gamePanel.monster[1] = new Monster_Slime(gamePanel);
        gamePanel.monster[1].entityWorldXPos = gamePanel.finalTileSize * 25;
        gamePanel.monster[1].entityWorldYPos = gamePanel.finalTileSize * 30;

        gamePanel.monster[2] = new Monster_Slime(gamePanel);
        gamePanel.monster[2].entityWorldXPos = gamePanel.finalTileSize * 22;
        gamePanel.monster[2].entityWorldYPos = gamePanel.finalTileSize * 30;

        gamePanel.monster[3] = new Betelgeuse(gamePanel);
        gamePanel.monster[3].entityWorldXPos = gamePanel.finalTileSize * 40;
        gamePanel.monster[3].entityWorldYPos = gamePanel.finalTileSize * 30;

    }
}
