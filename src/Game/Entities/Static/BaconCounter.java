package Game.Entities.Static;

import Main.Handler;
import Resources.Images;

public class BaconCounter extends BaseCounter {
    public BaconCounter(int xPos, int yPos, Handler handler) {
        super(Images.kitchenCounter[8], xPos, yPos,96,102,handler);
        item = Item.bacon;
    }

}
