package swp3.skku.edu.nemoya;

/**
 * Created by Kyuyeon on 2018-05-09.
 */

public class NemoBlock {

    final static int NORMAL_BLOCK = 0;
    final static int TOP_NUMBER_BLOCK = 1;
    final static int BOTTOM_NUMBER_BLOCK = 2;

    final static int NOT_COLORED = 0;
    final static int COLOERED_RIGHT = 1;
    final static int COLORED_WRONG = 2;
    final static int CROSSING = 3;
    final static int ERASED = 4;

    private int blockType;
    private int blockNumber;
    private String blockNumbers;
    private String color;
    private boolean isTransparent;
    private boolean isColoredByHint;
    private int isColored;

    NemoBlock(int blockType, int blockNumber, String color) {
        this.blockType = blockType;
        this.blockNumber = blockNumber;
        this.color = color;
        isTransparent = false;
        isColored = NOT_COLORED;
        isColoredByHint = false;
    }


    NemoBlock(int blockType, int blockNumber, String color, int isColored) {
        this.blockType = blockType;
        this.blockNumber = blockNumber;
        this.color = color;
        this.isColored = isColored;
        isTransparent = false;
        isColoredByHint = false;
    }

    NemoBlock(int blockType, String blockNumbers, String color) {
        this.blockType = blockType;
        this.blockNumbers = blockNumbers;
        this.color = color;
    }

    NemoBlock(int blockType, int blockNumber, String color, boolean isTransparent) {
        this.blockType = blockType;
        this.blockNumber = blockNumber;
        this.color = color;
        this.isTransparent = isTransparent;
        isColored = NOT_COLORED;
        isColoredByHint = false;
    }

    NemoBlock(int blockType, String blockNumbers, String color, boolean isTransparent) {
        this.blockType = blockType;
        this.blockNumbers = blockNumbers;
        this.color = color;
        this.isTransparent = isTransparent;
    }

    public int getBlockType() {
        return blockType;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public String getBlockNumbers() {
        return blockNumbers;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void transparentize() {
        isTransparent = true;
    }

    public boolean isTransparent() {
        return isTransparent;
    }

    public void setColored(int colored){ isColored = colored; }

    public int getColored(){ return isColored; }

    public void setColoredByHint(boolean coloredByHint){ isColoredByHint = coloredByHint; }

    public boolean getColoredByHint(){ return isColoredByHint; }
}
