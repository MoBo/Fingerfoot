package bode.moritz.footfinger;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCBezierTo;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRotateTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CCBezierConfig;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;
import org.cocos2d.utils.javolution.MathLib;

import android.content.Context;
import android.graphics.Path;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;



public class GameLayer extends CCColorLayer {


	private static final int ROTATE_FRISBEE = 1;
	private CCSprite background;
	private CCSprite ball;
	private boolean dragAndDrop = false;
	private CGSize winSize ;
	private long timeStamp;
	private float lastUpperPoint;
	private CGPoint startpoint;
	private MotionEvent savedMotionEvents;
	private float OUT_OF_BOUNDS_TOP;
	private float OUT_OF_BOUNDS_LEFT;
	private float OUT_OF_BOUNDS_RIGHT;
	private CGPoint frisbeeStartPosition;
	private Vibrator vibrator;
	
	protected GameLayer(ccColor4B color, Vibrator v) {
		super(color);
		
		vibrator = v;
		this.setIsTouchEnabled(true);
		
		
		winSize = CCDirector.sharedDirector().displaySize();
		frisbeeStartPosition = CGPoint.ccp(winSize.getWidth()/2f, 100f);
		OUT_OF_BOUNDS_TOP = winSize.getHeight()+100f;
		OUT_OF_BOUNDS_LEFT = -100f;
		OUT_OF_BOUNDS_RIGHT = winSize.getWidth()+100f;
		
		
		background = CCSprite.sprite("grass.jpg");
		background.setAnchorPoint(CGPoint.ccp(0f, 0f));
		background.setPosition(CGPoint.ccp(0, 0));
		//background.setScale(0.5f);
		
		ball = CCSprite.sprite("ball.png");
		
		
		addChild(background);
		
		addChild(ball);
		this.resetFrisbee();
		this.schedule("update");
	}
	
	private void resetFrisbee() {
		this.ball.setPosition(CGPoint.ccp(winSize.getWidth()/2f, 100f));
		this.rotateFrisbee();
	}

