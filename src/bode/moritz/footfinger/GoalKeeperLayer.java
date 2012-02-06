package bode.moritz.footfinger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;

import bode.moritz.footfinger.sound.SoundManager;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

public class GoalKeeperLayer extends CCColorLayer {

	private CCSprite background;
	private CGSize winSize;
	private float XGrid[];
	private float YGrid[];
	private float XOffset;
	private float YOffset;
	private CCSprite snake;
	private CCMoveTo moveTo = null;
	private float precisionLow = 1.5f;
	private float precisionHigh = 0.2f;
	private float snakeX, snakeY;
	long time = 0;
	long time2 = 0;
	boolean flag = true;
	private float x, y, z;
	private float maxX, maxY, maxZ;
	private boolean keeperRunnung;
	private ArrayList<String> al;
	private int hitBox;
	private double ay, az;
	private ServerSocket server;
	private Socket client;

	protected GoalKeeperLayer(ccColor4B color) {
		super(color);
		SoundEngine.sharedEngine().pauseSound();
		this.client = FootFingerActivity.getClient();
		
		this.setIsAccelerometerEnabled(true);
		this.setIsTouchEnabled(true);
		winSize = CCDirector.sharedDirector().displaySize();
		Log.v("SIZE", "size=" + winSize.height + " " + winSize.width);

		// addChild(background);

		XGrid = new float[4];
		YGrid = new float[3];
		XOffset = winSize.width / 6f;
		YOffset = winSize.height / 4f;

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
		// snake.setPosition(CGPoint.ccp(XGrid[0] + XOffset, YGrid[0] +
		// YOffset));
		snake.setPosition(CGPoint.ccp(0.5f * winSize.width, 0.75f * winSize.height));
		addChild(snake);

		snakeX = XGrid[1] + XOffset;
		snakeY = YGrid[0] + YOffset;

		new Thread(new NetworkTask()).start();
	}

	@Override
	public boolean ccTouchesMoved(MotionEvent e) {
		// if (!this.keeperRunnung) {
		// //
		// Log.v("OHYEAH", "duschlampeee=" + hitBox);
		// // moveTo = CCMoveTo.action(1, CGPoint.ccp(0, 0));
		// switch (hitBox) {
		// case 1:
		// moveTo = CCMoveTo.action(1, CGPoint.ccp(XGrid[0] + XOffset, YGrid[0]
		// + YOffset));
		// break;
		// case 2:
		// moveTo = CCMoveTo.action(1, CGPoint.ccp(XGrid[1] + XOffset, YGrid[1]
		// + YOffset));
		// break;
		//
		// case 3:
		// moveTo = CCMoveTo.action(1, CGPoint.ccp(XGrid[2] + XOffset, YGrid[0]
		// + YOffset));
		// break;
		// case 4:
		// moveTo = CCMoveTo.action(1, CGPoint.ccp(XGrid[0] + XOffset, YGrid[1]
		// + YOffset));
		// break;
		//
		// case 5:
		// moveTo = CCMoveTo.action(1, CGPoint.ccp(XGrid[1] + XOffset, YGrid[0]
		// + YOffset));
		// break;
		// case 6:
		// moveTo = CCMoveTo.action(1, CGPoint.ccp(XGrid[2] + XOffset, YGrid[1]
		// + YOffset));
		// break;
		// default:
		// moveTo = CCMoveTo.action(1, CGPoint.ccp(XGrid[0] + XOffset, YGrid[0]
		// + YOffset));
		//
		// break;
		// }
		//
		// CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "done");
		// CCSequence actions = CCSequence.actions(moveTo, actionMoveDone);
		// snake.runAction(actions);
		// this.keeperRunnung = true;
		// }

		return true;
	}

