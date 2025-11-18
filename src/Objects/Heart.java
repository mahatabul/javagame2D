package Objects;

import Entity.Entity;
import Main.GamePanel;

public class Heart extends Entity {

    public Heart(GamePanel gp) {
        super(gp);
        name = "Heart";

        image = setup("/StaticObjects/heart_full");
        image2 = setup("/StaticObjects/heart_half");
        image3 = setup("/StaticObjects/heart_blank");

    }
}
