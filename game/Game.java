package game;

import javafx.scene.layout.GridPane;

;

public class Game {
	// Class variables:
	static Chip[] chips;
	private static int currPlayer = 0;
	static Chip lastChip;
	private static int[] numbOfChips = { GameGUI.rows * 4, GameGUI.rows * 4 };

	// Class methods
	public static void startGame(Chip... chips) {
		Game.chips = chips;
	}

	public static int tileState(int x, int y) {
		for (Chip chip : chips) {
			if ((chip.getPos_x() == x && chip.getPos_y() == y) && x > -1 && y > -1) {
				return chip.getTeam() == currPlayer ? 1 : 2; 	// 1 -> friend on tile; 2 -> enemy
			}

		}
		if ((x < 0 || x > 7) || (y < 0 || y > 7)) {
			return 3; 											// 3 -> out of bounds
		}
		return 0; 												// 0 -> empty tile
	}

	public static Chip getChipAt(int x, int y) {
		for (int testChipId = 0; testChipId < chips.length; testChipId++) {
			if (chips[testChipId].getPos_x() == x && chips[testChipId].getPos_y() == y) {
				return chips[testChipId];
			}
		}
		return null;
	}

	public static void moveChip(Chip chip, int x, int y) {
		GameGUI.removeNode(chip.getImg());
		GameGUI.placeNode(chip.getImg(), x, y);
		chip.setPos_x(x);
		chip.setPos_y(y);
		cleanPossP(chip);
		nextPlayer();
	}

	private static void nextPlayer() {
		if (getCurrPlayer() == 0) {
			setCurrPlayer(1);
		} else {
			setCurrPlayer(0);
		}
		GameGUI.setTurn(false);
	}

	public static void isGameEnded() {
		if (getNumbOfChips(0) == 0) {			//actions when Player 2 wins (Player 1 has 0 chips left)
			nextPlayer();
			if (ConfirmBox.display(GameGUI.ply2Name + " won!", "Congratulations! " + GameGUI.ply2Name + "has won!\n do you want to close the game?")) {
				GameGUI.stage.close();
				System.exit(0);				//closes the program if the user wants to
			}
		} else if (getNumbOfChips(1) == 0) {	//actions when Player 1 wins (Player 2 has 0 chips left)
			GameGUI.setTurn(true);
			if (ConfirmBox.display(GameGUI.ply1Name + " won!", "Congratulations! " + GameGUI.ply1Name + "has won!\n do you want to close the game?")) {
				GameGUI.stage.close();
				System.exit(0);
			}
		}
		GameGUI.ply1CCOuntL.setText(Integer.toString(getNumbOfChips(0)) + "/" + (GameGUI.rows * 4));
		GameGUI.ply2CCOuntL.setText(Integer.toString(getNumbOfChips(1)) + "/" + (GameGUI.rows * 4));
	}

	public static void cleanPossP(Chip chip) {
		for (int i = 0; i < 4; i++) {
			GameGUI.removeNode(chip.getPossPosImg()[i]);
			for (int j = 0; j < 8; j++) {
				try {
					GameGUI.removeNode(chip.getPossPosImgS()[i][j]);
				} catch (Exception e) {
					System.out.println(GridPane.getRowIndex(chip.getPossPosImgS()[i][j]) + "\n" + e);
				}
			}
		}
	}

	public static int getCurrPlayer() {
		return currPlayer;
	}

	public static void setCurrPlayer(int currPlayer) {
		Game.currPlayer = currPlayer;
	}

	public static int getNumbOfChips(int pos) {
		int tempintW = GameGUI.rows * 4;
		int tempintB = GameGUI.rows * 4;
		for (Chip chip : chips) {
			if (chip.getPos_x() == -1) {
				if (chip.getTeam() == 0)
					tempintW--;
				if (chip.getTeam() == 1)
					tempintB--;
			}
		}
		numbOfChips[0] = tempintW;
		numbOfChips[1] = tempintB;
		return numbOfChips[pos];
	}

	public static void setNumbOfChips(int pos, int v) {
		numbOfChips[pos] = v;
	}
}