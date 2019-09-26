package Game.Entities.Dynamic;

import Game.Entities.Static.Burger;
import Game.Entities.Static.Item;
import Game.Entities.Static.Order;
import Main.Handler;
import Resources.Images;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Client extends BaseDynamicEntity {
    public int patience;
    public int OGpatience;
    public boolean inspector; // Client type
    public boolean antiV; // Client type
    public int eight;
    public int milestone;
    Order order;
    public boolean isLeaving = false;
    public Client(int xPos, int yPos, Handler handler) {
        super(Images.people[new Random().nextInt(9)], xPos, yPos,64,72, handler);
        inspector = (new Random().nextInt(10)==0);
        if(!inspector) { 
        	antiV= new Random().nextInt(10)==0;
        }
        patience = new Random().nextInt(120*60)+60*60;
        if (handler.getPlayer().gReview) {
        	patience+=patience*0.10;
        }
        if (handler.getPlayer().bReview) {
        	patience-=patience*0.06; 
        }
        OGpatience = patience;
        milestone = OGpatience;
        eight = (int)(OGpatience*0.08);
        
        int numOfIngredients = new Random().nextInt(4)+1;
        order = new Order();
        order.food = new Burger(xPos +72,yPos,52,22);
        ((Burger) order.food).addIngredient(Item.botBread);
        ((Burger) order.food).addIngredient(Item.burger);
        order.value += 1.0;
        for(int i = 0;i<numOfIngredients;i++){
            int ingredients = new Random().nextInt(4)+1;
            order.value += 0.5;
            switch (ingredients){
                case 1:
                    ((Burger) order.food).addIngredient(Item.lettuce);

                    break;
                case 2:
                    ((Burger) order.food).addIngredient(Item.tomato);

                    break;

                case 3:
                	((Burger) order.food).addIngredient(Item.cheese);

                	break;
                case 4:
                	((Burger) order.food).addIngredient(Item.bacon);

                	break;

            }
        }
        ((Burger) order.food).addIngredient(Item.topBread);
    }

    public void tick(){
    	patience--;

    	for (int i = 0;i<handler.getWorld().clients.size();i++) { 
    		if (handler.getWorld().clients.get(i).antiV &&
    				((handler.getWorld().clients.get(i).OGpatience 
    				- handler.getWorld().clients.get(i).patience)
    				% handler.getWorld().clients.get(i).eight==0)){
    			if(!(i+1 < 0 || i+1 >= handler.getWorld().clients.size()) && !(i-1 < 0 || i-1 >= handler.getWorld().clients.size())) {
    				if(new Random().nextInt(2)==0) {
    					handler.getWorld().clients.get(i+1).patience-=(handler.getWorld().clients.get(i+1).patience*0.04);
    				}
    				else {
    					handler.getWorld().clients.get(i-1).patience-=(handler.getWorld().clients.get(i-1).patience*0.04);
    				}
    			}
    			if(!(i+1 < 0 || i+1 >= handler.getWorld().clients.size()) && (i-1 < 0 || i-1 >= handler.getWorld().clients.size())) {
    					handler.getWorld().clients.get(i+1).patience-=(handler.getWorld().clients.get(i+1).patience*0.04);
    				}
    			if(!(i-1 < 0 || i-1 >= handler.getWorld().clients.size()) && (i+1 < 0 || i+1 >= handler.getWorld().clients.size())) {
					handler.getWorld().clients.get(i-1).patience-=(handler.getWorld().clients.get(i-1).patience*0.04);
				}
    		}
    	}

    	if(patience<=0){
    		isLeaving=true;
    		if(inspector) {
    			handler.getPlayer().bReview = true;
    			handler.getPlayer().gReview = false;
    		}
    	}
    }

    public void render(Graphics g){

        if(!isLeaving){
        	if (inspector) {
        		g.drawImage(Images.tint(Images.people[9],1.0f,((float)patience/(float)OGpatience),((float)patience/(float)OGpatience)),xPos,yPos,width,height,null);
        		g.setColor(Color.magenta);
        		g.drawString("Inspector", xPos+90, yPos+40);
            }else if (antiV){ //testing
            	g.drawImage(Images.tint(Images.people[9],1.0f,((float)patience/(float)OGpatience),((float)patience/(float)OGpatience)),xPos,yPos,width,height,null);
        		g.setColor(Color.magenta);
        		g.drawString("Anti-V", xPos+90, yPos+40);
            }
            else {
            g.drawImage(Images.tint(sprite,1.0f,((float)patience/(float)OGpatience),((float)patience/(float)OGpatience)),xPos,yPos,width,height,null);
        	}
        	g.setColor(Color.orange);
        	g.drawString(""+patience, xPos+90, yPos+30);
            ((Burger) order.food).render(g);
        }
    }

    public void move(){
        yPos+=102;
        ((Burger) order.food).y+=102;
    }
}
