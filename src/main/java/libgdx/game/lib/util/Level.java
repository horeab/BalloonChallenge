package libgdx.game.lib.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.os.Parcel;
import android.os.Parcelable;

public enum Level {

    // nrOfRows/nrOfCols ~ 0.5

    //@formatter:off
    LEVEL0(0, 0, null, null, true, true, null, HardcodedLevels.LEVEL00),
    LEVEL1(0, 1, null, null, true, true, null, HardcodedLevels.LEVEL01),
    LEVEL2(0, 2, null, null, true, true, null, HardcodedLevels.LEVEL02),
    LEVEL3(0, 3, null, null, true, true, null, HardcodedLevels.LEVEL03),
    LEVEL4(0, 4, null, null, true, true, null, HardcodedLevels.LEVEL04),

    LEVEL5(1, 0, 5, 10, false, true, null, null),
    LEVEL6(1, 1, 6, 12, false, true, null, null),
    LEVEL7(1, 2, 7, 14, false, true, null, null),
    LEVEL8(1, 3, 8, 16, false, true, false, null),
    LEVEL9(1, 4, 9, 18, false, true, false, null),

    LEVEL10(2, 0, 6, 12, false, false, null, null),
    LEVEL11(2, 1, 7, 14, false, false, null, null),
    LEVEL12(2, 2, 8, 16, false, false, null, null),
    LEVEL13(2, 3, 9, 18, false, false, null, null),
    LEVEL14(2, 4, 10, 20, false, false, null, null),
    LEVEL15(2, 5, 11, 22, false, false, false, null),
    LEVEL16(2, 6, 12, 24, false, false, false, null),;
    //@formatter:on

    private int stageNr;
    private int levelNr;

    private Integer nrOfRows;
    private Integer nrOfColumns;

    private boolean onePlayerLevel;
    private boolean easyLevel;

    private Boolean player1Starts;

    private int[][] matrix;

    private Level(int stageNr, int levelNr, Integer nrOfRows, Integer nrOfColumns, boolean onePlayerLevel, boolean easyLevel, Boolean player1Starts,
                  int[][] matrix) {
        this.stageNr = stageNr;
        this.levelNr = levelNr;
        this.nrOfRows = nrOfRows;
        this.nrOfColumns = nrOfColumns;
        this.onePlayerLevel = onePlayerLevel;
        this.easyLevel = easyLevel;
        this.matrix = matrix;
        this.player1Starts = player1Starts;
    }

    public Boolean getPlayer1Starts() {
        return player1Starts;
    }

    public boolean isEasyLevel() {
        return easyLevel;
    }

    public int getStageNr() {
        return stageNr;
    }

    public int getLevelNr() {
        return levelNr;
    }

    public Integer getNrOfRows() {
        if (nrOfRows == null) {
            return matrix.length;
        }
        return nrOfRows;
    }

    public Integer getNrOfColumns() {
        if (nrOfColumns == null) {
            return matrix[0].length;
        }
        return nrOfColumns;
    }

    public boolean isOnePlayerLevel() {
        return onePlayerLevel;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public static int getNrOfLevelsForStage(int stageNr) {
        return getLevelsForStage(stageNr).size();
    }

    public static int getNrOfStages() {
        Set<Integer> stages = new HashSet<Integer>();
        for (Level level : values()) {
            stages.add(level.getStageNr());
        }
        return stages.size();
    }

    public static List<Level> getLevelsForStage(int stageNr) {
        List<Level> levels = new ArrayList<Level>();
        for (Level level : values()) {
            if (level.getStageNr() == stageNr) {
                levels.add(level);
            }
        }
        return levels;
    }

    public static Level getLevelForStageNrAndLevelNr(int stageNr, int levelNr) {
        for (Level level : values()) {
            if (level.getStageNr() == stageNr && level.getLevelNr() == levelNr) {
                return level;
            }
        }
        return null;
    }
}
