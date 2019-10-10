package Game.World;

import Game.Entities.Dynamic.Client;
import Game.Entities.Dynamic.Player;
import Game.Entities.Static.*;
import Main.Handler;
import Resources.Images;

import java.awt.*;
import java.util.ArrayList;

public class Restaurant_1 extends BaseWorld {
	private int count=0;
	private int capacity = 5;
	public Restaurant_1(BaseCounter[] Counters, Handler handler) {
		super(Images.floor,Counters, handler, new Player(null,0,650,handler));

	}

	public void tick(){
		count++;
		if(count >= 5*60 && !isFull()){
			count = 0;
			int cantMove = 504; // Immovable position of client right in front of PlateCounter, 96 + 102*4
			for(Client client: this.clients){
				if(client.yPos < cantMove) { // Won't move client if position is immovable
					client.move();
				}
				cantMove-=102; // Will set new immovable position equivalent to the position of the following client
			}
			this.generateClient();
		}else if(count >= 5*60 && isFull()){
			count=0;
			boolean left=false;
			Client toLeave = null;
			ArrayList<Client> toMove = new ArrayList<>();
			for (Client client : this.clients) {
				if (client.isLeaving && !left) {
					toLeave = client;
					left=true;
				}else if (left) {
					toMove.add(client);
				}
			}
			if(left){
//				handler.getPlayer().gone++;
//				if (toLeave.inspector) {
//					handler.getPlayer().bReview = true;
//					handler.getPlayer().gReview = false;
//					handler.getPlayer().money = handler.getPlayer().money/2;
//
//				}
				this.clients.remove(toLeave);
				for (Client client : toMove) {
					client.move();
				}
				this.generateClient();
			}
		}

		for(Client client: this.clients){
			client.tick(); 

		}

		for(BaseCounter counter: Counters){
			counter.tick();
		}
		handler.getPlayer().tick();
	}

	public boolean isFull(){
		return this.clients.size() >=capacity;
	}
	public void render(Graphics g){
		g.drawImage(Background,0,0,handler.getWidth(), handler.getHeight(),null);
		//        g.drawImage(Images.welcome,5,90,43,82,null);

		g.drawImage(Images.wall[0], 0, 0, handler.getWidth(), 112, null);
		g.drawImage(Images.kitchenChairTable[0],handler.getWidth()/3,190,96,96,null);
		g.drawImage(Images.kitchenChairTable[2],handler.getWidth()/3+96,240,52,52,null);
		g.drawImage(Images.kitchenChairTable[2],handler.getWidth()/3-52,240,52,52,null);

		//        g.drawImage(Images.kitchenChairTable[0],handler.getWidth()/3+handler.getWidth()/6,190,96,96,null);
		//        g.drawImage(Images.kitchenChairTable[2],handler.getWidth()/3+handler.getWidth()/6+96,240,52,52,null);
		//        g.drawImage(Images.kitchenChairTable[2],handler.getWidth()/3+handler.getWidth()/6-52,240,52,52,null);

		g.drawImage(Images.kitchenChairTable[0],handler.getWidth()/3+handler.getWidth()/3,190,96,96,null);
		g.drawImage(Images.kitchenChairTable[2],handler.getWidth()/3+handler.getWidth()/3+96,240,52,52,null);
		g.drawImage(Images.kitchenChairTable[2],handler.getWidth()/3+handler.getWidth()/3-52,240,52,52,null);

		g.drawImage(Images.kitchenChairTable[0],handler.getWidth()/3+handler.getWidth()/3,392,96,96,null);
		g.drawImage(Images.kitchenChairTable[2],handler.getWidth()/3+handler.getWidth()/3+96,412,52,52,null);
		g.drawImage(Images.kitchenChairTable[2],handler.getWidth()/3+handler.getWidth()/3-52,412,52,52,null);

		g.drawImage(Images.kitchenChairTable[0],handler.getWidth()/3,392,96,96,null);
		g.drawImage(Images.kitchenChairTable[2],handler.getWidth()/3+96,412,52,52,null);
		g.drawImage(Images.kitchenChairTable[2],handler.getWidth()/3-52,412,52,52,null);

		for(Client client: clients){
			client.render(g);
		}

		for(BaseCounter counter: Counters){
			counter.render(g);
		}
		handler.getPlayer().render(g);
	}
}
