package bode.moritz.frisboros;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;

import android.util.Log;
import bode.moritz.frisboros.sound.SoundManager;

public class GoalKeeperLayer extends CCColorLayer {

	private CCSprite background;
	private CGSize winSize;
	private float XGrid[];
	private float YGrid[];
	private CCSprite snake;
	long time = 0;
	long time2 = 0;
	boolean flag = true;
	private double ay, az;
	private Socket client;

	protected GoalKeeperLayer() {
		super(ccColor4B.ccc4(0, 0, 0, 255));
		SoundEngine.sharedEngine().pauseSound();
		this.client = FootFingerActivity.getClient();

		this.setIsAccelerometerEnabled(true);
		this.setIsTouchEnabled(true);
		winSize = CCDirector.sharedDirector().displaySize();

		XGrid = new float[4];
		YGrid = new float[3];

		for (int i = 0; i < 4; i++) {
			XGrid[i] = i * winSize.width / 3;

		}
		for (int i = 0; i < 3; i++) {
			YGrid[i] = i * winSize.height / 2;
		}

		background = CCSprite.sprite("bg_keeper.png");
		background.setAnchorPoint(CGPoint.ccp(0f, 0f));
		background.setPosition(CGPoint.ccp(0, 0));

		addChild(background);

		snake = CCSprite.sprite("keeper.png");

		snake.setPosition(CGPoint.ccp(0.5f * winSize.width,
				0.75f * winSize.height));
		addChild(snake);

		new Thread(new NetworkTask()).start();
	}

	public void animate(int field) {

		switch (field) {
		case 1:
			snake.setPosition(CGPoint.ccp(0.0f * winSize.width,
					0.75f * winSize.height));
			break;
		case 2:
			snake.setPosition(CGPoint.ccp(0.0f * winSize.width,
					0.5f * winSize.height));
			break;
		case 3:
			snake.setPosition(CGPoint.ccp(0.0f * winSize.width,
					0.25f * winSize.height));
			break;
		case 4:
			snake.setPosition(CGPoint.ccp(0.5f * winSize.width,
					0.75f * winSize.height));
			break;
		case 5:
			snake.setPosition(CGPoint.ccp(0.5f * winSize.width,
					0.5f * winSize.height));
			break;
		case 6:
			snake.setPosition(CGPoint.ccp(0.5f * winSize.width,
					0.25f * winSize.height));
			break;
		default:
			break;
		}
	}

	@Override
	public void ccAccelerometerChanged(float x, float y, float z) {
		ay = y;
		az = z;

		if (az > -2) {
			// hier bin ich unten
			if (ay < 3.0 && ay > -3.0) {
				// waagerecht
				// Log.e("print", "unten waagerecht");
				animate(2);
			} else {
				if (ay >= 3.0) {
					// rechts
					// Log.e("print", "unten rechts");
					animate(3);
				} else if (ay <= -3.0) {
					// links
					// Log.e("print", "unten links");
					animate(1);
				}
			}
		} else {
			// hier bin ich oben
			if (ay < 3.0 && ay > -3.0) {
				// waagerecht
				// Log.e("print", "oben waagerecht");
				animate(5);
			} else {
				if (ay >= 3.0) {
					// rechts
					// Log.e("print", "oben rechts");
					animate(6);
				} else if (ay <= -3.0) {
					// links

					// Log.e("print", "oben links");
					animate(4);
				}
			}
		}

	}

	public void spriteMoveFinished(Object sender) {

	}

	private class NetworkTask implements Runnable {

		@Override
		public void run() {

			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						client.getInputStream()));
				PrintWriter out = new PrintWriter(client.getOutputStream(),
						true);
				String line = null;
				while ((line = in.readLine()) != null) {
					if (checkGameMatch(line)) {
						Log.e("print", "You're a winner");
						FootFingerActivity.makeToast("(-:");
						SoundManager.playSound(R.raw.success, 1f, 0);
						out.println("1");
					} else {
						Log.e("print", "You're a looser");
						FootFingerActivity.makeToast(")-:");
						SoundManager.playSound(R.raw.missed, 1f, 0);

						out.println("0");
					}
					Log.e("resultFromClient", line);

				}
			} catch (IOException e) {
				System.out.println("Read failed");
			}
		}

		private boolean checkGameMatch(String quadrant) {
			int quad = 1;
			try {
				quad = Integer.parseInt(quadrant);
			} catch (Exception E) {
				// no number
			}

			int actualquad = calculatequad();
			return quad == actualquad;

		}

		private int calculatequad() {
			if (az > -2) {
				// hier bin ich unten
				if (ay < 3.0 && ay > -3.0) {
					// waagerecht
					Log.e("print", "unten waagerecht");

					return 2;
				} else {
					if (ay >= 3.0) {
						// rechts
						Log.e("print", "unten rechts");
						return 1;
					} else if (ay <= -3.0) {
						// links
						Log.e("print", "unten links");
						return 3;
					}
				}
			} else {
				// hier bin ich oben
				if (ay < 3.0 && ay > -3.0) {
					// waagerecht
					Log.e("print", "oben waagerecht");
					return 5;
				} else {
					if (ay >= 3.0) {
						// rechts
						Log.e("print", "oben rechts");
						return 4;
					} else if (ay <= -3.0) {
						// links

						Log.e("print", "oben links");
						return 6;
					}
				}
			}
			return 0;
		}
	}
}
