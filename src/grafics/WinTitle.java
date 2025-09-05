package grafics;

import com.fasterxml.jackson.databind.JsonNode;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class WinTitle extends Stage{
	public WinTitle(Image image, String name, String desc, JsonNode data){
		setTitle(name);
		
		BorderPane root = new BorderPane();
		root.setStyle("-fx-padding: 20; -fx-background-color: #f8f9fa;");
		
		Scene scene = new Scene(root, 500,  500);
		setScene(scene);
		}
}