	public void animate(int field) {

		switch (field) {
		case 1:
			snake.setPosition(CGPoint.ccp(0.0f * winSize.width, 0.75f * winSize.height));
			break;
		case 2:
			snake.setPosition(CGPoint.ccp(0.0f * winSize.width, 0.5f * winSize.height));
			break;
		case 3:
			snake.setPosition(CGPoint.ccp(0.0f * winSize.width, 0.25f * winSize.height));
			break;
		case 4:
			snake.setPosition(CGPoint.ccp(0.5f * winSize.width, 0.75f * winSize.height));
			break;
		case 5:
			snake.setPosition(CGPoint.ccp(0.5f * winSize.width, 0.5f * winSize.height));
			break;
		case 6:
			snake.setPosition(CGPoint.ccp(0.5f * winSize.width, 0.25f * winSize.height));
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
//				Log.e("print", "unten waagerecht");
				animate(2);
			} else {
				if (ay >= 3.0) {
					// rechts
//					Log.e("print", "unten rechts");
					animate(3);
				} else if (ay <= -3.0) {
					// links
//					Log.e("print", "unten links");
					animate(1);
				}
			}
		} else {
			// hier bin ich oben
			if (ay < 3.0 && ay > -3.0) {
				// waagerecht
//				Log.e("print", "oben waagerecht");
				animate(5);
			} else {
				if (ay >= 3.0) {
					// rechts
//					Log.e("print", "oben rechts");
					animate(6);
				} else if (ay <= -3.0) {
					// links

//					Log.e("print", "oben links");
					animate(4);
				}
			}
		}

	}

	// public void done(Object sender) {
	// this.keeperRunnung = false;
	// }

	public static CCScene scene() {

		CCScene scene = CCScene.node();
		CCLayer layer = new GoalKeeperLayer(ccColor4B.ccc4(0, 0, 0, 255));

		scene.addChild(layer);

		return scene;
	}

	public void spriteMoveFinished(Object sender) {

	}

//	private class NetworkTask extends AsyncTask<Void, String, Void> {
//
//		@Override
//		protected Void doInBackground(Void... params) {
//			server = null;
//
//			try {
//				server = new ServerSocket(8080);
//			} catch (IOException e) {
//				Log.e("print", "Could not bind Server...");
//			}
//
//			Socket client;
//
//			try {
//				client = server.accept();
//				
//				try {
//					BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
//					PrintWriter out = new PrintWriter(client.getOutputStream(), true);
//					String line = null;
//					while ((line = in.readLine()) != null) {
//						if (checkGameMatch(line)) {
//							Log.e("print", "You're a winner");
//							FootFingerActivity.playEffect(R.raw.success);
//							// line = "You're a winner";
//							out.println("1");
//						} else {
//							Log.e("print", "You're a looser");
//							FootFingerActivity.playEffect(R.raw.fail);
//							// line = "You're a looser";
//
//							out.println("0");
//						}
//
//						publishProgress(new String[] { line });
//
//					}
//					// PrintWriter out = new
//					// PrintWriter(client.getOutputStream(),true);
//				} catch (IOException e) {
//					System.out.println("Read failed");
//				}
//
//			} catch (IOException e) {
//				System.out.println("Accept failed: 8080");
//			}
//			return null;
//		}
	
	
	private class NetworkTask implements Runnable {

		@Override
		public void run() {

		
				try {
					BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
					PrintWriter out = new PrintWriter(client.getOutputStream(), true);
					String line = null;
					while ((line = in.readLine()) != null) {
						if (checkGameMatch(line)) {
							Log.e("print", "You're a winner");
							FootFingerActivity.makeToast("(-:");
							SoundManager.playSound(R.raw.success, 1f, 0);
							// line = "You're a winner";
							out.println("1");
						} else {
							Log.e("print", "You're a looser");
							FootFingerActivity.makeToast(")-:");
							SoundManager.playSound(R.raw.missed,1f,0);
							// line = "You're a looser";

							out.println("0");
						}
						Log.e("resultFromClient", line);

					}
					// PrintWriter out = new
					// PrintWriter(client.getOutputStream(),true);
				} catch (IOException e) {
					System.out.println("Read failed");
				}
		}

		private boolean checkGameMatch(String quadrant) {
			int actualquadrant = 0;
			// Log.e("ERROR", quadrant);
			int quad = 1;
			try {
				quad = Integer.parseInt(quadrant);
			} catch (Exception E) {
				// es war keine Zahl
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
