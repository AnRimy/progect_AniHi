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

	private TextField textField_search;
	
	private Core client;
	
	@Override
	public void start(Stage primaryStage) {
		
	    String baseUrl = Config.getApiUrl();
	    String apiKey = Config.getApiKey();
	    client = new Core(baseUrl, apiKey);
	    
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
		
		textField_search = new TextField();
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
		button_search.setOnAction(e -> startSearch());

		TitlesOnMainWindow.loadCatalogData(borderPane_root, hBox_titleToday, scrollTitleToday, client);

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
	
	private void openScheduleWindow() {
		WinSchedule winSchedule = new WinSchedule();
	}
	
	private void startSearch() {
		if (textField_search.getText().length() >= 3)
			SearchTitles.scanning(textField_search.getText(), borderPane_root, client);
	}

	public static void showGUI() {
		launch();
	}
}
