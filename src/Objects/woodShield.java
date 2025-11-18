package Objects;

import Entity.Entity;
import Main.GamePanel;

public class woodShield extends Entity {
    public woodShield(GamePanel gp) {
        super(gp);
        name = "Wood Shield";
        down1 = setup("/weapons&shields/woodshield",30,30);
        defenseValue=1;
    }
}
