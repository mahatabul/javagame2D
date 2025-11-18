package Main;

import Entity.Player;

import java.io.*;

public class DataStore {
    GamePanel gp;
    private int xPos, yPos;
    private int lvl, life, str, atk, def, exp, nxtLvlExp, coin;
    private String wpn;
    private final String ReadWritepath = new File("res/saveData.txt").getAbsolutePath();

    private String mapPath;

    public DataStore(int x, int y,int lv,int lf, int st, int at, int df, int xp, int nlxp, int coin, String wepn, String mpath){
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
    }

    public DataStore(GamePanel gp){
        this.gp = gp;

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

            bw.write(1+"\n");
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
            bw.flush(); // ensure data is written to disk
            System.out.println("Game saved to: " + ReadWritepath);

        }catch (Exception e){
            throw new RuntimeException("Error saving data: " + e.getMessage());
        }
    }

    public void readData(){
        File file = new File(ReadWritepath);
        if (!file.exists()){
            System.out.println("No save file found, starting a new game.");
            return;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            if (line == null || Integer.parseInt(line.trim()) == 0) {
                System.out.println("Save file empty or invalid.");
                return;
            }

            // Read sequentially
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

            System.out.println("Save loaded successfully from: " + ReadWritepath);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading save data: " + e.getMessage());
        }
    }

    public void deleteData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ReadWritepath, false))) {
            bw.write("0\n");
            bw.flush();
            System.out.println("pSave data deleted.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

}
