package Game.Entities.Dynamic;

import Game.Entities.Static.*;
import Game.GameStates.State;
import Main.Handler;
import Resources.Animation;
import Resources.Images;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class Player extends BaseDynamicEntity {
    Item item;
    float money;
    float tip;
    DecimalFormat formatt = new DecimalFormat("#.00");
    int speed = 10;
    private Burger burger;
    public boolean wellDone = false;
    public boolean gReview = false;
    public boolean bReview = false;
    private String direction = "right";
    private int interactionCounter = 0;
    private Animation playerAnim;
    public Player(BufferedImage sprite, int xPos, int yPos, Handler handler) {
        super(sprite, xPos, yPos,82,112, handler);
        createBurger();
        playerAnim = new Animation(120,Images.chef);
    }

    public void createBurger(){
        burger = new Burger(handler.getWidth() - 110, 100, 100, 50);

    }

    public void tick(){
        playerAnim.tick();
        
        if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)) {
        	State.setState(handler.getGame().pauseState);
        }
        
        if (handler.getKeyManager().shiftButt) {
        	speed = 4;
        } else {
        	speed = 10;
        }
        if(xPos + width >= handler.getWidth()){
            direction = "left";

        } else if(xPos <= 0){
            direction = "right";
        }
        if (direction.equals("right")){
            xPos+=speed;
        } else{
            xPos-=speed;
        }
        if (interactionCounter > 15 && handler.getKeyManager().attbut){
            interact();
            interactionCounter = 0;
        } else {
        	interactionCounter++;
        }
        for(BaseCounter counter: handler.getWorld().Counters){
        	if (counter instanceof PlateCounter && counter.isInteractable()){
        		if (handler.getKeyManager().fattbut) {
        			createBurger();
        		}
        		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_R)) {
        			ringCustomer();
        		}
        	}
        }
    }

    private void ringCustomer() {

    	for(Client client: handler.getWorld().clients){
    		boolean matched = ((Burger)client.order.food).equals(handler.getCurrentBurger());
    		if(matched){
    			money+=client.order.value;
    			if(client.patience > client.OGpatience/2) {
    				money+=client.order.value*0.15;

    			}
    			if (client.inspector) {
    				gReview = true;
    				bReview = false;
    				handler.getWorld().clients.forEach(clint -> clint.patience+=clint.patience*0.12);
    			}
            	if(wellDone) {
            		money+=client.order.value*0.12;
            		wellDone = false;
            	}

            
                handler.getWorld().clients.remove(client);
                handler.getPlayer().createBurger();
                System.out.println("Total money earned is: " + String.valueOf(money));
                return;
            }
        }
    }

    public void render(Graphics g) {
        if(direction=="right") {
            g.drawImage(playerAnim.getCurrentFrame(), xPos, yPos, width, height, null);
        }else{
            g.drawImage(playerAnim.getCurrentFrame(), xPos+width, yPos, -width, height, null);

        }
        g.setColor(Color.green);
        burger.render(g);
//        g.setColor(Color.green);
//        g.fillRect(handler.getWidth()/2-180, 3, 320, 32);;
        g.setColor(Color.white);
        g.setFont(new Font("ComicSans", Font.BOLD, 22));
        g.drawString("Earnings: " + formatt.format(money), handler.getWidth()/2 -90, 60);
    }

    public void interact(){
        for(BaseCounter counter: handler.getWorld().Counters){
            if (counter.isInteractable()){
                counter.interact();
            }
        }
    }
    public Burger getBurger(){
        return this.burger;
    }
}
