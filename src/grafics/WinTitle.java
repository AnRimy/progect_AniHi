package grafics;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
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
		BorderPane pp = new BorderPane();
		pp.setStyle("-fx-background-color: white;");
		BorderPane root = new BorderPane();
//		root.setMinSize(1920, 1080);
		root.setStyle("-fx-background-color: white;");
		
        Button closeBtn = new Button("Закрыть");
        closeBtn.setMinSize(100, 200);
        closeBtn.setStyle("""
            -fx-background-color: #e74c3c;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-padding: 10px 20px;
        """);
        closeBtn.setOnAction(e -> hideWin());
		
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
		
		root.setLeft(img);
		root.setBottom(closeBtn);
		root.setRight(label_name);
//		root.setBottom(label_desc);
		root.setCenter(hbox_serPlayer);
		root.setTop(label_genres);
		
		pp.setCenter(root);
		return pp;
	}
	
	private void hideWin() {
		root.setVisible(false);
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
