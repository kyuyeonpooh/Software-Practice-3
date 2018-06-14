package swp3.skku.edu.nemoya;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Kyuyeon on 2018-05-09.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private ArrayList<NemoBlock> nemoBlocks;
    private GameActivity gameActivity;

    RecyclerViewAdapter(ArrayList<NemoBlock> nemoBlocks, GameActivity gameActivity) {
        this.nemoBlocks = nemoBlocks;
        this.gameActivity = gameActivity;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final CardView cardView;
        switch (viewType) {
            case NemoBlock.TOP_NUMBER_BLOCK:
                cardView = (CardView) LayoutInflater.from(gameActivity).inflate(R.layout.number_block_top,
                        parent, false);
                break;
            case NemoBlock.BOTTOM_NUMBER_BLOCK:
                cardView = (CardView) LayoutInflater.from(gameActivity).inflate(R.layout.number_block_bottom,
                        parent, false);
                break;
            default:
                cardView = (CardView) LayoutInflater.from(gameActivity).inflate(R.layout.nemo_block,
                        parent, false);

        }
        return new RecyclerViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.setData(nemoBlocks.get(position), position, gameActivity);
    }

    @Override
    public int getItemViewType(int position) {
        return nemoBlocks.get(position).getBlockType();
    }

    @Override
    public int getItemCount() {
        return nemoBlocks.size();
    }
}
