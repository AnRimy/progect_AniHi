package grafics;



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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.animation.TranslateTransition;
import javafx.util.Duration;

import mainLogic.Core;
import mainLogic.Config;

public class GuiCore extends Application{
	private HBox hBox_titleToday;
	private ScrollPane scrollTitleToday;
	
	@Override
	public void start(Stage primaryStage) {
    	BorderPane borderPane_root = new BorderPane();
    	borderPane_root.setStyle("-fx-background-color: #FFC943");
    	
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
    	hBox_titleToday.setStyle("-fx-background-color: #FFFFFF");
    	hBox_titleToday.setPadding(new Insets(0, 50, 0, 50));
    	hBox_titleToday.setAlignment(Pos.CENTER_LEFT);

    	
    	scrollTitleToday = new ScrollPane(hBox_titleToday);
    	scrollTitleToday.setHbarPolicy(ScrollBarPolicy.NEVER);
    	scrollTitleToday.setVbarPolicy(ScrollBarPolicy.NEVER);
    	scrollTitleToday.setVvalue(0);
    	scrollTitleToday.setFitToHeight(true);
    	scrollTitleToday.setPrefViewportHeight(500);
    	scrollTitleToday.setPannable(true);
    	
        StackPane overlayPane = new StackPane();
        overlayPane.getChildren().add(scrollTitleToday);
        overlayPane.getChildren().add(leftButton);
        overlayPane.getChildren().add(rightButton);
        StackPane.setAlignment(leftButton, Pos.CENTER_LEFT);
        StackPane.setAlignment(rightButton, Pos.CENTER_RIGHT);
        StackPane.setMargin(leftButton, new Insets(0, 0, 0, 20));
        StackPane.setMargin(rightButton, new Insets(0, 20, 0, 0));
    	
    	borderPane_root.setCenter(overlayPane);
    	
        leftButton.setOnAction(e -> scrollLeft());
        rightButton.setOnAction(e -> scrollRight());
    	
    	loadCatalogData();
    	
    	Scene scene = new Scene(borderPane_root,  1920, 1080);
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
	    	for (JsonNode data: rootNode) {
	    		String img = "https:" + data
	                    .path("poster")
	                    .path("fullsize").asText();
	    		String name = data.path("title").asText();
	    		String desc = data.path("description").asText("None");
	    		double ratingInt = data.path("rating").path("average").asDouble(0);
	    		String rating = String.format("%.2f", (double) ratingInt);
	    		
    			StackPane infoOnImage = new StackPane();	
    			
	            VBox vboxContentButton = new VBox(10);
	            vboxContentButton.setAlignment(Pos.TOP_CENTER);
	            vboxContentButton.setPadding(new Insets(25, 0, 0, 0));
	            
	            Image image = new Image(img, 200, 270, true, true, true);
	            ImageView imageView = new ImageView(image);
	            imageView.setFitWidth(200); 
	            imageView.setFitHeight(270);
	            imageView.setPreserveRatio(true);
	            
	            
	            Label label_desc = new Label(desc);
	            label_desc.setPrefSize(290, 200);
	            label_desc.setWrapText(true);
	            label_desc.setStyle("-fx-background-color: black;"
	            		+ "-fx-text-fill: white;");
	            
	            Label label_name = new Label(name);
	            label_name.setPrefSize(150, 50);
	            label_name.setWrapText(true);
	            label_name.setStyle("-fx-background-color: rgba(155, 155, 155, 0.5);"
	            		+ "-fx-font-size: 18px;"
	            		+ "-fx-text-fill: black;"
	            		+ "-fx-text-alignment: center;");
	            
	            Label label_rating = new Label(rating);
	            
	            
	            Button button = new Button("");
	            button.setMinSize(300, 500);
	            button.setPrefSize(300, 500);
	            button.setMaxSize(300, 500); 
	            button.setContentDisplay(ContentDisplay.TOP);
	            button.setStyle("-fx-background-color: #3498db;");
	            
	            infoOnImage.getChildren().addAll(imageView, label_name, label_rating);
	            StackPane.setAlignment(label_name, Pos.BOTTOM_CENTER);
	            StackPane.setAlignment(label_rating, Pos.TOP_RIGHT);
	            StackPane.setMargin(label_name, new Insets(10));
	            vboxContentButton.getChildren().addAll(infoOnImage, label_desc);
	            button.setGraphic(vboxContentButton);
				hBox_titleToday.getChildren().add(button);
    		}
    	}
	    catch (Exception e) {
            e.printStackTrace();
            Label errorLabel = new Label("Ошибка загрузки данных: " + e.getMessage());
            hBox_titleToday.getChildren().add(errorLabel);
        }
	}
	
	
	public static void showGUI(){
		launch();
	}
}
	
	
	
	
	
	
	
	
	
	
	
	
	