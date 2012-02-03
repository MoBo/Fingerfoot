package bode.moritz.footfinger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
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
import bode.moritz.footfinger.network.NetworkControllerClient;

public class FootFingerActivity extends Activity {
    private static CCGLSurfaceView _glSurfaceView;
	private static InputMethodManager inputMethodManager;
	private static WifiManager wim;
	private static Vibrator vibrator;
	private static Context context;
	private static CCScene currentScene;
	private static ArrayList<Class<?>> layerStack = new ArrayList<Class<?>>();

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
        
        SoundEngine.sharedEngine().preloadSound(getApplicationContext(), R.raw.background_music);
        SoundEngine.sharedEngine().preloadSound(getApplicationContext(), R.raw.frisbe_slow);
        SoundEngine.sharedEngine().preloadSound(getApplicationContext(), R.raw.frisbe_fast);
        SoundEngine.sharedEngine().preloadSound(getApplicationContext(), R.raw.swirl);
        SoundEngine.sharedEngine().preloadSound(getApplicationContext(), R.raw.missed);
        SoundEngine.sharedEngine().preloadSound(getApplicationContext(), R.raw.success);
        
        SoundEngine.sharedEngine().playSound(getApplicationContext(), R.raw.background_music, true);
        
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
    }

    @Override
    public void onPause()
    {
        super.onPause();
        SoundEngine.sharedEngine().pauseSound();
        currentScene = CCDirector.sharedDirector().getRunningScene();
        CCDirector.sharedDirector().onPause();
    }
     
    @Override
    public void onResume()
    {
        super.onResume();
     
        CCDirector.sharedDirector().onResume();
        
        //check if first not start
        if(currentScene!=null){
        	SoundEngine.sharedEngine().resumeSound();
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
        SoundEngine.sharedEngine().pauseSound();
        SoundEngine.sharedEngine().realesAllSounds();
        SoundEngine.sharedEngine().realesAllEffects();
        CCDirector.sharedDirector().end();
    }

    public static void setNextView(Class<?> nextLayer,CCScene scene){
    	layerStack.add(nextLayer);
    	CCDirector.sharedDirector().replaceScene(scene);
    }
    
    public static void gotoPreviousView(){
    	Class<?> layer = layerStack.remove(layerStack.size()-1);
    	if(layer == IndexPageLayer.class){
    		CCDirector.sharedDirector().replaceScene(IndexPageLayer.scene());
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
	
	public static void playSound(final int id, final boolean loop){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				SoundEngine.sharedEngine().pauseSound();
				SoundEngine.sharedEngine().playSound(context, id, loop);
			}
		}).start();
		
	}
	
	public static void playEffect(int id){
		SoundEngine.sharedEngine().pauseSound();
		SoundEngine.sharedEngine().playEffect(context, id);
	}
	
	public static void showSoftInput(OnKeyListener keyListener){
		_glSurfaceView.setOnKeyListener(keyListener);
		inputMethodManager.showSoftInput(_glSurfaceView, InputMethodManager.SHOW_FORCED);
	}
	
	public static void hideSoftInput(){
		inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}	
}