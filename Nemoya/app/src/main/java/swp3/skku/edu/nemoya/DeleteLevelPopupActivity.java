package swp3.skku.edu.nemoya;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by lion7 on 2018-06-10.
 */

public class DeleteLevelPopupActivity extends Activity {

    final static int SOUND_COUNT = 1;
    final static int CLOSE_SOUND = 0;

    private SoundPool soundPool;
    private int[] sounds = new int[SOUND_COUNT];

    TextView titleText, backgroundText;
    EditText deleteFileName;
    Button closeButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("popup", "here");
        loadSounds();
        Log.d("delete-1", "-1");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.delete_popup);
        Log.d("popup", "here2");
        titleText = findViewById(R.id.title_text);
        titleText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/JUA.ttf"));
        backgroundText = findViewById(R.id.background_text);
        backgroundText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/JUA.ttf"));
        deleteFileName = findViewById(R.id.filename_delete);
        deleteFileName.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/JUA.ttf"));
        closeButton = findViewById(R.id.close_btn);
        closeButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/JUA.ttf"));
        deleteButton = findViewById(R.id.delete_btn);
        deleteButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/JUA.ttf"));
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

    public void mOnCloseCancel(View v){
        Log.d("delete0", "none");
        Intent intent = new Intent();
        soundPool.play(sounds[CLOSE_SOUND], 1, 1, 0, 0, 1);
        intent.putExtra("result", "none");
        this.setResult(RESULT_OK, intent);
        finish();
    }

    public void mOnCloseDelete(View v){
        Log.d("delete0", String.valueOf(deleteFileName.getText()));
        Intent intent = new Intent();
        soundPool.play(sounds[CLOSE_SOUND], 1, 1, 0, 0, 1);
        intent.putExtra("result", String.valueOf(deleteFileName.getText()));
        this.setResult(RESULT_OK, intent);
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
