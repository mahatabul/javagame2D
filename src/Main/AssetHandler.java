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

    public void setNPC() {
        gamePanel.npc[0] = new NPC_rem(gamePanel, 30, 45);
        gamePanel.npc[1] = new NPC_Emilia(gamePanel,50, 50);
    }

    public void setMonster() {

        // slimes
        gamePanel.monster[0] = new Monster_Slime(gamePanel, 33, 38);
        gamePanel.monster[1] = new Monster_Slime(gamePanel, 43, 52);
        gamePanel.monster[2] = new Monster_Slime(gamePanel, 38, 48);
        gamePanel.monster[3] = new Monster_Slime(gamePanel, 38, 20);
        gamePanel.monster[4] = new Monster_Slime(gamePanel, 33, 20);
        gamePanel.monster[5] = new Monster_Slime(gamePanel, 49, 24);
        gamePanel.monster[6] = new Monster_Slime(gamePanel, 85, 19);
        gamePanel.monster[7] = new Monster_Slime(gamePanel, 86, 20);
        gamePanel.monster[8] = new Monster_Slime(gamePanel, 49, 29);
        gamePanel.monster[9] = new Monster_Slime(gamePanel, 47, 25);
        gamePanel.monster[10] = new Monster_Slime(gamePanel, 48, 28);
        gamePanel.monster[11] = new Monster_Slime(gamePanel, 46, 31);
        gamePanel.monster[12] = new Monster_Slime(gamePanel, 63, 21);
        gamePanel.monster[13] = new Monster_Slime(gamePanel, 81, 23);
        gamePanel.monster[14] = new Monster_Slime(gamePanel, 72, 65);
//        gamePanel.monster[15] = new Monster_Slime(gamePanel, 26, 30);
//        gamePanel.monster[16] = new Monster_Slime(gamePanel, 25, 30);
//        gamePanel.monster[17] = new Monster_Slime(gamePanel, 22, 30);
//        gamePanel.monster[18] = new Monster_Slime(gamePanel, 26, 30);
//        gamePanel.monster[19] = new Monster_Slime(gamePanel, 25, 30);
//        gamePanel.monster[20] = new Monster_Slime(gamePanel, 22, 30);
//        gamePanel.monster[21] = new Monster_Slime(gamePanel, 26, 30);
//        gamePanel.monster[22] = new Monster_Slime(gamePanel, 25, 30);
//        gamePanel.monster[23] = new Monster_Slime(gamePanel, 22, 30);
//        gamePanel.monster[24] = new Monster_Slime(gamePanel, 26, 30);


        // betelgeuse
        gamePanel.monster[25] = new Betelgeuse(gamePanel, 80, 79);


    }
}
