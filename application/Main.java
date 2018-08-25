package application;

import game.GameGUI;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
	static Slider sliderSup;
	static Slider sliderRow;
	static TextField ply1nameI;
	static TextField ply2nameI;

	@Override
	public void start(Stage primaryStage) throws Exception {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Game settings");

		window.setOnCloseRequest(e -> {
			e.consume();
			if (ConfirmBox.display("Close Program", "Do you want to abort the setup process and close the Game?")) {
				window.close();
				System.exit(0);
			}
		});

		//Labels
		Label ply1name = new Label("Name of player 1: ");
		Label ply2name = new Label("Name of player 2: ");
		Label rowL = new Label("Rows: ");
		Label rowsCountLabel = new Label("");

		//Inputs
		ply1nameI = new TextField("Player 1");
		ply2nameI = new TextField("Player 2");
		sliderRow = new Slider(1, 3, 2);
		sliderRow.valueProperty().addListener((obj, oldV, newV) -> sliderRow.setValue(newV.intValue()));
		rowsCountLabel.textProperty().bind((sliderRow.valueProperty()).asString());
		sliderSup = new Slider(0, 1, 0);
		sliderSup.valueProperty().addListener((obj, oldV, newV) -> sliderSup.setValue(newV.intValue()));
		Button startG = new Button("Start Game");
		startG.setOnAction(e -> {
			window.close();
			startGame();
		});

		HBox ply1 = new HBox(15);
		ply1.getChildren().addAll(ply1name, ply1nameI);
		HBox ply2 = new HBox(15);
		ply2.getChildren().addAll(ply2name, ply2nameI);
		HBox rowBox = new HBox(15);
		rowBox.getChildren().addAll(rowL, sliderRow, rowsCountLabel);

		VBox layout = new VBox(10);
		layout.getChildren().addAll(ply1, ply2, rowBox, startG);
		layout.setPadding(new Insets(15));

		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.show();
	}

	private static void startGame() {
		GameGUI game1 = new GameGUI(ply1nameI.getText(), ply2nameI.getText(), sliderRow.getValue());

	}

	public static void main(String[] args) {
		launch(args);
	}
}
