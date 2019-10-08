package libgdx.game.lib.model;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.Set;


public class MatrixWithChangedCells {

	private int[][] leveltMatrix;
	private Set<Pair> cellsToUpdate = new HashSet<Pair>();

	public int[][] getLeveltMatrix() {
		return leveltMatrix;
	}

	public void setLeveltMatrix(int[][] leveltMatrix) {
		this.leveltMatrix = leveltMatrix;
	}

	public Set<Pair> getCellsToUpdate() {
		return cellsToUpdate;
	}

	public void setCellsToUpdate(Set<Pair> cellsToUpdate) {
		this.cellsToUpdate = cellsToUpdate;
	}

	public void addCellToUpdate(Pair cellToUpdate) {
		this.cellsToUpdate.add(new MutablePair(cellToUpdate.getLeft(),cellToUpdate.getRight()));
	}

	public void addCellsToUpdate(Set<Pair> cellsToUpdate) {
		for (Pair point : cellsToUpdate) {
			addCellToUpdate(point);
		}
	}

}
