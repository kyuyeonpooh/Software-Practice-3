package swp3.skku.edu.nemoya;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by lion7 on 2018-06-10.
 */

public class SettingPopupActivity extends Activity {

    final static int SOUND_COUNT = 1;
    final static int CLOSE_SOUND = 0;

    private SoundPool soundPool;
    private int[] sounds = new int[SOUND_COUNT];

    SeekBar seekVolumn;
    TextView titleText, backgroundText, developersText;
    Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadSounds();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setting_popup);

        titleText = findViewById(R.id.title_text);
        titleText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/JUA.ttf"));
        backgroundText = findViewById(R.id.background_text);
        backgroundText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/JUA.ttf"));
        closeButton = findViewById(R.id.close_btn);
        closeButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/JUA.ttf"));
        developersText = findViewById(R.id.developers_text);
        developersText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/JUA.ttf"));

        seekVolumn = (SeekBar) findViewById(R.id.seekbar_volume);
        final AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int nMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int nCurrentVolumn = audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);

        seekVolumn.setMax(nMax);
        seekVolumn.setProgress(nCurrentVolumn);

        seekVolumn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        progress, 0);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        return;
    }

    public void mOnClose(View v){
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);
        soundPool.play(sounds[CLOSE_SOUND], 1, 1, 0, 0, 1);
        finish();
    }

    public void loadSounds(){

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(8).build();
        sounds[CLOSE_SOUND] = soundPool.load(this, R.raw.close_click, 1);
    }
}
