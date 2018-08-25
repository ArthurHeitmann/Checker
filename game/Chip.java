package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/*
 * Variable Explanations: team 0 -> white team 1 -> black tempImg -> the actual image of the chip on the game field possPosImg[] -> keeps all the
 * images, for the up to 4 actions a player has, to place his chip. Can be clicked to place Chip
 */

public class Chip {
	//Object variables
	private int team;
	private int pos_x;
	private int pos_y;
	private ImageView tempImg;
	private static ImageView possPosImg[] = new ImageView[4];
	private int imgCount = 0;
	private boolean aboutToAttack = false;
	private int[][] imgToAtt = new int[4][3];
	//Super mode
	private boolean superB = false;
	private ImageView[][] possPosImgS = new ImageView[4][8];
	private int[] sPossAtt = { -2, -2, -2, -2 };
	private Chip[] killChipS = new Chip[4];
	private int[][] directions = { { -1, -1 }, { 1, -1 }, { -1, 1 }, { 1, 1 } };

	//constructor
	public Chip(int col, int posX, int posY) {
		team = col;
		pos_x = posX;
		pos_y = posY;
		for (int i = 0; i < 4; i++) { //assign images to array of possible positions
			possPosImg[i] = new ImageView(new Image(Chip.class.getResourceAsStream("/img/shadow.png")));
			possPosImg[i].setPickOnBounds(true);
			for (int j = 0; j < 8; j++) {
				int i1 = i;
				int j1 = j;
				possPosImgS[i][j] = new ImageView(new Image(Chip.class.getResourceAsStream("/img/shadow.png")));
				possPosImgS[i][j].setPickOnBounds(true);
				possPosImgS[i][j].setOnMouseClicked(e -> sMove(i1, j1));
			}
		}
		if (team == 0) { //use black or white texture whether the team is 0(w) or 1(b)
			tempImg = new ImageView(new Image(Chip.class.getResourceAsStream("/img/fw.png")));
		} else {
			tempImg = new ImageView(new Image(Chip.class.getResourceAsStream("/img/fb.png")));
		}
		tempImg.setPickOnBounds(true);
		;
		tempImg.setOnMouseClicked((MouseEvent e) -> possPosClick()); //triggers method when chip is being clicked
	}

	//methods
	private void possPosClick() {
		if (Game.lastChip == this) { //removes any previous created possible position images
			Game.cleanPossP(this);
		} else if (Game.lastChip == null) {
		} else {
			Game.cleanPossP(Game.lastChip);
		}
		imgCount = 0;
		if (team == Game.getCurrPlayer()) { //checks whether the clicked chip equals the current player
			if (superB) {		//goes to super movement if chip is super
				sPossMove();
				Game.lastChip = this;
				return;
			}
			for (int y = -1; y < 2; y += 2) {			//look for attack possibilities
				for (int x = -1; x < 2; x += 2) {
					if (Game.tileState(pos_x + x, pos_y + y) == 2) {
						if (Game.tileState(pos_x + (x * 2), pos_y + (y * 2)) == 0) {
							GameGUI.placeNode(possPosImg[imgCount], pos_x + (x * 2), pos_y + (y * 2));
							aboutToAttack = true;
							clearArray();
							imgToAtt[imgCount][0] = pos_x + x;
							imgToAtt[imgCount][1] = pos_y + y;
							imgToAtt[imgCount][2] = imgCount;
							imgCount++;
						}
					}
				}
			}
			if (team == 0) {
				for (int j = 1; j > -2; j -= 2) {		//go to empty tile
					if (Game.tileState(pos_x + j, pos_y - 1) == 0) {
						GameGUI.placeNode(possPosImg[imgCount], pos_x + j, pos_y - 1);
						imgCount++;
						superBSet();
					}
				}
			} else {
				for (int j = 1; j > -2; j -= 2) {		//go to empty tile
					if (Game.tileState(pos_x + j, pos_y + 1) == 0) {
						GameGUI.placeNode(possPosImg[imgCount], pos_x + j, pos_y + 1);
						imgCount++;
						superBSet();
					}
				}

			}
		}

		Game.lastChip = this;
		possPosImg[0].setOnMouseClicked(e -> {
			Game.moveChip(this, (int) GridPane.getColumnIndex(possPosImg[0]), (int) GridPane.getRowIndex(possPosImg[0]));
			imgCount = 0;
			attack(0);
			superBSet();
		});
		possPosImg[1].setOnMouseClicked(e -> {
			Game.moveChip(this, (int) GridPane.getColumnIndex(possPosImg[1]), (int) GridPane.getRowIndex(possPosImg[1]));
			imgCount = 0;
			attack(1);
			superBSet();
		});
		possPosImg[2].setOnMouseClicked(e -> {
			Game.moveChip(this, (int) GridPane.getColumnIndex(possPosImg[2]), (int) GridPane.getRowIndex(possPosImg[2]));
			imgCount = 0;
			attack(2);
			superBSet();
		});
		possPosImg[3].setOnMouseClicked(e -> {
			Game.moveChip(this, (int) GridPane.getColumnIndex(possPosImg[3]), (int) GridPane.getRowIndex(possPosImg[3]));
			imgCount = 0;
			attack(3);
			superBSet();
		});
	}

