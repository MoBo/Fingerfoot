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

public class HelpPageLayer extends CCColorLayer {

	protected HelpPageLayer(ccColor4B color) {
		super(color);
		this.setIsTouchEnabled(true);
		
		
		CCSprite background = CCSprite.sprite("intro/intro_bg.png");
		// TODO: get screen size and then scale it
		//background.setScale(0.5f);
		background.setAnchorPoint(CGPoint.ccp(0f, 0f));
		
		addChild(background);
	}

	public static CCScene scene()
	{
	    CCScene scene = CCScene.node();
	    CCLayer layer = new IndexPageLayer(ccColor4B.ccc4(255, 255, 255, 255));
	 
	    scene.addChild(layer);
	 
	    return scene;
	}
}
