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


	protected MainMenuLayer(ccColor4B color) {
		super(color);
		
		this.setIsTouchEnabled(true);
		CCSprite background = CCSprite.sprite("intro/intro_bg.png");
		float winSize = (float) (CCDirector.sharedDirector().displaySize().getWidth()/960.0);
		background.setScale(winSize);
		background.setAnchorPoint(CGPoint.ccp(0f, 0f));
		addChild(background);
		
		
		CCSprite text = CCSprite.sprite("selection/text.png");
		text.setScale(winSize);
		text.setAnchorPoint(CGPoint.ccp(CCDirector.sharedDirector().displaySize().getWidth()/2, 60.0f));
		addChild(text);
		
		CCMenuItem keeperItem = CCMenuItemImage.item("selection/keeper_btn.png", "selection/keeper_btn_p.png", this, "keeperClick");
		CCMenuItem shooterItem = CCMenuItemImage.item("selection/shooter_btn.png", "selection/shooter_btn_p.png", this, "shooterClick");
		keeperItem.setScale(winSize);
		shooterItem.setScale(winSize);
		CCMenu menu = CCMenu.menu(keeperItem, shooterItem);
		menu.setAnchorPoint(CCDirector.sharedDirector().displaySize().getWidth()/2, 400.0f);
        menu.alignItemsVertically();
        
        addChild(menu);
	}

	public void keeperClick(Object sender){
		CCDirector.sharedDirector().replaceScene(GoalKeeperWaitingConnectionLayer.scene());
	}
	
	public void shooterClick(Object sender){
		CCDirector.sharedDirector().replaceScene(ShooterConnectLayer.scene());
	}
	
	
	public static CCScene scene()
	{
	    CCScene scene = CCScene.node();
	    CCLayer layer = new MainMenuLayer(ccColor4B.ccc4(255, 255, 255, 255));
	 
	    scene.addChild(layer);
	 
	    return scene;
	}

}
