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



public class MainMenuLayer extends CCColorLayer {


	protected MainMenuLayer(ccColor4B color) {
		super(color);
		
		this.setIsTouchEnabled(true);
		
		
		CCMenuItem keeperItem = CCMenuItemImage.item("keeper.png", "keeper.png", this, "keeperClick");
		CCMenuItem shooterItem = CCMenuItemImage.item("shooter.png", "shooter.png", this, "shooterClick");
		CCMenu menu = CCMenu.menu(keeperItem, shooterItem);
        menu.alignItemsVertically(300f);
        
        addChild(menu);
	}

	public void keeperClick(Object sender){
//		boolean landscape = CCDirector.sharedDirector().getLandscape();
//      CCDirector.sharedDirector().setLandscape(!landscape);
//		CCDirector.sharedDirector().replaceScene(GameLayer.scene(null));
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
