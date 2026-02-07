package Main;

import Entity.Entity;
import Objects.Heart;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class UI {
    private final int creditDuration = 400;
    public float titleFontsize = 80, otherFontsize = 35, dlgFontsize = 26;
    public int commandNum = 0;
    public boolean showControlsWindow = false;
    public String currentDialogue = "";
    public int slotcol = 0, slotrow = 0;
    public String[] menuItems = {"New Game", "Load Game", "Delete Save Data", "Scoreboard", "Options", "Exit"};
    public String[] pauseItems = {"Resume Game", "Save Game", "Options", "Exit Game"};
    public String[] optionsItems = {"Volume", "SE", "Controls", "Back"};
    public int levelUpmsgCntr = 0;
    public boolean LEVELEDUP = false;
    public boolean gamesaved = false;
    public boolean gameDeleted = false;
    public boolean showgamesavescrn = false;
    public boolean showgamedeletedscrn = false;
    public boolean shownosavescrn = false;
    // current volume level (0.0 → 1.0)
    public float musicVolume = 0.1F;
    public boolean seEnabled = true;
    GamePanel gp;
    Graphics2D g2;
    Font BBh, Jersey, Pixelify, Roboto, Varela, bangla;
    BufferedImage heart_full, heart_half, heart_blank;
    // messages
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> msgCounter = new ArrayList<>();
    private Timer msgTimer;


    private boolean scoreCommitted = false;



    // credit scene
    public int creditTimer = 0;
    int endingCreditTimer = 0;
    int endingScrollY = -1;

    public UI(GamePanel gp) {
        this.gp = gp;


        try {
            InputStream is;
            is = getClass().getResourceAsStream("/font/BBHSansBogle-Regular.ttf");
            BBh = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/font/bangla.ttf");
            bangla = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/font/Jersey25-Regular.ttf");
            Jersey = Font.createFont(Font.TRUETYPE_FONT, is);
            // font/PixelifySans-Regular.ttf
            is = getClass().getResourceAsStream("/font/PixelifySans-Regular.ttf");
            Pixelify = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/font/Roboto.ttf");
            Roboto = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/font/Varela.ttf");
            Varela = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Create heart
        Entity heart = new Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;

    }

    public void addmsg(String text) {
        message.add(text);
        msgCounter.add(0);
    }

    public void draw(Graphics2D g2) {

        this.g2 = g2;
        g2.setFont(Jersey);
        g2.setColor(Color.white);
        if (gp.gameState == gp.startingcreditState) {
            drawCreditScreen();
        } else if (gp.gameState == gp.endingCreditState) {
            drawEndingCreditScreen();
        } else if (gp.gameState == gp.pauseState) {
            drawPlayerLife();
            drawPauseScrn();
        } else if (gp.gameState == gp.titleState) {
            drawTitleScrn(g2);

        } else if (gp.gameState == gp.scoreBoardState){
            drawScoreboardPage(g2, gp);
        }
        else if (gp.gameState == gp.optionState) {
            drawOptionScrn();
            if (showControlsWindow) {
                drawControls(g2);
            }

        } else if (gp.gameState == gp.dialogueState) {
            drawDialogueScrn();

        } else if (gp.gameState == gp.gameoverstate) {
            drawGameOverScrn();
            // possible bug fix
//            gp.assetHandler.respawnMonster();
//            fixed inside key handler
        } else if (gp.gameState == gp.characterstate) {
            drawCharacterScrn();
            drawInventory();

        } else if (gp.gameState == gp.inputplayernamestate) {
            inputplayername();
        } else if (gp.gameState == gp.playState) {
            // Play state in here
            drawPlayerLife();
            drawMessage();
            if (LEVELEDUP) {
                drawLevelupScrn();
            }

        }
        else if (gp.gameState==gp.gameWinstate){
            drawGameWinScreen(gp);
        }
        if (showgamesavescrn) {
            showMsgonscrn("Game Saved");
        }
        if (showgamedeletedscrn) {
            showMsgonscrn("Game Deleted");
        }
        if (shownosavescrn) {
            showMsgonscrn("No Saved Game");
        }

        if (!gp.gameFinished){

            showAreaName(gp.player.entityWorldXPos, gp.player.entityWorldYPos);
        }

    }

    private void drawScoreboardPage(Graphics2D g2, GamePanel gp) {
        //grey background
        g2.setColor(Color.GRAY);
        g2.fillRect(0,0, gp.scrWidth, gp.scrHeight);


        //dark overlay
        g2.setColor(new Color(0,0,0,180));
        g2.fillRect(24,24, gp.scrWidth-48, gp.scrHeight-48);

        //title
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 64f));
        String txt = "Scoreboard!!!";
        int x = centerXfortext(txt);
        int y = gp.scrHeight/2-120;

        g2.setColor(Color.YELLOW);
        g2.drawString(txt, x, y);
        y += 20;


        //scores
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32f));
        gp.dataStorage.loadScoreBoard();
        x = gp.scrWidth/2-48;
        for(int i = 0, j = 1; i < 5; i++){
            if(gp.dataStorage.scoreBoard[i] != -1){
                y += 60;
                txt = j+". "+ gp.dataStorage.scoreBoard[i];
                j++;
                g2.drawString(txt,x,y);
                g2.setColor(Color.WHITE);
            }
        }
    }

    public void drawGameWinScreen(GamePanel gp) {

        // Dark overlay
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.scrWidth, gp.scrHeight);

        // Title
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 64f));
        String text = "YOU WON!";
        int x = centerXfortext(text);
        int y = gp.scrHeight / 2 - 120;

        g2.setColor(Color.YELLOW);
        g2.drawString(text, x, y);


        //score show
        if (!scoreCommitted) {
            gp.player.calculateScore();
            gp.dataStorage.updateScoreBoard();
            scoreCommitted = true;
        }

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32f));
        text = "YOUR SCORE: " + gp.player.score;
        x = centerXfortext(text);
        y += 80;

        g2.setColor(Color.YELLOW);
        g2.drawString(text, x, y);

        boolean isNewHS = gp.dataStorage.checkIfNewHS();

        if (!scoreCommitted) {
            gp.player.calculateScore();
            gp.dataStorage.updateScoreBoard();
            scoreCommitted = true;
        }

        if (isNewHS) {
            text = "NEW HIGHSCORE!!!";
            x = centerXfortext(text);
            y += 80;
            g2.drawString(text, x, y);
        }


        text = "PLAYTIME: "+((gp.player.playTime/1000000000)/60)+" Min, "+((gp.player.playTime/1000000000)%60)+" Sec";
        x = centerXfortext(text);
        y += 40;

        g2.setColor(Color.YELLOW);
        g2.drawString(text, x, y);

        text = "TOTAL XP: "+gp.player.totalXpEarned;
        x = centerXfortext(text);
        y += 40;

        g2.setColor(Color.YELLOW);
        g2.drawString(text, x, y);

        text = "DAMAGE TAKEN: "+gp.player.dmgTaken;
        x = centerXfortext(text);
        y += 40;

        g2.setColor(Color.YELLOW);
        g2.drawString(text, x, y);

        // Subtitle
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24f));
        text = "Press ENTER to continue";
        x = centerXfortext(text);
        y += 60;

        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);
    }

    public void drawCreditScreen() {
        if (endingScrollY == -1) {
            endingScrollY = gp.scrHeight;
        }
        // Black background
        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, gp.scrWidth, gp.scrHeight);

        // Title fade in (frames 0-60)
        int titleAlpha = Math.min(255, creditTimer * 4);
        g2.setFont(Jersey.deriveFont(Font.BOLD, 60f));
        g2.setColor(new Color(255, 255, 255, titleAlpha));
        String text = "RE:ZERO RPG";
        int x = centerXfortext(text);
        int y = gp.finalTileSize * 3;
        g2.drawString(text, x, y);

        // Credits fade in (starts at frame 100, fades over 60 frames)
        if (creditTimer > 100) {
            int creditsAlpha = Math.min(255, (creditTimer - 100) * 4);
            g2.setFont(Jersey.deriveFont(Font.PLAIN, 32f));
            g2.setColor(new Color(255, 255, 255, creditsAlpha));

            text = "Developed by: Raj & Mahatabul";
            x = centerXfortext(text);
            y += gp.finalTileSize * 4;
            g2.drawString(text, x, y);

        }

        // Skip instruction fade in (starts at frame 180, fades over 60 frames)
        if (creditTimer > 180) {
            int skipAlpha = Math.min(255, (creditTimer - 180) * 4);
            g2.setFont(bangla.deriveFont(Font.PLAIN, 24f));
            g2.setColor(new Color(255, 255, 255, skipAlpha));
            text = "Press ENTER to continue";
            x = centerXfortext(text);
            y = gp.scrHeight - gp.finalTileSize * 2;
            g2.drawString(text, x, y);
        }

        creditTimer++;
        if (creditTimer >= creditDuration) {
            gp.gameState = gp.titleState;
            creditTimer = 0;
        }
    }

    public void drawEndingCreditScreen() {
        // Black background
        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, gp.scrWidth, gp.scrHeight);

        // Calculate scroll position
        int baseY = endingScrollY;
        int spacing = gp.finalTileSize;

        // Set up for centered text
        g2.setColor(Color.WHITE);

        // === MAIN TITLE ===
        g2.setFont(Jersey.deriveFont(Font.BOLD, 70f));
        String text = "RE:ZERO RPG";
        int x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing * 2;

        // === SUBTITLE ===
        g2.setFont(Jersey.deriveFont(Font.PLAIN, 40f));
        text = "Starting Life in Another World";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing * 3;

        // === DEVELOPMENT TEAM ===
        g2.setFont(Jersey.deriveFont(Font.BOLD, 45f));
        text = "DEVELOPMENT TEAM";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing * 2;

        g2.setFont(Jersey.deriveFont(Font.PLAIN, 32f));

        text = "Game Director: Raj";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing;

        text = "Lead Programmer: Mahatabul";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing;

        text = "Co-Programmer: Raj";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing * 2;

        // === ART & DESIGN ===
        g2.setFont(Jersey.deriveFont(Font.BOLD, 45f));
        text = "ART & DESIGN";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing * 2;

        g2.setFont(Jersey.deriveFont(Font.PLAIN, 32f));

        text = "Character Design: Raj";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing;

        text = "Sprite Artist: Raj";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing;

        text = "Environment Artist: Raj";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing;

        text = "UI/UX Design: Mahatabul";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing * 2;

        // === STORY & WRITING ===
        g2.setFont(Jersey.deriveFont(Font.BOLD, 45f));
        text = "STORY & WRITING";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing * 2;

        g2.setFont(Jersey.deriveFont(Font.PLAIN, 32f));

        text = "Story by: Raj & Mahatabul";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing;

        text = "Dialogue Writer: Raj";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing;

        text = "Quest Design: Mahatabul";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing * 2;

        // === AUDIO ===
        g2.setFont(Jersey.deriveFont(Font.BOLD, 45f));
        text = "AUDIO";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing * 2;

        g2.setFont(Jersey.deriveFont(Font.PLAIN, 32f));

        text = "Music Composer: [Artist Name]";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing;

        text = "Sound Effects: [Artist Name]";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing;

        text = "Audio Engineer: [Name]";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing * 2;

        // === SPECIAL THANKS ===
        g2.setFont(Jersey.deriveFont(Font.BOLD, 45f));
        text = "SPECIAL THANKS";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing * 2;

        g2.setFont(Jersey.deriveFont(Font.PLAIN, 32f));

        text = "Original Story: Tappei Nagatsuki";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing;

        text = "Re:Zero Franchise";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing;

        text = "Our Families & Friends";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing;

        text = "And You, the Player!";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing * 3;

        // === FINAL MESSAGE ===
        g2.setFont(Jersey.deriveFont(Font.BOLD, 50f));
        text = "THANK YOU FOR PLAYING!";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing * 2;

        g2.setFont(Jersey.deriveFont(Font.PLAIN, 28f));
        text = "© 2025 Raj & Mahatabul";
        x = centerXfortext(text);
        g2.drawString(text, x, baseY);
        baseY += spacing * 4;

        // === SKIP INSTRUCTION ===
        if (endingCreditTimer > 180) {
            int skipAlpha = Math.min(200, (endingCreditTimer - 180) * 4);
            g2.setColor(new Color(255, 255, 255, skipAlpha));
            g2.setFont(bangla.deriveFont(Font.PLAIN, 24f));
            text = "Press ENTER to skip";
            x = centerXfortext(text);
            g2.drawString(text, x, gp.scrHeight - gp.finalTileSize);
        }

        // Scroll up slowly
        endingScrollY -= 1; // Adjust speed: lower = slower, higher = faster
        endingCreditTimer++;

        // Auto-return to title after credits finish
        if (endingScrollY + baseY < -gp.finalTileSize) {
            gp.gameState = gp.titleState;
            endingScrollY = gp.scrHeight;
            endingCreditTimer = 0;
        }
    }

    public void showAreaName(int x, int y) {
        if (x >= gp.tileManager.areaBounds[0][0] && x <= gp.tileManager.areaBounds[0][2]
                && y >= gp.tileManager.areaBounds[0][1] && y <= gp.tileManager.areaBounds[0][3]) {
            gp.ui.drawAreaName("Farm");
        } else if (x >= gp.tileManager.areaBounds[1][0] && x <= gp.tileManager.areaBounds[1][2]
                && y >= gp.tileManager.areaBounds[1][1] && y <= gp.tileManager.areaBounds[1][3]) {
            gp.ui.drawAreaName("Village");
        } else if (x >= gp.tileManager.areaBounds[2][0] && x <= gp.tileManager.areaBounds[2][2]
                && y >= gp.tileManager.areaBounds[2][1] && y <= gp.tileManager.areaBounds[2][3]) {
            gp.ui.drawAreaName("Boss Arena");
        }
    }

    public void showsavemsg() {
        showgamesavescrn = true;
        if (msgTimer != null) {
            msgTimer.cancel();
        }
        msgTimer = new Timer();
        msgTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                showgamesavescrn = false;
            }
        }, 2000);
    }

    public void showdeletemsg() {
        showgamedeletedscrn = true;
        if (msgTimer != null) {
            msgTimer.cancel();
        }
        msgTimer = new Timer();
        msgTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                showgamedeletedscrn = false;
            }
        }, 2000);
    }

    public void shownosavemsg() {
        shownosavescrn = true;
        if (msgTimer != null) {
            msgTimer.cancel();
        }
        msgTimer = new Timer();
        msgTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                shownosavescrn = false;
            }
        }, 2000);
    }


    private void showMsgonscrn(String s) {
        int x = (int) (gp.finalTileSize * 0.5), y = gp.finalTileSize * 9;
        g2.setFont(Jersey.deriveFont(Font.PLAIN, 26f));
        g2.setColor(Color.WHITE);
        g2.drawString(s, x, y);
    }


    private void drawMessage() {
        int msgX = gp.finalTileSize;
        int msgY = gp.finalTileSize * 4;

        g2.setFont(Jersey.deriveFont(25f));
        for (int i = 0; i < message.size(); i++) {
            if (message.get(i) != null) {
                g2.setColor(Color.black);
                g2.drawString(message.get(i), msgX + 2, msgY + 2);
                g2.setColor(Color.white);
                g2.drawString(message.get(i), msgX, msgY);
                int counter = msgCounter.get(i) + 1;
                msgCounter.set(i, counter);
                msgY += 50;
                if (msgCounter.get(i) > 100) {
                    message.remove(i);
                    msgCounter.remove(i);
                }
            }
        }

    }

    private void drawAreaName(String name) {
        int x = (int) (gp.finalTileSize * 0.5), y = gp.finalTileSize * 11;
        g2.setFont(Jersey.deriveFont(Font.PLAIN, 30f));
        g2.setColor(Color.WHITE);
        g2.drawString(name, x, y);
    }

    private void drawInventory() {
        final int frameX = gp.finalTileSize * 9, frameY = gp.finalTileSize, frameWidth = gp.finalTileSize * 6, frameHeight = gp.finalTileSize * 5;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);


        // slot
        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotsize = gp.finalTileSize;
        for (int i = 0; i < gp.player.inventory.size(); i++) {
            g2.drawImage(gp.player.inventory.get(i).down1, slotX + 10, slotY + 10, null);
            slotX += slotsize;
            if (i == 4 || i == 9 || i == 14) {
                slotX = slotXstart;
                slotY += slotsize;
            }
        }
        // cursor
        int cursorX = slotXstart + (gp.finalTileSize * slotcol);
        int cursorY = slotYstart + (slotsize * slotrow);
        int cursorWidth = gp.finalTileSize;
        int cursorHeight = gp.finalTileSize;

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);


    }

    private void drawCharacterScrn() {

        final int frameX = gp.finalTileSize, frameY = gp.finalTileSize, frameWidth = gp.finalTileSize * 4, frameHeight = (int) (gp.finalTileSize * 9.5);
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        g2.setFont(Jersey.deriveFont(18f));
        g2.setColor(Color.white);

        int textX = frameX + 20;
        int textY = frameY + gp.finalTileSize;
        final int lineheight = 35;

        g2.drawString("Level", textX, textY);
        textY += lineheight;
        g2.drawString("Life", textX, textY);
        textY += lineheight;
        g2.drawString("Strength", textX, textY);
        textY += lineheight;
        g2.drawString("Attack", textX, textY);
        textY += lineheight;
        g2.drawString("Defense", textX, textY);
        textY += lineheight;
        g2.drawString("Exp", textX, textY);
        textY += lineheight;
        g2.drawString("Next Level exp", textX, textY);
        textY += lineheight;
        g2.drawString("Coin", textX, textY);
        textY += lineheight + 15;
        g2.drawString("Weapon", textX, textY);


        // Values
        int tailX = (frameX + frameWidth) - 30;
        // reset
        int adjust = 12;
        textY = frameY + gp.finalTileSize;
        String value;
        value = String.valueOf(gp.player.level);
        textX = getXforAlignToRightText(value, tailX + adjust);
        g2.drawString(value, textX, textY);
        textY += lineheight;

        value = gp.player.life + "/" + gp.player.maxLife;
        textX = getXforAlignToRightText(value, tailX + adjust);
        g2.drawString(value, textX, textY);
        textY += lineheight;

        value = String.valueOf(gp.player.strength);
        textX = getXforAlignToRightText(value, tailX + adjust);
        g2.drawString(value, textX, textY);
        textY += lineheight;

        value = String.valueOf(gp.player.attack);
        textX = getXforAlignToRightText(value, tailX + adjust);
        g2.drawString(value, textX, textY);
        textY += lineheight;

        value = String.valueOf(gp.player.defense);
        textX = getXforAlignToRightText(value, tailX + adjust);
        g2.drawString(value, textX, textY);
        textY += lineheight;

        value = String.valueOf(gp.player.exp);
        textX = getXforAlignToRightText(value, tailX + adjust);
        g2.drawString(value, textX, textY);
        textY += lineheight;

        value = String.valueOf(gp.player.nextlevelexp);
        textX = getXforAlignToRightText(value, tailX + adjust);
        g2.drawString(value, textX, textY);
        textY += lineheight;

        value = String.valueOf(gp.player.coin);
        textX = getXforAlignToRightText(value, tailX + adjust);
        g2.drawString(value, textX, textY);
        textY += lineheight;

        g2.drawImage(gp.player.currentWeapon.down1, tailX - adjust, textY, null);


    }

    private void drawGameOverScrn() {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.scrWidth, gp.scrHeight);
        int x, y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));

        text = "Game Over!";
        // Shadow
        g2.setColor(Color.BLACK);
        x = centerXfortext(text);
        y = gp.finalTileSize * 4;
        g2.drawString(text, x, y);
        // Main
        g2.setColor(Color.white);
        g2.drawString(text, x - 4, y - 4);

        y += gp.finalTileSize * 2;
        drawMenuOption(g2, "Retry", 0, commandNum, y);
        y += gp.finalTileSize;
        drawMenuOption(g2, "Exit", 1, commandNum, y);

    }

    public void drawDialogueScrn() {
        // Window
        int x = gp.finalTileSize * 3, y = gp.finalTileSize / 2, width = (gp.scrWidth - (gp.finalTileSize * 4)), height = gp.finalTileSize * 4;
        drawSubWindow(x, y, width, height);

        g2.setFont(bangla.deriveFont(Font.PLAIN, dlgFontsize));
        x += gp.finalTileSize;
        y += gp.finalTileSize;
        g2.drawString(currentDialogue, x, y);
    }

    public void inputplayername() {
        g2.setColor(new Color(0, 0, 0, 250));
        g2.fillRect(0, 0, gp.scrWidth, gp.scrHeight);

        // Title text
        g2.setFont(bangla.deriveFont(Font.BOLD, 40f));
        g2.setColor(Color.WHITE);
        String text = "Enter Your Name:";
        int x = centerXfortext(text);
        int y = gp.finalTileSize * 4;
        g2.drawString(text, x, y);

        // Input box background (dark grey)
        y += gp.finalTileSize / 2;
        int boxX = gp.finalTileSize;
        int boxWidth = gp.scrWidth - gp.finalTileSize * 2;
        int boxHeight = gp.finalTileSize;

        g2.setColor(new Color(50, 50, 50)); // Dark grey
        g2.fillRect(boxX, y, boxWidth, boxHeight);

        // Optional: Add a border to the input box
        g2.setColor(Color.WHITE);
        g2.drawRect(boxX, y, boxWidth, boxHeight);

        // Player name input text
        g2.setFont(bangla.deriveFont(Font.PLAIN, 36f));
        g2.setColor(Color.WHITE);
        // Add some padding from the left edge
        y += boxHeight - 15; // Position text vertically centered in the box
        if (gp.player.playername.isEmpty()) {
            // Show placeholder text if name is empty
            x = boxX + gp.finalTileSize * 3;
            g2.setColor(new Color(150, 150, 150)); // Vague/grey color
            g2.drawString("Maximum 20 characters", x, y);
        } else {
            // Show actual player name
            x = boxX + gp.finalTileSize * 4;
            g2.setColor(Color.WHITE);
            g2.drawString(gp.player.playername, x, y);
        }

        // Instruction text
        g2.setFont(bangla.deriveFont(Font.PLAIN, 24f));
        text = "Press ENTER to confirm";
        x = centerXfortext(text);
        y += gp.finalTileSize + 20;
        g2.drawString(text, x, y);
    }

    public void drawLevelupScrn() {
        // Window
        int x = gp.finalTileSize * 5, y = gp.finalTileSize, width = (gp.finalTileSize * 6), height = gp.finalTileSize * 2;
        drawSubWindow(x, y, width, height);

        g2.setFont(Jersey.deriveFont(Font.PLAIN, 22f));
        x = centerXfortext("Level UP!!");
        y += gp.finalTileSize;
        g2.drawString("Level UP!!", x, y);
    }

    public void drawPlayerLife() {
        int x = gp.finalTileSize / 2;
        int y = gp.finalTileSize / 2;
        int i = 0;

        // Draw Blank heart
        while (i < gp.player.maxLife / 2) {
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.finalTileSize;
        }
        // reset
        x = gp.finalTileSize / 2;
        y = gp.finalTileSize / 2;
        i = 0;

        // Draw Current Life
        while (i < gp.player.life) {
            g2.drawImage(heart_half, x, y, null);
            i++;
            if (i < gp.player.life) {
                g2.drawImage(heart_full, x, y, null);
                i++;
                x += gp.finalTileSize;
            }
        }
    }

    public void drawPauseScrn() {
        // 1. Draw semi-transparent dark overlay
        g2.setColor(new Color(0, 0, 0, 150)); // black with 150 alpha
        g2.fillRect(0, 0, gp.scrWidth, gp.scrHeight);

        // 2. Draw "Paused" text on top
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, titleFontsize));
        String text = "Paused";
        int x = centerXfortext(text);
        int y = gp.finalTileSize * 3;

        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);

        // === Menu ===
        y += gp.finalTileSize * 3; // menu start position


        for (int i = 0; i < pauseItems.length; i++) {
            int textY = y + (i * gp.finalTileSize);
            drawMenuOption(g2, pauseItems[i], i, commandNum, textY);
        }
    }

    public void drawOptionScrn() {

        if (gp.previousState == gp.pauseState) {
            // Coming from pause → show semi-transparent overlay
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, gp.scrWidth, gp.scrHeight);
        } else if (gp.previousState == gp.titleState) {
            // Coming from title → solid background
            g2.setColor(new Color(0, 0, 0, 250));
            g2.fillRect(0, 0, gp.scrWidth, gp.scrHeight);
        } else {
            // Default fallback (e.g., if opened from play state directly)
            g2.setColor(new Color(0, 0, 0, 200));
            g2.fillRect(0, 0, gp.scrWidth, gp.scrHeight);
        }

        // === Title ===
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "Options";
        int x = centerXfortext(text);
        int y = gp.finalTileSize * 3;

        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);

        y += gp.finalTileSize * 3;

        for (int i = 0; i < optionsItems.length; i++) {
            String optionText = optionsItems[i];

            // Add dynamic values
            if (i == 0) {
                optionText = "Volume: " + (int) (musicVolume * 100) + "%";
            }
            if (i == 1) {
                optionText = "SE: " + (seEnabled ? "ON" : "OFF");
            }

            int textY = y + (i * gp.finalTileSize);
            drawMenuOption(g2, optionText, i, commandNum, textY);
        }

    }

    public void drawTitleScrn(Graphics2D g2) {
        // === Clear Background ===
        g2.setColor(Color.BLACK); // solid black background
        g2.fillRect(0, 0, gp.scrWidth, gp.scrHeight);

        // === Title ===
        g2.setFont(BBh);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, titleFontsize + 5F));

        String text = "Re-Zero";
        int x = centerXfortext(text);
        int y = gp.finalTileSize * 2;

        // Shadow
        g2.setColor(Color.gray);
        g2.drawString(text, x + 5, y + 5);

        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        g2.setFont(Jersey);

        // Character Image
        x = gp.scrWidth / 2 - (gp.finalTileSize * 2) / 2;
        y += gp.finalTileSize * 2 - 62;
        g2.drawImage(gp.player.standbyFront, x, y, gp.finalTileSize * 2, gp.finalTileSize * 2, null);

        // === Menu ===
        y += (int) (gp.finalTileSize * 3.5);

        for (int i = 0; i < menuItems.length; i++) {
            int textY = y + (i * gp.finalTileSize);
            drawMenuOption(g2, menuItems[i], i, commandNum, textY);
        }
    }

    private int centerXfortext(String text) {
        int textLen = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.scrWidth / 2 - textLen / 2;
    }

    private int getXforAlignToRightText(String text, int tailX) {
        int textLen = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return tailX - textLen;
    }

    private void drawMenuOption(Graphics2D g2, String text, int optionIndex, int commandNum, int baseY) {
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, otherFontsize));

        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();

        // Center X based on text width
        int x = centerXfortext(text);

        if (commandNum == optionIndex) {
            // Draw white background
            g2.setColor(Color.LIGHT_GRAY);
            int paddingX = 12;
            int paddingY = 10;
            g2.fillRect(x - paddingX, baseY - textHeight, textWidth + paddingX * 2, textHeight + paddingY);

            // Draw black text
            g2.setColor(Color.BLACK);
            g2.drawString(text, x, baseY);
        } else {
            // Unselected option → white text only
            g2.setColor(Color.WHITE);
            g2.drawString(text, x, baseY);
        }
    }

    public void drawControls(Graphics2D g2) {
        int frameX = gp.finalTileSize;
        int frameY = gp.finalTileSize;
        int frameWidth = gp.scrWidth - (gp.finalTileSize * 2);  // Slightly wider margins for breathing room
        int frameHeight = gp.scrHeight - (int) (gp.finalTileSize * 1.5);  // Taller to accommodate all text + back button

        // Background subwindow
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));

        int x, y;
        String text;

        // Title - positioned with more top margin
        text = "Controls";
        x = centerXfortext(text);
        y = frameY + (int) (gp.finalTileSize * 1.5);
        g2.drawString(text, x, y);

        // Switch to smaller font and add extra spacing after title
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
        y += gp.finalTileSize * 2;  // Increased spacing after title for better layout

        // Movement controls with consistent spacing
        text = "Move Up     : W / Up Arrow";
        x = gp.finalTileSize * 2;
        g2.drawString(text, x, y);
        y += gp.finalTileSize;  // Slightly more vertical space between lines

        text = "Move Down   : S / Down Arrow";
