package Objects;

import Entity.Projectile;
import Main.GamePanel;

public class FireBall extends Projectile {
    public FireBall(GamePanel gp) {
        super(gp);
        name = "Fireball";
        maxLife = 50;
        life = maxLife;
        entitySpeed = 5;
        attack = 5;
        usecost = 0;
        alive = false;
        getEntityImg();
    }

    @Override
    public void getEntityImg() {

        down1 = setup("/fireball/fireball_down_1");
        down2 = setup("/fireball/fireball_down_2");
        left1 = setup("/fireball/fireball_left_1");
        left2 = setup("/fireball/fireball_left_2");
        right1 = setup("/fireball/fireball_right_1");
        right2 = setup("/fireball/fireball_right_2");
        up1 = setup("/fireball/fireball_up_1");
        up2 = setup("/fireball/fireball_up_2");
    }


}
