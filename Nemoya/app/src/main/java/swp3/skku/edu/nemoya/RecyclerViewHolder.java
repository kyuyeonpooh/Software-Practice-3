package swp3.skku.edu.nemoya;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static swp3.skku.edu.nemoya.GameActivity.HINT_MODE;
import static swp3.skku.edu.nemoya.GameActivity.ROW_COUNT;
import static swp3.skku.edu.nemoya.GameActivity.COL_COUNT;
import static swp3.skku.edu.nemoya.GameActivity.LEN_BOTTOM_WIDTH;
import static swp3.skku.edu.nemoya.GameActivity.UNIT_LENGTH;
import static swp3.skku.edu.nemoya.GameActivity.WINNING;
import static swp3.skku.edu.nemoya.GameActivity.numHint;

/**
 * Created by Kyuyeon on 2018-05-17.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    final private static int DEFAULT_COLOR = Color.parseColor("#E5EFFF");
    final private static int DEFAULT_NUM_COLOR = Color.parseColor("#E1181818");
    final private static int TRANS_NUM_COLOR = Color.parseColor("#8F181818");
    final private static String LABEL_FILLED = "#FFBF00";

    private Context context = null;

    private CardView cardView;
    private TextView textView;

    private static int wrongCount = 0;

    RecyclerViewHolder(View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.card_view);
    }

    public void setData(final NemoBlock nemoBlock, final int position, final GameActivity gameActivity) {
        String numberText;
        textView = itemView.findViewById(R.id.text_view);
        context = gameActivity;

        switch (nemoBlock.getBlockType()) {
            case NemoBlock.TOP_NUMBER_BLOCK:
                numberText = Integer.toString(nemoBlock.getBlockNumber());
                if(nemoBlock.getBlockNumber() != 0)
                    textView.setText(numberText);
                else
                    textView.setText("");
                textView.setTextColor(Color.parseColor(nemoBlock.getColor()));

                if(!nemoBlock.isTransparent()){
                    cardView.setCardBackgroundColor(DEFAULT_NUM_COLOR);
                } else {
                    cardView.setCardBackgroundColor(TRANS_NUM_COLOR);
                }
                cardView.setLayoutParams(new RelativeLayout.LayoutParams(UNIT_LENGTH, 53));

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int Hint = gameActivity.getNumHint();
                        if( HINT_MODE && numHint > 0 && gameActivity.FixedColumn[position % COL_COUNT] != 1 ){

                            finishVerticalLine(position % COL_COUNT, gameActivity);
                            for(int i = 0; i < ROW_COUNT; i++){
                                int index = ( i * COL_COUNT ) + ( position % COL_COUNT );
                                String blockColor = gameActivity.getNormalBlocks().get(index).getColor();
                                int isColored = gameActivity.getNormalBlocks().get(index).getColored();
                                if(!blockColor.equals("empty")
                                        && (isColored==NemoBlock.NOT_COLORED || isColored==NemoBlock.ERASED
                                        || isColored==NemoBlock.CROSSING))
                                    gameActivity.setUserCount(i, position % COL_COUNT, true);
                                if(gameActivity.isHorizontallyFinished(i)) {
                                    finishHorizontalLine(i, gameActivity);
                                    finishHorizontalBlockLine(i, gameActivity);
                                }
                            }

                            finishVerticalBlockLine(position % COL_COUNT, gameActivity);

                            if( gameActivity.getAnswerCount() == gameActivity.getCheckCount() ){
                                gameActivity.gameClear(wrongCount);
                                wrongCount = 0;
                            }

                            HINT_MODE = false;
                            gameActivity.setNumHint(Hint-1);
                            gameActivity.ticketButton.setAlpha(1.0f);
                            gameActivity.ticketButton.setText(Integer.toString(numHint));
                            if(numHint <= 2)
                                gameActivity.startNotifyService();
                            if(numHint <= 0)
                                gameActivity.ticketButton.setAlpha(0.3f);
                            Log.d("num_ticket", Integer.toString(numHint));
                        }

                    }
                });

                break;
            case NemoBlock.BOTTOM_NUMBER_BLOCK:
                numberText = nemoBlock.getBlockNumbers();
                textView.setText(numberText);
                textView.setTextColor(Color.parseColor(nemoBlock.getColor()));
                if(!nemoBlock.isTransparent()){
                    cardView.setCardBackgroundColor(DEFAULT_NUM_COLOR);
                } else {
                    cardView.setCardBackgroundColor(TRANS_NUM_COLOR);
                }
                cardView.setLayoutParams(new RelativeLayout.LayoutParams(LEN_BOTTOM_WIDTH, UNIT_LENGTH));

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int Hint = gameActivity.getNumHint();

                        if( HINT_MODE && numHint > 0 && gameActivity.FixedRow[position] != 1){

                            finishHorizontalLine(position, gameActivity);
                            for(int i = 0; i < COL_COUNT; i++){
                                int index = ( position * COL_COUNT ) + i;
                                String blockColor = gameActivity.getNormalBlocks().get(index).getColor();
                                int isColored = gameActivity.getNormalBlocks().get(index).getColored();
                                if(!blockColor.equals("empty")
                                        && (isColored==NemoBlock.NOT_COLORED || isColored==NemoBlock.ERASED
                                        || isColored==NemoBlock.CROSSING))
                                    gameActivity.setUserCount(position, i, true);
                                if(gameActivity.isVerticallyFinished(i)) {
                                    finishVerticalLine(i, gameActivity);
                                    finishVerticalBlockLine(i, gameActivity);
                                }
                            }

                            finishHorizontalBlockLine(position, gameActivity);

                            if( gameActivity.getAnswerCount() == gameActivity.getCheckCount() ){
                                gameActivity.gameClear(wrongCount);
                                wrongCount = 0;
                            }

                            HINT_MODE = false;
                            gameActivity.setNumHint(Hint-1);
                            gameActivity.ticketButton.setAlpha(1.0f);
                            gameActivity.ticketButton.setText(Integer.toString(numHint));

                            if(numHint <= 2)
                                gameActivity.startNotifyService();
                            if(numHint <= 0) {
                                gameActivity.ticketButton.setAlpha(0.3f);
                            }
                            Log.d("num_ticket", Integer.toString(numHint));
                        }


                    }
                });

                break;
            case NemoBlock.NORMAL_BLOCK:
                cardView.setCardBackgroundColor(DEFAULT_COLOR);
                cardView.setLayoutParams(new RelativeLayout.LayoutParams(UNIT_LENGTH, UNIT_LENGTH));

                final int row = position / 15;
                final int column = position % 15;

                if ( nemoBlock.getColoredByHint() )
                    coloredByHint(nemoBlock, gameActivity, position, textView, cardView);
                else if(nemoBlock.getColored() == NemoBlock.COLORED_WRONG){
                    nemoBlock.setColored(NemoBlock.COLORED_WRONG);
                    textView.setText("X");
                    textView.setTextColor(Color.RED);
                }
                else if (!nemoBlock.getColor().equals("empty") && nemoBlock.getColored() == NemoBlock.COLOERED_RIGHT) {
                    textView.setTextColor(Color.GREEN);
                    cardView.setCardBackgroundColor(Color.parseColor(nemoBlock.getColor()));
                    nemoBlock.setColored(NemoBlock.COLOERED_RIGHT);
                }
                else if(nemoBlock.getColored() == NemoBlock.CROSSING){
                    Log.d("isGetColored", Integer.toString(position));
                    textView.setText("X");
                    textView.setTextColor(Color.BLACK);
                    cardView.setCardBackgroundColor(DEFAULT_COLOR);
                    nemoBlock.setColored(NemoBlock.CROSSING);
                }
                else if(nemoBlock.getColored() == NemoBlock.NOT_COLORED){
                    textView.setText("");
                }

                /**
                 Text color
                 1. RED: the block which user mistakenly checked and red crossed
                 2. GREEN: the block which user just filled the color
                 3. BLUE: the block which user just erased
                 4. BLACK: the block which user just crossed
                 */
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (textView.getCurrentTextColor() == Color.RED) {
                            return;
                        }
                        if( gameActivity.FixedRow[row] == 1 || gameActivity.FixedColumn[column] == 1 ) {
                            return;
                        }
                        switch (gameActivity.getMode()) {
                            case GameActivity.COLORING_MODE:
                                if(textView.getCurrentTextColor() == Color.GREEN){
                                    break;
                                }
                                textView.setText("");
                                if (!nemoBlock.getColor().equals("empty")) {
                                    nemoBlock.setColored(NemoBlock.COLOERED_RIGHT);
                                    textView.setTextColor(Color.GREEN);
                                    cardView.setCardBackgroundColor(Color.parseColor(nemoBlock.getColor()));
                                    gameActivity.setUserCount(row, column, true);
                                    if(gameActivity.isVerticallyFinished(column)){
                                        finishVerticalBlockLine(column, gameActivity);
                                        finishVerticalLine(column, gameActivity);
                                    }
                                    if(gameActivity.isHorizontallyFinished(row)){
                                        finishHorizontalBlockLine(row, gameActivity);
                                        finishHorizontalLine(row, gameActivity);
                                    }
                                    if( gameActivity.getAnswerCount() == gameActivity.getCheckCount() ){
                                        gameActivity.gameClear(wrongCount);
                                        wrongCount = 0;
                                    }
                                } else {
                                    nemoBlock.setColored(NemoBlock.COLORED_WRONG);
                                    textView.setText("X");
                                    doBlinkEffect();
                                    textView.setTextColor(Color.RED);
                                }
                                break;
                            case GameActivity.CROSSING_MODE:
                                Log.d("Onclick", Integer.toString(position));
                                textView.setText("X");
                                textView.setTextColor(Color.BLACK);
                                cardView.setCardBackgroundColor(DEFAULT_COLOR);
                                nemoBlock.setColored(NemoBlock.CROSSING);
                                break;
                            case GameActivity.ERASING_MODE:
                                if(textView.getCurrentTextColor() == Color.BLUE){
                                    break;
                                }
                                else if(textView.getCurrentTextColor() == Color.GREEN){
                                    gameActivity.setUserCount(row, column, false);
                                }
                                textView.setText("");
                                textView.setTextColor(Color.BLUE);
                                cardView.setCardBackgroundColor(DEFAULT_COLOR);
                                nemoBlock.setColored(NemoBlock.ERASED);
                                break;
                        }
                    }

                });
                break;
        }
    }

    private void coloredByHint(final NemoBlock nemoBlock, final GameActivity gameActivity, final int position,
                               TextView textView, CardView cardView){
        Log.d("Colored by hint", Integer.toString(position));
        textView.setText("");
        if(nemoBlock.getColored() == NemoBlock.COLORED_WRONG){
            nemoBlock.setColored(NemoBlock.COLORED_WRONG);
            textView.setText("X");
            textView.setTextColor(Color.RED);
        }
        else if (!nemoBlock.getColor().equals("empty")) {
            textView.setTextColor(Color.GREEN);
            cardView.setCardBackgroundColor(Color.parseColor(nemoBlock.getColor()));
            nemoBlock.setColored(NemoBlock.COLOERED_RIGHT);
        }
        else{
            textView.setText("X");
            textView.setTextColor(Color.BLACK);
            cardView.setCardBackgroundColor(DEFAULT_COLOR);
        }
    }

    private void doBlinkEffect(){
        wrongCount++;
        Log.d("gameing", Integer.toString(wrongCount));
        ObjectAnimator animator = ObjectAnimator.ofInt(cardView, "backgroundColor",
                DEFAULT_COLOR, Color.argb(200, 255, 0, 0), DEFAULT_COLOR);
        animator.setDuration(500);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(2);
        animator.start();
    }

    private void finishHorizontalLine(int row, GameActivity gameActivity){
        gameActivity.getBottomNumberBlocks().get(row).setColor(LABEL_FILLED);
        gameActivity.getBottomNumberBlocks().get(row).transparentize();
        gameActivity.getBottomNumberBlocksReader().getAdapter().notifyItemChanged(row);
        gameActivity.FixedRow[row] = 1;
    }

    private void finishVerticalLine(int column, GameActivity gameActivity){
        int i = column;
        while(i < gameActivity.getTopNumberBlocks().size()) {
            gameActivity.getTopNumberBlocks().get(i).setColor(LABEL_FILLED);
            gameActivity.getTopNumberBlocks().get(i).transparentize();
            i += COL_COUNT;
        }
        gameActivity.getTopNumberBlocksReader().getAdapter().notifyDataSetChanged();
        gameActivity.FixedColumn[column] = 1;
    }

    private void finishHorizontalBlockLine(int row, GameActivity gameActivity){
        for(int i = 0; i < COL_COUNT; i++) {
            int index = ( row * COL_COUNT ) + i;
            gameActivity.getNormalBlocks().get(index).setColoredByHint(true);
        }
        gameActivity.getNormalBlocksReader().getAdapter().notifyDataSetChanged();
    }

    private void finishVerticalBlockLine(int column, GameActivity gameActivity){
        for(int i = 0; i < ROW_COUNT; i++) {
            int index = ( i * COL_COUNT ) + column;
            gameActivity.getNormalBlocks().get(index).setColoredByHint(true);
        }
        gameActivity.getNormalBlocksReader().getAdapter().notifyDataSetChanged();
    }
}