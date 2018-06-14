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
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

/**
 * Created by lion7 on 2018-06-08.
 */

public class HintNotifyService extends Service {
    private NotificationManager mNotiManager;
    private HintNotifyThread mThread;
    private Notification mNoti;
    private static final String CHANNEL_ID = "default";
    private static final String CHANNEL_NAME = "wow";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mThread == null) {
            mNotiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            CHANNEL_NAME,
                            NotificationManager.IMPORTANCE_DEFAULT
                    );
            mNotiManager.createNotificationChannel(channel);

            ServiceHandler handler = new ServiceHandler();
            mThread = new HintNotifyThread(handler, HintNotifyService.this.getApplicationContext());
            mThread.start();
        }

        return START_STICKY;
    }

    public void onDestroy() {
        if(mThread != null) {
            mThread.stopForever();
            mThread = null;
        }
    }

    public class ServiceHandler extends Handler {
        @RequiresApi(api = Build.VERSION_CODES.O)

        @SuppressLint("NewApi")
        public void handleMessage(android.os.Message msg) {

            Intent intent = new Intent(HintNotifyService.this, CategoryActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(
                            HintNotifyService.this,
                            0,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mNoti = new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentTitle("네모야")
                    .setContentText("힌트가 모두 충전되었어요!")
                    .setSmallIcon(R.drawable.nemo_mini)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.nemo_mini))
                    .setTicker("Notice!!!")
                    .setContentIntent(pendingIntent)
                    .build();

            mNoti.defaults = Notification.DEFAULT_SOUND;
            mNoti.flags = Notification.FLAG_ONLY_ALERT_ONCE;
            mNoti.flags = Notification.FLAG_AUTO_CANCEL;

            mNotiManager.notify(777, mNoti);

            if(mThread == null) {
                mThread.stopForever();
                mThread = null;
            }
        }
    }
}