package Entity;

import Main.GamePanel;


public class NPC_rem extends Entity {
    public NPC_rem(GamePanel gp, int x, int y) {
        super(gp);
        entityWorldXPos = gp.finalTileSize*x;
        entityWorldYPos = gp.finalTileSize*y;
        direction = "down";
        entitySpeed = 1;
        standbyFlag = true;
        getEntityImg();
        setDialogue();
    }


    @Override
    public void getEntityImg() {

        standbyFront = setup("/PlayerSprite/rem/rem-standby front");
        standbyBack = setup("/PlayerSprite/rem/rem-standby back");
        left2 = standbyLeft = setup("/PlayerSprite/rem/rem-standby left");
        right2 = standbyRight = setup("/PlayerSprite/rem/rem-standby right");
        up1 = setup("/PlayerSprite/rem/rem-up1");
        up2 = setup("/PlayerSprite/rem/rem-up2");
        down1 = setup("/PlayerSprite/rem/rem-down1");
        down2 = setup("/PlayerSprite/rem/rem-down2");
        left1 = setup("/PlayerSprite/rem/rem-left1");
        right1 = setup("/PlayerSprite/rem/rem-right1");

    }

    public void setDialogue() {
        dialogues[0] = "মাস্টার সুবারু!";
        dialogues[1] = "অবশেষে আপনি পৌঁছেছেন।";
        dialogues[2] = "দয়া করে খামারটি বাঁচান।";
        dialogues[3] = "অন্যথায় গ্রামের মানুষ না খেয়ে মারা যাবে।";
        dialogues[4] = "পরে, দক্ষিণ-পূর্ব ভ্রমণ করুন।";
        dialogues[5] = "আপনি সেখানে নাটের গুরু খুঁজে পাবেন।";

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
