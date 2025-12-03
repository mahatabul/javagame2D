package Monster;

import Entity.Entity;
import Main.GamePanel;
import Objects.FireBall;

import java.util.Random;

public class Betelgeuse extends Entity {
    public Betelgeuse(GamePanel gp){
        super(gp);
        name = "Betelgeuse";
        entitySpeed = 2;
        maxLife = 1000;
        life = maxLife;
        type = 2;
        attack = 10;
        defense = 3;
        exp = 100;

        solidAreaDefaultX = solidArea.x = 3;
        solidAreaDefaultX = solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;

        attackArea.width = 48;
        attackArea.height = 48;


        projectile = new FireBall(gp);

        getEntityImg();
        getAttackImage();
    }

    private void getAttackImage() {

    }


    @Override
    public void getEntityImg(){
        standbyFront = setup("/betelguese/Betelguese0");
        standbyBack = setup("/betelguese/Betelguese1");
        left2 = standbyLeft = setup("/betelguese/Betelguese6");
        right2 = standbyRight = setup("/betelguese/Betelguese8");
        up1 = setup("/betelguese/Betelguese2");
        up2 = setup("/betelguese/Betelguese3");
        down1 = setup("/betelguese/Betelguese4");
        down2 = setup("/betelguese/Betelguese5");
        left1 = setup("/betelguese/Betelguese7");
        right1 = setup("/betelguese/Betelguese9");
    }


    public void setAction(){
        actionLockCounter++;


        if(getXpos(gp.player) <= entityWorldXPos+Radius && getXpos(gp.player) >= entityWorldXPos-Radius){
            chasePlayer(60, this.flag);
            this.flag = !this.flag;
        }
        else{
//            moveRandomly();
        }
    }

    void moveRandomly(){
        actionLockCounter++;

        if (actionLockCounter == 170) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;

            if (i <= 25) {
                direction = "up";
                projectile.set(entityWorldXPos, entityWorldYPos, direction, true, this);
                gp.projectileList.add(projectile);
            }
            if (i > 25 && i <= 50) {
                direction = "down";
                projectile.set(entityWorldXPos, entityWorldYPos, direction, true, this);
                gp.projectileList.add(projectile);
            }
            if (i > 50 && i <= 75) {
                direction = "left";
                projectile.set(entityWorldXPos, entityWorldYPos, direction, true, this);
                gp.projectileList.add(projectile);
            }
            if (i > 75) {
                direction = "right";
                projectile.set(entityWorldXPos, entityWorldYPos, direction, true, this);
                gp.projectileList.add(projectile);
            }
            actionLockCounter = 0;
        }
    }
}
