package Entity;

import Main.GamePanel;
import Main.KeyHandler;
import Objects.FireBall;
import Objects.Weapon_woodsword;
import Objects.woodShield;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity {
    GamePanel gamePanel;
    KeyHandler keyH;

    public final int scrXpos;
    public final int scrYpos;
    public ArrayList<Entity> inventory = new ArrayList<>();
//    public final int inventorySize = 20;

    public Player(GamePanel gp, KeyHandler kh) {
        super(gp);
        this.gamePanel = gp;
        this.keyH = kh;

        scrXpos = (gamePanel.scrWidth / 2) - (gamePanel.finalTileSize / 2);
        scrYpos = (gamePanel.scrHeight / 2) - (gamePanel.finalTileSize / 2);

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 12;
        solidArea.width = gamePanel.finalTileSize - 3 * solidArea.x;
        solidArea.height = gamePanel.finalTileSize - 3 * solidArea.y;

        attackArea.width = 48;
        attackArea.height = 48;
        if(gamePanel.ui.commandNum == 0){
            setDefaultValue();
        }else  if(gamePanel.ui.commandNum == 1){
            setSavedValue();
        }
        getPlayerImage();
        getAttackImage();
        setItems();

    }

    public void setDefaultValue() {
        entityWorldXPos = gamePanel.finalTileSize * 24;
        entityWorldYPos = gamePanel.finalTileSize * 21;
        entitySpeed = 4;
        direction = "down";
        standbyFlag = true;

        // Player Status
        level = 1;
        maxLife = 6;
        life = maxLife;
        strength = 1;
        dexterity = 1;
        exp = 0;
        nextlevelexp = 3;
        coin = 0;
        currentWeapon = new Weapon_woodsword(gp);
        currentShield = new woodShield(gp);
        attack = getAttack();
        defense = getDefense();

        projectile = new FireBall(gp);
    }

    //gets and sets saved status
    public void setSavedValue(){
        entityWorldXPos = gamePanel.dataStorage.getXPos();
        entityWorldYPos = gamePanel.dataStorage.getYPos();
        entitySpeed = 4;
        direction = "down";
        standbyFlag = true;

        // Player Status
        level = gamePanel.dataStorage.getLvl();
        maxLife = 6;
        life = gamePanel.dataStorage.getLife();
        strength = gamePanel.dataStorage.getStr();
//        dexterity = gamePanel.dataStorage.;
        exp = gamePanel.dataStorage.getExp();
        nextlevelexp = gamePanel.dataStorage.getNxtLvlExp();
        coin = gamePanel.dataStorage.getCoin();
        currentWeapon = new Weapon_woodsword(gp);
        currentShield = new woodShield(gp);
        attack = getAttack();
        defense = getDefense();

        projectile = new FireBall(gp);
    }

    private void setItems() {
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(currentWeapon);
        inventory.add(currentShield);
    }

    public int getAttack() {
        return strength * currentWeapon.attackValue;
    }

    public int getDefense() {
        return dexterity * currentShield.defenseValue;
    }

    public void update() {
        if (attacking) {
            attack();
        } else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.enterPressed) {
            if (keyH.upPressed) {

                direction = "up";
            } else if (keyH.downPressed) {

                direction = "down";
            } else if (keyH.leftPressed) {

                direction = "left";
            } else if (keyH.rightPressed) {

                direction = "right";
            }

            // check tile collision
            collisionOn = false;
            gamePanel.collisionChecker.CheckTile(this);

            gamePanel.eventHandler.checkEvent();

            //check NPC collision
            int npcIndex = gp.collisionChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // check monster collison
            int monsterIndex = gp.collisionChecker.checkEntity(this, gp.monster);
            interactMonster(monsterIndex);
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
        } else {
            standbyFlag = true;
        }

        if (gp.kh.shotKeypressed && !projectile.alive && shotcounter == 30) {
            projectile.set(entityWorldXPos, entityWorldYPos, direction, true, this);
            gp.projectileList.add(projectile);
            shotcounter = 0;

        }
        if (shotcounter < 30) {
            shotcounter++;

        }
        if (isinvincible) {
            invinciblecounter++;
            if (invinciblecounter > 60) {
                isinvincible = false;
                invinciblecounter = 0;
            }
        }
        if (life <= 0) {
            gp.gameState = gp.gameoverstate;
            gp.playSE(6, 1f);
        }

        // level up msg
        if (gp.ui.LEVELEDUP) {
            gp.ui.levelUpmsgCntr++;
            if (gp.ui.levelUpmsgCntr > 120) {
                gp.ui.LEVELEDUP = false;
                gp.ui.levelUpmsgCntr = 0;
            }
        }
    }

    public void interactNPC(int idx) {
        if (gp.kh.enterPressed) {
            if (idx != 999) {
                gp.gameState = gp.dialogueState;
                gp.npc[idx].speak();

            } else {
                gp.playSE(4, 1f);
                attacking = true;
            }
            gp.kh.enterPressed = false;
        }

    }

    public void interactMonster(int idx) {
        if (idx != 999) {
            if (!isinvincible && !gp.monster[idx].dying) {
                gp.playSE(5, 1f);
                int damage = gp.monster[idx].attack - defense;
                if (damage < 0) {
                    damage = 0;
                }
                life -= damage;
                isinvincible = true;

            }
        }
    }

    public void attack() {
        spriteCounter++;
        if (spriteCounter <= 5) {
            attackSpriteNum = 1;
        } else if (spriteCounter <= 20) {
            attackSpriteNum = 2;

            int currentWorldX = entityWorldXPos;
            int currentWorldY = entityWorldYPos;
            int solidAreawidth = solidArea.width;
            int solidAreaheight = solidArea.height;

            switch (direction) {
                case "up":
                    entityWorldYPos -= attackArea.height;
                    break;
                case "down":
                    entityWorldYPos += attackArea.height;
                    break;
                case "left":
                    entityWorldXPos -= attackArea.width;
                    break;
                case "right":
                    entityWorldXPos += attackArea.width;
                    break;
            }
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;
            int monsterIndex = gp.collisionChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex, attack);
            entityWorldXPos = currentWorldX;
            entityWorldYPos = currentWorldY;
            solidArea.width = solidAreawidth;
            solidArea.height = solidAreaheight;

        } else if (spriteCounter <= 25) {
            attackSpriteNum = 3;
        } else if (spriteCounter <= 30) {
            attackSpriteNum = 4;
        } else if (spriteCounter <= 35) {
            attackSpriteNum = 5;
        } else {
            attackSpriteNum = 6;
            attacking = false;
            spriteCounter = 0;
        }

    }

    public void damageMonster(int i, int atk) {
        if (i != 999) {
            if (!gp.monster[i].isinvincible) {
                int damage = atk - gp.monster[i].defense;
                if (damage < 0) {
                    damage = 0;
                }
                gp.monster[i].life -= damage;
                gp.ui.addmsg(damage + " damage!");
                gp.playSE(2, 1f);
                gp.monster[i].damageReaction();
                gp.monster[i].isinvincible = true;
                if (gp.monster[i].life <= 0) {
                    gp.monster[i].dying = true;
                    gp.ui.addmsg("Killed the " + gp.monster[i].name);
                    gp.ui.addmsg("Exp gained " + gp.monster[i].exp);
                    exp += gp.monster[i].exp;
                    checkLevel();

                }
            }
            System.out.println("Hit!!");
        } else {
            System.out.println("Miss");
        }
    }

    private void checkLevel() {

        if (exp >= nextlevelexp) {
            gp.playSE(8, 1f);

            level++;
            nextlevelexp = nextlevelexp * 2;
            maxLife += 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();
            gp.ui.addmsg("Leveled up " + level);
            life = maxLife;
            showLevel();
        }
    }

    public void showLevel() {
        gp.ui.levelUpmsgCntr = 0;
        gp.ui.LEVELEDUP = true;

    }

    public void draw(Graphics2D g2d) {

        BufferedImage image = null;

        if (!standbyFlag) {
            if (attacking) {
                if (direction.equals("left")) {
                    if (attackSpriteNum == 1) {
                        image = attackleft1;
                    }
                    if (attackSpriteNum == 2) {
                        image = attackleft2;
                    }
                    if (attackSpriteNum == 3) {
                        image = attackleft3;
                    }
                    if (attackSpriteNum == 4) {
                        image = attackleft4;
                    }
                    if (attackSpriteNum == 5) {
                        image = attackleft5;
                    }
                    if (attackSpriteNum == 6) {
                        image = attackleft6;
                    }
                } else if (direction.equals("right")) {
                    if (attackSpriteNum == 1) {
                        image = attackright1;
                    }
                    if (attackSpriteNum == 2) {
                        image = attackright2;
                    }
                    if (attackSpriteNum == 3) {
                        image = attackright3;
                    }
                    if (attackSpriteNum == 4) {
                        image = attackright4;
                    }
                    if (attackSpriteNum == 5) {
                        image = attackright5;
                    }
                    if (attackSpriteNum == 6) {
                        image = attackright6;
                    }

                } else if (direction.equals("down")) {
                    if (attackSpriteNum == 1) {
                        image = attackdown1;
                    } else if (attackSpriteNum == 2) {
                        image = attackdown2;
                    } else {
                        image = attackdown1;
                    }

                } else if (direction.equals("up")) {
                    if (attackSpriteNum == 1) {
                        image = attackup1;
                    } else if (attackSpriteNum == 2) {
                        image = attackup2;
                    } else {
                        image = attackup1;
                    }

                }

            } else {
                if (direction.equals("up")) {
                    image = spriteNum ? up1 : up2;
                } else if (direction.equals("down")) {
                    image = spriteNum ? down1 : down2;
                } else if (direction.equals("left")) {
                    image = spriteNum ? left1 : left2;
                } else if (direction.equals("right")) {
                    image = spriteNum ? right1 : right2;
                }
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

        int x = scrXpos, y = scrYpos;

        if (scrXpos > entityWorldXPos) {
            x = entityWorldXPos;
        }
        if (scrYpos > entityWorldYPos) {
            y = entityWorldYPos;
        }

        int rightOffset = gamePanel.scrWidth - scrXpos;
        if (rightOffset > gamePanel.worldTotalWidth - entityWorldXPos) {
            x = gamePanel.scrWidth - (gamePanel.worldTotalWidth - entityWorldXPos);
        }

        int bottomOffset = gamePanel.scrHeight - scrYpos;
        if (bottomOffset > gamePanel.worldTotalHeight - entityWorldYPos) {
            y = gamePanel.scrHeight - (gamePanel.worldTotalHeight - entityWorldYPos);
        }
        if (isinvincible) {
            long elapsed = System.currentTimeMillis() % 300; // cycle every 300ms
            if (elapsed < 50 || (elapsed > 100 && elapsed < 150) || (elapsed > 200 && elapsed < 250)) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f)); // blur
            } else {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));   // normal
            }
        }


        g2d.drawImage(image, x, y, null);

        // Reset alpha
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public void getPlayerImage() {


        // new sprite
        standbyFront = setup("/PlayerSprite/subaru/movement/subaru0");
        standbyBack = setup("/PlayerSprite/subaru/movement/subaru3");
        left2 = standbyLeft = setup("/PlayerSprite/subaru/movement/subaru9");
        right2 = standbyRight = setup("/PlayerSprite/subaru/movement/subaru7");
        up1 = setup("/PlayerSprite/subaru/movement/subaru4");
        up2 = setup("/PlayerSprite/subaru/movement/subaru5");
        down1 = setup("/PlayerSprite/subaru/movement/subaru1");
        down2 = setup("/PlayerSprite/subaru/movement/subaru2");
        left1 = setup("/PlayerSprite/subaru/movement/subaru8");
        right1 = setup("/PlayerSprite/subaru/movement/subaru6");

    }

    public void getAttackImage() {
        attackleft1 = setup("/PlayerSprite/subaru/fight/wooden sword/subaru-w s lf1");
        attackleft2 = setup("/PlayerSprite/subaru/fight/wooden sword/subaru-w s lf2");
        attackleft3 = setup("/PlayerSprite/subaru/fight/wooden sword/subaru-w s lf3");
        attackleft4 = setup("/PlayerSprite/subaru/fight/wooden sword/subaru-w s lf4");
        attackleft5 = setup("/PlayerSprite/subaru/fight/wooden sword/subaru-w s lf5");
        attackleft6 = setup("/PlayerSprite/subaru/fight/wooden sword/subaru-w s lf6");
        attackright1 = setup("/PlayerSprite/subaru/fight/wooden sword/subaru-w s rf1");
        attackright2 = setup("/PlayerSprite/subaru/fight/wooden sword/subaru-w s rf2");
        attackright3 = setup("/PlayerSprite/subaru/fight/wooden sword/subaru-w s rf3");
        attackright4 = setup("/PlayerSprite/subaru/fight/wooden sword/subaru-w s rf4");
        attackright5 = setup("/PlayerSprite/subaru/fight/wooden sword/subaru-w s rf5");
        attackright6 = setup("/PlayerSprite/subaru/fight/wooden sword/subaru-w s rf6");
        attackdown1 = setup("/PlayerSprite/subaru/fight/wooden sword/subaru-w s lf7");
        attackdown2 = setup("/PlayerSprite/subaru/fight/wooden sword/subaru-w s lf8");
        attackup1 = setup("/PlayerSprite/subaru/fight/wooden sword/subaru-w s lf9");
        attackup2 = setup("/PlayerSprite/subaru/fight/wooden sword/subaru-w s lf10");
    }


}
