package libgdx.game.lib.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import main.LevelFinishedActivity;
import main.MainActivity;
import main.balloonchallenge.R;
import model.CurrentLevel;
import model.LevelInfo;
import model.MatrixWithChangedCells;
import model.PlayerPosition;
import util.MatrixValue;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class MainViewCreator {

	private static final int NR_OF_TRIES_TO_FIND_A_CORRECT_MOVEMENT = 200;

	public final static int ROW_ID_STARTING_INT_VALUE = 200;
	public final static long BALLOON_PAUSE_INTERVAL = 200;
	public final static long PLAYER2_PAUSE_INTERVAL = 400;
	private int imageViewIdForFinalBalloonPosition = 777;

	private Activity context;
	private ImageManager imageManager;
	private SoundManager soundManager;

	private CurrentLevel currentLevel;

	private int nrOfRows;
	private int nrOfCols;

	private LevelInfo levelInfo;

	private MatrixCoordinatesUtils mcu;

	public MainViewCreator(CurrentLevel currentLevel, LevelInfo levelInfo, Activity context) {
		this.context = context;
		this.nrOfRows = currentLevel.getLeveltMatrix().length;
		this.nrOfCols = currentLevel.getLeveltMatrix()[0].length;
		this.currentLevel = currentLevel;
		this.levelInfo = levelInfo;
		soundManager = new SoundManager(context);
		imageManager = new ImageManager(context);
		mcu = new MatrixCoordinatesUtils(nrOfCols, nrOfRows);
	}

	public void addGameRows() {
		List<LinearLayout> gameRows = createGameRows();
		LinearLayout container = (LinearLayout) context.findViewById(R.id.gameContainer);
		for (LinearLayout row : gameRows) {
			container.addView(row);
		}
	}

	private List<LinearLayout> createGameRows() {
		List<LinearLayout> rows = new ArrayList<LinearLayout>();
		for (int i = 0; i < nrOfRows; i++) {
			LinearLayout row = new LinearLayout(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, (float) 1 / nrOfRows);
			row.setId(ROW_ID_STARTING_INT_VALUE + i);
			row.setGravity(Gravity.FILL_HORIZONTAL);
			row.setLayoutParams(params);
			rows.add(row);
		}
		return rows;
	}

	public List<LinearLayout> getCreatedRows() {
		List<LinearLayout> rows = new ArrayList<LinearLayout>();
		for (int i = 0; i < nrOfRows; i++) {
			LinearLayout row = (LinearLayout) context.findViewById(ROW_ID_STARTING_INT_VALUE + i);
			rows.add(row);
		}
		return rows;
	}

	public LinearLayout getCreatedRow(int rowNr) {
		return getCreatedRows().get(rowNr);
	}

	public void createDisplayOfMatrix(int[][] matrix) {
		int row = 0;
		updateMatrixWithStartPositionForPlayer();
		for (LinearLayout viewRow : getCreatedRows()) {
			viewRow.removeAllViews();
			for (int col = 0; col < nrOfCols; col++) {
				View img = createImgView(matrix[row][col], col, row);
				int colColor = getColorForColumn(col);
				img.setBackgroundColor(context.getResources().getColor(colColor));
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
				viewRow.addView(img, lp);
			}
			row++;
		}
	}

	private int getColorForColumn(int col) {
		int colColor = R.color.transparent_light_green_level1;
		if (col % 2 == 0) {
			colColor = R.color.transparent_light_blue_level1;
		}
		return colColor;
	}

	private void updateMatrixWithStartPositionForPlayer() {
		int value = currentLevel.isPlayer1Turn() ? MatrixValue.PLAYER_1.getValue() : MatrixValue.PLAYER_2.getValue();
		Set<Integer> startPositions = currentLevel.isPlayer1Turn() ? currentLevel.getStartPositionColumnsForPlayer1() : currentLevel
				.getStartPositionColumnsForPlayer2();
		resetFirstRowFromMatrix();
		int firstRowIndex = mcu.getFirstRowIndex();
		for (int j = 0; j < nrOfCols; j++) {
			if (startPositions.contains(j)) {
				currentLevel.getLeveltMatrix()[firstRowIndex][j] = value;
			}
		}
	}

	private void refreshDisplayOfMatrix(Set<Point> cellsToUpdate, int[][] matrix) {
		updateMatrixWithStartPositionForPlayer();
		for (Point point : cellsToUpdate) {
			View img = createImgView(matrix[point.y][point.x], point.x, point.y);
			int colColor = getColorForColumn(point.x);
			img.setBackgroundColor(context.getResources().getColor(colColor));
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
			LinearLayout createdRow = getCreatedRow(point.y);
			createdRow.removeViewAt(point.x);
			createdRow.addView(img, point.x, lp);
		}
	}

	public void refreshScore() {
		TextView scorePlayer1 = (TextView) context.findViewById(R.id.scorePlayer1);
		scorePlayer1.setText(calculateScore(currentLevel.getFinalPositionPointsForPlayer1().values()) + "");
		TextView scorePlayer2 = (TextView) context.findViewById(R.id.scorePlayer2);
		scorePlayer2.setText(calculateScore(currentLevel.getFinalPositionPointsForPlayer2().values()) + "");
	}

	public void isPlayer2First() {
		if (!currentLevel.isOnePlayerLevel() && currentLevel.isPlayer2Computer() && !currentLevel.isPlayer1Turn()) {
			togglePlayer(true);
		}
	}

	private int calculateScore(Collection<Integer> collection) {
		int score = 0;
		for (Integer integer : collection) {
			score = score + integer;
		}
		return score;
	}

	private void resetFirstRowFromMatrix() {
		int firstRowIndex = mcu.getFirstRowIndex();
		for (int j = 0; j < nrOfCols; j++) {
			currentLevel.getLeveltMatrix()[firstRowIndex][j] = MatrixValue.AIR.getValue();
		}
	}

	private View createImgView(final int mtrxVal, final int selectedColumn, final int row) {
		View image = null;
		if (mtrxVal == MatrixValue.FINAL_PLAYER_1.getValue()) {
			image = imageManager.getFinalPositionImageWithPoints(currentLevel.getFinalPositionPointsForPlayer1().get(new Point(selectedColumn, row)),
					imageViewIdForFinalBalloonPosition, MatrixValue.FINAL_PLAYER_1);
			imageViewIdForFinalBalloonPosition = imageViewIdForFinalBalloonPosition + 1;
		} else if (mtrxVal == MatrixValue.FINAL_PLAYER_2.getValue()) {
			image = imageManager.getFinalPositionImageWithPoints(currentLevel.getFinalPositionPointsForPlayer2().get(new Point(selectedColumn, row)),
					imageViewIdForFinalBalloonPosition, MatrixValue.FINAL_PLAYER_2);
			imageViewIdForFinalBalloonPosition = imageViewIdForFinalBalloonPosition + 1;
		} else {
			image = imageManager.getImage(MatrixValue.getMatrixValue(mtrxVal));
		}
		image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!currentLevel.isPlayer2Computer() || (currentLevel.isPlayer2Computer() && currentLevel.isPlayer1Turn())) {
					clickBalloon(selectedColumn, mtrxVal);
				}
			}
		});
		return image;
	}

	private void putFinalPositionForPlayer(Point finalPosition) {
		if (currentLevel.isPlayer1Turn()) {
			currentLevel.getFinalPositionPointsForPlayer1().put(new Point(finalPosition.x, finalPosition.y),
					currentLevel.getCurrentMove().getMovementFinishedInfo().getScore());
		} else {
			currentLevel.getFinalPositionPointsForPlayer2().put(new Point(finalPosition.x, finalPosition.y),
					currentLevel.getCurrentMove().getMovementFinishedInfo().getScore());
		}
	}

	private void clickBalloon(int clickedColumn, int cellValue) {
		boolean isPlayerClicked = cellValue == MatrixValue.PLAYER_1.getValue() || cellValue == MatrixValue.PLAYER_2.getValue();
		if (isPlayerClicked && currentLevel.getCurrentMove().isMovementStopped()) {
			currentLevel.getCurrentMove().setClickedColumn(clickedColumn);

			Point currentPosition = new Point(clickedColumn, mcu.getFirstRowIndex());
			currentLevel.getCurrentMove().setPlayerPosition(mcu.fillCurrentPositionInfo(currentPosition, currentLevel.getLeveltMatrix()));
			resetState();

			new MoveElement().execute();
		}
	}

	private void movePlanes() {
		for (Point plane : currentLevel.getPlanes()) {
			updatePlaneNeighbours(plane);
			processOldPlanePosition(plane);
			movePlaneRight(plane);
		}
		for (Point plane : currentLevel.getPlanes()) {
			if (currentLevel.getLeveltMatrix()[plane.y][plane.x] == MatrixValue.AIR.getValue()) {
				currentLevel.getLeveltMatrix()[plane.y][plane.x] = MatrixValue.PLANE.getValue();
				currentLevel.getCurrentMove().getMovementFinishedInfo().addCellToUpdate(plane);
			}
		}
	}

	private void movePlaneRight(Point plane) {
		if (plane.x + 1 <= nrOfCols - 1) {
			plane.x = plane.x + 1;
		} else {
			plane.x = 0;
		}
	}

	private void processOldPlanePosition(Point plane) {
		int planePositionValue = currentLevel.getLeveltMatrix()[plane.y][plane.x];
		if (planePositionValue == MatrixValue.PLANE.getValue()) {
			currentLevel.getLeveltMatrix()[plane.y][plane.x] = MatrixValue.AIR.getValue();
		}
		currentLevel.getCurrentMove().getMovementFinishedInfo().addCellToUpdate(plane);
	}

	private void updatePlaneNeighbours(Point plane) {
		MatrixWithChangedCells matrixWithChangedCells = mcu.verifyPlaneNeighbours(plane, currentLevel.getLeveltMatrix(), currentLevel.isPlayer1Turn());
		currentLevel.getCurrentMove().getMovementFinishedInfo().addCellsToUpdate(matrixWithChangedCells.getCellsToUpdate());
		currentLevel.setLeveltMatrix(matrixWithChangedCells.getLeveltMatrix());
	}

	private class MoveElement extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			int tries = 0;
			while (!simulateMovementAndVerifyIfItsCorrect(tries, currentLevel.getCurrentMove().getPlayerPosition().copy(),
					MatrixCoordinatesUtils.cloneArray(currentLevel.getLeveltMatrix()))) {
				tries++;
			}
			int nrOfMoves = currentLevel.getCurrentMove().getMovementFinishedInfo().getNrOfMoves();
			int indx = 0;

			currentLevel.getCurrentMove().getMovementFinishedInfo().setDestroyed(false);
			removePlayerStartPosition(currentLevel.getCurrentMove().getClickedColumn());
			while (indx < nrOfMoves) {
				playSound(indx);
				startElemMovement();
				refreshMatrixOnUi(currentLevel.getCurrentMove().getMovementFinishedInfo().getCellsToUpdate());
				currentLevel.getCurrentMove().getMovementFinishedInfo().getCellsToUpdate().clear();
				pauseMovement();
				indx++;
			}
			return null;
		}

		private void refreshMatrixOnUi(Set<Point> cellsToUpdate) {
			final Set<Point> cellsToUpdateCopy = new HashSet<Point>(cellsToUpdate);
			context.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					refreshDisplayOfMatrix(cellsToUpdateCopy, currentLevel.getLeveltMatrix());
				}
			});
		}

		private void pauseMovement() {
			try {
				Thread.sleep(BALLOON_PAUSE_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			movePlanes();
			finalPositionUpdate();

			addFirstRowToCellUpdate();
			refreshScore();
			currentLevel.getCurrentMove().setMovementStopped(true);

			togglePlayer(false);
			refreshMatrixOnUi(currentLevel.getCurrentMove().getMovementFinishedInfo().getCellsToUpdate());

			if (isLevelFinished()) {
				levelFinished(calculateScore(currentLevel.getFinalPositionPointsForPlayer1().values()), calculateScore(currentLevel
						.getFinalPositionPointsForPlayer2().values()));
			}
		}
	}

	private boolean isLevelFinished() {
		boolean levelFinished = false;
		if (currentLevel.getCurrentMove().isMovementStopped() && currentLevel.getStartPositionColumnsForPlayer1().isEmpty()) {
			if (currentLevel.isOnePlayerLevel()) {
				levelFinished = true;
			} else if (!currentLevel.isOnePlayerLevel() && currentLevel.getStartPositionColumnsForPlayer2().isEmpty()) {
				levelFinished = true;
			}
		}
		return levelFinished;
	}

	private void addFirstRowToCellUpdate() {
		int firstRowIndex = mcu.getFirstRowIndex();
		for (int j = 0; j < nrOfCols; j++) {
			currentLevel.getCurrentMove().getMovementFinishedInfo().addCellToUpdate(new Point(j, firstRowIndex));
		}
	}

	private void finalPositionUpdate() {
		if (!currentLevel.getCurrentMove().getMovementFinishedInfo().isDestroyed()) {
			Point finalPosition = currentLevel.getCurrentMove().getMovementFinishedInfo().getFinalPosition();
			currentLevel.getLeveltMatrix()[finalPosition.y][finalPosition.x] = MatrixCoordinatesUtils.getFinalPositionForPlayer(currentLevel.isPlayer1Turn())
					.getValue();
			currentLevel.getCurrentMove().getMovementFinishedInfo().addCellToUpdate(finalPosition);

			if (!currentLevel.getCurrentMove().getMovementFinishedInfo().isDestroyed()) {
				putFinalPositionForPlayer(finalPosition);
			}
		}
	}

	private void removePlayerStartPosition(int clickedColumn) {
		if (currentLevel.isPlayer1Turn()) {
			currentLevel.getStartPositionColumnsForPlayer1().remove(clickedColumn);
		} else {
			currentLevel.getStartPositionColumnsForPlayer2().remove(clickedColumn);
		}
	}

	private void startElemMovement() {
		Set<Point> cellsToUpdate = new HashSet<Point>();

		int firstRowIndex = mcu.getFirstRowIndex();
		if (currentLevel.getCurrentMove().getPlayerPosition().getCurrentPosition().getPoint().y == firstRowIndex) {
			cellsToUpdate.add(new Point(currentLevel.getCurrentMove().getClickedColumn(), firstRowIndex));
		}

		Point nextPosition = calculateNextPosition(currentLevel.getCurrentMove().getPlayerPosition());
		if (nextPosition != null) {
			cellsToUpdate.add(nextPosition);
			cellsToUpdate.add(currentLevel.getCurrentMove().getPlayerPosition().getCurrentPosition().getPoint());
			cellsToUpdate.add(currentLevel.getCurrentMove().getPlayerPosition().getUpValue().getPoint());

			currentLevel.setLeveltMatrix(mcu.movePlayer(currentLevel.getCurrentMove().getPlayerPosition(), nextPosition, currentLevel.getLeveltMatrix(),
					currentLevel.isPlayer1Turn()));

			currentLevel.getCurrentMove().setPlayerPosition(mcu.fillCurrentPositionInfo(nextPosition, currentLevel.getLeveltMatrix()));
		}
		cellsToUpdate.addAll(processDestroyedBalloon());
		currentLevel.getCurrentMove().getMovementFinishedInfo().addCellsToUpdate(cellsToUpdate);
	}

	private void playSound(int index) {
		int row = currentLevel.getCurrentMove().getPlayerPosition().getCurrentPosition().getPoint().y;
		List<Integer> soundsToPlayInOrder = currentLevel.getCurrentMove().getMovementFinishedInfo().getSoundsToPlayInOrder();
		int cellValue = soundsToPlayInOrder.get(index);
		soundManager.playSound(row, cellValue);
	}

	private Set<Point> processDestroyedBalloon() {
		Set<Point> cellsToUpdate = new HashSet<Point>();
		if (currentLevel.getCurrentMove().getMovementFinishedInfo().isDestroyed()) {
			currentLevel.getLeveltMatrix()[currentLevel.getCurrentMove().getMovementFinishedInfo().getFinalPosition().y][currentLevel.getCurrentMove()
					.getMovementFinishedInfo().getFinalPosition().x] = MatrixCoordinatesUtils.getDestroyedPlayer(currentLevel.isPlayer1Turn()).getValue();
			cellsToUpdate.add(currentLevel.getCurrentMove().getMovementFinishedInfo().getFinalPosition());
		}
		return cellsToUpdate;
	}

	private boolean simulateMovementAndVerifyIfItsCorrect(int tries, PlayerPosition playerPositionCopy, int[][] clonedMatrix) {
		int nrOfMoves = 0;

		Point finalPosition = new Point(playerPositionCopy.getCurrentPosition().getPoint());
		Point nextPosition = calculateNextPosition(playerPositionCopy);

		while (nextPosition != null) {

			incrementPointsForCurrentMove(clonedMatrix, nextPosition);

			if (!mcu.verifyMoveValidity(nextPosition, clonedMatrix, playerPositionCopy, currentLevel.getCurrentMove().getCurrentRandomTornadoNextPosition())
					&& tries < NR_OF_TRIES_TO_FIND_A_CORRECT_MOVEMENT) {
				resetState();
				return false;
			}

			clonedMatrix = mcu.movePlayer(playerPositionCopy, nextPosition, clonedMatrix, currentLevel.isPlayer1Turn());

			finalPosition.x = nextPosition.x;
			finalPosition.y = nextPosition.y;

			playerPositionCopy = mcu.fillCurrentPositionInfo(nextPosition, clonedMatrix);

			if (!mcu.verifyMoveValidity(nextPosition, clonedMatrix, playerPositionCopy, currentLevel.getCurrentMove().getCurrentRandomTornadoNextPosition())
					&& tries < NR_OF_TRIES_TO_FIND_A_CORRECT_MOVEMENT) {
				resetState();
				return false;
			}

			nextPosition = calculateNextPosition(playerPositionCopy);

			nrOfMoves++;
		}

		boolean playerDestroyedAfterTornado = currentLevel.getCurrentMove().getMovementFinishedInfo().isDestroyed()
				&& currentLevel.getCurrentMove().getCurrentRandomTornadoNextPosition() != null;
		if ((finalPosition.equals(currentLevel.getCurrentMove().getCurrentRandomTornadoNextPosition()) || playerDestroyedAfterTornado)
				&& tries < NR_OF_TRIES_TO_FIND_A_CORRECT_MOVEMENT) {
			resetState();
			return false;
		}

		if (currentLevel.getCurrentMove().getMovementFinishedInfo().isDestroyed()) {
			clonedMatrix[finalPosition.y][finalPosition.x] = MatrixCoordinatesUtils.getDestroyedPlayer(currentLevel.isPlayer1Turn()).getValue();
			currentLevel.getCurrentMove().getMovementFinishedInfo().getSoundsToPlayInOrder().add(MatrixValue.DESTROYED_PLAYER_1.getValue());
			nrOfMoves++;
		}

		currentLevel.getCurrentMove().getMovementFinishedInfo().setNrOfMoves(nrOfMoves);
		currentLevel.getCurrentMove().getMovementFinishedInfo().setFinalPosition(finalPosition);

		return true;
	}

	private int calculateBestColumnForPlayer() {
		Map<Integer, Integer> colsAndScores = new HashMap<Integer, Integer>();
		int[][] cloneArray = MatrixCoordinatesUtils.cloneArray(currentLevel.getLeveltMatrix());
		resetState();
		for (Integer i : currentLevel.getStartPositionColumnsForPlayer2()) {
			Point currentPosition = new Point(i, mcu.getFirstRowIndex());
			PlayerPosition playerPosition = mcu.fillCurrentPositionInfo(currentPosition, cloneArray);

			int tries = 0;
			while (!simulateMovementAndVerifyIfItsCorrect(tries, playerPosition, cloneArray)) {
				tries++;
			}

			int score = currentLevel.getCurrentMove().getMovementFinishedInfo().getScore();
			if (currentLevel.getCurrentMove().getMovementFinishedInfo().isDestroyed()) {
				score = -1;
			}
			colsAndScores.put(i, score);
			resetState();
		}
		return mcu.bestColumnForPlayer(colsAndScores, currentLevel.getStartPositionColumnsForPlayer2());
	}

	private void togglePlayer(boolean isFirstTurn) {
		if (!currentLevel.isOnePlayerLevel() && !isFirstTurn) {
			currentLevel.setIsPlayer1Turn(!currentLevel.isPlayer1Turn());
		}
		if (!currentLevel.isPlayer1Turn() && !currentLevel.isOnePlayerLevel() && currentLevel.isPlayer2Computer()
				&& !currentLevel.getStartPositionColumnsForPlayer2().isEmpty()) {

			int bestColumnForPlayer2 = 0;
			if (!currentLevel.isPlayer2ComputerMovesRandom()) {
				bestColumnForPlayer2 = calculateBestColumnForPlayer();
			} else {
				bestColumnForPlayer2 = new ArrayList<Integer>(currentLevel.getStartPositionColumnsForPlayer2()).get(new Random().nextInt(currentLevel
						.getStartPositionColumnsForPlayer2().size()));
			}
			currentLevel.getCurrentMove().setMovementStopped(true);

			pauseMovementForSecondPlayer(bestColumnForPlayer2);
		}
	}

	private void pauseMovementForSecondPlayer(int bestColumnForPlayer) {
		final int finalBestColumnForPlayer2 = bestColumnForPlayer;
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				clickBalloon(finalBestColumnForPlayer2, MatrixValue.PLAYER_2.getValue());
			}
		}, PLAYER2_PAUSE_INTERVAL);
	}

	private void incrementPointsForCurrentMove(int[][] clonedMatrix, Point nextPosition) {
		if (nextPosition != null) {
			int mtrxValue = clonedMatrix[nextPosition.y][nextPosition.x];
			int score = currentLevel.getCurrentMove().getMovementFinishedInfo().getScore() + mcu.calculatePointsForCurrentMove(mtrxValue);
			currentLevel.getCurrentMove().getMovementFinishedInfo().setScore(score);
			currentLevel.getCurrentMove().getMovementFinishedInfo().getSoundsToPlayInOrder().add(mtrxValue);
		}
	}

	private void resetState() {
		currentLevel.getCurrentMove().getMovementFinishedInfo().setScore(0);
		currentLevel.getCurrentMove().setMovementStopped(false);
		currentLevel.getCurrentMove().getMovementFinishedInfo().setDestroyed(false);
		currentLevel.getCurrentMove().getMovementFinishedInfo().getSoundsToPlayInOrder().clear();
		currentLevel.getCurrentMove().setCurrentRandomTornadoNextPosition(null);
	}

	private Point calculateNextPosition(PlayerPosition pos) {

		Point nextPos = null;
		Point currentPos = pos.getCurrentPosition().getPoint();

		MatrixValue uv = pos.getUpValue().getMatrixValue();
		MatrixValue lv = pos.getLeftValue().getMatrixValue();
		MatrixValue rv = pos.getRightValue().getMatrixValue();
		MatrixValue cv = pos.getCurrentPosition().getMatrixValue();

		if (cv.isRandomUp()) {

			nextPos = new Point();
			if (currentLevel.getCurrentMove().getCurrentRandomTornadoNextPosition() == null) {
				currentLevel.getCurrentMove().setCurrentRandomTornadoNextPosition(mcu.getRandomUpPosition(currentPos, currentLevel.getLeveltMatrix()));
			}
			nextPos.x = currentLevel.getCurrentMove().getCurrentRandomTornadoNextPosition().x;
			nextPos.y = currentLevel.getCurrentMove().getCurrentRandomTornadoNextPosition().y;

		} else if (uv != null) {
			if (uv.isMoveUp() && uv.isOverlap()) {
				nextPos = new Point();
				nextPos.x = currentPos.x;
				nextPos.y = currentPos.y - 1;
			} else if (uv.isMoveLeft()) {

				if (lv != null && lv.isDestroy()) {
					currentLevel.getCurrentMove().getMovementFinishedInfo().setDestroyed(true);
				} else if (lv != null && lv.isOverlap()) {
					nextPos = new Point();
					nextPos.x = currentPos.x - 1;
					nextPos.y = currentPos.y;
				}

			} else if (uv.isMoveRight()) {

				if (rv != null && rv.isDestroy()) {
					currentLevel.getCurrentMove().getMovementFinishedInfo().setDestroyed(true);
				} else if (rv != null && rv.isOverlap()) {
					nextPos = new Point();
					nextPos.x = currentPos.x + 1;
					nextPos.y = currentPos.y;
				}

			} else if (uv.isDestroy()) {
				currentLevel.getCurrentMove().getMovementFinishedInfo().setDestroyed(true);
			}
		}

		return nextPos;
	}

	private void levelFinished(int player1Score, int player2Score) {
		Intent intent = new Intent(context, LevelFinishedActivity.class);
		intent.putExtra(LevelFinishedActivity.INTENT_PLAYER1_SCORE, player1Score);
		intent.putExtra(LevelFinishedActivity.INTENT_PLAYER2_SCORE, player2Score);
		intent.putExtra(MainActivity.INTENT_LEVEL_INFO, levelInfo);
		context.startActivity(intent);
		context.finish();
	}

}
