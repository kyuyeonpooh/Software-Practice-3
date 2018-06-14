package swp3.skku.edu.nemoya;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

public class BackgroundMusicService extends Service {

    private BackgroundMusicThread mThread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mThread == null) {
            ServiceHandler handler = new BackgroundMusicService.ServiceHandler();
            mThread = new BackgroundMusicThread(handler, BackgroundMusicService.this.getApplicationContext());
            mThread.start();
        }
        return START_STICKY;
    }

    public void onDestroy() {
        mThread.stopForever();
        mThread = null;
    }

    public class ServiceHandler extends Handler {
        @RequiresApi(api = Build.VERSION_CODES.O)

        @SuppressLint("NewApi")
        public void handleMessage(android.os.Message msg) {

        }
    }
}
