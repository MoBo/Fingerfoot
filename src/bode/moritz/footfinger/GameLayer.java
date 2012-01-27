package bode.moritz.footfinger;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;


import android.util.Log;
import android.view.MotionEvent;



public class GameLayer extends CCColorLayer {


	private CCSprite background;
	private CCSprite ball;
	private boolean dragAndDrop = true;
	private CGSize winSize;

	protected GameLayer(ccColor4B color) {
		super(color);
		
		
		this.setIsTouchEnabled(true);
		
		winSize = CCDirector.sharedDirector().displaySize();
		
		background = CCSprite.sprite("grass.jpg");
		background.setAnchorPoint(CGPoint.ccp(0f, 0f));
		background.setPosition(CGPoint.ccp(0, 0));
		
		ball = CCSprite.sprite("ball.png");
		ball.setPosition(CGPoint.ccp(winSize.getWidth()/2f, winSize.getHeight()/2f));
		
		addChild(background);
		
		addChild(ball);
	}
	
	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		//check if touch hits ball
		if(CGRect.containsPoint(ball.getBoundingBox(), CGPoint.ccp(event.getRawX(),winSize.getHeight()-event.getRawY()))){
			dragAndDrop  = true;
		}
		return super.ccTouchesBegan(event);
	}
	
	@Override
	public boolean ccTouchesMoved(MotionEvent event) {
		if(dragAndDrop==true){
			CCMoveTo actionMove = CCMoveTo.action(0f, CGPoint.ccp(event.getRawX(),winSize.getHeight()-event.getRawY()));
			
			CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "spriteMoveFinished");
			CCSequence actions = CCSequence.actions(actionMove, actionMoveDone);
			
			ball.runAction(actions);
		}
		return super.ccTouchesMoved(event);
	}
	
	
	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		if(dragAndDrop==true){
			dragAndDrop = false;
		}
		return super.ccTouchesEnded(event);
	}
	
	public static CCScene scene()
	{
	    CCScene scene = CCScene.node();
	    CCLayer layer = new GameLayer(ccColor4B.ccc4(255, 255, 255, 255));
	 
	    scene.addChild(layer);
	 
	    return scene;
	}

	public void spriteMoveFinished(Object sender)
	{
		
	}
}
