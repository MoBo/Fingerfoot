package bode.moritz.frisboros;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor4B;

public class HelpScreenLayer extends CCColorLayer {

	protected HelpScreenLayer() {
		super(ccColor4B.ccc4(255, 255, 255, 255));

		float winScaleWidthFactor = (float) (CCDirector.sharedDirector()
				.displaySize().getWidth() / 960.0f);

		CCSprite background = CCSprite.sprite("help_screen.png");
		background.setScale(winScaleWidthFactor);
		background.setAnchorPoint(CGPoint.ccp(0f, 0f));
		this.addChild(background);
	}

}
