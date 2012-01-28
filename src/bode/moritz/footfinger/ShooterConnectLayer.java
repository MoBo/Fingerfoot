package bode.moritz.footfinger;

import java.io.IOException;
import java.net.UnknownHostException;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;

import android.util.Log;
import bode.moritz.footfinger.network.NetworkControllerClient;



public class ShooterConnectLayer extends CCColorLayer {

	private final String IP_TO_CONNECT_TO = "134.102.25.228";
	private final int PORT_TO_CONNECT_TO = 8080;
	private NetworkControllerClient networClient;
	protected ShooterConnectLayer(ccColor4B color) {
		super(color);
		
		this.setIsTouchEnabled(true);
		
		
		CCMenuItem keeperItem = CCMenuItemImage.item("enterip/shooter.png", "enterip/shooter_p.png", this, "connectClick");
		CCMenu menu = CCMenu.menu(keeperItem, keeperItem);
        menu.alignItemsVertically(300f);
        
        addChild(menu);
	}

	 public void connectClick(Object sender){
		 try {
			this.networClient = new NetworkControllerClient();
			this.networClient.connectSocket(this.IP_TO_CONNECT_TO, this.PORT_TO_CONNECT_TO);
			CCDirector.sharedDirector().replaceScene(ShooterLayer.scene(networClient));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	
	
	public static CCScene scene()
	{
	    CCScene scene = CCScene.node();
	    CCLayer layer = new ShooterConnectLayer(ccColor4B.ccc4(255, 255, 255, 255));
	 
	    scene.addChild(layer);
	 
	    return scene;
	}

}
