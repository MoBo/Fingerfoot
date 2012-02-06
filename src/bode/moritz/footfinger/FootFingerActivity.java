package bode.moritz.footfinger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.sound.SoundEngine;

import android.app.Activity;
import android.content.Context;
import android.media.SoundPool;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import bode.moritz.footfinger.network.NetworkControllerClient;
import bode.moritz.footfinger.sound.SoundManager;

public class FootFingerActivity extends Activity {
    private static CCGLSurfaceView _glSurfaceView;
	private static InputMethodManager inputMethodManager;
	private static WifiManager wim;
	private static Vibrator vibrator;
	private static Context context;
	private static CCScene currentScene;
	private static ArrayList<Class<?>> layerStack = new ArrayList<Class<?>>();
	private static Socket currentClient;
	private boolean backgroundMusicPaused = false;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
     
        _glSurfaceView = new CCGLSurfaceView(this);
        
       
        
		CCDirector.sharedDirector().attachInView(_glSurfaceView);
        
        CCDirector.sharedDirector().setDisplayFPS(true);
     
        CCDirector.sharedDirector().setAnimationInterval(1.0f / 60.0f);
        CCDirector.sharedDirector().setDeviceOrientation(CCDirector.kCCDeviceOrientationPortrait);  
        
        
        
        this.setContentView(_glSurfaceView);
        
        CCScene scene = IndexPageLayer.scene();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        wim= (WifiManager) getSystemService(WIFI_SERVICE);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        
        CCDirector.sharedDirector().runWithScene(scene);
    }
    
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	SoundManager.getInstance();
        SoundManager.initSounds(this);
        SoundManager.loadSounds(new SoundPool.OnLoadCompleteListener() {
 			
 			@Override
 			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
 				if(sampleId==SoundManager.getSampleId(R.raw.background_music)){
 					SoundManager.playSound(R.raw.background_music, 1f, -1);
 				}
 				
 			}
 		});
    }

    @Override
    public void onPause()
    {
        super.onPause();
        SoundManager.pauseSound(R.raw.background_music);
        this.backgroundMusicPaused  = true;
        currentScene = CCDirector.sharedDirector().getRunningScene();
        CCDirector.sharedDirector().onPause();
    }
     
    @Override
    public void onResume()
    {
        super.onResume();
     
        CCDirector.sharedDirector().onResume();
//        SoundEngine.sharedEngine().preloadSound(getApplicationContext(), R.raw.background_music);
//        SoundEngine.sharedEngine().preloadSound(getApplicationContext(), R.raw.frisbe_slow);
//        SoundEngine.sharedEngine().preloadSound(getApplicationContext(), R.raw.frisbe_fast);
//        SoundEngine.sharedEngine().preloadSound(getApplicationContext(), R.raw.swirl);
//        SoundEngine.sharedEngine().preloadSound(getApplicationContext(), R.raw.missed);
//        SoundEngine.sharedEngine().preloadSound(getApplicationContext(), R.raw.success);
//        
//        SoundEngine.sharedEngine().playSound(getApplicationContext(), R.raw.background_music, true);
        
        if(this.backgroundMusicPaused){
        	SoundManager.resumeSound(R.raw.background_music);
        	this.backgroundMusicPaused =false;
        }
        //check if first not start
        if(currentScene!=null){
        	//SoundEngine.sharedEngine().resumeSound();
            CCDirector.sharedDirector().runWithScene(currentScene);
        }
    }  
     
    @Override
    public void onStop()
    {
        super.onStop();
        try {
			NetworkControllerClient.getInstance().close();
		} catch (IOException e) {
			// do nothing
		}
        SoundManager.cleanup();
//        SoundEngine.sharedEngine().realesAllSounds();
//        SoundEngine.sharedEngine().realesAllEffects();
        CCDirector.sharedDirector().end();
    }

    public static void setNextView(Class<?> thisLayer, Class<?> nextLayer){
    	layerStack.add(thisLayer);
    	setViewByClass(nextLayer);
    }
    
    public static void gotoPreviousView(){
    	Class<?> layer = layerStack.remove(layerStack.size()-1);
    	// TODO Still buggy
    	if(layer == GoalKeeperLayer.class){
    		layerStack.add(GoalKeeperLayer.class);
    		//Doing nothing at the moment 
    		// TODO Need a toast for the User to notify him if really want to quit the game
    		CCDirector.sharedDirector().getActivity().finish();
    	}else if(layer == ShooterLayer.class){
    		layerStack.add(ShooterLayer.class);
    		CCDirector.sharedDirector().getActivity().finish();
    		//Doing nothing at the moment 
    		// TODO Need a toast for the User to notify him if really want to quit the game
    	}else{
    		setViewByClass(layer);
    	}
    	
    }
    
    private static void setViewByClass(Class<?> newLayer){
    	// TODO Change this to Reflection
    	if(newLayer == IndexPageLayer.class){
    		CCDirector.sharedDirector().replaceScene(IndexPageLayer.scene());
    	}else if(newLayer == MainMenuLayer.class){
    		CCDirector.sharedDirector().replaceScene(MainMenuLayer.scene());
    	}else if(newLayer == ShooterConnectLayer.class){
    		CCDirector.sharedDirector().replaceScene(ShooterConnectLayer.scene());
    	}else if(newLayer == GoalKeeperWaitingConnectionLayer.class){
    		CCDirector.sharedDirector().replaceScene(GoalKeeperWaitingConnectionLayer.scene());
    	}else if(newLayer == GoalKeeperLayer.class){
    		CCDirector.sharedDirector().replaceScene(GoalKeeperLayer.scene());
    	}else if(newLayer == ShooterLayer.class){
    		CCDirector.sharedDirector().replaceScene(ShooterLayer.scene());
    	}else if(newLayer == HelpScreenLayer.class){
    		CCDirector.sharedDirector().replaceScene(HelpScreenLayer.scene());
    	}
    }
    
    @Override
    public void onBackPressed() {
    	if(layerStack.size()<1){
    		super.onBackPressed();
    	}else{
    		gotoPreviousView();
    	}
    }
    
	public static Vibrator getVibrator() {
		return vibrator;
	}
	
	public static String getIPAdresse(){
		 int ipAdresse = wim.getConnectionInfo().getIpAddress();
		 return ( ipAdresse & 0xFF) + "." +
         ((ipAdresse >>  8 ) & 0xFF) + "." +
         ((ipAdresse >> 16 ) & 0xFF) + "." +
         ((ipAdresse >> 24 ) & 0xFF);
	}
	
//	public static void playSound(final int id, final boolean loop){
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				SoundEngine.sharedEngine().pauseSound();
//				SoundEngine.sharedEngine().playSound(context, id, loop);
//			}
//		}).start();
//		
//	}
	
//	public static void playEffect(int id){
//		SoundEngine.sharedEngine().pauseSound();
//		SoundEngine.sharedEngine().playEffect(context, id);
//	}
	
	public static void showSoftInput(OnKeyListener keyListener){
		_glSurfaceView.setOnKeyListener(keyListener);
		inputMethodManager.showSoftInput(_glSurfaceView, InputMethodManager.SHOW_FORCED);
	}
	
	public static void hideSoftInput(){
		inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}

	public static void setClient(Socket client) {
		currentClient = client;
	}

	public static Socket getClient() {
		return currentClient;
	}	
	
	public static void makeToast(final String string) {
		CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(context, string, Toast.LENGTH_SHORT)
				.show();
			}
		});
	}
}