package grafics;

import com.fasterxml.jackson.databind.JsonNode;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class WinTitle extends Stage{
	public WinTitle(Image image, String name, String desc, JsonNode data){
		setTitle(name);
		
		BorderPane root = new BorderPane();
		root.setStyle("fx-background-color: black");
		
		ImageView img = new ImageView(image);
		root.setLeft(img);
		
		
		
		Scene scene = new Scene(root, 1920,  1080);
		setScene(scene);
		}
}
