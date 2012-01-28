package bode.moritz.footfinger;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Window;
import android.view.WindowManager;

public class FootFingerActivity extends Activity {
    private CCGLSurfaceView _glSurfaceView;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
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
        
      
        CCScene scene = IndexPageLayer.scene();

        //Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //CCScene scene = GameLayer.scene(v);

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
     
        CCDirector.sharedDirector().end();
    }
}