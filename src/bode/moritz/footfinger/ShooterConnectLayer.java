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
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;

import android.util.Log;
import bode.moritz.footfinger.network.NetworkControllerClient;



public class ShooterConnectLayer extends CCColorLayer {

	private final String IP_TO_CONNECT_TO = "134.102.26.87";
	private final int PORT_TO_CONNECT_TO = 8080;
	private NetworkControllerClient networClient;
	protected ShooterConnectLayer(ccColor4B color) {
		super(color);
		
		this.setIsTouchEnabled(true);
		float winScaleWidthFactor = (float) (CCDirector.sharedDirector().displaySize().getWidth()/960.0f);
		
		CCSprite background = CCSprite.sprite("intro/intro_bg.png");
		float winSize = (float) (CCDirector.sharedDirector().displaySize().getWidth()/960.0f);
		background.setScale(winSize);
		background.setAnchorPoint(CGPoint.ccp(0f, 0f));
		
		addChild(background);
		
		CCSprite text = CCSprite.sprite("selection/text_shooter.png");
		//text.setScale(winScaleWidthFactor);
		text.setPosition(CGPoint.ccp(CCDirector.sharedDirector().displaySize().getWidth()/2, 650.0f));
		addChild(text);
		
		
		CCMenuItem shooterItem = CCMenuItemImage.item("enterip/shooter.png", "enterip/shooter_p.png", this, "connectClick");
		CCMenuItem backItem = CCMenuItemImage.item("selection/prev_btn.png", "selection/prev_btn_p.png", this, "prevClick");
		backItem.setScale(winScaleWidthFactor);
		shooterItem.setScale(winScaleWidthFactor);
		
		backItem.setScale(winScaleWidthFactor);
		backItem.setPosition(0f, -300f);
		
		CCMenu menu = CCMenu.menu(shooterItem, backItem);
        //menu.alignItemsVertically(300f);
        
        addChild(menu);
	}

	 public void connectClick(Object sender){
		 try {
			NetworkControllerClient networClient = NetworkControllerClient.getInstance();
			networClient.connectSocket(this.IP_TO_CONNECT_TO, this.PORT_TO_CONNECT_TO);
			CCDirector.sharedDirector().replaceScene(ShooterLayer.scene(FootFingerActivity.getVibrator()));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	
	public void prevClick(Object sender){
		CCDirector.sharedDirector().replaceScene(MainMenuLayer.scene());
	}
	public static CCScene scene()
	{
	    CCScene scene = CCScene.node();
	    CCLayer layer = new ShooterConnectLayer(ccColor4B.ccc4(255, 255, 255, 255));
	 
	    scene.addChild(layer);
	 
	    return scene;
	}

}
