package swp3.skku.edu.nemoya;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import static swp3.skku.edu.nemoya.GameActivity.COL_COUNT;
import static swp3.skku.edu.nemoya.GameActivity.NUM_ROW_COUNT;
import static swp3.skku.edu.nemoya.GameActivity.ROW_COUNT;

/**
 * Created by Kyuyeon on 2018-05-18.
 */

public class NormalBlocksReader extends AsyncTask<Void, Void, Void> {

    private GameActivity gameActivity;
    private ArrayList<ArrayList<String>> colors;
    private RecyclerViewAdapter adapter;
    private Boolean isorigin;

    NormalBlocksReader(GameActivity gameActivity, Boolean isorigin) {
        this.gameActivity = gameActivity;
        this.isorigin = isorigin;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        colors = new ArrayList<>(ROW_COUNT);
        BufferedReader reader = null;
        StringTokenizer tokenizer;

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
        try {
            for (int i = 0; i < ROW_COUNT + NUM_ROW_COUNT; i++) {
                reader.readLine();
            }
            for (int i = 0; i < ROW_COUNT; i++) {
                ArrayList<String> row = new ArrayList<>(COL_COUNT);
                tokenizer = new StringTokenizer(reader.readLine());
                while (tokenizer.hasMoreTokens()) {
                    row.add(tokenizer.nextToken());
                }
                colors.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void voids) {

        RecyclerView view = gameActivity.findViewById(R.id.normal_blocks);
        ArrayList<NemoBlock> blocks = gameActivity.getNormalBlocks();
        for (int i = 0; i < colors.size(); i++) {
            for (int j = 0; j < colors.get(i).size(); j++) {
                if(gameActivity.FixedColumn[j]==1 || gameActivity.FixedRow[i] == 1)
                    blocks.add(new NemoBlock(NemoBlock.NORMAL_BLOCK, 0, colors.get(i).get(j), NemoBlock.CROSSING));
                else
                    blocks.add(new NemoBlock(NemoBlock.NORMAL_BLOCK, 0, colors.get(i).get(j)));
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
