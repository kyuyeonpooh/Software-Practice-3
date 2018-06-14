package swp3.skku.edu.nemoya;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import static swp3.skku.edu.nemoya.GameActivity.COL_COUNT;
import static swp3.skku.edu.nemoya.GameActivity.NUM_ROW_COUNT;
import static swp3.skku.edu.nemoya.GameActivity.ROW_COUNT;
import static swp3.skku.edu.nemoya.GameActivity.LEN_TOP_WIDTH;

/**
 * Created by Kyuyeon on 2018-05-18.
 */

public class TopNumberBlocksReader extends AsyncTask<Void, Void, Void> {

    private GameActivity gameActivity;
    private ArrayList<ArrayList<Integer>> topNumbers;
    private RecyclerViewAdapter adapter;
    private boolean isorigin;

    TopNumberBlocksReader(GameActivity gameActivity, boolean isorigin) {
        this.gameActivity = gameActivity;
        this.isorigin = isorigin;
        Log.d("here", gameActivity.getFilePath());
        Log.d("here", String.valueOf(isorigin) + "dd");

    }

    @Override
    protected Void doInBackground(Void... voids) {
        topNumbers = new ArrayList<>(NUM_ROW_COUNT);
        BufferedReader reader = null;
        StringTokenizer tokenizer;
        Log.d("file", "here");
        if(isorigin){
            Log.d("here?", "why here");
            try {
                reader = new BufferedReader(new InputStreamReader(
                        gameActivity.getAssets().open(gameActivity.getFilePath())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.d("here?2", "good");
            if(isExternalStorageReadable()){
                try {
                    reader = new BufferedReader(new FileReader(gameActivity.getFilePath()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
        try {
            for (int i = 0; i < NUM_ROW_COUNT; i++) {
                ArrayList<Integer> row = new ArrayList<>(COL_COUNT);
                tokenizer = new StringTokenizer(reader.readLine());
                while (tokenizer.hasMoreTokens()) {
                    row.add(Integer.parseInt(tokenizer.nextToken()));
                }
                for (int n : row) {
                    if (n != 0) {
                        topNumbers.add(row);
                        break;
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LEN_TOP_WIDTH = topNumbers.size();

        return null;
    }


    @Override
    protected void onPostExecute(Void voids) {
        RecyclerView view = gameActivity.findViewById(R.id.number_blocks_top);
        ArrayList<NemoBlock> blocks = gameActivity.getTopNumberBlocks();

        for(int j = 0; j < topNumbers.get(0).size(); j++){
            int sum = 0;
            for(int i = 0; i < topNumbers.size(); i++){
                sum += topNumbers.get(i).get(j);
            }
            if(sum == 0){
                gameActivity.FixedColumn[j] = 1;
            }
            gameActivity.setVerticalAnswerCount(j, sum);
        }

        for (int i = 0; i < topNumbers.size(); i++) {
            for (int j = 0; j < topNumbers.get(i).size(); j++) {
                if(gameActivity.getVerticalAnswerCount(j) != 0) {
                    blocks.add(new NemoBlock(NemoBlock.TOP_NUMBER_BLOCK,
                            topNumbers.get(i).get(j), "#FFFFFF"));
                }
                else{
                    blocks.add(new NemoBlock(NemoBlock.TOP_NUMBER_BLOCK,
                            topNumbers.get(i).get(j), "#FFBF00", true));
                }
            }
        }

        adapter = new RecyclerViewAdapter(blocks, gameActivity);
        view.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(gameActivity, COL_COUNT,
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