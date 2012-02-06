package bode.moritz.footfinger;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor4B;

public class HelpScreenLayer extends CCColorLayer {

	protected HelpScreenLayer(ccColor4B color) {
		super(color);
		
		float winScaleWidthFactor = (float) (CCDirector.sharedDirector().displaySize().getWidth()/960.0f);
		
		CCSprite background = CCSprite.sprite("help_screen.png");
		background.setScale(winScaleWidthFactor);
		background.setAnchorPoint(CGPoint.ccp(0f, 0f));
		this.addChild(background);
	}

	public static CCScene scene() {

		CCScene scene = CCScene.node();
		CCLayer layer = new HelpScreenLayer(ccColor4B.ccc4(255, 255, 255, 255));

		scene.addChild(layer);

		return scene;
	}
}
