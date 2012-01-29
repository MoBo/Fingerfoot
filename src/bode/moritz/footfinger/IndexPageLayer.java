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
import org.cocos2d.types.ccColor4B;

public class IndexPageLayer extends CCColorLayer {

	protected IndexPageLayer(ccColor4B color) {
		super(color);
		this.setIsTouchEnabled(true);
		
		
		CCSprite background = CCSprite.sprite("backgroundIn.png");
		background.setScale(0.5f);
		background.setAnchorPoint(CGPoint.ccp(0f, 0f));
		
		addChild(background);
		
		CCMenuItem startItem = CCMenuItemImage.item("play_btn.png", "play_btn.png", this, "startClick");
		startItem.setScale(0.5f);
		CCMenuItem helpItem = CCMenuItemImage.item("how_to_btn.png", "how_to_btn.png", this, "helpClick");
		helpItem.setScale(0.5f);
		CCMenu menu = CCMenu.menu(startItem, helpItem);
        menu.alignItemsVertically(0);
        
        addChild(menu);
	}

	public void startClick(Object sender){
		CCDirector.sharedDirector().replaceScene(MainMenuLayer.scene());
		//FootFingerActivity.getInstance().startGoalkeeper();
	}
	
	public void helpClick(Object sender){
		
	}

	public static CCScene scene()
	{
	    CCScene scene = CCScene.node();
	    CCLayer layer = new IndexPageLayer(ccColor4B.ccc4(255, 255, 255, 255));
	 
	    scene.addChild(layer);
	 
	    return scene;
	}
}