	private void rotateFrisbee() {
		float angle = this.ball.getRotation()+90f;
		float time;
		if(dragAndDrop){
			time = 0.2f;
		}else{
			time = 0.5f;
		}
		
		CCRotateTo actionRotate = CCRotateTo.action(time,angle);
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "rotateFrisbeeFinished");
		CCSequence actions = CCSequence.actions(actionRotate, actionMoveDone);
		ball.runAction(actions);
	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		//check if touch hits ball
		if(CGRect.containsPoint(ball.getBoundingBox(), CGPoint.ccp(event.getRawX(),winSize.getHeight()-event.getRawY()))){
			this.savedMotionEvents = MotionEvent.obtain(event);
			dragAndDrop  = true;
			new Thread(new VibrateTask()).start();
			this.timeStamp = System.currentTimeMillis();
			this.lastUpperPoint = winSize.getHeight()-event.getRawY();
			this.startpoint = CGPoint.ccp(event.getRawX(), winSize.getHeight()-event.getRawY());
		}
		return super.ccTouchesBegan(event);
	}
	
	@Override
	public boolean ccTouchesMoved(MotionEvent event) {
		if(dragAndDrop==true){
			this.addBatch(event);
			
			float yPosition = winSize.getHeight()-event.getRawY();
			
			if(yPosition>this.lastUpperPoint){
				this.lastUpperPoint = yPosition;
			}else if(lastUpperPoint-yPosition>20f){
				this.timeStamp = System.currentTimeMillis();
			}
			
			this.ball.setPosition(CGPoint.ccp(event.getX(),winSize.getHeight()-event.getRawY()));

		}
		return super.ccTouchesMoved(event);
	}
	
	
	private void addBatch(MotionEvent event) {
		savedMotionEvents.addBatch(event.getEventTime(),event.getX(),event.getY(),event.getPressure(),event.getSize()+1,0);
	}

	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		if(dragAndDrop==true){
			addBatch(event);
			float yPosition = winSize.getHeight()-event.getRawY();
			float xPosition = event.getRawX();
			if(yPosition>=this.lastUpperPoint){
				lastUpperPoint = yPosition;
				
				//Log.e("animationTime", animationTime+" ");
				float time = (float)(System.currentTimeMillis() - this.timeStamp)/1000f;
				
				// check if it was a tap?
				//if(time>0.5f){
					calculateShoot(event,time);
				//}
				
				
				
				
//				CCBezierConfig config = new CCBezierConfig();
//				config.endPosition = CGPoint.ccp(winSize.getWidth()+100f, endPoint);
//				config.controlPoint_1 = CGPoint.ccp(startpoint.x+50, startpoint.y+50);
//				config.controlPoint_2 = CGPoint.ccp(startpoint.x+100, startpoint.y+100);
//				CCBezierTo actionMove = CCBezierTo.action(animationTime, config);
				
			}
			
			
			
			dragAndDrop = false;
		}
		return super.ccTouchesEnded(event);
	}

	private float calculateLength(MotionEvent lastEvent) {
		float lastX = lastEvent.getX();
		float lastY = winSize.getHeight()-lastEvent.getY();
		
		float firstX = this.savedMotionEvents.getHistoricalX(0);
		float firstY = winSize.getHeight()-this.savedMotionEvents.getHistoricalY(0);
		
		//Log.e("length", firstX+" " + lastX);
		//Log.e("length", firstY+" " + lastY);
		
		float a = lastY - firstY;
		float b = Math.abs(firstX-lastX);
		//return c
		//Log.e("length", Math.sqrt(a*a+b*b)+"");
		return (float) Math.sqrt(a*a+b*b);
	}

	private void calculateShoot(MotionEvent lastEvent,float time) {
		MotionEvent event = savedMotionEvents;
		float lastXPosition = lastEvent.getX();
		float lastYPosition = winSize.getHeight() - lastEvent.getY();
		
		float startXPosition = 0f;
		float startYPosition =0f;
		CGPoint moveTo = null;
		
		
		float length = this.calculateLength(event);
		float speed = length/time;
		
		for (int i = event.getHistorySize()-1; i > 0; i--) {
			//Log.e("leftRight", lastYPosition + " " + (winSize.getHeight()-event.getHistoricalY(i)));
			if(lastYPosition-(winSize.getHeight()-event.getHistoricalY(i))>50f){
				startXPosition = event.getHistoricalX(i);
				startYPosition = winSize.getHeight()-event.getHistoricalY(i);
				break;
			}
		}
		
		
		float b = lastXPosition-startXPosition;
		float c = 0f;
		float a = 0f;
		float angle = 0f;
		if(b>0){
			//calculate small triangle to get angle
			Log.e("direction", "rechts");
			
			a= lastYPosition - startYPosition;
			c = (float) Math.sqrt(a*a+b*b);
			//angle = 90f-(float) Math.toDegrees(Math.asin(b/c));
			angle = (float) Math.acos(b/c);
			//calculate big triangle to get point frisbee should move to
			Log.e("ab", "a " + a + " b" + b);
			b = winSize.getWidth()+100f-startXPosition;
			a = (float) (Math.tan(angle)*b);
			c = (float) Math.sqrt(a*a+b*b);
			moveTo = CGPoint.ccp(winSize.getWidth()+100f, startYPosition+a);
		}else{
			//calculate small triangle to get angle
			Log.e("direction", "links");
			a= lastYPosition - startYPosition;
			b= b*-1;
			c = (float) Math.sqrt(a*a+b*b);
			//angle = 90f-(float) Math.toDegrees(Math.asin(b/c));
			angle = (float) Math.acos(b/c);
			//calculate big triangle to get point frisbee should move to
			//Log.e("ab", "a " + a + " b" + b);
			b = 10f+startXPosition;
			a = (float) (Math.tan(angle)*b);
			c = (float) Math.sqrt(a*a+b*b);
			moveTo = CGPoint.ccp(-10f, startYPosition+a);
		}
		Log.e("length",c+" " +moveTo.y + " " + moveTo.x);
		// check if not flying backward
		if(ball.getPosition().y<moveTo.y){
			float animationTime = c/speed;
			
			CCMoveTo actionMove = CCMoveTo.action(animationTime, moveTo);
			CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "spriteMoveFinished");
			CCSequence actions = CCSequence.actions(actionMove, actionMoveDone);
			
			ball.runAction(actions);
		}
		
	}
	
	public void update(float dt)
	{
		// check if frisbee out of visible position
		float yPosition = this.ball.getPosition().y;
		float xPosition = this.ball.getPosition().x;
		if(yPosition>OUT_OF_BOUNDS_TOP||xPosition<=OUT_OF_BOUNDS_LEFT||xPosition>=OUT_OF_BOUNDS_RIGHT||Float.isNaN(xPosition)){
			this.ball.stopAllActions();
			this.resetFrisbee();
		}
	}
	
	public static CCScene scene(Vibrator v)
	{
	    CCScene scene = CCScene.node();
	    CCLayer layer = new GameLayer(ccColor4B.ccc4(255, 255, 255, 255),v);
	 
	    scene.addChild(layer);
	 
	    return scene;
	}

	public void spriteMoveFinished(Object sender)
	{
		this.resetFrisbee();
	}
	
	public void rotateFrisbeeFinished(Object sender){
		this.rotateFrisbee();
	}
	
	private class VibrateTask implements Runnable {
	    
	 

		@Override
		public void run() {
			 while (dragAndDrop) {
					vibrator.vibrate(200);
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						//doNothing
					}
				}
		         vibrator.cancel();
		}}
}