//        x = centerXfortext(text);
        g2.drawString(text, x, y);
        y += gp.finalTileSize;

        text = "Move Left   : A / Left Arrow";
//        x = centerXfortext(text);
        g2.drawString(text, x, y);
        y += gp.finalTileSize;

        text = "Move Right  : D / Right Arrow";
//        x = centerXfortext(text);
        g2.drawString(text, x, y);
        y += gp.finalTileSize;  // Extra space before pause

        // Pause
        text = "Pause Game  : P";
//        x = centerXfortext(text);
        g2.drawString(text, x, y);
        y += gp.finalTileSize;  // More space before back button

        // Back option - ensure it's near bottom but not cut off
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
        int backY = y + gp.finalTileSize;
        drawMenuOption(g2, "Back", 0, commandNum, backY);
    }

    public void drawSubWindow(int x, int y, int width, int height) {

        Color c = new Color(0, 0, 0, 220);
        g2.setColor(c);

        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(x + 2, y + 2, width, height, 25, 25);
    }

    public void increseVolume() {

        if (musicVolume < 1.0F) {
            musicVolume += 0.1F;
        }

        // Apply to music
        gp.music.setVolume(musicVolume);
        gp.config.saveConfig();

    }

    public void decreseVolume() {

        if (musicVolume > 0.1F) {
            musicVolume -= 0.1F;
        }

        // Apply to music
        gp.music.setVolume(musicVolume);
        gp.config.saveConfig();

    }

    public void toggleSe() {
        seEnabled = !seEnabled;
        gp.config.saveConfig();

    }


}
