package grafics;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
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
        
        Label sc = new Label("asd");
        sc.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
        root.setCenter(sc);
        
        centerPane.getChildren().add(root);
        parent.getChildren().add(centerPane); 
    }
}
