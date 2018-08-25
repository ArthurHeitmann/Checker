package game;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameGUI {
	static String ply1Name;
	static String ply2Name;
	static Label ply1nl;
	static Label ply2nl;
	static StringProperty ply1a;
	static StringProperty ply2a;
	static GridPane gField;
	static int rows;
	static Chip[] chips;
	static Stage stage;
	static Label ply1CCOuntL;
	static Label ply2CCOuntL;

	public GameGUI(String ply1n, String ply2n, double rows) {
		ply1Name = ply1n;					//Give Settings from previous windows to current game
		ply2Name = ply2n;					//and create the Game GUI
		GameGUI.rows = (int) rows;
		generateGUI();
	}

	private static void generateGUI() {
		stage = new Stage();
		ply1a = new SimpleStringProperty("■");					//Property to automatically update current active player
		ply2a = new SimpleStringProperty(" ");
		ply1nl = new Label(ply1Name + ply1a.getValue());
		ply2nl = new Label(ply2a.getValue() + ply2Name);

		BorderPane CCount = new BorderPane();
		ply1CCOuntL = new Label((rows * 4) + "/" + (rows * 4));
		ply2CCOuntL = new Label((rows * 4) + "/" + (rows * 4));
		CCount.setLeft(ply1CCOuntL);
		CCount.setRight(ply2CCOuntL);

		AnchorPane gInfo = new AnchorPane();					//Make game info appear in right and left top corner
		AnchorPane.setLeftAnchor(ply1nl, 0.0);
		AnchorPane.setRightAnchor(ply2nl, 0.0);
		AnchorPane.setTopAnchor(ply1nl, 0.0);
		AnchorPane.setTopAnchor(ply2nl, 0.0);
		gInfo.getChildren().addAll(ply1nl, ply2nl);

		gField = new GridPane();								//actual game field
		gField.setPadding(new Insets(10));
		placeTiles();											//create visuals
		createChips(rows);

		VBox layout = new VBox();
		layout.getChildren().addAll(gInfo, CCount, gField);
		Scene scene = new Scene(layout, 820, 920);
		scene.getStylesheets().add(GameGUI.class.getResource("style.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
		Game.startGame(chips);
	}

	private static void createChips(int rows) {
		int chipID = 0;
		chips = new Chip[rows * 8];
		for (int row = 0; row < rows; row++) {
			for (int colu = 0; colu < 8; colu++) {
				if (isTileW(colu, row)) {
					createChip(chipID, 1, colu, row);
				}
				//////////Black///////to////////White////////////////
				if (isTileW(colu, 7 - row)) {
					createChip(chipID, 0, colu, 7 - row);
				}
				chipID++;
			}
		}
	}

	public static void placeNode(Node node, int x, int y) {
		gField.add(node, x, y);
		try {
		} catch (Exception e) {
			System.out.println("x: " + x + " y: " + y + "\n" + e);
		}
	}

	private static void createChip(int chipId, int color, int col, int row) {
		chips[chipId] = new Chip(color, col, row);
		gField.add(chips[chipId].getImg(), col, row);
	}

	public static boolean isTileW(int pos_x, int pos_y) {
		if ((pos_x > 7 || pos_y > 7) || (pos_x < 0 || pos_y < 0)) {
			return false;
		}
		if (pos_x % 2 == 0) {
			if (pos_y % 2 == 0) {
				return true;
			} else {
				return false;
			}
		} else {
			if (pos_y % 2 == 0) {
				return false;
			} else {
				return true;
			}
		}
	}

	public static void removeNode(Node node) {
		gField.getChildren().remove(node);
	}

	public static void setTurn(boolean won) {
		if (Game.getCurrPlayer() == 0) {
			ply1nl.setText(ply1Name + "■");
			ply2nl.setText(ply2Name + "");
		} else if (Game.getCurrPlayer() == 1) {
			ply2nl.setText("■" + ply2Name);
			ply1nl.setText(ply1Name + "");
		} else {
			System.out.println("Wrong turn input");
		}
		if (won) {
			if (Game.getCurrPlayer() == 0) {
				ply2nl.setText(ply2Name + "WINNER --> ");
				ply1nl.setText(ply1Name + "");
			} else {
				ply1nl.setText("WINNER --> " + ply1Name);
				ply2nl.setText(ply2Name + "");
			}
		}
	}

	private static void placeTiles() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (i % 2 == 0) {
					if (j % 2 == 0) {
						gField.add(new ImageView(new Image(GameGUI.class.getResourceAsStream("/img/tw.png"))), i, j);
					} else {
						gField.add(new ImageView(new Image(GameGUI.class.getResourceAsStream("/img/tb.png"))), i, j);
					}
				} else {
					if (j % 2 == 0) {
						gField.add(new ImageView(new Image(GameGUI.class.getResourceAsStream("/img/tb.png"))), i, j);
					} else {
						gField.add(new ImageView(new Image(GameGUI.class.getResourceAsStream("/img/tw.png"))), i, j);
					}
				}
			}
		}
	}

}
