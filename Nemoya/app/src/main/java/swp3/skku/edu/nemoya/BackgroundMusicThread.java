package swp3.skku.edu.nemoya;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import static swp3.skku.edu.nemoya.GameActivity.MAX_NUM_HINT;
import static swp3.skku.edu.nemoya.GameActivity.numHint;
import static swp3.skku.edu.nemoya.GameActivity.ticketButton;

/**
 * Created by lion7 on 2018-06-08.
 */

public class BackgroundMusicThread extends Thread {

    private BackgroundMusicService.ServiceHandler mHandler;
    private boolean mIsRun = true;
    private Context context;

    private static MediaPlayer mediaPlayer;
    private static int paused = 0;
    private static boolean isPaused = false;

    public BackgroundMusicThread(BackgroundMusicService.ServiceHandler handler, Context context) {
        Log.d("playmusic", "musicOn");
        mHandler = handler;
        this.context = context;
        mediaPlayer = MediaPlayer.create(context, R.raw.background_music);
        mediaPlayer.setVolume(0.15f, 0.15f);
        mediaPlayer.setLooping(true);
        mediaPlayer.seekTo(paused);
        mediaPlayer.start();
    }

    public void stopForever() {
        synchronized (this) {
            mIsRun = false;
        }
    }

    @SuppressLint("SetTextI18n")
    public void run() {
        while (mIsRun) {
            if(shouldShowNotification(context) && !isPaused){
                Log.d("playmusic", "musicOff");
                mHandler.sendEmptyMessage(0);
                isPaused = true;
                mediaPlayer.pause();
                paused = mediaPlayer.getCurrentPosition();
            }
            else if(!shouldShowNotification(context) && isPaused){
                Log.d("playmusic", "musicOn");
                mediaPlayer.seekTo(paused);
                mediaPlayer.start();
                isPaused = false;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static boolean shouldShowNotification(Context context) {
        ActivityManager.RunningAppProcessInfo myProcess = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(myProcess);
        if (myProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
            return true;

        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        // app is in foreground, but if screen is locked show notification anyway
        return km.inKeyguardRestrictedInputMode();
    }
}