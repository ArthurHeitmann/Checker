package game;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {
	static boolean answer;
	public static boolean display(String title, String message) {
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);
		
		Label label = new Label(message);
		
		Button btnY = new Button("Yes");
		Button btnN = new Button("No");
		btnY.setOnAction(e -> {
			answer = true;
			window.close();
		});
		btnN.setOnAction(e -> {
			answer = false;
			window.close();
		});
		
		
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, btnY, btnN);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		
		return answer;
	}
}
