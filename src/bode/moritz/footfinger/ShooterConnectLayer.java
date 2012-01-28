package bode.moritz.footfinger;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;



public class ShooterConnectLayer extends CCColorLayer {


	protected ShooterConnectLayer(ccColor4B color) {
		super(color);
		
		this.setIsTouchEnabled(true);
		
		
		CCMenuItem keeperItem = CCMenuItemImage.item("connect.png", "connect.png", this, "connectClick");
		CCMenu menu = CCMenu.menu(keeperItem, keeperItem);
        menu.alignItemsVertically(300f);
        
        addChild(menu);
	}

	 public void connectClick(Object sender){
		 
	 }
	
	
	public static CCScene scene()
	{
	    CCScene scene = CCScene.node();
	    CCLayer layer = new ShooterConnectLayer(ccColor4B.ccc4(255, 255, 255, 255));
	 
	    scene.addChild(layer);
	 
	    return scene;
	}

}
