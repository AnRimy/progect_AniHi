package grafics;

import javafx.scene.layout.BorderPane;

import mainLogic.Core;

public class WinSchedule {
    private BorderPane parent;
    private Core client;
    private BorderPane scheduleRoot;
    
    public WinSchedule(BorderPane parent, Core client) {
        this.parent = parent;
        this.client = client;
    }

	public void createWin()
	{
		parent.getChildren().clear();
		BorderPane root = new BorderPane();

		scheduleRoot = new BorderPane();
        
        BorderPane contentPanel = new BorderPane();
        contentPanel.setStyle("""
            -fx-background-color: 
                linear-gradient(to bottom, #2c3e50, #34495e);
            -fx-padding: 20px;
        """);
        
        scheduleRoot.setCenter(contentPanel);
        parent.getChildren().add(scheduleRoot);
	}
}
