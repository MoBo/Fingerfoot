package bode.moritz.footfinger;

import java.io.IOException;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.sound.SoundEngine;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Window;
import android.view.WindowManager;
import bode.moritz.footfinger.network.NetworkControllerClient;

public class FootFingerActivity extends Activity {
    private CCGLSurfaceView _glSurfaceView;
	private static Vibrator vibrator;
	private static Context context;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
     
        _glSurfaceView = new CCGLSurfaceView(this);
     
        setContentView(_glSurfaceView);
    }
    
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	
    	CCDirector.sharedDirector().attachInView(_glSurfaceView);
        
        CCDirector.sharedDirector().setDisplayFPS(true);
     
        CCDirector.sharedDirector().setAnimationInterval(1.0f / 60.0f);
        CCDirector.sharedDirector().setDeviceOrientation(CCDirector.kCCDeviceOrientationPortrait);
      
        SoundEngine.sharedEngine().preloadSound(getApplicationContext(), R.raw.background_music);
        SoundEngine.sharedEngine().preloadSound(getApplicationContext(), R.raw.frisbe_slow);
        SoundEngine.sharedEngine().preloadSound(getApplicationContext(), R.raw.frisbe_fast);
        SoundEngine.sharedEngine().preloadEffect(context, R.raw.swirl);
        
        SoundEngine.sharedEngine().playSound(getApplicationContext(), R.raw.background_music, true);
        
        
        CCScene scene = IndexPageLayer.scene();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //CCScene scene = ShooterLayer.scene(vibrator);
        
        CCDirector.sharedDirector().runWithScene(scene);
    }
    
    @Override
    public void onPause()
    {
        super.onPause();
     
        CCDirector.sharedDirector().pause();
    }
     
    @Override
    public void onResume()
    {
        super.onResume();
     
        CCDirector.sharedDirector().resume();
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
        SoundEngine.sharedEngine().realesAllSounds();
        SoundEngine.sharedEngine().realesAllEffects();
        CCDirector.sharedDirector().end();
    }

	public static Vibrator getVibrator() {
		return vibrator;
	}
	
	public static void playSound(int id, boolean loop){
		SoundEngine.sharedEngine().playSound(context, id, loop);
	}
	
	public static void playEffect(int id){
		SoundEngine.sharedEngine().playEffect(context, id);
	}
	
}