package Entity;

import Main.GamePanel;

public class Projectile extends Entity {
    Entity user;

    public Projectile(GamePanel gp) {
        super(gp);
    }

    public void set(int worldX, int worldY, String dir, boolean alive, Entity user) {
        this.entityWorldXPos = worldX;
        this.entityWorldYPos = worldY;
        this.direction = dir;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife;
    }

    public void update() {
        if (user==gp.player){
            int monIdx = gp.collisionChecker.checkEntity(this,gp.monster);
            if (monIdx!=999){
                alive=false;
                gp.player.damageMonster(monIdx,attack);
            }
        }
        switch (direction) {
            case "up":
                entityWorldYPos -= entitySpeed;
                break;
            case "down":
                entityWorldYPos += entitySpeed;
                break;
            case "left":
                entityWorldXPos -= entitySpeed;
                break;
            case "right":
                entityWorldXPos += entitySpeed;
                break;
        }
        life--;
        if (life < 0) {
            alive = false;

        }
        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNum = !spriteNum;
            spriteCounter = 0;
        }
    }
}
