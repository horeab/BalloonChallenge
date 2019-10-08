package libgdx.game.lib.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class MovementFinishedInfo {

    private Pair finalPosition;
    private int nrOfMoves;
    private boolean destroyed;
    private int score;

    private Set<Pair> cellsToUpdate = new HashSet<Pair>();

    private List<Integer> soundsToPlayInOrder = new ArrayList<Integer>();

    public Pair getFinalPosition() {
        return finalPosition;
    }

    public void setFinalPosition(Pair finalPosition) {
        this.finalPosition = finalPosition;
    }

    public int getNrOfMoves() {
        return nrOfMoves;
    }

    public void setNrOfMoves(int nrOfMoves) {
        this.nrOfMoves = nrOfMoves;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public Set<Pair> getCellsToUpdate() {
        return cellsToUpdate;
    }

    public void addCellToUpdate(Pair cellToUpdate) {
        this.cellsToUpdate.add(new MutablePair(cellToUpdate.getLeft(), cellToUpdate.getRight()));
    }

    public void addCellsToUpdate(Set<Pair> cellsToUpdate) {
        for (Pair point : cellsToUpdate) {
            addCellToUpdate(point);
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Integer> getSoundsToPlayInOrder() {
        return soundsToPlayInOrder;
    }

    public void setSoundsToPlayInOrder(List<Integer> soundsToPlayInOrder) {
        this.soundsToPlayInOrder = soundsToPlayInOrder;
    }
}
