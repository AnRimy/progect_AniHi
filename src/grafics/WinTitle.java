package grafics;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Insets;
import javafx.scene.layout.CornerRadii;

public class WinTitle extends Stage{	
	private Image image;
	private String name;
	private String desc;
	private JsonNode data;
	private BorderPane root;

	public WinTitle(Image image, String name, String desc, JsonNode data){
		this.image = image;
		this.name = name;
		this.desc = desc;
		this.data = data;
	}
		
	public BorderPane createWin() {
		BorderPane root = new BorderPane();
		root.setMinSize(1920, 1080);
		
		StackPane main_container = new StackPane();
	
		Rectangle blur_background = new Rectangle(1920, 1080);
		blur_background.setFill(Color.rgb(100, 100, 100, 0.1));
		GaussianBlur blur = new GaussianBlur(50);
		blur_background.setEffect(blur);
		
		Rectangle darkOverlay = new Rectangle(1920, 1080);
		darkOverlay.setFill(Color.rgb(0, 0, 0, 0.5));
		darkOverlay.setArcHeight(20);
		darkOverlay.setArcWidth(20);
		
		StackPane vertical_root = new StackPane();
		vertical_root.setMaxWidth(900);
		vertical_root.setStyle("-fx-background-color: red");
		
		
        Button closeBtn = new Button("Закрыть");
        closeBtn.setMinSize(100, 200);
        closeBtn.setStyle("""
            -fx-background-color: #e74c3c;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-padding: 10px 20px;
        """);
        
		
		setTitle(name);
		ArrayList<Integer> series = new ArrayList<>();
		for (int i = 1; i <= 55; i++) {
		    series.add(i);
		}
		
		ImageView img = new ImageView(image);
		
		Label label_name = new Label(name);
		
		Label label_desc = new Label(desc);
		label_desc.setWrapText(true);
		
		String genres = "";
		JsonNode nodeGenres = data.path("genres");
		if (nodeGenres.isArray()) {
			for(JsonNode genre: nodeGenres)
				genres+=(genre.path("title").asText("None")) + " ";
		}
		
		Label label_genres = new Label(genres);
		label_genres.setStyle("-fx-background-color: red;");
		
		VBox vbox_serias = new VBox(5);
		vbox_serias.setPadding(new Insets(15));
		for(int s: series) {
			Button button_seria =  new Button("Серия " + Integer.toString(s));
			button_seria.setMinWidth(55);
			button_seria.setPrefWidth(100);
			vbox_serias.getChildren().add(button_seria);
		}
		
		ScrollPane scrollSeries = new ScrollPane(vbox_serias);
		scrollSeries.setMaxHeight(600);
		scrollSeries.setFitToWidth(true);
		scrollSeries.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollSeries.setStyle("""
			    -fx-background-color: blue;
			    -fx-background: transparent;
			""");
		
		Label label_video = new Label();
		label_video.setPrefSize(900, 600);
		label_video.setStyle("-fx-background-color: rgb(100, 155, 100)");
		
		HBox hbox_serPlayer = new HBox();
		hbox_serPlayer.getChildren().addAll(scrollSeries, label_video);
		
		vertical_root.getChildren().addAll(img, closeBtn);
		
		main_container.getChildren().addAll(blur_background, darkOverlay, vertical_root);
		
		root.setCenter(main_container);
		
//		bP_top_panel.setLeft(img);
//		bP_top_panel.setBottom(closeBtn);
//		bP_top_panel.setRight(label_name);
//		root.setBottom(label_desc);
//		bP_top_panel.setCenter(hbox_serPlayer);
//		bP_top_panel.setTop(label_genres);
		
	    closeBtn.setOnAction(e -> {
	        Node source = (Node) e.getSource();
	        Scene scene = source.getScene();
	        
	        if (scene != null) {
	            Parent rootParent = scene.getRoot();
	            if (rootParent instanceof BorderPane) {
	                BorderPane mainRoot = (BorderPane) rootParent;
	                mainRoot.getChildren().remove(root);
	            }
	        }
	    });
		
		return root;
	}
	
	private void hideWin() {
 
	}
	
	public String getRealVideoUrl(String iframeUrl) {
	    try {
	        String fullUrl = iframeUrl.startsWith("//") ? "https:" + iframeUrl : iframeUrl;
	        
	        HttpURLConnection connection = (HttpURLConnection) new URL(fullUrl).openConnection();
	        connection.setRequestMethod("GET");
	        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
	        connection.setInstanceFollowRedirects(false);
	        
	        int responseCode = connection.getResponseCode();
	        
	        if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || 
	            responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
	            String realUrl = connection.getHeaderField("Location");
	            return realUrl != null ? realUrl : fullUrl;
	        }
	        
	        return fullUrl;
	    } catch (Exception e) {
	        return iframeUrl;
	    }
	}
}
