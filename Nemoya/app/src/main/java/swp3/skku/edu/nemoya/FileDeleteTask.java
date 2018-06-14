package swp3.skku.edu.nemoya;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import static swp3.skku.edu.nemoya.GameActivity.COL_COUNT;
import static swp3.skku.edu.nemoya.GameActivity.NUM_COL_COUNT;
import static swp3.skku.edu.nemoya.GameActivity.NUM_ROW_COUNT;
import static swp3.skku.edu.nemoya.GameActivity.ROW_COUNT;

/**
 * Created by Kyuyeon on 2018-06-13.
 */

public class FileDeleteTask extends AsyncTask<String, Void, Void> {

    LevelSelectionCustom activity;
    private File mFile = null;

    public FileDeleteTask(LevelSelectionCustom activity, String filename) {
        this.activity = activity;
        Log.d("delete3", filename);
        mFile = new File(activity.getExternalFilesDir("myLevel"), filename);
    }

    @Override
    protected Void doInBackground(String... strings) {
        if(mFile.delete()){
            Log.d("delete4", mFile.getPath());
        }
        else{
            Log.d("delete5", mFile.getPath());
        }
        return null;
    }
}
