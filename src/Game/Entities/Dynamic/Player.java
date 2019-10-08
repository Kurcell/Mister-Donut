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
import java.util.Random;

public class Player extends BaseDynamicEntity {
    Item item;
    public float money;
    public DecimalFormat formatt = new DecimalFormat("#.00");
    int speed = 10;
    public int gone;
    public int served;
    int radioTicks = 0;
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
        System.out.println("radio:" +radioTicks);
        System.out.println("time:"+handler.getRadioCounter().timeWindow);
        
        if(money>=50) {
        	State.setState(handler.getGame().winState);
        }
        if(gone>=10) {
        	State.setState(handler.getGame().loseState);
        }
        
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
        if(handler.getRadioCounter().timeWindow!=0) {
        	handler.getRadioCounter().timeWindow--;
        }
        if(radioTicks!=0) {
        	radioTicks--;
        }
        if(radioTicks==1) {
        	handler.getRadioCounter().timeWindow=2*60;
        }
        
        for(BaseCounter counter: handler.getWorld().Counters){
        	if (counter instanceof RadioCounter && counter.isInteractable()){
        		if(handler.getKeyManager().attbut) {
        			radioTicks = new Random().nextInt(120*60);
        			if(handler.getRadioCounter().timeWindow!=0){
        				for(Client client : handler.getWorld().clients) {
        					client.patience = client.OGpatience;
        				}
        			}
        		}
        	}
        	if (counter instanceof PlateCounter && counter.isInteractable()){
        		if (handler.getKeyManager().fattbut) {
        			createBurger();
        		}
        		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_R)) {
        			for(int i = 0; i<handler.getWorld().clients.size();i++) {
        				ringCustomer(handler.getWorld().clients.get(i));
        			}
        		}
        		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_1) && handler.getWorld().clients.size()>0) {
        			ringCustomer(handler.getWorld().clients.get(0));
        		}
        		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_2) && handler.getWorld().clients.size()>1) {
        			ringCustomer(handler.getWorld().clients.get(1));
        		}
        		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_3) && handler.getWorld().clients.size()>2) {
        			ringCustomer(handler.getWorld().clients.get(2));
        		}
        		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_4) && handler.getWorld().clients.size()>3) {
        			ringCustomer(handler.getWorld().clients.get(3));
        		}
        		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_5) && handler.getWorld().clients.size()>4) {
        			ringCustomer(handler.getWorld().clients.get(4));
        		}
        	}
        }
    }

    private void ringCustomer(Client client) {

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
    		for(Client customer : handler.getWorld().clients) {
        		customer.patience += (customer.OGpatience)*(0.25);
        		if(customer.patience>customer.OGpatience) {
        			customer.patience = customer.OGpatience;
        		}
        	}
    		served++;


    		handler.getWorld().clients.remove(client);
    		handler.getPlayer().createBurger();
    		System.out.println("Total money earned is: " + String.valueOf(money));
    		return;

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
        g.drawString("Served: " + served +"  Left: "+gone, handler.getWidth()/2 -90, 80);
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
