package Main;

import Entity.Entity;
import Entity.Player;
import Tile.TileManager;


import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class GamePanel extends JPanel implements Runnable {

    final int originalTileSize = 16; // 16*16 tile size                             //
    final int scaling = 3;           // changeable scaling depending on screen size //

    public final int finalTileSize = originalTileSize * scaling; // tile size after scaling  //
    public final int maxScreenColumn = 16;                     // number of tile column  16  //
    public final int maxScreenRow = 12;                        // number of tile rows   12   //

    public final int scrWidth = finalTileSize * maxScreenColumn; // final screen width       //
    public final int scrHeight = finalTileSize * maxScreenRow;   // final screen height      //

    // World Settings
    public int maxWorldCol = 100;
    public int maxWorldRow = 100;
    public final int worldTotalWidth = finalTileSize * maxWorldCol;
    public final int worldTotalHeight = finalTileSize * maxWorldRow;

    // for full screen
    int scrnWidth2 = scrWidth;
    int scrnHeight2 = scrHeight;
    BufferedImage tempScreen;
    public Graphics2D g2;

    // Sound and Effects
    public Sound music = new Sound();
    public Sound se = new Sound();

    // Config

    Config config = new Config(this);

    // just a flag
    boolean flag = true;

    // time related for score
    long lastPlayTimeChecked = 0;
    boolean lastChekedFlag = false;

    // Save-Load related
    public DataStore dataStorage = new DataStore(this);


    // fps //
    int FPS = 60;

    public TileManager tileManager = new TileManager(this);
    public KeyHandler kh = new KeyHandler(this);
    Thread gameThread;

    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public AssetHandler assetHandler = new AssetHandler(this);


    // pulled ui before player to fix null pointer error for dataStore
    public UI ui = new UI(this);
    public Player player = new Player(this, this.kh);
    public Entity[] staticObjects = new Entity[10];
    public Entity[] npc = new Entity[10];
    public Entity[] monster = new Entity[30];
    ArrayList<Entity> entityList = new ArrayList<>();
    public ArrayList<Entity> projectileList = new ArrayList<>();

    // Event Handler
    public EventHandler eventHandler = new EventHandler(this);

    // Game State

    public int gameState;

    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int optionState = 3;
    public final int dialogueState = 4;
    public int previousState = 0;
    public final int gameoverstate = 5;
    public final int characterstate = 6;
    public final int inputplayernamestate = 7;
    public final int gameWinstate = 8;
    public final int startingcreditState = 9;
    public final int endingCreditState = 10;
    public final int scoreBoardState = 11;

    public boolean gameFinished = false;


    public GamePanel() {    // constructor              //
        this.setPreferredSize(new Dimension(scrWidth, scrHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(kh);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = (double) 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(gameState != playState && lastChekedFlag){
                lastChekedFlag = false;
                this.player.playTime += (currentTime - (lastPlayTimeChecked==0 ? currentTime: lastPlayTimeChecked));

            }
            if(gameState == playState && lastChekedFlag == false){
                lastChekedFlag = true;
                lastPlayTimeChecked = currentTime;
            }


            if (delta >= 1) {
                updateScr();
                painttempscreen();    // named repaint but calls public method paintComponent //
                drawToscreen();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {  //// terminal debug ////
                //System.out.println("FPS: " + drawCount);
                //System.out.println(player.playername);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void updateScr() {
        if (gameState == playState) {
            // Player
            player.update();

            // NPC
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].update();
                }
            }
            // monster
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    if (monster[i].alive && !monster[i].dying) {
                        monster[i].update();
                    }
                    if (!monster[25].alive){
                        gameState = gameWinstate;gameFinished=true;
                        break;
                    }
                    if (!monster[i].alive && i!=25) {
                        monster[i] = null;
                    }
                }
            }
            // projectile
            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                    if (projectileList.get(i).alive) {
                        projectileList.get(i).update();
                    } else {
                        projectileList.remove(i);
                    }

                }
            }
        }
    }

    public void painttempscreen() {


        if (gameState == titleState) {
            ui.draw(g2);

        } else {
            tileManager.draw(g2);   // tile drawn first and then player/entity drawn on top of painted tile //

            entityList.add(player);

            // adding npc
            for (Entity value : npc) {
                if (value != null) {
                    entityList.add(value);
                }
            }

            // adding projectiles
            for (Entity value : projectileList) {
                if (value != null) {
                    entityList.add(value);
                }
            }

            // adding monsters
            for (Entity value : monster) {
                if (value != null) {
                    entityList.add(value);
                }
            }

            // adding objects static ones!
            for (Entity staticObject : staticObjects) {
                if (staticObject != null) {
                    entityList.add(staticObject);
                }
            }
            // Sort
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity o1, Entity o2) {
                    int result = Integer.compare(o1.entityWorldYPos, o2.entityWorldYPos);
                    return result;
                }
            });

            // Now draw these entities

            for (Entity entity : entityList) {
                entity.draw(g2);

            }
            // reset this list
            entityList.clear();

            ui.draw(g2);
        }


    }

    public void setUpGame() {
        config.loadConfig();
        assetHandler.setNPC();
        assetHandler.setMonster();
        gameState = startingcreditState;
        playMusic(0);
        music.setVolume(ui.musicVolume);
        tempScreen = new BufferedImage(scrnWidth2, scrnHeight2, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();
        setFullscreen();
    }

    public void setFullscreen() {
        // get local screen device
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);

        // get full screen width and height
        scrnWidth2 = Main.window.getWidth();
        scrnHeight2 = Main.window.getHeight();

    }

    public void drawToscreen() {
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, scrnWidth2, scrnHeight2, null);
        g.dispose();
    }

    public void playMusic(int i) {

        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int index, float volume) {

        if (!ui.seEnabled) return; // block SE if disabled
        se.setFile(index);
        se.setVolume(volume);
        se.play();
    }

    public void retry() {
        player.setDefaultValue();
        assetHandler.setNPC();
        assetHandler.setMonster();
    }

    public void restart() {
        player.setDefaultValue();
        assetHandler.setMonster();
    }


}
