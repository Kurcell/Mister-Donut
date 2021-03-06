package Game.GameStates;

import Main.Handler;
import Resources.Images;
import Display.UI.UIImageButton;
import Display.UI.UIManager;

import java.awt.*;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class LoseState extends State {

    private int count = 0;
    private UIManager uiManager;

    public LoseState(Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUimanager(uiManager);

        uiManager.addObjects(new UIImageButton(100, 280, 180, 80, Images.Restart, () -> {
            handler.getMouseManager().setUimanager(null);
            handler.getGame().reStart();
            State.setState(handler.getGame().gameState);
        }));
        

        uiManager.addObjects(new UIImageButton(160, 300+(64+16), 128, 64, Images.Quit, () -> {
            handler.getMouseManager().setUimanager(null);
            State.setState(handler.getGame().menuState);
        }));




    }

    @Override
    public void tick() {
        handler.getMouseManager().setUimanager(uiManager);
        uiManager.tick();
        count++;
        if( count>=30){
            count=30;
        }
        if(handler.getKeyManager().pbutt && count>=30){
            count=0;

            State.setState(handler.getGame().gameState);
        }


    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Images.GameOver,0,0,handler.getWidth(),handler.getHeight(),null);
        uiManager.Render(g);
        
        g.setColor(Color.black);
        g.setFont(new Font("ComicSans", Font.BOLD, 32));
        g.drawString("Earnings: " + handler.getPlayer().formatt.format(handler.getPlayer().money), handler.getWidth()/2 +80, 400);
        g.drawString("Served: " + handler.getPlayer().served, handler.getWidth()/2 +140, 320);
        g.drawString("Left: "+handler.getPlayer().gone, handler.getWidth()/2 +115, 360);

    }
}
