package Entity;

import Main.GamePanel;

public class NPC_Emilia extends Entity {
    public NPC_Emilia(GamePanel gp, int x, int y) {
        super(gp);
        entityWorldXPos = gp.finalTileSize*x;
        entityWorldYPos = gp.finalTileSize*y;
        direction = "down";
        entitySpeed = 1;
        standbyFlag = true;
        getEntityImg();
        setDialogue();
    }

    public NPC_Emilia(GamePanel gp) {
        super(gp);
        direction = "down";
        entitySpeed = 1;
        getEntityImg();
    }


    @Override
    public void getEntityImg() {

        standbyFront = setup("/PlayerSprite/emilia/emilia0");
        standbyBack = setup("/PlayerSprite/emilia/emilia3");
        left2 = standbyLeft = setup("/PlayerSprite/emilia/emilia8");
        right2 = standbyRight = setup("/PlayerSprite/emilia/emilia9");
        up1 = setup("/PlayerSprite/emilia/emilia4");
        up2 = setup("/PlayerSprite/emilia/emilia5");
        down1 = setup("/PlayerSprite/emilia/emilia1");
        down2 = setup("/PlayerSprite/emilia/emilia2");
        left1 = setup("/PlayerSprite/emilia/emilia6");
        right1 = setup("/PlayerSprite/emilia/emilia7");

    }


    public void setDialogue() {
        dialogues[0] = "Ara Subaru-kun!";
        dialogues[1] = "Did you get used to the mansion?";
        dialogues[2] = "Ram called you Barusu?";
        dialogues[3] = "She has no ill intention.";
        dialogues[4] = "I bet she likes to mess with you.";
    }
    @Override
    public void update() {
        // NPC stays still - only update sprite animation for idle effect
        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNum = !spriteNum;
            spriteCounter = 0;
        }
    }

    public void speak() {
        super.speak();
    }
}