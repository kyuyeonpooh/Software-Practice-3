package swp3.skku.edu.nemoya;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class CategoryActivity extends Activity {
    final static int SOUND_COUNT = 2;
    final static int CATEGORY_SELECT = 0;
    final static int SETTING_BUTTON = 1;

    private SoundPool soundPool;
    private int[] sounds = new int[SOUND_COUNT];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_page);

        loadSounds();

        TextView title = CategoryActivity.this.findViewById(R.id.title_nemo);

        Button characterButton = CategoryActivity.this.findViewById(R.id.character_button);
        Button animalButton = CategoryActivity.this.findViewById(R.id.animal_button);
        Button logoButton = CategoryActivity.this.findViewById(R.id.logo_button);
        Button myLevelButton = CategoryActivity.this.findViewById(R.id.my_level_button);
        ImageButton settingButton = CategoryActivity.this.findViewById(R.id.setting_button);

        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/JUA.ttf"));
        characterButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/JUA.ttf"));
        animalButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/JUA.ttf"));
        logoButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/JUA.ttf"));
        myLevelButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/JUA.ttf"));

        characterButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(CategoryActivity.this, LevelSelection.class);
                intent.putExtra("wrong", 0);
                LevelSelection.title="캐릭터";
                intent.putExtra("category", "c");
                startActivity(intent);
                soundPool.play(sounds[CATEGORY_SELECT], 1, 1, 0, 0, 1);
            }
        });
        animalButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(CategoryActivity.this, LevelSelection.class);
                intent.putExtra("wrong", 0);
                LevelSelection.title="동물과 식물";
                intent.putExtra("category", "d");
                startActivity(intent);
                soundPool.play(sounds[CATEGORY_SELECT], 1, 1, 0, 0, 1);
            }
        });
        logoButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(CategoryActivity.this, LevelSelection.class);
                intent.putExtra("wrong", 0);
                LevelSelection.title="로고";
                intent.putExtra("category", "l");
                startActivity(intent);
                soundPool.play(sounds[CATEGORY_SELECT], 1, 1, 0, 0, 1);
            }
        });
        myLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, LevelSelectionCustom.class);
                intent.putExtra("wrong", 0);
                startActivity(intent);
                soundPool.play(sounds[CATEGORY_SELECT], 1, 1, 0, 0, 1);
            }
        });
        settingButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                soundPool.play(sounds[SETTING_BUTTON], 1, 1, 0, 0, 1);
                startActivityForResult(new Intent(CategoryActivity.this, SettingPopupActivity.class), 1);
            }
        });
    }

    public void loadSounds(){

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(8).build();
        sounds[CATEGORY_SELECT] = soundPool.load(this, R.raw.stage_click, 1);
        sounds[SETTING_BUTTON] = soundPool.load(this, R.raw.setting_click, 1);
    }
}
