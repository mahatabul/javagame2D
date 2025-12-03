package Entity;

import Main.GamePanel;
import Main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Entity {
    public GamePanel gp;
    public int entityWorldXPos, entityWorldYPos;
    public int entitySpeed;
    // for walking
    public BufferedImage standbyFront, standbyBack, standbyLeft, standbyRight, up1, up2, down1, down2, left1, left2, right1, right2;
    // for attacking
    public BufferedImage attackleft1, attackleft2, attackleft3, attackleft4, attackleft5, attackleft6, attackright1, attackright2, attackright3, attackright4, attackright5, attackright6, attackup1, attackup2, attackdown1, attackdown2;
    public String direction = "down";
    public boolean standbyFlag;

    // Attack
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public boolean isinvincible = false;
    public int invinciblecounter = 0;
    public int shotcounter = 0;
    boolean attacking = false;
    public int attackSpriteNum;
    public int dyingCounter = 0;
    public boolean alive = true;
    public boolean dying = false;
    public boolean hpbaron = false;
    int hpbarCounter = 0;
    public Projectile projectile;
    public int usecost;


    // Others
    public int spriteCounter = 0;
    public boolean spriteNum = true;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;
    public boolean collisionOn = false;
    public int actionLockCounter = 0;


    // aggro radius in pixel
    public double Radius = 5000.0;
    public boolean flag = true;



    // Dialogue
    String[] dialogues = new String[20];
    int dialogueIdx = 0;

    public int type;// 0 for player, 1 for npc, 2 for monster

    // Character Status
    public int maxLife;
    public int life;
    Timer dialogueTimer;
    public int level, strength, attack, defense, dexterity, exp, nextlevelexp, coin;
    public Entity currentWeapon, currentShield;

    public int attackValue, defenseValue;

    // New Object Change
    public BufferedImage image, image2, image3;
    public String name;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void setAction() {
    }

    public void update() {
        setAction();
        collisionOn = false;
        gp.collisionChecker.CheckTile(this);
        gp.collisionChecker.checkObject(this, false);
        boolean contactPlayer = gp.collisionChecker.checkPlayer(this);
        gp.collisionChecker.checkEntity(this, gp.npc);
        gp.collisionChecker.checkEntity(this, gp.monster);

        if (this.type == 2 && contactPlayer) {
            if (!gp.player.isinvincible) {
                gp.playSE(5, 1f);
                int damage = attack - gp.player.defense;
                if (damage < 0) {
                    damage = 0;
                }
                gp.player.life -= damage;

                gp.player.isinvincible = true;
            }
        }

        // if collision is false player can move
        if (!collisionOn) {
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

        }
        standbyFlag = false;
        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNum = !spriteNum;
            spriteCounter = 0;
        }
        if (isinvincible) {
            invinciblecounter++;
            if (invinciblecounter > 40) {
                isinvincible = false;
                invinciblecounter = 0;
            }
        }

    }

    // for gp tilesize image scalling
    public BufferedImage setup(String imagePath) {
        UtilityTool utool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + ".png")));
            image = utool.scaleImage(image, gp.finalTileSize, gp.finalTileSize);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return image;
    }

    // for custom size image scalling
    public BufferedImage setup(String imagePath, int x, int y) {
        UtilityTool utool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + ".png")));
            image = utool.scaleImage(image, x, y);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return image;
    }

    public void draw(Graphics2D g2d) {
        int scrX = entityWorldXPos - gp.player.entityWorldXPos + gp.player.scrXpos;
        int scrY = entityWorldYPos - gp.player.entityWorldYPos + gp.player.scrYpos;
        BufferedImage image = null;

        if (entityWorldXPos + gp.finalTileSize > gp.player.entityWorldXPos - gp.player.scrXpos &&
                entityWorldXPos - gp.finalTileSize < gp.player.entityWorldXPos + gp.player.scrXpos &&
                entityWorldYPos + gp.finalTileSize > gp.player.entityWorldYPos - gp.player.scrYpos &&
                entityWorldYPos - gp.finalTileSize < gp.player.entityWorldYPos + gp.player.scrYpos
        ) {
            if (!standbyFlag) {
                if (direction.equals("up")) {
                    image = spriteNum ? up1 : up2;
                } else if (direction.equals("down")) {
                    image = spriteNum ? down1 : down2;
                } else if (direction.equals("left")) {
                    image = spriteNum ? left1 : left2;
                } else if (direction.equals("right")) {
                    image = spriteNum ? right1 : right2;
                }
            } else {
                if (direction.equals("down")) {
                    image = standbyFront;
                } else if (direction.equals("up")) {
                    image = standbyBack;
                } else if (direction.equals("left")) {
                    image = standbyLeft;
                } else if (direction.equals("right")) {
                    image = standbyRight;
                }
            }

            // monster hp bar
            if (type == 2 && hpbaron) {
                double onscale = (double) gp.finalTileSize / maxLife;
                double hpBarvalue = onscale * life;
                g2d.setColor(new Color(35, 35, 35));
                g2d.fillRect(scrX - 1, scrY - 16, gp.finalTileSize + 2, 12);
                g2d.setColor(new Color(255, 0, 30));
                g2d.fillRect(scrX, scrY - 15, (int) hpBarvalue, 10);
                hpbarCounter++;
                if (hpbarCounter > 300) {
                    hpbaron = false;
                    hpbarCounter = 0;
                }

            }

            if (isinvincible) {
                hpbaron = true;
                hpbarCounter = 0;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f)); // blur

            }
            if (dying) {
                dyingAnimation(g2d);
            }
            g2d.drawImage(image, scrX, scrY, gp.finalTileSize, gp.finalTileSize, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }

    public void damageReaction() {
    }

    public void dyingAnimation(Graphics2D g2d) {
        dyingCounter++;
        if (dyingCounter <= 5) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f)); // blur
        } else if (dyingCounter <= 10) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // normal
        } else if (dyingCounter <= 15) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .4f)); // blur
        } else if (dyingCounter <= 20) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // normal
        } else if (dyingCounter <= 25) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .4f)); // blur
        } else if (dyingCounter <= 30) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // normal
        } else if (dyingCounter <= 35) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .4f)); // blur
        } else if (dyingCounter <= 40) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // normal
        } else if (dyingCounter <= 45) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .4f)); // blur
        } else {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // normal
            alive = false;
        }

    }

    public void speak() {

        if (dialogueTimer != null) {
            dialogueTimer.cancel();
        }

        dialogueIdx = 0;
        showDialogue();

        dialogueTimer = new Timer();
        dialogueTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                if (dialogues[dialogueIdx] == null) {
                    dialogueIdx = 0;
                    gp.gameState = gp.playState;
                    dialogueTimer.cancel();
                    return;
                }

                showDialogue();
            }
        }, 3000, 3000);
    }

    private void showDialogue() {
        gp.ui.currentDialogue = dialogues[dialogueIdx];
        dialogueIdx++;

        switch (gp.player.direction) {
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }
    }

    public void getEntityImg() {

    }


    // enemy aggro implementation
    public void chasePlayer(int interval, boolean flag){

        actionLockCounter++;

        if(actionLockCounter > interval){
            // trying to create an invisible radial aggro field

            double playerX = getXpos(gp.player);
            double playerY = getYpos(gp.player);

            if(Math.pow(playerX-this.entityWorldXPos, 2)+Math.pow(playerY-this.entityWorldYPos, 2)<=Radius*Radius){
                // implies that player gets inside aggro range

                if(playerX<=this.entityWorldXPos && flag){
                    direction = "left";
                }
                else if(playerX>this.entityWorldXPos && flag){
                    direction = "right";
                }
                if(playerY<=this.entityWorldYPos && !flag){
                    direction = "up";
                }
                else if(playerY>this.entityWorldYPos && !flag){
                    direction = "down";
                }
            }

            actionLockCounter = 0;
        }
    }


    // helper position getter fn
    public double getYpos(Entity e) {
        return e.entityWorldYPos;
    }

    public double getXpos(Entity e) {
        return e.entityWorldXPos;
    }

}
