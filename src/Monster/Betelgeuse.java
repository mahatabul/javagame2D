package Monster;

import Entity.Entity;
import Main.GamePanel;
import Objects.FireBall;

import java.util.Random;

public class Betelgeuse extends Entity {
    public Betelgeuse(GamePanel gp, double x, double y){
        super(gp);
        name = "Betelgeuse";
        entitySpeed = 2;
        maxLife = 50;
        life = maxLife;
        type = 2;
        attack = 10;
        defense = 3;
        exp = 100;
        spawnX = x;
        spawnY = y;

        entityWorldXPos = (int) (gp.finalTileSize*spawnX);
        entityWorldYPos = (int) (gp.finalTileSize*spawnY);

        // chase range parameters
        Radius = 11*48;
        X0 = 72;
        X1 = 89;
        Y0 = 70;
        Y1 = 84;


        solidAreaDefaultX = solidArea.x = 3;
        solidAreaDefaultX = solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;

        attackArea.width = 20;
        attackArea.height = 20;


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
            standbyFlag = true;
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

            if(entityWorldXPos <= X0+5) direction = "right";
            else if(entityWorldXPos >= X1-5) direction = "left";

            if(entityWorldYPos <= Y0+5) direction = "down";
            else if(entityWorldYPos >= Y1-5) direction = "up";

            actionLockCounter = 0;
        }
    }
}
