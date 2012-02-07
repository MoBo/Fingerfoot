package bode.moritz.frisboros;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor4B;


public class IndexPageLayer extends CCColorLayer {

	public IndexPageLayer() {
		super(ccColor4B.ccc4(255, 255, 255, 255));
		this.setIsTouchEnabled(true);

		CCSprite background = CCSprite.sprite("intro/intro_bg.png");
		float winSize = (float) (CCDirector.sharedDirector().displaySize()
				.getWidth() / 960.0f);
		background.setScale(winSize);
		background.setAnchorPoint(CGPoint.ccp(0f, 0f));

		addChild(background);

		CCMenuItem startItem = CCMenuItemImage.item("intro/start_btn.png",
				"intro/start_btn_p.png", this, "startClick");
		startItem.setScale(winSize);
		CCMenuItem helpItem = CCMenuItemImage.item("intro/how_btn.png",
				"intro/how_btn_p.png", this, "helpClick");
		helpItem.setScale(winSize);
		CCMenu menu = CCMenu.menu(startItem, helpItem);
		menu.alignItemsVertically(0);

		addChild(menu);
	}

	public void startClick(Object sender) {
		FootFingerActivity.setNextView(this.getClass(), MainMenuLayer.class);
	}

	public void helpClick(Object sender) {
		FootFingerActivity.setNextView(this.getClass(), HelpScreenLayer.class);
	}
}
