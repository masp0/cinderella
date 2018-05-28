package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;


public class MainController implements Initializable {
	
	@FXML //fx:id="menu1"
	private MenuItem menu1;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		menu1.setOnAction(new EventHandler<ActionEvent>() {
	        public void handle(ActionEvent t) {
	        	System.exit(1);
	        }
	    });
		
	}
}

