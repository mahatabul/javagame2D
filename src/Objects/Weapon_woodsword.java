package Objects;

import Entity.Entity;
import Main.GamePanel;

public class Weapon_woodsword extends Entity {
    public Weapon_woodsword(GamePanel gp) {
        super(gp);
        name = "Wood Sword";
        down1 = setup("/weapons&shields/wood_sword",30,30);
        attackValue=1;
    }
}
