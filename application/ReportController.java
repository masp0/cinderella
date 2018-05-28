package application;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class ReportController implements Initializable {

	@FXML
	private Button nReport;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		nReport.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	try {    
	        		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("report_dialog.fxml"));
	                Parent root1;
					root1 = (Parent) fxmlLoader.load();
	                Stage stage = new Stage();
	                stage.initModality(Modality.APPLICATION_MODAL);
	                stage.setTitle("Create new Report");
	                stage.setScene(new Scene(root1));  
	                stage.show();
					}
	                catch (IOException e) {
						
						e.printStackTrace();
					}
	            
	        }
	});
		
		
	}
	
}
