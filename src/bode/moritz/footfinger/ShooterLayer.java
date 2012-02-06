package bode.moritz.footfinger;

import java.io.IOException;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRotateTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;

import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import bode.moritz.footfinger.R;
import bode.moritz.footfinger.network.NetworkControllerClient;
import bode.moritz.footfinger.sound.SoundManager;

public class ShooterLayer extends CCColorLayer {

	private enum ACCEL_STATE {
		left, right, up, down, middleCenter
	}

	private enum DRAW_DIRECTION {
		left, middle, right
	}

	private CCSprite background;
	private CCSprite ball;
	private boolean dragAndDrop = false;
	private CGSize winSize;
	private long timeStamp;
	private float lastUpperPoint;
	private MotionEvent savedMotionEvents;
	private float OUT_OF_BOUNDS_TOP;
	private float OUT_OF_BOUNDS_LEFT;
	private float OUT_OF_BOUNDS_RIGHT;
	private Vibrator vibrator;
	private float precisionHigh = 0.2f;
	private float oldAccelY;
	private ACCEL_STATE lastAccelStateTopDown = ACCEL_STATE.up;
	private NetworkControllerClient networkClient;
	private int lastHitBox;
	private boolean shotWasSended = false;

	protected ShooterLayer() {
		super(ccColor4B.ccc4(255, 255, 255, 255));

		this.networkClient = NetworkControllerClient.getInstance();
		vibrator = FootFingerActivity.getVibrator();
		this.setIsTouchEnabled(true);
		this.setIsAccelerometerEnabled(true);

		winSize = CCDirector.sharedDirector().displaySize();
		OUT_OF_BOUNDS_TOP = winSize.getHeight() + 100f;
		OUT_OF_BOUNDS_LEFT = -100f;
		OUT_OF_BOUNDS_RIGHT = winSize.getWidth() + 100f;

		float winScaleWidthFactor = (float) (CCDirector.sharedDirector()
				.displaySize().getWidth() / 960.0f);
		background = CCSprite.sprite("background.png");
		background.setScale(winScaleWidthFactor);
		background.setAnchorPoint(CGPoint.ccp(0f, 0f));
		background.setPosition(CGPoint.ccp(0, 0));

		ball = CCSprite.sprite("ball.png");
		ball.setScale(0.5f);
		addChild(background);

		addChild(ball);

		this.resetFrisbee();
		this.schedule("update");
	}

	private void resetFrisbee() {

		this.ball.setPosition(CGPoint.ccp(winSize.getWidth() / 2f, 100f));
		this.shotWasSended = false;
		this.rotateFrisbee();
	}

