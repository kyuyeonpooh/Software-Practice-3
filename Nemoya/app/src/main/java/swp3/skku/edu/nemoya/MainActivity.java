package swp3.skku.edu.nemoya;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
    final static int SOUND_COUNT = 1;
    final static int MAIN_SOUND = 0;

    private SoundPool soundPool;
    private int[] sounds = new int[SOUND_COUNT];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        loadSounds();
        startBackgroundMusicService();

        TextView title = MainActivity.this.findViewById(R.id.textView);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/DK.otf"));

        TextView titleMessage = MainActivity.this.findViewById(R.id.title_message);
        titleMessage.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/DK.otf"));

        findViewById(R.id.title_layout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                soundPool.play(sounds[MAIN_SOUND], 1, 1, 0, 0, 1);
                startActivity(new Intent(MainActivity.this, CategoryActivity.class));
                finish();
            }
        });

    }

    public void startBackgroundMusicService(){
        Intent intent = new Intent(MainActivity.this, BackgroundMusicService.class);
        startService(intent);
    }

    public void loadSounds(){

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(8).build();
        sounds[MAIN_SOUND] = soundPool.load(this, R.raw.main_click, 1);
    }
}
