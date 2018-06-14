package swp3.skku.edu.nemoya;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

/**
 * Created by Kyuyeon on 2018-06-12.
 */

public class LevelSelectionCustom extends Activity {
    final static int LEVEL_SELECT = 0;
    final static int SOUND_COUNT = 1;

    private SoundPool soundPool;
    private int[] sounds = new int[SOUND_COUNT];

    private ImageView star1;
    private ImageView star2;
    private ImageView star3;

    private int[] star;
    private int levelCount;
    static int starAnimation = 0;
    private int currentWrongCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_list_custom);
        loadSounds();

        final SharedPreferences prefs = getSharedPreferences("level", MODE_PRIVATE);
        Intent intent = getIntent();

        if(intent.getExtras() != null)
            currentWrongCount= intent.getExtras().getInt("wrong", 0);
        else
            currentWrongCount = 0;

        TableLayout tableLayout = findViewById(R.id.table_layout);
        Button createLevelButton = findViewById(R.id.create_level);
        Button deleteLevelButton = findViewById(R.id.delete_level);
        TextView title = findViewById(R.id.my_page_title);

        star1 = LevelSelectionCustom.this.findViewById(R.id.star1);
        star2 = LevelSelectionCustom.this.findViewById(R.id.star2);
        star3 = LevelSelectionCustom.this.findViewById(R.id.star3);

        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/JUA.ttf"));
        createLevelButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/JUA.ttf"));
        deleteLevelButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/JUA.ttf"));

        Vector filePaths;
        String path = this.getExternalFilesDir(null).getPath();

        filePaths = getFileNames(path+"/myLevel", "txt");
        levelCount = filePaths.size();


        String FileName[] = new String[levelCount];
        for(int i=0; i<levelCount; i++){
            String temp[] = String.valueOf(filePaths.get(i)).split("/");
            FileName[i] = temp[temp.length-1];
            int idx = FileName[i].indexOf(".");
            FileName[i] = FileName[i].substring(0, idx);
        }

        star = new int [levelCount];
        for(int i=0; i < levelCount; i++){
            star[i] = prefs.getInt(FileName[i], -1 );
        }

        createLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LevelSelectionCustom.this, GameCreateActivity.class);
                startActivity(intent);
                finish();
            }
        });

        deleteLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(LevelSelectionCustom.this, DeleteLevelPopupActivity.class), 1);
            }
        });

        int count = 0;
        while(count < 20){
            TableRow tableRow = new TableRow(this);
            while(tableRow.getChildCount() < 3 && count < 20){
                Button button = new Button(this);
                final int height = (int)TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                final int margin = (int)TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                final int paddingTop = (int)TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, height);
                params.setMargins(margin, margin, margin, margin);
                button.setLayoutParams(params);
                button.setAllCaps(false);
                if(count < levelCount)
                    button.setText(FileName[count]);
                button.setPadding(0, paddingTop, 0, 0);
                button.setGravity(Gravity.TOP | Gravity.CENTER);
                setBackground(button, count);
                button.setSoundEffectsEnabled(true);
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
                setEnable(button, count);
                if(count <levelCount)
                    setOnclick(button, String.valueOf(filePaths.get(count)));
                tableRow.addView(button);
                count++;
            }
            tableLayout.addView(tableRow);
        }

        if(starAnimation == 1) {
            starAnimation();
            starAnimation = 0;
        }

    }

    public void loadSounds(){
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(8).build();
        sounds[LEVEL_SELECT] = soundPool.load(this, R.raw.stage_click, 1);
    }

    public void setOnclick(Button but, final String filepath){

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LevelSelectionCustom.this, GameActivity.class);
                intent.putExtra("level", filepath);
                intent.putExtra("category", "none");
                GameActivity.custom = 1;
                startActivity(intent);
                soundPool.play(sounds[LEVEL_SELECT], 1, 1, 0, 0, 1);
                finish();
            }
        });
    }
    public void setEnable(Button but, int i){

        if( i < levelCount)
            but.setEnabled(true);
        else
            but.setEnabled(false);

    }

    public Vector getFileNames(String targetDir, String fileExt) {
            Vector fileNames = new Vector();
            File dir = new File(targetDir);
            fileExt = fileExt.toLowerCase();

            if(dir.isDirectory()) {
                String dirName = dir.getPath();
                String[] filenames = dir.list(null);
                int cntFiles = filenames.length;

                for (int iFile = 0; iFile < cntFiles; iFile++) {

                    String filename = filenames[iFile];
                    String fullFileName = dirName + "/" + filename;
                    File file = new File(fullFileName);

                    boolean isDirectory = file.isDirectory();
                    if (!isDirectory && filename.toLowerCase().endsWith(fileExt)) {
                        fileNames.add(fullFileName);
                    }
                }
            }
        return fileNames;
    }

    public void setBackground(Button but, int i){

        if(i < levelCount) {
            if(star[i]== -1){
                but.setBackgroundResource(R.drawable.stage_open);
            }
            else if (star[i] == 0)
                but.setBackgroundResource(R.drawable.stage_clear_border);
            else if (star[i] <= 10) {
                but.setBackgroundResource(R.drawable.stage_star2);
            } else {
                but.setBackgroundResource(R.drawable.stage_star1);
            }

        }
        else{
            but.setBackgroundResource(R.drawable.stage_border);
        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                String result = intent.getStringExtra("result");
                Log.d("delete1", result);
                if(!result.equals("none")){
                    Log.d("delete2", result);
                    FileDeleteTask task = new FileDeleteTask(LevelSelectionCustom.this, result + ".txt");
                    task.execute();

                    Intent f5 = new Intent(LevelSelectionCustom.this, LevelSelectionCustom.class);
                    intent.putExtra("wrong", 0);
                    startActivity(f5);
                    Toast.makeText(LevelSelectionCustom.this,
                            "삭제되었습니다.", Toast.LENGTH_SHORT).show();

                    finish();
                }
            }
        }
    }
}
