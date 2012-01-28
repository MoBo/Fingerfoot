package bode.moritz.footfinger;

import java.io.IOException;
import java.net.UnknownHostException;

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

import android.util.Log;
import bode.moritz.footfinger.network.NetworkControllerClient;

public class ShooterLayer extends CCColorLayer {

	private final String IP_TO_CONNECT_TO = "134.102.25.228";
	private final int PORT_TO_CONNECT_TO = 8080;
	private NetworkControllerClient networClient;

	protected ShooterLayer(ccColor4B color,
			NetworkControllerClient networkControllerClient) {
		super(color);
		this.networClient = networkControllerClient;
		this.setIsTouchEnabled(true);

		CCMenuItem shootItem = CCMenuItemImage.item("shoot.png", "shoot.png",
				this, "shootClick");
		CCMenu menu = CCMenu.menu(shootItem, shootItem);
		menu.alignItemsVertically(300f);

		addChild(menu);
	}

	public void shootClick(Object sender) {
		if (this.networClient.isConnected()) {
			
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Log.e("Server Response", networClient
								.transmitAndGetResponse(""+(int)(Math.round(Math.random()*6d))));
					} catch (IOException e) {
						// do nothing
					}

				}
			}).start();
		}
	}

	public static CCScene scene(NetworkControllerClient networkControllerClient) {

		CCScene scene = CCScene.node();
		CCLayer layer = new ShooterLayer(ccColor4B.ccc4(255, 255, 255, 255),
				networkControllerClient);

		scene.addChild(layer);

		return scene;
	}

}
