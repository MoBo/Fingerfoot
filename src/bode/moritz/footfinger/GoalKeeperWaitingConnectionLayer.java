package bode.moritz.footfinger;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemLabel;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

public class GoalKeeperWaitingConnectionLayer extends CCColorLayer{

	
	
	private CGSize winSize;

	protected GoalKeeperWaitingConnectionLayer(ccColor4B color) {
		super(color);
		winSize = CCDirector.sharedDirector().displaySize();
		CCLabel title = CCLabel.makeLabel("Waiting For Shooter...", "DroidSans", 24);
		title.setPosition(CGPoint.ccp(winSize.getWidth()/2f, winSize.getHeight()/2f));
		title.setColor(ccColor3B.ccBLACK);
		
		CCLabel information = CCLabel.makeLabel("He has to connect to 127.0.0.1", "DroidSans", 24);
		information.setPosition(CGPoint.ccp(winSize.getWidth()/2f, winSize.getHeight()/2f-100f));
		information.setColor(ccColor3B.ccBLACK);
		
		addChild(title);
		addChild(information);
	}
	
	public void menuCallbackConfig(Object sender){
		
	}
	
	public static CCScene scene()
	{
	    CCScene scene = CCScene.node();
	    CCLayer layer = new GoalKeeperWaitingConnectionLayer(ccColor4B.ccc4(255, 255, 255, 255));
	 
	    scene.addChild(layer);
	 
	    return scene;
	}

}
