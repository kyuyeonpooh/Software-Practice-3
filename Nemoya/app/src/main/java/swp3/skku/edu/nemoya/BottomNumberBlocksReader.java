package swp3.skku.edu.nemoya;

import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import static swp3.skku.edu.nemoya.GameActivity.LEN_BOTTOM_WIDTH;
import static swp3.skku.edu.nemoya.GameActivity.LEN_TOP_WIDTH;
import static swp3.skku.edu.nemoya.GameActivity.NUM_COL_COUNT;
import static swp3.skku.edu.nemoya.GameActivity.NUM_ROW_COUNT;
import static swp3.skku.edu.nemoya.GameActivity.ROW_COUNT;
import static swp3.skku.edu.nemoya.GameActivity.LETTER_WIDTH;
import static swp3.skku.edu.nemoya.GameActivity.UNIT_LENGTH;

/**
 * Created by Kyuyeon on 2018-05-18.
 */

public class BottomNumberBlocksReader extends AsyncTask<Void, Void, Void> {

    private GameActivity gameActivity;
    private ArrayList<ArrayList<Integer>> bottomNumbers;
    private ArrayList<String> bottomNumberStrings;
    private int startingColumn = 0, maxLineLength = 0;
    private RecyclerViewAdapter adapter;
    private Boolean isorigin;

    BottomNumberBlocksReader(GameActivity gameActivity, Boolean isorigin) {
        this.gameActivity = gameActivity;
        this.isorigin = isorigin;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        bottomNumbers = new ArrayList<>(ROW_COUNT);
        bottomNumberStrings = new ArrayList<>(ROW_COUNT);
        BufferedReader reader = null;
        StringTokenizer tokenizer;
        StringBuilder line;

        if(isorigin){
            try {
                reader = new BufferedReader(new InputStreamReader(
                        gameActivity.getAssets().open(gameActivity.getFilePath())));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else{
            if(isExternalStorageReadable()){
                try {
                    reader = new BufferedReader(new FileReader(gameActivity.getFilePath()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
        int blockNumber;
        try {

            for (int i = 0; i < NUM_ROW_COUNT; i++) {
                reader.readLine();
            }
            for (int i = 0; i < ROW_COUNT; i++) {
                ArrayList<Integer> row = new ArrayList<>(NUM_COL_COUNT);
                tokenizer = new StringTokenizer(reader.readLine());
                int sum = 0;
                while (tokenizer.hasMoreTokens()) {
                    int n = Integer.parseInt(tokenizer.nextToken());
                    row.add(n);
                    sum += n;
                }
                gameActivity.setHorizontalAnswerCount(i, sum);
                if(sum == 0)
                    gameActivity.FixedRow[i] = 1;
                bottomNumbers.add(row);
            }
            gameActivity.setAnswerCount();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int j = 0; j < NUM_COL_COUNT; j++) {
            for (int i = 0; i < ROW_COUNT; i++) {
                if (bottomNumbers.get(i).get(j) != 0) {
                    startingColumn = j;
                    break;
                }
            }
            if (startingColumn > 0)
                break;
        }
        if (startingColumn > 0) {
            for (ArrayList<Integer> arrayList : bottomNumbers) {
                for (int i = 0; i < startingColumn; i++) {
                    arrayList.remove(0);
                }
            }
        }
        for (int i = 0; i < bottomNumbers.size(); i++) {
            line = new StringBuilder(" ");
            for (int j = 0; j < bottomNumbers.get(i).size(); j++) {
                blockNumber = bottomNumbers.get(i).get(j);
                if(blockNumber != 0)
                    line.append(Integer.toString(blockNumber)).append("  ");
            }
            if (line.length() > maxLineLength)
                maxLineLength = line.length();
            bottomNumberStrings.add(line.toString());
        }
        return null;


    }

    @Override
    protected void onPostExecute(Void voids) {

        RecyclerView view = gameActivity.findViewById(R.id.number_blocks_bottom);
        GridLayout mGridLayout = gameActivity.findViewById(R.id.grid_layout);
        ArrayList<NemoBlock> blocks = gameActivity.getBottomNumberBlocks();

        LEN_BOTTOM_WIDTH = LETTER_WIDTH * maxLineLength;
        view.setMinimumWidth(LEN_BOTTOM_WIDTH);

        for (int i = 0; i < bottomNumbers.size(); i++) {
            if(gameActivity.getHorizontalAnswerCount(i) != 0) {
                blocks.add(new NemoBlock(NemoBlock.BOTTOM_NUMBER_BLOCK,
                        bottomNumberStrings.get(i), "#FFFFFF"));
            }
            else{
                blocks.add(new NemoBlock(NemoBlock.BOTTOM_NUMBER_BLOCK,
                        bottomNumberStrings.get(i), "#FFBF00", true));
            }
        }

        Display display = gameActivity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        UNIT_LENGTH = (int)((size.x - LEN_BOTTOM_WIDTH) / 15.5);

        int grid_width = LEN_BOTTOM_WIDTH + UNIT_LENGTH * 15;
        mGridLayout.setLayoutParams(new LinearLayout.LayoutParams(grid_width,
                LEN_TOP_WIDTH * 53 + UNIT_LENGTH * 15));

        adapter = new RecyclerViewAdapter(blocks, gameActivity);
        view.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(gameActivity, 1,
                GridLayoutManager.VERTICAL, false);
        view.setLayoutManager(manager);

    }

    public RecyclerViewAdapter getAdapter() {
        return adapter;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
