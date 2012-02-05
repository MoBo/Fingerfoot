package bode.moritz.footfinger.sound;

import java.util.HashMap;

import org.cocos2d.sound.SoundEngine;

import bode.moritz.footfinger.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;


public class SoundManager {
	
	static private SoundManager _instance;
	private static SoundPool mSoundPool; 
	private static HashMap<Integer, Integer> mSoundPoolMap; 
	private static AudioManager  mAudioManager;
	private static Context mContext;
	
	private SoundManager()
	{   
	}
	
	/**
	 * Requests the instance of the Sound Manager and creates it
	 * if it does not exist.
	 * 
	 * @return Returns the single instance of the SoundManager
	 */
	static synchronized public SoundManager getInstance() 
	{
	    if (_instance == null) 
	      _instance = new SoundManager();
	    return _instance;
	 }
	
	/**
	 * Initialises the storage for the sounds
	 * 
	 * @param theContext The Application context
	 */
	public static  void initSounds(Context theContext) 
	{ 
		 mContext = theContext;
	     mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
	     mSoundPoolMap = new HashMap<Integer, Integer>(); 
	     mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE); 	    
	} 
	
	/**
	 * Add a new Sound to the SoundPool
	 * 
	 * @param Index - The Sound Index for Retrieval
	 * @param SoundID - The Android ID for the Sound asset.
	 */
	public static void addSound(int Index,int SoundID)
	{
		mSoundPoolMap.put(Index, mSoundPool.load(mContext, SoundID, 1));
	}
	
	/**
	 * Loads the various sound assets
	 * Currently hardcoded but could easily be changed to be flexible.
	 */
	public static void loadSounds(SoundPool.OnLoadCompleteListener listner)
	{
		mSoundPool.setOnLoadCompleteListener(listner);
		mSoundPoolMap.put(R.raw.background_music, mSoundPool.load(mContext, R.raw.background_music, 1));
		mSoundPoolMap.put(R.raw.frisbe_slow, mSoundPool.load(mContext, R.raw.frisbe_slow, 1));
		mSoundPoolMap.put(R.raw.frisbe_fast, mSoundPool.load(mContext, R.raw.frisbe_fast, 1));
		mSoundPoolMap.put(R.raw.swirl, mSoundPool.load(mContext, R.raw.swirl, 1));
		mSoundPoolMap.put(R.raw.missed, mSoundPool.load(mContext, R.raw.missed, 1));	
		mSoundPoolMap.put(R.raw.success, mSoundPool.load(mContext, R.raw.success, 1));	
	}
	
	/**
	 * Plays a Sound
	 * 
	 * @param index - The Index of the Sound to be played
	 * @param speed - The Speed to play not, not currently used but included for compatibility
	 */
	public static void playSound(int index,float speed,int loop) 
	{ 		
		     float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); 
		     streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		     mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, loop, speed); 
	}
	
	/**
	 * Stop a Sound
	 * @param index - index of the sound to be stopped
	 */
	public static void stopSound(int index)
	{
		mSoundPool.stop(mSoundPoolMap.get(index));
	}
	
	public static void pauseSound(int index)
	{
		mSoundPool.pause(mSoundPoolMap.get(index));
	}
	
	public static void resumeSound(int index)
	{
		mSoundPool.resume(mSoundPoolMap.get(index));
	}
	
	
	
	public static int getSampleId(int sourceID){
		return mSoundPoolMap.get(sourceID);
	}
	
	
	public static void cleanup()
	{
		mSoundPool.release();
		mSoundPool = null;
	    mSoundPoolMap.clear();
	    mAudioManager.unloadSoundEffects();
	    _instance = null;
	    
	}

	
}