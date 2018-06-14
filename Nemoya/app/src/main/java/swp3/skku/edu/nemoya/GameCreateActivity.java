package swp3.skku.edu.nemoya;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static swp3.skku.edu.nemoya.GameActivity.COL_COUNT;
import static swp3.skku.edu.nemoya.GameActivity.LEN_BOTTOM_WIDTH;
import static swp3.skku.edu.nemoya.GameActivity.ROW_COUNT;
import static swp3.skku.edu.nemoya.GameActivity.UNIT_LENGTH;

/**
 * Created by Kyuyeon on 2018-06-13.
 */

public class GameCreateActivity extends Activity implements View.OnClickListener{

    final static int SOUND_COUNT = 2;
    final static int COLOR_CLICK = 0;
    final static int CREATE_CLICK = 1;

    private SoundPool soundPool;
    private int[] sounds = new int[SOUND_COUNT];

    final String DEFAULT_WHITE = "#F4F8FF";
    final String DEFAULT_GRAY = "#EDEDED";
    final int COLOR_COUNT = 8;

    final String RED = "#FF4C4C";
    final String ORANGE = "#FFB14C";
    final String YELLOW = "#FFF654";
    final String GREEN = "#49CE42";
    final String BLUE = "#4291CE";
    final String PURPLE = "#8F42CE";
    final String BLACK = "#000000";
    final String WHITE = "#FFFFFF";

    private String[][] myBoard = new String[ROW_COUNT][COL_COUNT];
    private String brushColor = RED;

    private ImageButton[] colorButton = new ImageButton[COLOR_COUNT];
    private int[] colorButtonId = {
            R.id.red_brush, R.id.orange_brush, R.id.yellow_brush, R.id.green_brush,
            R.id.blue_brush, R.id.purple_brush, R.id.black_brush, R.id.eraser
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_create_page);
        loadSounds();

        final EditText editText = findViewById(R.id.filename_input);
        Button submitButton = findViewById(R.id.submit_btn);
        submitButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/JUA.ttf"));
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundPool.play(sounds[CREATE_CLICK], 1, 1, 0, 0, 1);
                if(isExternalStorageWritable()){
                    String filename = editText.getText().toString();
                    if(filename.length() == 0) {
                        Toast.makeText(GameCreateActivity.this,
                                "파일 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (filename.length() > 6) {
                        Toast.makeText(GameCreateActivity.this,
                                "파일 이름은 6글자 이하로 해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    FileOutTask task = new FileOutTask(GameCreateActivity.this, filename + ".txt");
                    task.execute();
                    Log.d("popup", "here0");
                    Intent popup = new Intent(GameCreateActivity.this, CompletePopupActivity.class);
                    popup.putExtra("str", "생성되었습니다.");
                    startActivityForResult(popup, 1);

                }
                else{
                    Log.e("error", "not writable");
                }
            }
        });

        for(int i = 0; i < COLOR_COUNT; i++){
            colorButton[i] = findViewById(colorButtonId[i]);
            colorButton[i].setOnClickListener(this);
        }

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int tableWidth = (int) (size.x - 20);
        int tableHeight = tableWidth;
        int itemWidth = (int) (tableWidth / 15.5);
        int itemHeight =(int) (tableHeight / 15.5);

        TableLayout tableLayout = findViewById(R.id.my_board);
        tableLayout.setLayoutParams(new TableLayout.LayoutParams(tableWidth, tableHeight));
        for(int i = 0; i < ROW_COUNT; i++){
            TableRow tableRow = new TableRow(this);
            tableRow.setGravity(Gravity.CENTER);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(tableWidth, tableHeight));
            for(int j = 0; j < COL_COUNT; j++){
                final Button item = new Button(this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(itemWidth, itemHeight);
                if(i % 5 != 4 && j % 5 != 4) {
                    lp.setMargins(1, 1, 2, 2);
                }
                else if(i % 5 != 4){
                    lp.setMargins(1, 1, 7, 2);
                }
                else if(j % 5 != 4){
                    lp.setMargins(1,1,2,7);
                }
                else{
                    lp.setMargins(1,1,7,7);
                }

                item.setLayoutParams(lp);
                if((i + j) % 2 == 0){
                    item.setBackgroundColor(Color.parseColor(DEFAULT_WHITE));
                } else {
                    item.setBackgroundColor(Color.parseColor(DEFAULT_GRAY));
                }
                final int row = i;
                final int col = j;
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myBoard[row][col] = brushColor;
                        if(brushColor != null && brushColor != WHITE){
                            item.setBackgroundColor(Color.parseColor(brushColor));
                        } else {
                            if((row + col) % 2 == 0){
                                item.setBackgroundColor(Color.parseColor(DEFAULT_WHITE));
                            } else {
                                item.setBackgroundColor(Color.parseColor(DEFAULT_GRAY));
                            }
                        }
                    }
                });
                tableRow.addView(item);
            }
            tableLayout.addView(tableRow);
        }


    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public String[][] getMyBoard() {
        return myBoard;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        for(int i = 0; i < COLOR_COUNT; i++){
            colorButton[i].setBackgroundColor(Color.TRANSPARENT);
        }

        switch (id){
            case R.id.red_brush:
                brushColor = RED;
                colorButton[0].setBackgroundColor(Color.parseColor(DEFAULT_GRAY));
                break;
            case R.id.orange_brush:
                brushColor = ORANGE;
                colorButton[1].setBackgroundColor(Color.parseColor(DEFAULT_GRAY));
                break;
            case R.id.yellow_brush:
                brushColor = YELLOW;
                colorButton[2].setBackgroundColor(Color.parseColor(DEFAULT_GRAY));
                break;
            case R.id.green_brush:
                brushColor = GREEN;
                colorButton[3].setBackgroundColor(Color.parseColor(DEFAULT_GRAY));
                break;
            case R.id.blue_brush:
                brushColor = BLUE;
                colorButton[4].setBackgroundColor(Color.parseColor(DEFAULT_GRAY));
                break;
            case R.id.purple_brush:
                brushColor = PURPLE;
                colorButton[5].setBackgroundColor(Color.parseColor(DEFAULT_GRAY));
                break;
            case R.id.black_brush:
                brushColor = BLACK;
                colorButton[6].setBackgroundColor(Color.parseColor(DEFAULT_GRAY));
                break;
            case R.id.eraser:
                brushColor = WHITE;
                colorButton[7].setBackgroundColor(Color.parseColor(DEFAULT_GRAY));
                break;
        }
        soundPool.play(sounds[COLOR_CLICK], 1, 1, 0, 0, 1);
    }

    public void loadSounds(){
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(8).build();
        sounds[COLOR_CLICK] = soundPool.load(this, R.raw.color_click, 1);
        sounds[CREATE_CLICK] = soundPool.load(this, R.raw.setting_click, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode == 1){

            if(resultCode == RESULT_OK){

                Intent i= new Intent(GameCreateActivity.this, LevelSelectionCustom.class);
                i.putExtra("wrong", 0);
                startActivity(i);
                finish();

            }
        }
    }

}
