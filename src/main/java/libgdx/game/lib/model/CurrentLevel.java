package libgdx.game.lib.model;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class CurrentLevel {

	private int[][] leveltMatrix;

	private List<Pair> planes = new ArrayList<Pair>();

	private CurrentMove currentMove = new CurrentMove();

	private Map<Pair, Integer> finalPositionPairsForPlayer1 = new HashMap<Pair, Integer>();
	private Map<Pair, Integer> finalPositionPairsForPlayer2 = new HashMap<Pair, Integer>();

	private Set<Integer> startPositionColumnsForPlayer1 = new HashSet<Integer>();
	private Set<Integer> startPositionColumnsForPlayer2 = new HashSet<Integer>();

	private boolean player1 = true;
	private boolean player2Computer = true;
	private boolean player2ComputerMovesRandom = false;
	private boolean onePlayerLevel = false;

	public int[][] getLeveltMatrix() {
		return leveltMatrix;
	}

	public void setLeveltMatrix(int[][] leveltMatrix) {
		this.leveltMatrix = leveltMatrix;
	}

	public List<Pair> getPlanes() {
		return planes;
	}

	public void setPlanes(List<Pair> planes) {
		this.planes = planes;
	}

	public CurrentMove getCurrentMove() {
		return currentMove;
	}

	public void setCurrentMove(CurrentMove currentMove) {
		this.currentMove = currentMove;
	}

	public Map<Pair, Integer> getFinalPositionPairsForPlayer1() {
		return finalPositionPairsForPlayer1;
	}

	public Set<Integer> getStartPositionColumnsForPlayer1() {
		return startPositionColumnsForPlayer1;
	}

	public Set<Integer> getStartPositionColumnsForPlayer2() {
		return startPositionColumnsForPlayer2;
	}

	public void setFinalPositionPairsForPlayer1(Map<Pair, Integer> finalPositionPairsForPlayer1) {
		this.finalPositionPairsForPlayer1 = finalPositionPairsForPlayer1;
	}

	public Map<Pair, Integer> getFinalPositionPairsForPlayer2() {
		return finalPositionPairsForPlayer2;
	}

	public void setFinalPositionPairsForPlayer2(Map<Pair, Integer> finalPositionPairsForPlayer2) {
		this.finalPositionPairsForPlayer2 = finalPositionPairsForPlayer2;
	}

	public Boolean isPlayer1Turn() {
		if (onePlayerLevel) {
			player2Computer = false;
			return true;
		}
		return player1;
	}

	public void setIsPlayer1Turn(Boolean isPlayer1Turn) {
		this.player1 = isPlayer1Turn;
	}

	public boolean isPlayer2Computer() {
		if (onePlayerLevel) {
			player2Computer = false;
		}
		return player2Computer;
	}

	public void setPlayer2Computer(boolean player2Computer) {
		this.player2Computer = player2Computer;
	}

	public boolean isOnePlayerLevel() {
		return onePlayerLevel;
	}

	public void setOnePlayerLevel(boolean onePlayerLevel) {
		this.onePlayerLevel = onePlayerLevel;
	}

	public boolean isPlayer2ComputerMovesRandom() {
		return player2ComputerMovesRandom;
	}

	public void setPlayer2ComputerMovesRandom(boolean player2ComputerMovesRandom) {
		this.player2ComputerMovesRandom = player2ComputerMovesRandom;
	}
}
