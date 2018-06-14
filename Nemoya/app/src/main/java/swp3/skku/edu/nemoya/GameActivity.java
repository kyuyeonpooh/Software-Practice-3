package swp3.skku.edu.nemoya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class GameActivity extends AppCompatActivity {

    final static int ROW_COUNT = 15;
    final static int COL_COUNT = 15;
    final static int NUM_ROW_COUNT = ROW_COUNT / 2 + ROW_COUNT % 2;
    final static int NUM_COL_COUNT = COL_COUNT / 2 + COL_COUNT % 2;
    final static int MAX_NUM_HINT = 3;

    final static int SOUND_COUNT = 5;
    final static int WINNING = 0;
    final static int TICKET_BUTTON = 1;
    final static int COLORING_BUTTON = 2;
    final static int CROSSING_BUTTON = 3;
    final static int ERASING_BUTTON = 4;

    final static int LETTER_WIDTH = 14;
    static int LEN_BOTTOM_WIDTH = 0;
    static int LEN_TOP_WIDTH = 0;
    static int UNIT_LENGTH = 0;
    static boolean HINT_MODE = false;
    static boolean NOTIFY_MODE = false;

    final static int COLORING_MODE = 0;
    final static int CROSSING_MODE = 1;
    final static int ERASING_MODE = 2;

    private int mode = COLORING_MODE;
    static int numHint = 3;
    static int custom = 0;

    private String filePath;
    private String level;
    private String category;

    private ArrayList<NemoBlock> topNumberBlocks = new ArrayList<>();
    private ArrayList<NemoBlock> bottomNumberBlocks = new ArrayList<>();
    private ArrayList<NemoBlock> normalBlocks = new ArrayList<>();

    private int[] verticalUserCount = new int[COL_COUNT];
    private int[] horizontalUserCount = new int[ROW_COUNT];
    private int[] verticalAnswerCount = new int[COL_COUNT];
    private int[] horizontalAnswerCount = new int[ROW_COUNT];
    public int FixedRow [] = new int[ROW_COUNT];
    public int FixedColumn [] = new int[COL_COUNT];

    private int checkCount = 0;
    private int answerCount = 0;

    private SoundPool soundPool;
    private int[] sounds = new int[SOUND_COUNT];

    TopNumberBlocksReader topNumberBlocksReader;
    BottomNumberBlocksReader bottomNumberBlocksReader;
    NormalBlocksReader normalBlocksReader;

    private ImageButton coloringButton, crossingButton, eraseButton;
    private ImageView gameClearImg;
    public static TextView ticketButton;

    private Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_page);

        Intent intent = getIntent();
        level = intent.getExtras().getString("level");
        category = intent.getExtras().getString("category");

        if(custom == 0) {
            filePath = "levels/" + category + "level" + level + ".txt";
        }
        else{
            filePath = level;
        }

        coloringButton = findViewById(R.id.coloring_button);
        crossingButton = findViewById(R.id.crossing_button);
        eraseButton = findViewById(R.id.erase_button);
        ticketButton = findViewById(R.id.ticket_button);
        gameClearImg = findViewById(R.id.game_clear);

        ticketButton.setText(Integer.toString(numHint));
        ticketButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/JUA.ttf"));

        if(custom == 0) {
            topNumberBlocksReader = new TopNumberBlocksReader(this, true);
            topNumberBlocksReader.execute();
            bottomNumberBlocksReader = new BottomNumberBlocksReader(this, true);
            bottomNumberBlocksReader.execute();
            normalBlocksReader = new NormalBlocksReader(this, true);
            normalBlocksReader.execute();
        }

        else{

            topNumberBlocksReader = new TopNumberBlocksReader(this, false);
            topNumberBlocksReader.execute();
            bottomNumberBlocksReader = new BottomNumberBlocksReader(this, false);
            bottomNumberBlocksReader.execute();
            normalBlocksReader = new NormalBlocksReader(this, false);
            normalBlocksReader.execute();

        }

        LEN_BOTTOM_WIDTH = 0;
        LEN_TOP_WIDTH = 0;
        UNIT_LENGTH = 0;

        loadSounds();

        crossingButton.setAlpha(0.3f);
        eraseButton.setAlpha(0.3f);
        ticketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("num_ticket", Integer.toString(numHint));
                if(!HINT_MODE && numHint > 0) {
                    HINT_MODE = true;
                    ticketButton.setAlpha(0.3f);
                }
                else if(numHint > 0){
                    HINT_MODE = false;
                    ticketButton.setAlpha(1.0f);
                }
                soundPool.play(sounds[TICKET_BUTTON], 1, 1, 0, 0, 1);
            }
        });
        coloringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = COLORING_MODE;
                coloringButton.setAlpha(1.0f);
                crossingButton.setAlpha(0.3f);
                eraseButton.setAlpha(0.3f);
                soundPool.play(sounds[COLORING_BUTTON], 1, 1, 0, 0, 1);
            }
        });
        crossingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = CROSSING_MODE;
                coloringButton.setAlpha(0.3f);
                crossingButton.setAlpha(1.0f);
                eraseButton.setAlpha(0.3f);
                soundPool.play(sounds[CROSSING_BUTTON], 1, 1, 0, 0, 1);
            }
        });
        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = ERASING_MODE;
                coloringButton.setAlpha(0.3f);
                crossingButton.setAlpha(0.3f);
                eraseButton.setAlpha(1.0f);
                soundPool.play(sounds[ERASING_BUTTON], 1, 1, 0, 0, 1);
            }
        });



    }

    public ArrayList<NemoBlock> getTopNumberBlocks() {
        return topNumberBlocks;
    }

    public ArrayList<NemoBlock> getBottomNumberBlocks() {
        return bottomNumberBlocks;
    }

    public ArrayList<NemoBlock> getNormalBlocks() {
        return normalBlocks;
    }

    public TopNumberBlocksReader getTopNumberBlocksReader() {
        return topNumberBlocksReader;
    }

    public BottomNumberBlocksReader getBottomNumberBlocksReader() {
        return bottomNumberBlocksReader;
    }

    public NormalBlocksReader getNormalBlocksReader() {
        return normalBlocksReader;
    }

    public void setAnswerCount() {
        for(int i = 0; i < ROW_COUNT; i++){
            answerCount += horizontalAnswerCount[i];
        }
    }

    public void setVerticalAnswerCount(int col, int answer){
        verticalAnswerCount[col] = answer;
    }

    public int getVerticalAnswerCount(int col){
        return verticalAnswerCount[col];
    }

    public void setHorizontalAnswerCount(int row, int answer){
        horizontalAnswerCount[row] = answer;
    }

    public int getHorizontalAnswerCount(int row) { return horizontalAnswerCount[row]; }

    public void setUserCount(int row, int column, boolean increase) {
        if(increase){
            verticalUserCount[column]++;
            horizontalUserCount[row]++;
            checkCount++;
        } else {
            verticalUserCount[column]--;
            horizontalUserCount[row]--;
            checkCount--;
        }

    }

    public boolean isVerticallyFinished(int position){
        Log.d("isVerticallyFinished", Integer.toString(verticalAnswerCount[position]) + "/"
                + Integer.toString(verticalUserCount[position]));
        return verticalAnswerCount[position] == verticalUserCount[position];
    }

    public boolean isHorizontallyFinished(int position){
        Log.d("isHorizontallyFinished", Integer.toString(horizontalAnswerCount[position]) + "/"
                + Integer.toString(horizontalUserCount[position]));
        return horizontalAnswerCount[position] == horizontalUserCount[position];
    }

    public int getMode() {
        return mode;
    }

    public int getAnswerCount(){ return answerCount; }

    public int getCheckCount() { return checkCount; }

    public int getNumHint(){
        return numHint;
    }

    public void setNumHint(int n){
        numHint = n;
    }

    public String getFilePath() {return filePath; }

    public void startNotifyService(){
        NOTIFY_MODE = true;
        Intent intent = new Intent(GameActivity.this, HintNotifyService.class);
        startService(intent);
    }

    public void stopNotifyService(){
        NOTIFY_MODE = false;
        Intent intent = new Intent(GameActivity.this, HintNotifyService.class);
        stopService(intent);
    }

    public void gameClear(int wrongCount) {

        SharedPreferences pref = getSharedPreferences("level", MODE_PRIVATE);

        coloringButton.setAlpha(0.0f);
        crossingButton.setAlpha(0.0f);
        eraseButton.setAlpha(0.0f);

        if(custom == 0){

            String levelBefore = pref.getString(category + "level", "0");
            int before = Integer.parseInt(levelBefore);
            int now = Integer.parseInt(level);
            int wrongBefore = pref.getInt(category + level, 0 );

            SharedPreferences.Editor editor = pref.edit();

            if(before < now) {
                editor.putString(category+"level", level);
                editor.putInt(category+level, wrongCount);
            }
            else if (wrongBefore > wrongCount){
                editor.putInt(category+level, wrongCount);
            }
            editor.apply();

            gameClearImg.setVisibility(View.VISIBLE);
            anim = AnimationUtils.loadAnimation(this, R.anim.gameclear);
            gameClearImg.setAnimation(anim);
            soundPool.play(sounds[WINNING], 1, 1, 0, 0, 1);

            goBack(wrongCount);
        }

        else{

            String temp[] = String.valueOf(filePath).split("/");
            String filename = temp[temp.length-1];
            int idx = filename.indexOf(".");
            filename = filename.substring(0, idx);

            int wrongbefore = pref.getInt(filename, -1);

            SharedPreferences.Editor editor = pref.edit();
            if(wrongbefore == -1){
                editor.putInt(filename, wrongCount);
                editor.apply();
            }
            else if(wrongbefore > wrongCount){
                editor.putInt(filename, wrongCount);
                editor.apply();
            }

            gameClearImg.setVisibility(View.VISIBLE);
            anim = AnimationUtils.loadAnimation(this, R.anim.gameclear);
            gameClearImg.setAnimation(anim);
            soundPool.play(sounds[WINNING], 1, 1, 0, 0, 1);
            goBackCustom(wrongCount);

        }

    }

    public void loadSounds(){

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(8).build();
        sounds[WINNING] = soundPool.load(this, R.raw.winning_sound, 1);
        sounds[TICKET_BUTTON] = soundPool.load(this, R.raw.color_click, 1);
        sounds[COLORING_BUTTON] = soundPool.load(this, R.raw.color_click, 1);
        sounds[CROSSING_BUTTON] = soundPool.load(this, R.raw.color_click, 1);
        sounds[ERASING_BUTTON] = soundPool.load(this, R.raw.color_click, 1);
    }

    public void goBack(final int wrong){

        Handler timer = new Handler();
        timer.postDelayed(new Runnable(){
            public void run(){
                LevelSelection.starAnimation = 1;
                Intent intent = new Intent(GameActivity.this, LevelSelection.class);
                intent.putExtra("wrong", wrong);
                intent.putExtra("category", category);
                startActivity(intent);
                finish();
                finish();
            }
        }, 2500);

    }

    public void goBackCustom(final int wrong){

        Handler timer = new Handler();
        timer.postDelayed(new Runnable(){
            public void run(){
                LevelSelectionCustom.starAnimation = 1;
                Intent intent = new Intent(GameActivity.this, LevelSelectionCustom.class);
                intent.putExtra("wrong", wrong);
                startActivity(intent);
                finish();
                finish();
            }
        }, 2500);

    }

}