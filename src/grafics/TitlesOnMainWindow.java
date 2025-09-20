package grafics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import mainLogic.Core;


public class TitlesOnMainWindow {
	static BorderPane borderPane_root;
	public static void loadCatalogData(BorderPane parent, HBox hBox_titleToday, ScrollPane scrollTitleToday, Core client) {
		borderPane_root = parent;


	    ObjectMapper mapper = new ObjectMapper();

	    try {
	        String response = client.getOngoing();
	        JsonNode rootNode = mapper.readTree(response).path("response");
	        for (JsonNode data : rootNode) {
	            String img = "https:" + data.path("poster").path("fullsize").asText();// fullsize 
	            int id = data.path("anime_id").asInt();
	            String name = data.path("title").asText();
	            String desc = data.path("description").asText("None");
	            double ratingDoub = data.path("rating").path("average").asDouble(0);
	            String rating = String.format("%.2f", ratingDoub);

	            StackPane mainButtonContainer = new StackPane();
	            mainButtonContainer.setMinSize(300, 500);
	            mainButtonContainer.setPrefSize(300, 500);
	            mainButtonContainer.setMaxSize(300, 500);

	            ImageView blurredBackground = new ImageView(new Image(img, true));
	            blurredBackground.setFitWidth(300);
	            blurredBackground.setFitHeight(500);
	            blurredBackground.setPreserveRatio(false);
	            
	            GaussianBlur blur = new GaussianBlur(15);
	            blurredBackground.setEffect(blur);

	            Rectangle darkOverlay = new Rectangle(300, 500);
	            darkOverlay.setFill(Color.rgb(0, 0, 0, 0.3));
	            darkOverlay.setArcHeight(20);
	            darkOverlay.setArcWidth(20);

	            VBox contentContainer = new VBox(10);
	            contentContainer.setAlignment(Pos.TOP_CENTER);
	            contentContainer.setPadding(new Insets(25, 0, 0, 0));

	            // image and pruning
	            ImageView sharpImageView = new ImageView(new Image(img, 200, 270, true, true, true));
	            sharpImageView.setFitWidth(200);
	            sharpImageView.setFitHeight(270);
	            sharpImageView.setPreserveRatio(true);
	            Rectangle clip = new Rectangle(200, 270);
	            clip.setArcWidth(20);
	            clip.setArcHeight(20);
	            sharpImageView.setClip(clip);
	            
	            StackPane imageContainer = new StackPane();
	            imageContainer.setMinSize(200, 270);
	            imageContainer.setMaxSize(200, 270);
	            
	            Label label_name = new Label(name);
	            label_name.setPrefSize(280, 50);
	            label_name.setWrapText(true);
	            label_name.setStyle("""
	                -fx-background-color: rgba(0, 0, 0, 0.7);
	                -fx-font-size: 16px;
	                -fx-text-fill: white;
	                -fx-text-alignment: center;
	                -fx-alignment: center;
	                -fx-padding: 5px;
	                -fx-background-radius: 5px;
	            """);

	            Label label_rating = new Label("★ " + rating);
	            label_rating.setStyle("""
	                -fx-background-color: rgba(255, 215, 0, 0.8);
	                -fx-text-fill: black;
	                -fx-font-weight: bold;
	                -fx-font-size: 14px;
	                -fx-padding: 3px 8px;
	                -fx-background-radius: 10px;
	            """);

	            imageContainer.getChildren().addAll(sharpImageView, label_name, label_rating);
	            StackPane.setAlignment(label_name, Pos.BOTTOM_CENTER);
	            StackPane.setAlignment(label_rating, Pos.TOP_RIGHT);
	            StackPane.setMargin(label_name, new Insets(10));
	            StackPane.setMargin(label_rating, new Insets(10));

	            Label label_desc = new Label(desc);
	            label_desc.setPrefSize(290, 200);
	            label_desc.setMaxSize(290, 200);
	            label_desc.setWrapText(true);
	            label_desc.setStyle("""
	                -fx-background-color: rgba(0, 0, 0, 0.8);
	                -fx-text-fill: white;
	                -fx-padding: 10px;
	                -fx-font-size: 12px;
	                -fx-background-radius: 10px;
	            """);

	            contentContainer.getChildren().addAll(imageContainer, label_desc);
	            contentContainer.setMargin(label_desc, new Insets(10));

	            mainButtonContainer.getChildren().addAll(blurredBackground, darkOverlay, contentContainer);

	            Button button = new Button("");
	            button.setMinSize(300, 500);
	            button.setPrefSize(300, 500);
	            button.setMaxSize(300, 500);
	            button.setStyle("""
	                -fx-background-color: transparent;
	                -fx-border-color: #3498db;
	                -fx-border-width: 2px;
	                -fx-border-radius: 10px;
	            """);
	            
	            button.setGraphic(mainButtonContainer);
	            button.setContentDisplay(ContentDisplay.CENTER);

	            button.setOnAction(e -> openDetailWindow(sharpImageView.getImage(), name, id, desc, data, client));
	            hBox_titleToday.getChildren().add(button);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        Label errorLabel = new Label("Ошибка загрузки данных: " + e.getMessage());
	        hBox_titleToday.getChildren().add(errorLabel);
	    }
	}
	
	private static void openDetailWindow(Image image, String name, int id, String desc, JsonNode data, Core client) {
		WinTitle win = new WinTitle(image, name, id, desc, data, client);
		BorderPane winTitle = win.createWin();
		borderPane_root.getChildren().add(winTitle);
	}
}