	private void rotateFrisbee() {
		float angle = this.ball.getRotation() + 90f;
		float time;
		if (dragAndDrop) {
			time = 0.2f;
		} else {
			time = 0.5f;
		}

		CCRotateTo actionRotate = CCRotateTo.action(time, angle);
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this,
				"rotateFrisbeeFinished");
		CCSequence actions = CCSequence.actions(actionRotate, actionMoveDone);
		ball.runAction(actions);
	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		// check if touch hits ball
		if (CGRect.containsPoint(
				ball.getBoundingBox(),
				CGPoint.ccp(event.getRawX(),
						winSize.getHeight() - event.getRawY()))) {
			this.savedMotionEvents = MotionEvent.obtain(event);
			dragAndDrop = true;
			new Thread(new VibrateTask()).start();

			this.timeStamp = System.currentTimeMillis();
			this.lastUpperPoint = winSize.getHeight() - event.getRawY();
		}
		return super.ccTouchesBegan(event);
	}

	@Override
	public boolean ccTouchesMoved(MotionEvent event) {
		if (dragAndDrop == true) {
			this.addBatch(event);

			float yPosition = winSize.getHeight() - event.getRawY();

			if (yPosition > this.lastUpperPoint) {
				this.lastUpperPoint = yPosition;
			} else if (lastUpperPoint - yPosition > 20f) {
				this.timeStamp = System.currentTimeMillis();
			}

			this.ball.setPosition(CGPoint.ccp(event.getX(), winSize.getHeight()
					- event.getRawY()));

		}
		return super.ccTouchesMoved(event);
	}

	private void addBatch(MotionEvent event) {
		savedMotionEvents.addBatch(event.getEventTime(), event.getX(),
				event.getY(), event.getPressure(), event.getSize() + 1, 0);
	}

	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		if (dragAndDrop == true) {
			addBatch(event);
			float yPosition = winSize.getHeight() - event.getRawY();
			if (yPosition >= this.lastUpperPoint) {
				lastUpperPoint = yPosition;

				float time = (float) (System.currentTimeMillis() - this.timeStamp) / 1000f;

				calculateShoot(event, time);
			}
			dragAndDrop = false;
		}
		return super.ccTouchesEnded(event);
	}

	private float calculateLength(MotionEvent lastEvent) {
		float lastX = lastEvent.getX();
		float lastY = winSize.getHeight() - lastEvent.getY();

		float firstX = this.savedMotionEvents.getHistoricalX(0);
		float firstY = winSize.getHeight()
				- this.savedMotionEvents.getHistoricalY(0);

		float a = lastY - firstY;
		float b = Math.abs(firstX - lastX);
		return (float) Math.sqrt(a * a + b * b);
	}

	private void calculateShoot(MotionEvent lastEvent, float time) {
		MotionEvent event = savedMotionEvents;
		float lastXPosition = lastEvent.getX();
		float lastYPosition = winSize.getHeight() - lastEvent.getY();

		float startXPosition = 0f;
		float startYPosition = 0f;
		CGPoint moveTo = null;

		float length = this.calculateLength(event);
		float speed = length / time;

		for (int i = event.getHistorySize() - 1; i > 0; i--) {
			if (lastYPosition - (winSize.getHeight() - event.getHistoricalY(i)) > 50f) {
				startXPosition = event.getHistoricalX(i);
				startYPosition = winSize.getHeight() - event.getHistoricalY(i);
				break;
			}
		}

		float b = lastXPosition - startXPosition;
		float c = 0f;
		float a = 0f;
		float angle = 0f;

		DRAW_DIRECTION drawDirection;

		if (b > 0) {
			// rechts

			a = lastYPosition - startYPosition;
			c = (float) Math.sqrt(a * a + b * b);
			angle = (float) Math.acos(b / c);

			// Set the direction

			float angleDegree = (float) Math.toDegrees(angle);

			if (angleDegree > 75) {
				drawDirection = DRAW_DIRECTION.middle;
			} else {
				drawDirection = DRAW_DIRECTION.right;
			}

			// calculate big triangle to get the location where the frisbee
			// should move to
			b = winSize.getWidth() + 100f - startXPosition;
			a = (float) (Math.tan(angle) * b);
			c = (float) Math.sqrt(a * a + b * b);
			moveTo = CGPoint.ccp(winSize.getWidth() + 100f, startYPosition + a);
		} else {
			// calculate small triangle to get angle
			a = lastYPosition - startYPosition;
			b = b * -1;
			c = (float) Math.sqrt(a * a + b * b);
			angle = (float) Math.acos(b / c);

			// Set the direction

			float angleDegree = (float) Math.toDegrees(angle);

			if (angleDegree > 75) {
				drawDirection = DRAW_DIRECTION.middle;
			} else {
				drawDirection = DRAW_DIRECTION.left;
			}

			// calculate big triangle to get the location where the frisbee
			// should move to
			b = 100f + startXPosition;
			a = (float) (Math.tan(angle) * b);
			c = (float) Math.sqrt(a * a + b * b);
			moveTo = CGPoint.ccp(-100f, startYPosition + a);
		}

		calculateHitBoxes(drawDirection);

		// check if not flying backward
		if (ball.getPosition().y < moveTo.y) {
			float animationTime = c / speed;

			CCMoveTo actionMove = CCMoveTo.action(animationTime, moveTo);
			CCCallFuncN actionMoveDone = CCCallFuncN.action(this,
					"spriteMoveFinished");
			CCSequence actions = CCSequence.actions(actionMove, actionMoveDone);

			Log.e("playSwirlSound", "true");
			SoundManager.playSound(R.raw.swirl, 1f, 0);

			ball.runAction(actions);
		}

	}

	private void calculateHitBoxes(DRAW_DIRECTION drawDirection) {
		// |4|5|6|
		// |1|2|3|
		int hitBox = 0;
		if (this.lastAccelStateTopDown == ACCEL_STATE.up) {
			if (drawDirection == DRAW_DIRECTION.left) {
				hitBox = 4;
			} else if (drawDirection == DRAW_DIRECTION.middle) {
				hitBox = 5;
			} else if (drawDirection == DRAW_DIRECTION.right) {
				hitBox = 6;
			}
		} else if (this.lastAccelStateTopDown == ACCEL_STATE.down) {
			if (drawDirection == DRAW_DIRECTION.left) {
				hitBox = 1;
			} else if (drawDirection == DRAW_DIRECTION.middle) {
				hitBox = 2;
			} else if (drawDirection == DRAW_DIRECTION.right) {
				hitBox = 3;
			}
		}
		Log.e("hitBox", hitBox + "");
		this.lastHitBox = hitBox;
	}

	public void update(float dt) {
		// check if frisbee out of visible position
		float yPosition = this.ball.getPosition().y;
		float xPosition = this.ball.getPosition().x;
		if (yPosition > OUT_OF_BOUNDS_TOP || xPosition < OUT_OF_BOUNDS_LEFT
				|| xPosition > OUT_OF_BOUNDS_RIGHT) {
			if (!shotWasSended) {
				this.ball.stopAllActions();
				this.sendShoot();
			}

		} else if (Float.isNaN(xPosition)) {
			// user tapped on the frisbee
			this.ball.stopAllActions();
			this.resetFrisbee();
		}
	}

	public void spriteMoveFinished(Object sender) {
		this.sendShoot();
	}

	private void sendShoot() {
		if (this.networkClient.isConnected()) {
			shotWasSended = true;
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(500);
						String result = networkClient
								.transmitAndGetResponse(lastHitBox + "");
						if ("0".equals(result)) {
							FootFingerActivity.makeToast("You win!");
						} else if ("1".equals(result)) {
							FootFingerActivity.makeToast("You've lost!");
						}
						resetFrisbee();
					} catch (IOException e) {
						// do nothing
					} catch (InterruptedException e) {
						// do nothing
					}

				}
			}).start();
		}

	}

	public void rotateFrisbeeFinished(Object sender) {
		this.rotateFrisbee();
	}

	@Override
	public void ccAccelerometerChanged(float accelX, float accelY, float accelZ) {
		if (Math.abs(this.oldAccelY - accelY) > precisionHigh) {
			if (accelY > 4) {
				this.lastAccelStateTopDown = ACCEL_STATE.up;
			} else if (accelY > -4) {
				// middle
			} else if (accelY <= -4) {
				this.lastAccelStateTopDown = ACCEL_STATE.down;
			}
		}

		this.oldAccelY = accelY;
	}

	private class VibrateTask implements Runnable {

		@Override
		public void run() {
			while (dragAndDrop) {
				vibrator.vibrate(200);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// doNothing
				}
			}
			vibrator.cancel();
		}
	}
}
