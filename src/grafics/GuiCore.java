package grafics;

import java.io.InputStream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.animation.TranslateTransition;
import javafx.util.Duration;

import mainLogic.Core;
import mainLogic.Config;

public class GuiCore extends Application {
	private HBox hBox_titleToday;
	private ScrollPane scrollTitleToday;

	@Override
	public void start(Stage primaryStage) {
		BorderPane borderPane_root = new BorderPane();
		StackPane backgroundContainer = new StackPane();

		BorderPane borderPane = new BorderPane();
		Image image = new Image("file:background21111.png");
        borderPane.setStyle("-fx-background-image: url('" + image + "'); " +
                "-fx-background-size: cover; " +
                "-fx-background-position: center; " +
                "-fx-background-repeat: no-repeat;");
        System.out.print(image);
        
        
		Button leftButton = new Button("◀");
		Button rightButton = new Button("▶");

		String buttonStyle = """
				    -fx-background-color: rgb(155, 155, 155);
				    -fx-text-fill: white;
				    -fx-font-size: 24px;
				    -fx-font-weight: bold;
				    -fx-min-width: 60px;
				    -fx-min-height: 60px;
				    -fx-background-radius: 5px;
				    -fx-border-radius: 60px;
				""";
		leftButton.setStyle(buttonStyle);
		rightButton.setStyle(buttonStyle);

		hBox_titleToday = new HBox(50);
		hBox_titleToday.setPadding(new Insets(0, 50, 0, 50));
		hBox_titleToday.setAlignment(Pos.CENTER_LEFT);
		hBox_titleToday.setStyle("""
			    -fx-background-color: transparent;
			    -fx-background: transparent;
			""");

		scrollTitleToday = new ScrollPane(hBox_titleToday);
		scrollTitleToday.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollTitleToday.setVbarPolicy(ScrollBarPolicy.NEVER);
		scrollTitleToday.setVvalue(0);
		scrollTitleToday.setFitToHeight(true);
		scrollTitleToday.setPrefViewportHeight(500);
		scrollTitleToday.setPannable(true);
		scrollTitleToday.setStyle("""
			    -fx-background-color: transparent;
			    -fx-background: transparent;
			""");

		
		StackPane overlayPane = new StackPane();
		overlayPane.getChildren().add(scrollTitleToday);
		overlayPane.getChildren().add(leftButton);
		overlayPane.getChildren().add(rightButton);
		StackPane.setAlignment(leftButton, Pos.CENTER_LEFT);
		StackPane.setAlignment(rightButton, Pos.CENTER_RIGHT);
		StackPane.setMargin(leftButton, new Insets(0, 0, 0, 20));
		StackPane.setMargin(rightButton, new Insets(0, 20, 0, 0));
		overlayPane.setStyle("""
				-fx-background-color: transparent;
				-fx-background: transparent;
		""");


		borderPane_root.setCenter(overlayPane);

		leftButton.setOnAction(e -> scrollLeft());
		rightButton.setOnAction(e -> scrollRight());

		loadCatalogData();

		Scene scene = new Scene(borderPane_root, 1920, 1080);
		primaryStage.setTitle("AniHi");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void scrollRight() {
		scrollTitleToday.setHvalue(scrollTitleToday.getHvalue() + 0.2);
	}

	private void scrollLeft() {
		scrollTitleToday.setHvalue(scrollTitleToday.getHvalue() - 0.2);
	}

	private void loadCatalogData() {
	    String baseUrl = Config.getApiUrl();
	    String apiKey = Config.getApiKey();

	    ObjectMapper mapper = new ObjectMapper();
	    Core client = new Core(baseUrl, apiKey);

	    try {
	        String response = client.getOngoing();
	        JsonNode rootNode = mapper.readTree(response).path("response");
	        for (JsonNode data : rootNode) {
	            String img = "https:" + data.path("poster").path("fullsize").asText();
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

	            ImageView sharpImageView = new ImageView(new Image(img, 200, 270, true, true, true));
	            sharpImageView.setFitWidth(200);
	            sharpImageView.setFitHeight(270);
	            sharpImageView.setPreserveRatio(true);
	
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

	            button.setOnAction(e -> openDetailWindow(sharpImageView.getImage(), name, desc, data));
	            hBox_titleToday.getChildren().add(button);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        Label errorLabel = new Label("Ошибка загрузки данных: " + e.getMessage());
	        hBox_titleToday.getChildren().add(errorLabel);
	    }
	}
	
	private static void openDetailWindow(Image image, String name, String desc, JsonNode data) {
		WinTitle win = new WinTitle(image, name, desc, data);
		win.show();
	}

	public static void showGUI() {
		launch();
	}
}
