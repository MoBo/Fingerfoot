package bode.moritz.footfinger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.menus.CCMenuItemLabel;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import android.util.Log;

public class GoalKeeperWaitingConnectionLayer extends CCColorLayer{

	
	
	private CGSize winSize;

	protected GoalKeeperWaitingConnectionLayer(ccColor4B color) {
		super(color);
		winSize = CCDirector.sharedDirector().displaySize();
//		CCLabel title = CCLabel.makeLabel("Waiting For Shooter...", "DroidSans", 24);
//		title.setPosition(CGPoint.ccp(winSize.getWidth()/2f, winSize.getHeight()/2f));
//		title.setColor(ccColor3B.ccBLACK);
//		
		
			
		
		
		
		CCLabel information = CCLabel.makeLabel(FootFingerActivity.getIPAdresse(), "DroidSans", 30f);
		information.setPosition(CGPoint.ccp(winSize.getWidth()/2f-20f, winSize.getHeight()/2f-20f));
		information.setColor(ccColor3B.ccBLACK);
//		
//		addChild(title);
		
//		
		float winScaleWidthFactor = (float) (CCDirector.sharedDirector().displaySize().getWidth()/960.0f);
		
		CCSprite background = CCSprite.sprite("intro/intro_bg.png");
		background.setScale(winScaleWidthFactor);
		background.setAnchorPoint(CGPoint.ccp(0f, 0f));
		
		addChild(background);
		
		CCSprite text = CCSprite.sprite("selection/keeper_txt.png");
		text.setScale(winScaleWidthFactor);
		text.setPosition(CGPoint.ccp(CCDirector.sharedDirector().displaySize().getWidth()/2, 650.0f));
		addChild(text);
		
		CCSprite keeperItem = CCSprite.sprite("enterip/keeper.png");
		keeperItem.setScale(winScaleWidthFactor);
		keeperItem.setPosition(winSize.getWidth()/2f, winSize.getHeight()/2f);
		addChild(keeperItem);
		
		CCMenuItem backItem = CCMenuItemImage.item("selection/prev_btn.png", "selection/prev_btn_p.png", this, "prevClick");
		backItem.setScale(winScaleWidthFactor);
		keeperItem.setScale(winScaleWidthFactor);
		
		backItem.setScale(winScaleWidthFactor);
		backItem.setPosition(0f, -300f);
		
		CCMenu menu = CCMenu.menu(backItem);
		
		addChild(menu);
		addChild(information);
		
		
		new Thread(new Runnable() {
			
			private ServerSocket server;

			@Override
			public void run() {
				try {
					server = new ServerSocket(8080);
					//Log.e("IP of my Android := ",server.g.toString());
				} catch (IOException e) {
					Log.e("print", "Could not bind Server...");
				}

				Socket client;

				
				try {
					client = server.accept();
					//Client connected
					FootFingerActivity.setClient(client);
					FootFingerActivity.setNextView(getClass(), ShooterLayer.class);
				} catch (IOException e) {
					// do nothing
				}
			}
		}).start();
	}
	
	public void prevClick(Object sender){
		FootFingerActivity.gotoPreviousView();
	}
	
	public static CCScene scene()
	{
	    CCScene scene = CCScene.node();
	    CCLayer layer = new GoalKeeperWaitingConnectionLayer(ccColor4B.ccc4(255, 255, 255, 255));
	 
	    scene.addChild(layer);
	 
	    return scene;
	}

}
