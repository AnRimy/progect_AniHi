package grafics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
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

import mainLogic.Core;
import mainLogic.Config;

public class GuiCore extends Application {
	private BorderPane borderPane_root;
	private HBox hBox_titleToday;
	private ScrollPane scrollTitleToday;

	@Override
	public void start(Stage primaryStage) {
		Button button_mainMenu = new Button("Main");
		button_mainMenu.setPrefSize(50, 50);
		button_mainMenu.setStyle("-fx-background-radius: 25px");
		Button button_schedule = new Button("Schedule");
		button_schedule.setPrefSize(50, 50);
		button_schedule.setStyle("-fx-background-radius: 25px");
		
		// search widgets
		StackPane sp_search = new StackPane();
		sp_search.setMaxWidth(200);
		sp_search.setMinHeight(50);
		
		TextField textField_search = new TextField();
		textField_search.setPromptText("Поиск");
		textField_search.setMaxWidth(200);
		textField_search.setMaxHeight(60);
		textField_search.setStyle("-fx-background-color: rgba(200, 25, 0, 0.9);"
				+ "-fx-border-color: rgb(255, 234, 0);"
				+ "-fx-background-radius: 30px;"
				+ "-fx-border-radius: 30px;");
		
		Button button_search = new Button("search");
		button_search.setPrefSize(48, 48);
		button_search.setStyle("-fx-background-color: rgba(220, 25, 0, 1);"
				+ "-fx-border-color: rgb(255, 234, 0);"
				+ "-fx-border-radius: 25px;"
				+ "-fx-background-radius: 25px;");
		
		sp_search.getChildren().setAll(textField_search, button_search);
		sp_search.setAlignment(button_search, Pos.CENTER_RIGHT);
		
		
		borderPane_root = new BorderPane();
		borderPane_root.setStyle("""
			    -fx-background-color: linear-gradient(to bottom, #e74c3c, #e67e22);
			    -fx-background-radius: 10px;
			    -fx-background-insets: 5px;
			""");
		
		HBox stackPane_bot_panel = new HBox(20);
		stackPane_bot_panel.setMaxWidth(500);
		stackPane_bot_panel.setMinHeight(60);
		stackPane_bot_panel.getChildren().addAll(button_mainMenu, button_schedule);
		stackPane_bot_panel.setAlignment(Pos.CENTER);
		stackPane_bot_panel.setStyle("-fx-background-color: rgba(155, 155, 155, 0.5);"
				+ "-fx-border-radius: 30px;"
				+ "-fx-background-radius: 30px");

		BorderPane borderPane = new BorderPane();
        
		Button leftButton = new Button("⇦");
		Button rightButton = new Button("⇨");

		String buttonStyle = """
				    -fx-background-color: rgba(200, 25, 0, 0.6);
				    -fx-text-fill: white;
				    -fx-font-size: 24px;
				    -fx-font-weight: bold;
				    -fx-min-width: 60px;
				    -fx-min-height: 60px;
				    -fx-background-radius: 60px;
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
		scrollTitleToday.setMaxHeight(520);
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
		overlayPane.setMaxHeight(625);
		overlayPane.setStyle("""
				-fx-background-color: transparent;
				-fx-background: transparent;
		""");

		borderPane_root.setCenter(overlayPane);
		borderPane_root.setMargin(overlayPane, new Insets(0, 4, 0, 4));
		borderPane_root.setBottom(stackPane_bot_panel);
		borderPane_root.setTop(sp_search);
		borderPane_root.setAlignment(sp_search, Pos.TOP_RIGHT);
		borderPane_root.setMargin(sp_search, new Insets(15, 15, 0, 0));
		borderPane_root.setAlignment(stackPane_bot_panel, Pos.BOTTOM_CENTER);
		borderPane_root.setMargin(stackPane_bot_panel, new Insets(15));

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
	            String img = "https:" + data.path("poster").path("fullsize").asText();// fullsize 
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

	            button.setOnAction(e -> openDetailWindow(sharpImageView.getImage(), name, desc, data));
	            hBox_titleToday.getChildren().add(button);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        Label errorLabel = new Label("Ошибка загрузки данных: " + e.getMessage());
	        hBox_titleToday.getChildren().add(errorLabel);
	    }
	}
	
	private void openDetailWindow(Image image, String name, String desc, JsonNode data) {
		WinTitle win = new WinTitle(image, name, desc, data);
		BorderPane winTitle = win.createWin();
		borderPane_root.getChildren().add(winTitle);
	}
	
	private void openScheduleWindow() {
		WinSchedule winSchedule = new WinSchedule();
		
	}

	public static void showGUI() {
		launch();
	}
}
