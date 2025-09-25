package grafics;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import mainLogic.Core;

public class WinSchedule {
    private BorderPane parent;
    private Core client;
    private BorderPane scheduleRoot;
    
    public WinSchedule(BorderPane parent, Core client) {
        this.parent = parent;
        this.client = client;
    }

    public void createWin() {
        parent.getChildren().clear();

        StackPane centerPane = new StackPane();
        centerPane.setStyle("-fx-background-color: green;");
        centerPane.setAlignment(Pos.CENTER);
        
        
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: red;");
        root.setMinSize(400, 300);
        
        VBox bp_schedule = new VBox(5);
        bp_schedule.setMaxWidth(150);
        bp_schedule.setAlignment(Pos.CENTER);
        bp_schedule.setStyle("-fx-background-color: yellow;");
        
        for (int i = 0; i<= 6; i++) {
        	HBox hb_oneDay = new HBox(5);
        	ScrollPane sb = new ScrollPane(hb_oneDay);
        	sb.setMaxWidth(100);
        	sb.setHbarPolicy(ScrollBarPolicy.NEVER);
    		sb.setVbarPolicy(ScrollBarPolicy.NEVER);
        	sb.setFitToHeight(true);
        	sb.setFitToWidth(true);
        	sb.setStyle("-fx-background-color: white;");
        	
        	for (int j = 0; j<= 6; j++) {
	            Button button = new Button("");
	            button.setMinSize(100, 150);
	            button.setMaxSize(100, 150);
	            button.setStyle("""
	                -fx-background-color: grey;
	                -fx-border-color: #3498db;
	                -fx-border-width: 2px;
	                -fx-border-radius: 10px;
	            """);
	            hb_oneDay.getChildren().add(button);
        	}
        	bp_schedule.getChildren().add(sb);
        }
        root.setCenter(bp_schedule);
        centerPane.getChildren().add(root);
        parent.setCenter(centerPane); 
    }
}
