package Monster;

import Entity.Entity;
import Main.GamePanel;

import java.util.Random;

public class Monster_Slime extends Entity {
    public Monster_Slime(GamePanel gp) {
        super(gp);
        name = "Green Slime";
        entitySpeed = 1;
        maxLife = 4;
        life = maxLife;
        type = 2;
        attack = 2;
        defense = 0;
        exp = 1;

        solidAreaDefaultX = solidArea.x = 3;
        solidAreaDefaultX = solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        getImage();
    }

    public void damageReaction() {
        actionLockCounter = 0;
        direction = gp.player.direction;
    }

    public void getImage() {
        standbyFront = setup("/slime-sprite/Green_Slime/Walk2");
        standbyBack = setup("/slime-sprite/Green_Slime/Walk2");
        left2 = standbyLeft = setup("/slime-sprite/Green_Slime/Walk2");
        right2 = standbyRight = setup("/slime-sprite/Green_Slime/Walk2");
        up1 = setup("/slime-sprite/Green_Slime/Walk1");
        up2 = setup("/slime-sprite/Green_Slime/Walk3");
        down1 = setup("/slime-sprite/Green_Slime/Walk1");
        down2 = setup("/slime-sprite/Green_Slime/Walk3");
        left1 = setup("/slime-sprite/Green_Slime/Walk1");
        right1 = setup("/slime-sprite/Green_Slime/Walk3");
    }

    public void setAction() {
        actionLockCounter++;

        if (actionLockCounter == 170) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;

            if (i <= 25) {
                direction = "up";
            }
            if (i > 25 && i <= 50) {
                direction = "down";
            }
            if (i > 50 && i <= 75) {
                direction = "left";
            }
            if (i > 75) {
                direction = "right";
            }
            actionLockCounter = 0;
        }


    }
}
