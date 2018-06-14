package swp3.skku.edu.nemoya;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class LevelSelection extends Activity {

    final static int SOUND_COUNT = 1;
    final static int LEVEL_SELECT = 0;

    private SoundPool soundPool;
    private int[] sounds = new int[SOUND_COUNT];

    private ImageView star1;
    private ImageView star2;
    private ImageView star3;

    private int[] star;
    private int enable;
    private int problemNum = 9;

    private int currentWrongCount;
    private String category;
    public static String title;

    public static int starAnimation = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_list);
        loadSounds();

        Intent intent = getIntent();
        currentWrongCount= intent.getExtras().getInt("wrong");
        category = intent.getExtras().getString("category");

        SharedPreferences prefs =getSharedPreferences("level", MODE_PRIVATE);
        String result = prefs.getString(category+"level", "0");
        enable = Integer.parseInt(result);

        TextView Title = LevelSelection.this.findViewById(R.id.title);
        Log.d("title22", title);
        Title.setText(title);
        Title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/JUA.ttf"));

        star1 = LevelSelection.this.findViewById(R.id.star1);
        star2 = LevelSelection.this.findViewById(R.id.star2);
        star3 = LevelSelection.this.findViewById(R.id.star3);

        star = new int [problemNum];
        for(int i=0; i < enable; i++){
            star[i] = prefs.getInt(category + Integer.toString(i + 1), 0 );

        }

        makeLayout();

        if(starAnimation == 1) {
            starAnimation();
            starAnimation = 0;
        }

    }

    public void makeLayout(){

        TableLayout tableLayout = (TableLayout) findViewById(R.id.table_layout);

        int count = 0;
        while(count < problemNum) {
            TableRow tableRow = new TableRow(this);
            while (tableRow.getChildCount() < 3) {
                Button button = new Button(this);
                final int height = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                final int margin = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                final int paddingTop = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, height);
                params.setMargins(margin, margin, margin, margin);
                button.setLayoutParams(params);
                button.setText(Integer.toString(count + 1));
                button.setPadding(0, paddingTop, 0, 0);
                button.setGravity(Gravity.TOP | Gravity.CENTER);
                setBackground(button, count);
                button.setSoundEffectsEnabled(true);
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23);
                setEnable(button, count);
                setOnclick(button, Integer.toString(count + 1));
                tableRow.addView(button);
                count++;
            }
            tableLayout.addView(tableRow);

        }

    }

    public void setOnclick(Button but, final String level){

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LevelSelection.this, GameActivity.class);
                intent.putExtra("category", category);
                intent.putExtra("level", level);
                GameActivity.custom = 0;
                startActivity(intent);
                soundPool.play(sounds[LEVEL_SELECT], 1, 1, 0, 0, 1);
                finish();
            }
        });

    }

    public void setBackground(Button but, int i){

        if(i < enable) {

            if (star[i] == 0)
                but.setBackgroundResource(R.drawable.stage_clear_border);
            else if (star[i] <= 10) {
                but.setBackgroundResource(R.drawable.stage_star2);
            } else {
                but.setBackgroundResource(R.drawable.stage_star1);
            }

        }
        else if (i == enable){
            but.setBackgroundResource(R.drawable.stage_open);
        }
        else{
            but.setBackgroundResource(R.drawable.stage_border);
        }

    }

    public void setEnable(Button but, int i){

        if( i <= enable)
            but.setEnabled(true);
        else
            but.setEnabled(false);

    }

    public void loadSounds(){

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(8).build();
        sounds[LEVEL_SELECT] = soundPool.load(this, R.raw.stage_click, 1);

    }

    public void starAnimation(){

        Animation animstar1, animstar2, animstar3;

        animstar1 = AnimationUtils.loadAnimation(this, R.anim.star);
        animstar2 = AnimationUtils.loadAnimation(this, R.anim.star);
        animstar3 = AnimationUtils.loadAnimation(this, R.anim.star);

        animstar1.setStartOffset(50);
        animstar2.setStartOffset(450);
        animstar3.setStartOffset(850);

        star1.setVisibility(View.VISIBLE);
        star1.setAnimation(animstar1);

        if(currentWrongCount <= 10) {
            star2.setVisibility(View.VISIBLE);
            star2.setAnimation(animstar2);
        }
        if(currentWrongCount == 0) {
            star3.setVisibility(View.VISIBLE);
            star3.setAnimation(animstar3);
        }

        Handler timer = new Handler();
        timer.postDelayed(new Runnable(){
            public void run(){
                star1.setVisibility(View.INVISIBLE);
                star2.setVisibility(View.INVISIBLE);
                star3.setVisibility(View.INVISIBLE);
            }
        }, 2500);
    }
}
