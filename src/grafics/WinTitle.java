package grafics;

import java.util.ArrayList;

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
		Label label_name = new Label(name);
		Label label_desc = new Label(desc);
		String genres = "";
		JsonNode nodeGenres = data.path("genres");
		if (nodeGenres.isArray()) {
			for(JsonNode genre: nodeGenres)
				genres+=(genre.path("title").asText("None")) + " ";
		}
		Label label_genres = new Label(genres);
		System.out.println(data.path("genres"));
		label_genres.setStyle("-fx-background-color: red;");
		
		root.setLeft(img);
		root.setRight(label_name);
		root.setCenter(label_desc);
		root.setTop(label_genres);
		
		
		
		Scene scene = new Scene(root, 1920,  1080);
		setScene(scene);
		}
}
