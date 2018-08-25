package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
		
		HBox text = new HBox();
		text.getChildren().add(label);
		text.setPadding(new Insets(0, 0, 17, 0));
		
		HBox btns = new HBox(10);
		btns.getChildren().addAll(btnY, btnN);
		btns.setAlignment(Pos.CENTER);
		btns.setPadding(new Insets(0, 0, 10, 0));
		
		BorderPane layout = new BorderPane();
		layout.setTop(text);
		layout.setBottom(btns);
		layout.setPadding(new Insets(10));
		layout.minHeight(100);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		
		return answer;
	}
}
