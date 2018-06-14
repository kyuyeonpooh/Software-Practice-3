package swp3.skku.edu.nemoya;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import static swp3.skku.edu.nemoya.GameActivity.COL_COUNT;
import static swp3.skku.edu.nemoya.GameActivity.NUM_COL_COUNT;
import static swp3.skku.edu.nemoya.GameActivity.NUM_ROW_COUNT;
import static swp3.skku.edu.nemoya.GameActivity.ROW_COUNT;

/**
 * Created by Kyuyeon on 2018-06-13.
 */

public class FileOutTask extends AsyncTask<String, Void, Void> {

    GameCreateActivity activity;
    private File mFile = null;

    public FileOutTask (GameCreateActivity activity, String filename) {
        this.activity = activity;
        mFile = new File(activity.getExternalFilesDir("myLevel"), filename);
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            FileOutputStream stream =  new FileOutputStream(mFile);
            PrintWriter writer = new PrintWriter(stream);

            String[][] board = activity.getMyBoard();
            int[] topNumbers = new int[NUM_ROW_COUNT * COL_COUNT];
            int[] bottomNumbers = new int[ROW_COUNT * NUM_COL_COUNT];

            /* calculate top number data */
            int k;
            for(int j = 0; j < 15; j++) {
                k = 0;
                int count = 0;
                for(int i = 14; i >= 0; i--) {
                    if(board[i][j] != null && i != 0) {
                        count++;
                    }
                    else if(i == 0 && board[i][j] != null) {
                        count++;
                        topNumbers[15*(7-k)+j] = count;
                        k++;
                        count = 0;
                    }
                    else if(count != 0) {
                        topNumbers[15*(7-k)+j] = count;
                        k++;
                        count = 0;
                    }
                }
            }

            /* write top number data into file */
            for(int i = 0; i < 15 * 8; i++) {
                writer.print(Integer.toString(topNumbers[i]) + " ");
                if(i % 15 == 14)
                    writer.println("");
            }
            writer.flush();

            /* calculate bottom number data */
            for(int i = 14; i >= 0; i--) {
                k = 0;
                int count = 0;
                for(int j = 14; j >= 0; j--) {
                    if(board[i][j] != null && j != 0) {
                        count++;
                    }
                    else if(j == 0 && board[i][j] != null) {
                        count++;
                        bottomNumbers[8*i+(7-k)] = count;
                        k++;
                        count = 0;
                    }
                    else if(count != 0) {
                        bottomNumbers[8*i+(7-k)] = count;
                        k++;
                        count = 0;
                    }
                }
            }

            /* write bottom number data into file */
            for(int i = 0; i < 15 * 8; i++) {
                writer.print(Integer.toString(bottomNumbers[i]) + " ");
                if(i % 8 == 7)
                    writer.println("");
            }
            writer.flush();

            /* write color block data */
            for(int i = 0; i < ROW_COUNT; i++){
                StringBuilder builder = new StringBuilder();
                for(int j = 0; j < COL_COUNT; j++){
                    if(board[i][j] == null){
                        builder.append("empty ");
                    } else {
                        builder.append(board[i][j]).append(" ");
                    }
                }
                writer.println(builder.toString());
            }
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
