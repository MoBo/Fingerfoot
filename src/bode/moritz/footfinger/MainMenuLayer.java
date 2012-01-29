package bode.moritz.footfinger;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;



public class MainMenuLayer extends CCColorLayer {


	CGSize winSize;
	
	protected MainMenuLayer(ccColor4B color) {
		super(color);
		
		this.setIsTouchEnabled(true);
		CCSprite background = CCSprite.sprite("intro/intro_bg.png");
		float winScaleWidthFactor = (float) (CCDirector.sharedDirector().displaySize().getWidth()/960.0f);
		winSize = CCDirector.sharedDirector().displaySize();
		
		background.setScale(winScaleWidthFactor);
		background.setAnchorPoint(CGPoint.ccp(0f, 0f));
		addChild(background);
		
		
		CCSprite text = CCSprite.sprite("selection/text_mainmenu.png");
		text.setScale(winScaleWidthFactor);
		text.setPosition(CGPoint.ccp(CCDirector.sharedDirector().displaySize().getWidth()/2, 650.0f));
		addChild(text);
		
		CCMenuItem keeperItem = CCMenuItemImage.item("selection/keeper_btn.png", "selection/keeper_btn_p.png", this, "keeperClick");
		CCMenuItem shooterItem = CCMenuItemImage.item("selection/shooter_btn.png", "selection/shooter_btn_p.png", this, "shooterClick");
		CCMenuItem backItem = CCMenuItemImage.item("selection/prev_btn.png", "selection/prev_btn_p.png", this, "prevClick");
		
		shooterItem.setScale(winScaleWidthFactor);
		shooterItem.setPosition(0f, +50f);
		keeperItem.setScale(winScaleWidthFactor);
		keeperItem.setPosition(0f, -50f);
		backItem.setScale(winScaleWidthFactor);
		backItem.setPosition(0f, -300f);
		//backItem.setPosition(CGPoint.ccp(winSize.getWidth()/2f, 100f));
		
		CCMenu menu = CCMenu.menu(keeperItem, shooterItem, backItem);
		menu.setAnchorPoint(CCDirector.sharedDirector().displaySize().getWidth()/2, 400.0f);
        //menu.alignItemsVertically();
        
        addChild(menu);
	}

	public void keeperClick(Object sender){
//		boolean landscape = CCDirector.sharedDirector().getLandscape();
//      CCDirector.sharedDirector().setLandscape(!landscape);
        
		CCDirector.sharedDirector().replaceScene(GoalKeeperWaitingConnectionLayer.scene());
		//CCDirector.sharedDirector().setDeviceOrientation(CCDirector.kCCDeviceOrientationLandscapeLeft);
	}
	
	public void shooterClick(Object sender){
		CCDirector.sharedDirector().replaceScene(ShooterConnectLayer.scene());
	}
	
	public void prevClick(Object sender){
		CCDirector.sharedDirector().replaceScene(IndexPageLayer.scene());
	}
	
	
	public static CCScene scene()
	{
	    CCScene scene = CCScene.node();
	    CCLayer layer = new MainMenuLayer(ccColor4B.ccc4(255, 255, 255, 255));
	    
	    scene.addChild(layer);
	 
	    return scene;
	}

	@Override
	public void onEnterTransitionDidFinish() {
		// TODO Auto-generated method stub
		super.onEnterTransitionDidFinish();
	}
	

}
