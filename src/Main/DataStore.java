package Main;

import Entity.Player;

import java.io.*;
import java.util.Arrays;

public class DataStore {
    GamePanel gp;
    private int xPos, yPos;
    private int lvl, life, str, atk, def, exp, nxtLvlExp, coin, xpernd, dmgtkn;
    private long pltm;
    private String wpn;
    private String playername;
    // Save files in user's home directory
    private static final String GAME_DIR = System.getProperty("user.home") + "/.rezero-game/";
    private final String ReadWritepath;
    private final String scoreBoardPath;

    private String mapPath;

    //scoreboard!!! scoreboard!!!
    public int[] scoreBoard = new int[]{-1, -1};

    public DataStore(int x, int y, int lv, int lf, int st, int at, int df, int xp, int nlxp, int coin, String wepn, String mpath, String playername, String readWritepath, String scoreBoardPath, String scoreBoardPath1){
        this.xPos = x;
        this.yPos = y;
        this.mapPath = mpath;

        this.lvl = lv;
        this.life = lf;
        this.str = st;
        this.atk = at;
        this.def = df;
        this.exp = xp;
        this.nxtLvlExp = nlxp;
        this.coin = coin;
        this.wpn = wepn;
        this.playername = playername;

        // initialize paths
        File gameDir = new File(GAME_DIR);
        if (!gameDir.exists()) {
            gameDir.mkdirs();
        }
        this.ReadWritepath = GAME_DIR + "saveData.txt";
        this.scoreBoardPath = GAME_DIR + "scoreBoard.txt";
    }

    public DataStore(GamePanel gp){
        this.gp = gp;
        // creates game directory if it doesn't exist
        File gameDir = new File(GAME_DIR);
        if (!gameDir.exists()) {
            gameDir.mkdirs();
        }

        // setting paths in user directory
        ReadWritepath = GAME_DIR + "saveData.txt";
        scoreBoardPath = GAME_DIR + "scoreBoard.txt";
    }
    public void applyToPlayer(Player player) {
        player.entityWorldXPos = this.xPos;
        player.entityWorldYPos = this.yPos;
        player.level = this.lvl;
        player.life = this.life;
        player.strength = this.str;
        player.attack = this.atk;
        player.defense = this.def;
        player.exp = this.exp;
        player.nextlevelexp = this.nxtLvlExp;
        player.coin = this.coin;
        player.currentWeapon.name = this.wpn;
        player.playername = this.playername;
        player.playTime = this.pltm;
        player.totalXpEarned = this.xpernd;
        player.dmgTaken = this.dmgtkn;
    }


    public void saveData(){

        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(ReadWritepath, false));
            // saving only when necessary
            // doing before crashes with null pointer exception

            this.xPos = gp.player.entityWorldXPos;
            this.yPos = gp.player.entityWorldYPos;
            this.mapPath = gp.tileManager.maps[gp.tileManager.currentMapIdx];

            this.lvl = gp.player.level;
            this.life = gp.player.life;
            this.str = gp.player.strength;
            this.atk = gp.player.attack;
            this.def = gp.player.defense;
            this.exp = gp.player.exp;
            this.nxtLvlExp = gp.player.nextlevelexp;
            this.coin = gp.player.coin;
            this.wpn = gp.player.currentWeapon.name;
            this.playername = gp.player.playername;
            this.pltm = gp.player.playTime;
            this.xpernd = gp.player.totalXpEarned;
            this.dmgtkn = gp.player.dmgTaken;

            bw.write(1+"\n");
            bw.write(playername +"\n");
            bw.write(mapPath+"\n");
            bw.write(xPos+"\n");
            bw.write(yPos+"\n");

            bw.write(lvl+"\n");
            bw.write(life+"\n");
            bw.write(str+"\n");
            bw.write(atk+"\n");
            bw.write(def+"\n");
            bw.write(exp+"\n");
            bw.write(nxtLvlExp+"\n");
            bw.write(coin+"\n");
            bw.write(wpn+"\n");
            bw.write(pltm+"\n");
            bw.write(xpernd+"\n");
            bw.write(dmgtkn+"\n");
            bw.flush(); // ensure data is written to disk


        }catch (Exception e){
            throw new RuntimeException("Error saving data: " + e.getMessage());
        }
    }

    public void readData(){
        File file = new File(ReadWritepath);
        if (!file.exists()){
            return;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            if (line == null || Integer.parseInt(line.trim()) == 0) {
                return;
            }

            // Read sequentially
            playername = br.readLine();
            mapPath = br.readLine();
            xPos = Integer.parseInt(br.readLine());
            yPos = Integer.parseInt(br.readLine());
            lvl = Integer.parseInt(br.readLine());
            life = Integer.parseInt(br.readLine());
            str = Integer.parseInt(br.readLine());
            atk = Integer.parseInt(br.readLine());
            def = Integer.parseInt(br.readLine());
            exp = Integer.parseInt(br.readLine());
            nxtLvlExp = Integer.parseInt(br.readLine());
            coin = Integer.parseInt(br.readLine());
            wpn = br.readLine();
            pltm = Long.parseLong(br.readLine());
            xpernd = Integer.parseInt(br.readLine());
            dmgtkn = Integer.parseInt(br.readLine());

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading save data: " + e.getMessage());
        }
    }

    public void deleteData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ReadWritepath, false))) {
            bw.write("0\n");
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean loadScoreBoard() {
        File file = new File(scoreBoardPath);
        if (!file.exists()) {
            return false;
        }
        // reset
        Arrays.fill(scoreBoard, -1);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int i = 0;

            while ((line = br.readLine()) != null && i < scoreBoard.length) {
                scoreBoard[i] = Integer.parseInt(line.trim());
                i++;
            }

            return i > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public void updateScoreBoard() {

        int scr = gp.player.score;

        loadScoreBoard();

        scoreBoard[0] = scr;

        if (scr > scoreBoard[1]) {
            scoreBoard[1] = scr;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(scoreBoardPath))) {
            bw.write(scoreBoard[0] + "\n");
            bw.write(scoreBoard[1] + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public boolean checkIfNewHS(){
        int scr = gp.player.score;
        if(!loadScoreBoard()){
            return true;
        }
        return scr > scoreBoard[1];
    }

    public String getMapPath() {
        return mapPath;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public int getLvl() {
        return lvl;
    }

    public int getLife() {
        return life;
    }

    public int getStr() {
        return str;
    }

    public int getAtk() {
        return atk;
    }

    public int getDef() {
        return def;
    }

    public int getExp() {
        return exp;
    }

    public int getNxtLvlExp() {
        return nxtLvlExp;
    }

    public int getCoin() {
        return coin;
    }

    public String getWpn() {
        return wpn;
    }

    public String getPlayerName() { // Added getter
        return playername;
    }

    public long getPlayTime() {return pltm;}
    public int getXpErnd() {return xpernd;}
    public int getdmgtkn() {return dmgtkn;}

}