	private void superBSet() {
		if ((team == 0 && pos_y == 0) || (team == 1 && pos_y == 7)) {
			superB = true;
			GameGUI.removeNode(tempImg);
			if (team == 0) {
				tempImg = new ImageView(new Image(Chip.class.getResourceAsStream("/img/fw_s.png")));
			} else {
				tempImg = new ImageView(new Image(Chip.class.getResourceAsStream("/img/fb_s.png")));
			}
			GameGUI.placeNode(tempImg, pos_x, pos_y);
			tempImg.setOnMouseClicked((MouseEvent e) -> possPosClick());
		}
	}

	private void sMove(int direc, int id) {
		Game.moveChip(this, GridPane.getColumnIndex(possPosImgS[direc][id]), GridPane.getRowIndex(possPosImgS[direc][id]));
		if (aboutToAttack) {
			GameGUI.removeNode(killChipS[direc].getImg());
			killChipS[direc].setPos_x(-1);
			killChipS[direc].setPos_y(-1);
			Game.isGameEnded();
		}
	}

	private void sPossMove() {
		for (int i = 0; i < 4; i++) {
			//killChipS[i] = null;
			sPossAtt[i] = -2;
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < sGetDirecCount()[i]; j++) {
				GameGUI.placeNode(possPosImgS[i][j], pos_x + (directions[i][0] * (j + 1)), pos_y + (directions[i][1] * (j + 1)));
			}
		}
		if (aboutToAttack) {
			for (int i = 0; i < 4; i++) {
				if (sPossAtt[i] > -1) {
					GameGUI.placeNode(possPosImgS[i][7], pos_x + (sPossAtt[i] * directions[i][0]), pos_y + (sPossAtt[i] * directions[i][1]));	///error
				}
			}
		}
	}

	private int[] sGetDirecCount() {
		int[] possCount = { 0, 0, 0, 0 };
		int extCounter1 = 0;
		for (int i = -1; i < 2; i += 2) {		//repeat for every direction Y
			for (int k = -1; k < 2; k += 2) {	//repeat for every direction X
				boolean obst = false;
				for (int j = 1; j < 9; j++) {	//repeat for max length in each direction
					if ((Game.tileState(pos_x + (k * j), pos_y + (i * j)) == 1) || (Game.tileState(pos_x + (k * j), pos_y + (i * j)) == 3)) {	//counts not empty tiles
						obst = true;		//so that the above counter stops once the next field isn't empty any more
					}
					if ((Game.tileState(pos_x + (k * j), pos_y + (i * j)) == 2) && !obst) {	//counts all empty tile is one direction
						obst = true;
						if ((Game.tileState(pos_x + (k * (j + 1)), pos_y + (i * (j + 1))) == 0)) {
							sPossAtt[extCounter1] = j + 1;
							aboutToAttack = true;
							killChipS[extCounter1] = Game.getChipAt(pos_x + (k * j), pos_y + (i * j));
						}
					}
					if ((Game.tileState(pos_x + (k * j), pos_y + (i * j)) == 0) && !obst) {	//counts all empty tile is one direction
						possCount[extCounter1]++;
					}
				}
				extCounter1++;
			}
		}
		return possCount;
	}

	private void attack(int id) {
		if (aboutToAttack) {
			for (int i = 0; i < 4; i++) {
				if (imgToAtt[i][2] == id) {
					//removes the killed chip and changes the coordinates out of the field, so that it's not in the way any more
					GameGUI.removeNode(Game.getChipAt(imgToAtt[i][0], imgToAtt[i][1]).getImg());
					Game.getChipAt(imgToAtt[i][0], imgToAtt[i][1]).setPos_x(-1);
					Game.getChipAt(-1, imgToAtt[i][1]).setPos_y(-1);
					clearArray();
					Game.isGameEnded();
				}
			}
			aboutToAttack = false;
		}
	}

	private void clearArray() {
		for (int i = 0; i < imgToAtt.length; i++) {
			for (int j = 0; j < 3; j++) {
				imgToAtt[i][j] = -1;
			}
		}
	}

	//getters and setters
	ImageView getImg() {
		return tempImg;
	}

	ImageView[] getPossPosImg() {
		return possPosImg;
	}

	ImageView[][] getPossPosImgS() {
		return possPosImgS;
	}

	int getTeam() {
		return team;
	}

	int getPos_x() {
		return pos_x;
	}

	int getPos_y() {
		return pos_y;
	}

	void setPos_x(int x) {
		pos_x = x;
	}

	void setPos_y(int y) {
		pos_y = y;
	}

}
