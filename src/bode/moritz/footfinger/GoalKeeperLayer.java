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

public class GoalKeeperLayer extends CCColorLayer {

	protected GoalKeeperLayer(ccColor4B color) {
		super(color);
		
		CGSize winsize = CCDirector.sharedDirector().winSize();
		CCSprite ball = CCSprite.sprite("ball.png");
		ball.setPosition(CGPoint.ccp(winsize.width/2f, winsize.height/2f));
		addChild(ball);
		
		
	}


	public static CCScene scene()
	{
	    CCScene scene = CCScene.node();
	    CCLayer layer = new GoalKeeperLayer(ccColor4B.ccc4(255, 255, 255, 255));
	    
	    scene.addChild(layer);
	    CCDirector.sharedDirector().setLandscape(true);
	    
	    return scene;
	}
}
