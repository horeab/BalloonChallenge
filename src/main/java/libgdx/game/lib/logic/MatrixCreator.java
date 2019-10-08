package libgdx.game.lib.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import util.MatrixValue;
import android.graphics.Point;

public class MatrixCreator {

	private int nrOfRows;
	private int nrOfCols;
	private List<Point> planes = new ArrayList<Point>();
	int[][] matrix;

	public MatrixCreator(int nrOfRows, int nrOfCols) {
		this.nrOfRows = nrOfRows;
		this.nrOfCols = nrOfCols;
		matrix = createMatrix();
	}

	public int[][] getCreatedMatrix() {
		return matrix;
	}

	public List<Point> getPlanes() {
		Collections.reverse(planes);
		return planes;
	}

	public Set<Integer> startPositionList() {
		Set<Integer> startPosition = new HashSet<Integer>();
		for (int j = 0; j < nrOfCols; j++) {
			startPosition.add(j);
		}
		return startPosition;
	}

	private int[][] createMatrix() {

		int nrOfCells = (nrOfRows - 2) * nrOfCols;
		int[][] matrix = createEmptyMatrix();

		int nrOfLeftArrowsToAdd = calculateNrOfItemsBasedOnPercent(nrOfCells, MatrixValue.ARROW_LEFT.getMinPercent(), MatrixValue.ARROW_LEFT.getMaxPercent());
		int nrOfRightArrowsToAdd = calculateNrOfItemsBasedOnPercent(nrOfCells, MatrixValue.ARROW_RIGHT.getMinPercent(), MatrixValue.ARROW_RIGHT.getMaxPercent());
		int nrOfTornadosToAdd = calculateNrOfItemsBasedOnPercent(nrOfCells, MatrixValue.TORNADO.getMinPercent(), MatrixValue.TORNADO.getMaxPercent());
		int nrOfPointsToAdd = calculateNrOfItemsBasedOnPercent(nrOfCells, MatrixValue.POINTS.getMinPercent(), MatrixValue.POINTS.getMaxPercent());
		int nrOfBLockingCloudsToAdd = calculateNrOfItemsBasedOnPercent(nrOfCells, MatrixValue.BLOCKING_CLOUD.getMinPercent(),
				MatrixValue.BLOCKING_CLOUD.getMaxPercent());
		int nrOfPlanesToAdd = calculateNrOfItemsBasedOnPercent(nrOfCells, MatrixValue.PLANE.getMinPercent(), MatrixValue.PLANE.getMaxPercent());

		matrix = addElementToMatrix(matrix, nrOfLeftArrowsToAdd, MatrixValue.ARROW_LEFT.getValue(), true);
		matrix = addElementToMatrix(matrix, nrOfRightArrowsToAdd, MatrixValue.ARROW_RIGHT.getValue(), true);
		matrix = addElementToMatrix(matrix, nrOfBLockingCloudsToAdd, MatrixValue.BLOCKING_CLOUD.getValue(), true);
		matrix = addElementToMatrix(matrix, nrOfPointsToAdd, MatrixValue.POINTS.getValue(), true);
		matrix = addElementToMatrix(matrix, nrOfTornadosToAdd, MatrixValue.TORNADO.getValue(), false);
		matrix = addPlanesToMatrix(matrix, nrOfPlanesToAdd);

		return matrix;
	}

	public List<Point> findPlanesInMatrix(int[][] matrix) {
		List<Point> planes = new ArrayList<Point>();
		for (int i = 0; i < nrOfRows; i++) {
			for (int j = 0; j < nrOfCols; j++) {
				if (matrix[i][j] == MatrixValue.PLANE.getValue()) {
					planes.add(new Point(j, i));
				}
			}
		}
		return planes;
	}

	private int[][] addPlanesToMatrix(int[][] matrix, int nrOfElemsToAdd) {
		while (nrOfElemsToAdd > 0) {
			Point randomPoint = getRandomRowAndColumnForElement(MatrixValue.PLANE.getValue(), matrix, true);
			matrix[randomPoint.y][randomPoint.x] = MatrixValue.PLANE.getValue();
			planes.add(randomPoint);
			nrOfElemsToAdd--;
		}
		return matrix;
	}

	private int[][] addElementToMatrix(int[][] matrix, int nrOfElemsToAdd, int valueToAdd, boolean addToLastRow) {
		while (nrOfElemsToAdd > 0) {
			Point randomPoint = getRandomRowAndColumnForElement(valueToAdd, matrix, addToLastRow);
			matrix[randomPoint.y][randomPoint.x] = valueToAdd;
			nrOfElemsToAdd--;
		}
		return matrix;
	}

	private Point getRandomRowAndColumnForElement(int valueToAdd, int[][] matrix, boolean addToLastRow) {
		int randomCol = new Random().nextInt(nrOfCols);
		int bestRow = getBestRowToAddToForBalancing(matrix, valueToAdd, addToLastRow);

		while (matrix[bestRow][randomCol] != MatrixValue.AIR.getValue()) {
			randomCol = new Random().nextInt(nrOfCols);
			bestRow = getBestRowToAddToForBalancing(matrix, valueToAdd, addToLastRow);
		}

		Point point = new Point(randomCol, bestRow);

		return point;

	}

	private int[][] createEmptyMatrix() {
		int[][] leveltMatrix = new int[nrOfRows][nrOfCols];
		for (int i = 0; i < nrOfRows; i++) {
			for (int j = 0; j < nrOfCols; j++) {
				int val = MatrixValue.AIR.getValue();
				leveltMatrix[i][j] = val;
			}
		}
		return leveltMatrix;
	}

	private int getBestRowToAddToForBalancing(int[][] matrix, int valueToAdd, boolean addToLastRow) {
		List<Integer> valuesOnRows = new ArrayList<Integer>();
		for (int i = 0; i < nrOfRows - 3; i++) {
			int values = 0;
			for (int j = 0; j < nrOfCols; j++) {
				if (matrix[i][j] == valueToAdd) {
					values++;
				}
			}
			valuesOnRows.add(values);
		}

		if (!addToLastRow) {
			valuesOnRows.remove(0);
		}

		int minValue = 99;
		int indx = 0;
		for (Integer integer : valuesOnRows) {
			if (integer < minValue) {
				minValue = integer;
			}
			indx++;
		}

		indx = 0;
		List<Integer> bestRows = new ArrayList<Integer>();
		for (Integer valueOnRow : valuesOnRows) {
			if (valueOnRow == minValue) {
				bestRows.add(indx);
			}
			indx++;
		}

		int bestRow = bestRows.get(new Random().nextInt(bestRows.size()));

		if (!addToLastRow) {
			bestRow = bestRow + 1;
		}

		return bestRow;
	}

	private int calculateNrOfItemsBasedOnPercent(int nrOfCells, int minPercent, int maxPercent) {
		int minNrOfItems = (int) (((float) minPercent / 100) * (float) nrOfCells);
		int maxNrOfItems = (int) (((float) maxPercent / 100) * (float) nrOfCells);

		return new Random().nextInt((maxNrOfItems + 1) - minNrOfItems) + minNrOfItems;
	}
}
