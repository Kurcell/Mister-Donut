package Game.Entities.Static;

import Main.Handler;
import Resources.Images;

import java.awt.*;

public class RadioCounter extends BaseCounter {
	public int timeWindow = 0;
    public RadioCounter(int xPos, int yPos, Handler handler) {
        super(Images.kitchenCounter[3], xPos, yPos,96,102,handler);

    }


    @Override
    public void interact() {

    }

    @Override
    public void render(Graphics g){
        
    	if(timeWindow!=0) {
        	g.drawImage(Images.tint(sprite,0,0,255),xPos,yPos,width,height,null);
        }
        else {
        	g.drawImage(sprite,xPos,yPos,width,height,null);
        }
        g.drawImage(Images.radio,xPos+10,yPos-30,width-20,height-30,null);
    }
}